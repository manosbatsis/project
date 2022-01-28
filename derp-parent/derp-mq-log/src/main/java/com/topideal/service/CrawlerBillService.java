package com.topideal.service;

/**
 * 爬虫日志 MQ
 */
public interface CrawlerBillService {

    //保存爬虫日志
    boolean save(String json, String topics, String tags) throws Exception;

}
