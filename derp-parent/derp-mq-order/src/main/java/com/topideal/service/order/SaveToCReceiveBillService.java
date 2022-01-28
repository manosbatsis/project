package com.topideal.service.order;

/**
 * @Description: 生成toc结算单
 * @Author: Chen Yiluan
 * @Date: 2021-03-08 14:06
 **/
public interface SaveToCReceiveBillService {

    /**
     * 生成toc结算单
     */
    boolean saveToCReceiveBill(String json, String keys, String topics, String tags)throws Exception;

    /**
     * 经分销调用蓝精灵获取结算数据
     * @param json
     * @param keys
     * @param topics
     * @param tags
     * @throws Exception
     */
    void saveToCReceiveBillBySmurfs(String json, String keys, String topics, String tags) throws Exception;

    /**
     * 读取爬虫库的平台结算数据生成Toc应收账单
     * @param json
     * @param keys
     * @param topics
     * @param tags
     */
    void SaveToCReceiveBillByCrawler(String json, String keys, String topics, String tags) throws Exception;
}
