package com.topideal.controller.maindata;

import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.code.OPCodeEnum;
import com.topideal.common.system.annotation.SystemControllerLog;
import com.topideal.service.maindata.SynsCustomerInfoService;
import com.topideal.tools.ResponseFactory;

import net.sf.json.JSONObject;


/**
 * （主数据）客商信息下发接口  (本接口已经作废,用2.0对对接)
 * @author gy
 */
@Controller
@RequestMapping("/derpapi/1102")
public class SyncCustomerInfoController {
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(SyncCustomerInfoController.class);
	@Autowired
	private SynsCustomerInfoService synsCustomerInfoService;

	/**
	 * 客户信息下发接口
	 * @param json (主数据传的是单个客户的json信息)
	 * @return
	 */
	@RequestMapping(params="method=erp5104",method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
	@SystemControllerLog(point= DERP_LOG_POINT.POINT_12102001010,model=DERP_LOG_POINT.POINT_12102001010_Label,keyword = "ccode")
    public Map getMainCustomerInfo(@RequestBody String json){
		try {
			//json对象
			JSONObject jsonData = JSONObject.fromObject(json);
			//客户ID校验
			if (jsonData.get("ccode")==null||StringUtils.isBlank(jsonData.getString("ccode"))) {
				return ResponseFactory.error(OPCodeEnum.ERROR_201, "ccode is NULL");
			}
			// 客户名称校验
			if (jsonData.get("cname")==null||StringUtils.isBlank(jsonData.getString("cname"))) {
				return ResponseFactory.error(OPCodeEnum.ERROR_201, "cname is NULL");
			}
			//客户简称校验
			if (jsonData.get("cShortname")==null||StringUtils.isBlank(jsonData.getString("cShortname"))) {
				return ResponseFactory.error(OPCodeEnum.ERROR_201, "cShortname is NULL");
			}
		
			//synsCustomerInfoService.synsCustomerInfo(jsonData.toString());
		}catch(Exception e){
			LOGGER.error("主数据同步客商信息异常,错误信息:{}",e.getMessage());
			return ResponseFactory.error(OPCodeEnum.ERROR_902, e.getMessage());
		}
		return ResponseFactory.success(OPCodeEnum.SUCCESS);
	}

}
