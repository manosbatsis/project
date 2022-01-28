package com.topideal.service.pushback;

/**
 * 爬虫出库单库存扣减回推
 **/
public interface CrawlerOutDepotInventoryLowerPushBackService {

    /**
     * 抓取出库单收发明细信息再回调给ordermq的爬虫出库单库存回调通知接口保存批次信息
     */
    public boolean FetchCrawlerOutDepotInfo(String json, String keys, String topics, String tags) throws Exception;

}
