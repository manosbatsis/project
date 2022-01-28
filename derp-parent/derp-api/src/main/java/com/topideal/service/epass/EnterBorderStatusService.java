package com.topideal.service.epass;

import net.sf.json.JSONObject;

/**
 * 进境状态回推接口
 * @author 杨创
 *2018/6/5
 */
public interface EnterBorderStatusService {
	/**
	 * 保存进境状态回推信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public JSONObject enterBorderStatusInfo(String json,Long merchantId)throws Exception;
}
