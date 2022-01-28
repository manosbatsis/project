package com.topideal.service.pushback.sale;

/**
 * 爬虫出库单库存扣减回推商品批次信息 service
 **/
public interface CrawlerInventoryDetailPushBackService {

    /**
     * 保存爬虫出库单商品批次信息，若出库单批次信息已存在则结束
     */
    public boolean saveBillBatch(String json, String keys, String topics, String tags) throws Exception;
}
