package com.topideal.service.api.transfer;
/**
 * 入库申报接口
 * @author yucaifu
 *2018/5/25
 */
public interface DBWarehousService {
	/**
	 * 保存入库申报信息
	 * @param json 入库申报接口报文
	 * @return
	 */
	public  boolean saveWarehousInfo(String json,String keys,String topics,String tags)throws Exception;
}
