package com.topideal.service.timer;

/**
 * 爬虫生成电商售后退款单
 * @author qiancheng.chen
 *
 */
public interface CrawlerOrderReturnIdepotService {
	void saveOrderReturnIdepot(String json,String keys,String topics,String tags) throws Exception;
}
