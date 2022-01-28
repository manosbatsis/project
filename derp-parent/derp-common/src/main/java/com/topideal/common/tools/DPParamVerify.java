package com.topideal.common.tools;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DPParamVerify {
	
	/*打印日志*/
	private static final Logger logger = LoggerFactory.getLogger(DPSign.class);
	
	/**
	 * 数据非空校验
	 * @param obj
	 * @param vnMap
	 * @return
	 */
	public static String verifyNullForMap(Map<String, Object> map,String[] param){
		logger.info("开始数据非空校验");
		String nullKey =null;
		try {
			if(param!=null&&param.length>0){
				for(String paramName:param){
					String value=(String) map.get(paramName);
					if(value==null||value.trim().equals("")){
						nullKey=paramName;
						logger.info("为空字段:"+nullKey);
						return nullKey;
					}
				}
			}
		} catch (Exception e) {
			logger.error("数据非空校验异常", e);
		}
		return nullKey;
	}
	
	
}
