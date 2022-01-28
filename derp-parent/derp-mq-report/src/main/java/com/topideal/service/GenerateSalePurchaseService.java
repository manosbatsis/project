package com.topideal.service;

public interface GenerateSalePurchaseService {
	
	/**
	 * 生成购销采销日报数据
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @return
	 * @throws Exception
	 */
	public boolean generateSalePurchaseInterface(String json, String keys, String topics, String tags) throws Exception;

}
