package com.topideal.service.api;
/**
 * 大货理货接口
 * 杨创
 * 2019.04.01 
 */
public interface BigCargoTallyService {
	/**
	 * 大货理货接口
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean saveBigCargoTallyInfo(String json, String keys, String topics, String tags)throws Exception;
}
