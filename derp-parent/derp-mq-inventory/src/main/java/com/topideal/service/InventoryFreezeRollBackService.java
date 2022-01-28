package com.topideal.service;

/**
 * 单据回滚重新冻结库存
 */
public interface InventoryFreezeRollBackService {

    /**
     *  单据回滚重新冻结/释放库存
     * @param json
     * @return
     * @throws Exception
     */
    public boolean saveInventoryfreezeRollBack(String json,String keys,String topics,String tags)throws Exception;





}
