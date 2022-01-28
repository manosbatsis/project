package com.topideal.service.pushback.sale;

/**
 * 爬虫生成出库单库存扣减成功回推
 */
public interface CrawlerStockOutPushBackService {
	/**
	 * 爬虫生成出库单库存扣减成功回推
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean modifyStatus(String json, String keys, String topics, String tags) throws Exception;

}
