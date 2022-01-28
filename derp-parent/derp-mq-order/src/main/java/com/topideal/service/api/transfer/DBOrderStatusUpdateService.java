package com.topideal.service.api.transfer;
/**
 * 调拨模块单据状态更新
 */
public interface DBOrderStatusUpdateService {
	/**
	 * 调拨模块单据状态更新
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean updateDBOrderStatusInfo(String json, String keys, String topics, String tags)throws Exception;
}
