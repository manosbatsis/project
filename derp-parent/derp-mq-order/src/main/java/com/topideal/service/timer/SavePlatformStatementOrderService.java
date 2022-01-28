package com.topideal.service.timer;

import java.sql.SQLException;
import java.text.ParseException;

public interface SavePlatformStatementOrderService {

	/**
	 * 生成云集平台结算单
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @throws SQLException 
	 * @throws ParseException 
	 */
	void saveYJPlatStatementOrder(String json, String keys, String topics, String tags) throws Exception;

	/**
	 * 生成唯品平台结算单
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @throws SQLException 
	 * @throws ParseException 
	 * @throws Exception 
	 */
	void saveVipPlatStatementOrder(String json, String keys, String topics, String tags) throws Exception;

	/**
	 * 生成天猫平台结算单
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @throws SQLException 
	 * @throws ParseException 
	 * @throws Exception 
	 */
	void saveTMPlatStatementOrder(String json, String keys, String topics, String tags) throws Exception;

}
