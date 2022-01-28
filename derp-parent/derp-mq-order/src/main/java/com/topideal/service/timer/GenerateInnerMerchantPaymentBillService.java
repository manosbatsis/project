package com.topideal.service.timer;

import java.sql.SQLException;

/**
 * 生成内部公司应付账单
 * @author qiancheng.chen
 *
 */
public interface GenerateInnerMerchantPaymentBillService {
	
	/**
	 * 生成内部公司应付账单
	 * @param json
	 * @param key
	 * @param topics
	 * @param tags
	 * @throws SQLException
	 */
	void generateInnerMerchantPaymentBill(String json, String key, String topics, String tags) throws Exception;
}
