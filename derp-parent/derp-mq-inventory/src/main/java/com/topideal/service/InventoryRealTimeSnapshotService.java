package com.topideal.service;

/**
 *  实时库存快照   业务
 * Created by baols on 2018/6/11
 */
public interface InventoryRealTimeSnapshotService {

    /**
     *  实时库存快照 
     * @param json
     * @return
     * @throws Exception
     */
    public boolean synsInventoryRealTimeInterface(String json,String keys,String topics,String tags)throws Exception;

    /**
     *  Gss菜鸟仓实时库存快照 
     * @param json
     * @return
     * @throws Exception
     */
    //public boolean synsInventoryRealTimeGss(String json, String keys, String topics, String tags) throws Exception;


}
