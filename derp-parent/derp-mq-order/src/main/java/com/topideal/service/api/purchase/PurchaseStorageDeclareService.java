package com.topideal.service.api.purchase;

/**
 * 入库申报接口
 * @author zhanghx
 * 2018/7/16
 */
public interface PurchaseStorageDeclareService {
	
	/**
	 * 保存入库申报信息
	 * @param json 入库申报接口报文
	 * @return
	 */
	public  boolean saveStorageDeclareInfo(String json,String keys,String topics,String tags)throws Exception;
	
}
