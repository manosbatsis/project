package com.topideal.service;

/**
 *  众邦云实时库存快照
 */
public interface InventoryYwmsTimeSnapshotService {

    /**
     * 下推众邦云查询库存信息
     */
    void synsInventoryRealTimeYwms(String json, String keys, String topics, String tags) throws Exception;

}
