package com.topideal.service.timer;

/**
 * 平台采购单未上架信息发送消息
 * @author 杨创
 *2021-06-22
 */
public interface PlatformPurNotShalfMessageService {
	/**
	 * 平台采购单未上架信息发送消息
	 */
	void sendPurNotShalfMessage(String json, String keys, String topics, String tags) throws Exception;

}
