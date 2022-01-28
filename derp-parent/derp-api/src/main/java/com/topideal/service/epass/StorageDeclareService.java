package com.topideal.service.epass;

import net.sf.json.JSONObject;

/**
 * 入库申报接口
 * @author 杨创
 *2018/5/25
 */
public interface StorageDeclareService {
	/**
	 * 保存入库申报信息
	 * @param json 入库申报接口报文
	 * @return
	 */
	public  JSONObject storageDeclareInfo(String json,Long merchantId,String isRookie,String contractNoTag)throws Exception;
}
