package com.topideal.service.timer;

/**
 * 消费菜鸟电商订单单个数据
 * @author 杨创
 *2021/03/25
 */
public interface CrawlerRookieGetOneOrderService {

	/**
	 * 单个插入
	 * @param orderObject
	 * @param merchantInfoMongo
	 * @param depotInfoMongo
	 * @param s
	 * @param isEnforce 是否强制抓单 1-是 0-否
	 * @throws Exception
	 */
	public void insertOrder(String json,String keys,String topics,String tags)throws Exception;

}
