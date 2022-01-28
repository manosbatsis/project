package com.topideal.service;

/**
 * 生成已经月结的月结账单快照
 * yangchuang  2019-08-12
 */
public interface InventoryMonthlyAccountSnapshotService {

    /**
     *生成已经月结的月结账单快照
     * @param json
     * @return
     * @throws Exception
     */
    public boolean synsMonthlyAccountSnapshot(String json,String keys,String topics,String tags)throws Exception;
}
