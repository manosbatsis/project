package com.topideal.service.timer;

/**
  *   嘉宝的采购SD单同步到宝信
 * @author qiancheng.chen
 * @date 2020-12-23
 *
 */
public interface SynPurchaseSdJBToBXService {

	/**
	  * 嘉宝的采购SD单同步到宝信
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @throws Exception
	 */
	public void synsPurchaseSdJBToBX(String json,String keys,String topics,String tags) throws Exception;
}
