package com.topideal.service.api.sale;
/**
 * 进境状态回推接口
 */
public interface DSEnterBorderStatusService {
	/**
	 * 保存进境状态回推信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean saveEnterBorderStatusInfo(String jso,String keys,String topics,String tags)throws Exception;
}
