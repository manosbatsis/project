package com.topideal.service.pushback;
/**
 * 大货理货加减成功回推
 * 杨创 
 * 2019/04/01
 */
public interface BigCargoTallyPushBackService {
	/**
	 * 大货理货加减成功回推
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean updateBigCargoTally(String json, String keys, String topics, String tags)throws Exception;
}
