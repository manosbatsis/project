package com.topideal.service.user.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.ConfigConstants;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.MD5Utils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.BusinessUnitDao;
import com.topideal.dao.main.MerchantRelDao;
import com.topideal.dao.user.RoleInfoDao;
import com.topideal.dao.user.UserBuRelDao;
import com.topideal.dao.user.UserInfoDao;
import com.topideal.dao.user.UserMerchantRelDao;
import com.topideal.dao.user.UserRoleRelDao;
import com.topideal.entity.dto.user.UserInfoDTO;
import com.topideal.entity.vo.main.BusinessUnitModel;
import com.topideal.entity.vo.main.MerchantRelModel;
import com.topideal.entity.vo.user.RoleInfoModel;
import com.topideal.entity.vo.user.UserBuRelModel;
import com.topideal.entity.vo.user.UserInfoModel;
import com.topideal.entity.vo.user.UserMerchantRelModel;
import com.topideal.entity.vo.user.UserRoleRelModel;
import com.topideal.mongo.dao.UserMerchantRelMongoDao;
import com.topideal.service.user.UserInfoService;

/**
 * Created by weixiaolei on 2018/4/11.
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private MerchantRelDao merchantRelDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private UserMerchantRelDao userMerchantRelDao;
    @Autowired
    private UserMerchantRelMongoDao userMerchantRelMongoDao;
    
    @Autowired
    private BusinessUnitDao businessUnitDao ;
    
    @Autowired
    private UserBuRelDao userBuRelDao ;
    
    @Autowired
    private UserRoleRelDao userRoleRelDao;
    @Autowired
    private RoleInfoDao roleInfoDao;

    /**
     * 用户授权角色 ( 确定)操作
     */
	@Override
	public boolean saveUserBindRole(Long id, Long roleId) throws SQLException {
		UserInfoModel userInfo = userInfoDao.searchById(id);
		if (userInfo==null) {
			throw new RuntimeException("没有查询到用户信息");
		}
		//清空用户下所有角色
		UserRoleRelModel userRoleRel=new  UserRoleRelModel();
		userRoleRel.setUserId(id);
		List<UserRoleRelModel> userRoleRelList = userRoleRelDao.list(userRoleRel);
		List<Long> idList =new ArrayList<Long>();
		if (userRoleRelList.size()>0) idList = userRoleRelList.stream().map(UserRoleRelModel -> UserRoleRelModel.getId()).collect(Collectors.toList());
		if (idList.size()>0)userRoleRelDao.delete(idList);
		if (roleId==null) return true;
		// 生成用户角色关系数据
		UserRoleRelModel saveUserRoleRel=new UserRoleRelModel();
		saveUserRoleRel.setRoleId(roleId);
		saveUserRoleRel.setUserId(id);
		saveUserRoleRel.setCreateDate(TimeUtils.getNow());
		userRoleRelDao.save(saveUserRoleRel);
		return true;
	}

    
    @Override
    public UserInfoModel searchUserInfoDetail(Long id) throws SQLException{
        return  userInfoDao.searchById(id);
    }
    @Override
    public UserInfoModel searchUserInfo(UserInfoModel model) throws SQLException{
        return  userInfoDao.searchUserInfo(model);
    }
    @Override
    public List<UserInfoModel> list(UserInfoModel model) throws SQLException{
        return  userInfoDao.list(model);
    }
    @Override
    public UserInfoDTO listUserInfo(UserInfoDTO dto, String userType, Long userId, Long parentId)throws SQLException {
        if(DERP_SYS.USERINFO_USERTYPE_2.equals(userType)){
        	dto.setParentId(userId);
        } else if(DERP_SYS.USERINFO_USERTYPE_3.equals(userType)) {
        	dto.setCreater(userId);
        }
        UserInfoDTO userDTO = userInfoDao.searchDTOByPage(dto);
        if(userDTO.getList() != null && userDTO.getList().size() > 0) {
        	List<UserInfoDTO> userList = userDTO.getList();
        	for(UserInfoDTO user : userList) {
        		String roleList = "";
        		String roleIds = "";
        		UserRoleRelModel userRoleRelModel = new UserRoleRelModel();
        		userRoleRelModel.setUserId(user.getId());        		
        		List<UserRoleRelModel> userRoleList = userRoleRelDao.list(userRoleRelModel);
        		for(UserRoleRelModel userRole : userRoleList) {
        			RoleInfoModel roleInfo = roleInfoDao.searchById(userRole.getRoleId());
        			if (roleInfo==null) continue;
        			roleList += roleInfo.getName() +",";
        			roleIds+=roleInfo.getId()+",";
        		}
        		if(StringUtils.isNotBlank(roleList)) {
        			roleList = roleList.substring(0, roleList.length()-1);
        		}
        		if(StringUtils.isNotBlank(roleIds)) {
        			roleIds = roleIds.substring(0, roleIds.length()-1);
        		}
        		user.setRoles(roleList);  
        		user.setRoleIds(roleIds);
        	}
        }
        return userDTO;
    }

    @SuppressWarnings("rawtypes")
	@Override
    public boolean delUser(List ids)throws SQLException{
        int num=userInfoDao.delete(ids);
        if(num>=1){
            return true;
        }
        return false;
    }

    @Override
    public ViewResponseBean saveUser(UserInfoDTO dto,String userType,Long userId,Long parentId)throws Exception{
        String username=dto.getUsername();
        UserInfoDTO oldUser=userInfoDao.searchByUsername(username);
        if(oldUser!=null){
            return ResponseFactory.error(StateCodeEnum.ERROR_309);//用户名已经存在
        }
        UserInfoModel model = new UserInfoModel();
        model.setUsername(dto.getUsername());
        model.setCode(dto.getCode());
        model.setName(dto.getName());
        model.setEmail(dto.getEmail());
        model.setTel(dto.getTel());
        model.setSex(dto.getSex());
        model.setAccountType(dto.getAccountType());
        //设置默认密码
        model.setPassword(MD5Utils.encode(ConfigConstants.DEFAULT_PASSWORD));
        //设置创建人
        model.setCreater(userId);
        //创建时间
        model.setCreateDate(TimeUtils.getNow());
        //状态
        model.setDisable(DERP_SYS.USERINFO_DISABLE_0);
        //绑定公司
        String merchantIds = dto.getMerchantIds();
        List<String> merchantIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(merchantIds)) {
            merchantIdList = Arrays.asList(merchantIds.split(","));
        }
        //绑定事业部
        String buIds = dto.getBuIds();
        List<String> buIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(buIds)) {
        	buIdList = Arrays.asList(buIds.split(","));
        }
        //普通用户
        if (DERP_SYS.USERINFO_ACCOUNTTYPE_1.equals(dto.getAccountType())) {

            UserMerchantRelModel userMerchantRelModel = new UserMerchantRelModel();
            userMerchantRelModel.setUserId(userId);
            List<UserMerchantRelModel> userMerchantRelModels = userMerchantRelDao.list(userMerchantRelModel);
            List<String> parentMerchantIds = new ArrayList<>();
            for (UserMerchantRelModel merchantRelModel : userMerchantRelModels) {
                parentMerchantIds.add(merchantRelModel.getMerchantId().toString());
            }

            //平台管理  商家超管
            if(DERP_SYS.USERINFO_USERTYPE_1.equals(userType)){
                model.setUserType(DERP_SYS.USERINFO_USERTYPE_2);
                model.setParentId(userId);
            }else if(DERP_SYS.USERINFO_USERTYPE_2.equals(userType)){//商家超管创建   商家帐号
                if (!parentMerchantIds.containsAll(merchantIdList)) {
                    throw new RuntimeException("该用户绑定的公司在父级用户不存在！");
                }
                model.setUserType(DERP_SYS.USERINFO_USERTYPE_3);
                model.setParentId(userId);
            }else{
                if (!parentMerchantIds.containsAll(merchantIdList)) {
                    throw new RuntimeException("该用户绑定的公司在父级用户不存在！");
                }
                model.setUserType(DERP_SYS.USERINFO_USERTYPE_3);//商家帐号管理员  创建商家帐号
                model.setParentId(parentId);
            }
        } else if (DERP_SYS.USERINFO_ACCOUNTTYPE_0.equals(dto.getAccountType())) { //后台管理员
            model.setUserType(DERP_SYS.USERINFO_USERTYPE_1);
        }
        //保存用户信息
        Long id= userInfoDao.save(model);
        //普通用户
        if (DERP_SYS.USERINFO_ACCOUNTTYPE_1.equals(dto.getAccountType())) {
            for (String mId : merchantIdList) {
                UserMerchantRelModel userMerchantRelModel = new UserMerchantRelModel();
                userMerchantRelModel.setUserId(id);
                userMerchantRelModel.setMerchantId(Long.valueOf(mId));
                userMerchantRelDao.save(userMerchantRelModel);
            }
            
            for (String buId : buIdList) {
            	BusinessUnitModel buModel = businessUnitDao.searchById(Long.valueOf(buId));
            	
            	if(buModel != null) {
            		UserBuRelModel userBuRelModel = new UserBuRelModel() ;
            		
            		userBuRelModel.setBuCode(buModel.getCode());
            		userBuRelModel.setBuId(buModel.getId());
            		userBuRelModel.setBuName(buModel.getName());
            		userBuRelModel.setUserId(id);
            		userBuRelModel.setCreateDate(TimeUtils.getNow());
            		
            		userBuRelDao.save(userBuRelModel) ;
            	}
			}
        }
        if(id!=null){
            return ResponseFactory.success();
        }
        return ResponseFactory.error(StateCodeEnum.ERROR_305);
    }

    @Override
    public ViewResponseBean modify(UserInfoDTO model)throws Exception{
    	//设置修改时间
    	model.setModifyDate(TimeUtils.getNow());
        if (model.getId() == null) {
            throw new RuntimeException("用户id不能为空");
        }
        String username = model.getUsername();
        UserInfoDTO oldUser = userInfoDao.searchByUsername(username);
        if(oldUser!=null && !model.getId().equals(oldUser.getId())){
            return ResponseFactory.error(StateCodeEnum.ERROR_309);//用户名已经存在
        }
        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setId(model.getId());
        userInfoModel.setUsername(model.getUsername());
        userInfoModel.setCode(model.getCode());
        userInfoModel.setName(model.getName());
        userInfoModel.setEmail(model.getEmail());
        userInfoModel.setTel(model.getTel());
        userInfoModel.setSex(model.getSex());
        int num=userInfoDao.modify(userInfoModel);
        //绑定公司
        String merchantIds = model.getMerchantIds();
        List<String> merchantIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(merchantIds)) {
            merchantIdList = Arrays.asList(merchantIds.split(","));
        }
        
      //绑定事业部
        String buIds = model.getBuIds();
        List<String> buIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(buIds)) {
        	buIdList = Arrays.asList(buIds.split(","));
        }

        if (model.getAccountType().equals(DERP_SYS.USERINFO_ACCOUNTTYPE_1)) {
            //删除原绑定公司
            userMerchantRelDao.delAllByUserId(model.getId());

            //新增用户公司关系
            for (String mId : merchantIdList) {
                UserMerchantRelModel userMerchantRelModel = new UserMerchantRelModel();
                userMerchantRelModel.setUserId(model.getId());
                userMerchantRelModel.setMerchantId(Long.valueOf(mId));
                userMerchantRelDao.save(userMerchantRelModel);
            }
            
            userBuRelDao.delByUserId(model.getId()) ;
            
            for (String buId : buIdList) {
            	
            	BusinessUnitModel buModel = businessUnitDao.searchById(Long.valueOf(buId));
            	
            	if(buModel == null) {
            		continue ;
            	}
            	
            	UserBuRelModel userBuRelModel = new UserBuRelModel() ;
        		
        		userBuRelModel.setBuCode(buModel.getCode());
        		userBuRelModel.setBuId(buModel.getId());
        		userBuRelModel.setBuName(buModel.getName());
        		userBuRelModel.setUserId(model.getId());
        		userBuRelModel.setCreateDate(TimeUtils.getNow());
        		
        		userBuRelDao.save(userBuRelModel) ;
			}
        }

        if(num>=1){
            return ResponseFactory.success();
        }
        return ResponseFactory.error(StateCodeEnum.ERROR_305);
    }

    @Override

    public boolean resetPwd(Long id,Long userId) throws SQLException {
        UserInfoModel model=new UserInfoModel();
        model.setId(id);
        //设置默认密码
        model.setPassword(MD5Utils.encode(ConfigConstants.DEFAULT_PASSWORD));
        //设置重置密码时间
        model.setResetPwdDate(TimeUtils.getNow());
        //重置密码人员
        model.setResetPwdId(userId);
        int num=userInfoDao.modify(model);
        if(num>=1){
            return true;
        }
        return false;
    }
 
    @Override

    public Map<String,String> modifyPwd(Long id, String oldPwd, String password) throws SQLException {
        Map<String,String> retMap = new HashMap<>();

        boolean flag = isLetterDigit(password);
        if(flag==false){
            retMap.put("code","9999");
            retMap.put("message","密码需6-20位数字+字母");
            return retMap;
        }
        UserInfoModel userInfoModel=userInfoDao.searchById(id);
        if(!userInfoModel.getPassword().equals(MD5Utils.encode(oldPwd))){
            retMap.put("code","9999");
            retMap.put("message","旧密码不正确");
            return retMap;
        }

        UserInfoModel model=new UserInfoModel();
        model.setId(id);
        model.setPassword(MD5Utils.encode(password));
        //修改密码时间
        model.setModifyPwdDate(TimeUtils.getNow());
        int num=userInfoDao.modify(model);
        retMap.put("code","0000");
        retMap.put("message","操作成功");
        return retMap;
    }
    /**
     * 密码6-20位数字+字母
     */
    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;
        boolean isLetter = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]{6,20}$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }

    @Override
    public UserInfoDTO searchByUsername(String username){
        return userInfoDao.searchByUsername(username);
    }

    @Override
    public List<String> getBtnPersonInfo(long id) {
        return userInfoDao.getBtnPersonInfo(id);
    }

    /**
     * 通过商家id ,获取关联商家信息
     * @param merchantId
     * @return
     * @throws Exception
     */
    public String searchRelMerchantIds(Long merchantId)throws Exception{
        MerchantRelModel model=new MerchantRelModel();
        model.setMerchantId(merchantId);
        List<MerchantRelModel> list=merchantRelDao.list(model);
        if(list!=null){
             StringBuilder sb=new StringBuilder();
            for (MerchantRelModel merchantRelModel : list) {
                sb.append(merchantRelModel.getProxyMerchantId()+",");
            }
            //删除最后一个字符 ，
            if(sb.indexOf(",")>-1){
                sb.deleteCharAt(sb.length()-1);
                return sb.toString();
            }
        }
        return null;
    }

	@Override
	public UserInfoDTO searchDetail(Long id) throws SQLException {
		return userInfoDao.getDetails(id);
	}

    @Override
    public boolean enableUser(Long id, String type) throws SQLException {
        //获取用户信息
        UserInfoModel userInfo = new UserInfoModel();
        userInfo.setId(id);
        userInfo.setDisable(type);
        int num = userInfoDao.modify(userInfo);
        if (num > 0) {
            return true;
        }
        return false;
    }

	@Override
	public List<UserBuRelModel> getBuRel(Long id) throws SQLException {
		
		UserBuRelModel relModel = new UserBuRelModel() ;
		relModel.setUserId(id);
		
		return userBuRelDao.list(relModel) ;
		
	}


	@Override
	public int modifyUserInfo(UserInfoModel model) throws Exception {
		return userInfoDao.modify(model);
	}


}
