package com.topideal.service.timer;

import com.topideal.mongo.entity.MerchantInfoMongo;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface SaveToCTemporaryBillService {

	/**
	 * 根据订单+商品货号维度生成上个月的To C暂估应收数据
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @throws SQLException 
	 * @throws ParseException 
	 */
	void saveToCTemporaryBill(String json, String keys, String topics, String tags) throws Exception;

	/**
	 * 生成To C暂估费用数据
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @throws SQLException
	 * @throws ParseException
	 */
	void saveToCCostTemporaryBill(String json, String keys, String topics, String tags) throws Exception;

	List<MerchantInfoMongo> getMerchantList(Integer merchantId);
}
