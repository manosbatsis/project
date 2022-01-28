package com.topideal.service.pushback.transfer;

public interface DBBackService {
   /**
    * 调拨库存出库回调通知
    * */
	public boolean synsOutDepotBack(String json,String keys,String topics,String tags) throws Exception;
	/**
    * 调拨库存入库回调通知
    * */
	public boolean synsInDepotBack(String json,String keys,String topics,String tags) throws Exception;
}
