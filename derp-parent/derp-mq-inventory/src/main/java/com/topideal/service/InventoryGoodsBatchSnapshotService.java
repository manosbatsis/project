package com.topideal.service;

/**
 *  库存商品批次快照   业务
 * Created by baols on 2018/6/11
 */
public interface InventoryGoodsBatchSnapshotService {

    /**
     *   库存商品批次快照
     * @param json
     * @return
     * @throws Exception
     */
    public boolean synsInventoryGoodsBatchSnapshot(String json,String keys,String topics,String tags)throws Exception;





}
