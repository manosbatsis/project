package com.topideal.service.impl;

import com.topideal.api.idm.IDMRestPost;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.user.UserInfoDao;
import com.topideal.entity.vo.user.UserInfoModel;
import com.topideal.service.UserInfoService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {

	/*打印日志*/
	private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

	@Autowired
	private UserInfoDao UserInfoDao ;

	@Autowired
	private RMQLogProducer rocketLogMQProducer;

	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_12207000100,model=DERP_LOG_POINT.POINT_12207000100_Label)
	public void synsUserInfo(String json, String keys, String topics, String tags) throws Exception {
        // 将字符串转成json
		JSONObject jsonData = JSONObject.fromObject(json);
		String userId = (String)jsonData.get("userId");//工号
		String startTime = (String)jsonData.get("startTime");//开始时间 yyyyMMddHHmmss

		//修改时间没指定则默认近两天的
		if(StringUtils.isBlank(startTime)){
			startTime = TimeUtils.getYesterday().replaceAll("-","")+"000000";//获取前一天日期
		}

		String endTime = TimeUtils.formatFullTimeNum();//结束时间 yyyyMMddHHmmss 默认当前时间

		/**查询条件拼装
		 * 1.userId-工号 非必填
		 * 2.userModifyTime-修改时间 yyyyMMddHHmmss+Z 必填
		 * 3.userAuthorityTag-授权 只拿有经销系统权限的用户 必填
		 * 查询条件拼装 %26:&  %3e%3d:>=   %3c%3d:<=
		 * */
		String filter = "";
		if(StringUtils.isNotBlank(userId)){
			filter = "(userId="+userId+")";
		}else {
			filter += "(userModifyTime%3e%3d" + startTime + "Z)";
			filter += "(userModifyTime%3c%3d" + endTime + "Z)";
	    }
		//filter += "(userAuthorityTag=AUTH_JFX_ycf:AUTH_JFX_ycf)";
		//filter += "(userAuthorityTag=AUTH_JFX:AUTH_JFX)";
		filter += "(userAuthorityTag="+ ApolloUtil.idmAuthStr+":"+ApolloUtil.idmAuthStr+")";
		filter = "(%26"+filter+")";

		String userUrl = "/api/v3/user/paged?filter=FILTER&pageSize=20&cookie=COOKIE";
		String cookie = "";
		int page=1;
		while(true){
			logger.info("【第"+page+"页开始】....");
			String url = userUrl.replace("FILTER", filter).replace("COOKIE", cookie);
			String resp = IDMRestPost.restPost(url, null, "GET");
			System.out.println(resp);
			if(StringUtils.isBlank(resp)) break;

			JSONObject respJsonData = JSONObject.fromObject(resp);

			if(respJsonData.get("body").equals("null")) break;

            JSONObject body = (JSONObject) respJsonData.get("body");
			JSONArray userArray = body.getJSONArray("users");
			if(userArray==null) break;

			for(int i = 0;i<userArray.size();i++){
				JSONObject userObj = userArray.getJSONObject(i);
				String userLoginIdStr = (String)userObj.get("userLoginId");//登录账号
				String userIdStr = (String)userObj.get("userId");//工号 必填
				String userCn = (String)userObj.get("userCn");//中文名
				String userMobile = (String)userObj.get("userMobile");//手机号
				String userMail = (String)userObj.get("userMail");//邮箱
				String userStatus = (String)userObj.get("userStatus");//用户状态 0禁用；1正常

				String erpStatus = DERP_SYS.USERINFO_DISABLE_0;//转换为经销用户状态 默认启用
				if(userStatus.equals("0")) erpStatus = DERP_SYS.USERINFO_DISABLE_1;

				UserInfoModel userInfo = new UserInfoModel();
				userInfo.setCode(userIdStr);
				List<UserInfoModel> userList = UserInfoDao.list(userInfo);
				UserInfoModel userModel = null;
				if(userList == null||userList.size()<=0) {
					//新增
					userModel = new UserInfoModel();
					userModel.setUsername(userLoginIdStr);//登录账号
					userModel.setName(userCn);//中文名
					userModel.setCode(userIdStr);//工号
					userModel.setTel(userMobile);//手机号
					userModel.setEmail(userMail);//邮件
					userModel.setDisable(DERP_SYS.USERINFO_DISABLE_0);//启用
					userModel.setUserType(DERP_SYS.USERINFO_USERTYPE_3);//用户类型
					userModel.setAccountType(DERP_SYS.USERINFO_ACCOUNTTYPE_1);//账号类型
					userModel.setDisable(erpStatus);//用户状态
					UserInfoDao.save(userModel);
				}else if(userList.size()>1) {
					logger.info("工号存在多个账号,工号:"+userIdStr+"[跳过]");
				    continue;
				}else{
					//修改
					userModel = userList.get(0);
					userModel.setUsername(userLoginIdStr);//登录账号
					userModel.setName(userCn);//中文名
					userModel.setTel(userMobile);//手机号
					userModel.setEmail(userMail);//邮件
					userModel.setDisable(erpStatus);//用户状态
					UserInfoDao.modify(userModel);
				}
			}

			logger.info("【第"+page+"页结束】");
			page++;
			Object cookieObj = body.get("cookie");
            if(cookieObj==null||cookieObj.equals("null")) break;//为空代表没有下一页了
			cookie = cookieObj.toString();
		}

	}

    /**测试方法
	 * */
	public static void synsUserInfo2(){
		// 将字符串转成json

		String filter = "(userId=004951)";
		//String filter = "(%26(userModifyTime%3e%3d20180101000000Z)(userModifyTime%3c%3d20220101000000Z))";
		//String filter = "(userAuthorityTag=AUTH_JFX_ycf:AUTH_JFX_ycf)";

		//String filter = "(%26(userId=97472564)(userModifyTime%3e%3d20180101000000Z)(userModifyTime%3c%3d20220101000000Z)(userAuthorityTag=AUTH_JFX_ycf:AUTH_JFX_ycf))";
		//String filter = "(%26(userId=004951)(userAuthorityTag=AUTH_JFX:AUTH_JFX))";

		/**查询条件拼装
		 * 1.userId-工号 非必填
		 * 2.userModifyTime-修改时间 yyyyMMddHHmmss+Z
		 * 3.userAuthorityTag-授权 只拿有经销系统权限的用户
		 * */
		String userUrl = "/api/v3/user/paged?filter=FILTER&pageSize=2&cookie=COOKIE";
		String url = userUrl.replace("FILTER", filter).replace("COOKIE", "");
		String resp = IDMRestPost.restPost(url, null, "GET");
		System.out.println(resp);

	}

	public static void main(String[] args) {
		synsUserInfo2();
	}

}
