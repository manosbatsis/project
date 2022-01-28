package com.topideal.service.api.sale;
/**
 * 入库申报接口
 * @author 杨创
 *2018/5/25
 */
public interface XSTStorageDeclareService {
	/**
	 * 保存入库申报信息
	 * @param json 入库申报接口报文
	 * @return
	 */
	public  boolean saveStorageDeclareInfo(String json,String keys,String topics,String tags)throws Exception;
}
