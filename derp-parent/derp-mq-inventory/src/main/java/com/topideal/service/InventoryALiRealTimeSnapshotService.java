package com.topideal.service;

/**
 *  实时库存快照   业务
 */
public interface InventoryALiRealTimeSnapshotService {

    /**
     *  菜鸟仓实时库存快照
     * @param json
     * @return
     * @throws Exception
     */
    public boolean synsInventoryRealTimeALi(String json, String keys, String topics, String tags) throws Exception;


}
