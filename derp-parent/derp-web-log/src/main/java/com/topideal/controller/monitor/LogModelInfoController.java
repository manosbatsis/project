package com.topideal.controller.monitor;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.LogPointBasic;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/logModelInfo")
public class LogModelInfoController {


	/**
	 * modelCode:1：业务模块 2：推送外部API 3：仓储模块  4：库存模块  5：api模块 6：报表模块
	 * isInspected是否巡检: 0 -否， 1-是
	 * */
	@RequestMapping("/getLogInfos.asyn")
	public ViewResponseBean getLogInfos(String modelCode,String isInspected) {
		try {
			List<LogPointBasic> pointList = new ArrayList();
			for(LogPointBasic logPoint : DERP_LOG_POINT.logPointList){
                if(StringUtils.isNotBlank(modelCode)&&!modelCode.equals(logPoint.getModelCode())){
                	continue;
				}
				if(StringUtils.isNotBlank(isInspected)&&!isInspected.equals(logPoint.getIsInspected())){
					continue;
				}
				pointList.add(logPoint);
			}
			return ResponseFactory.success(pointList) ;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
		
	}
}
