package com.topideal.service.storage;
/**
 * 自然过期结转Service
 * @author 杨创
 *2019/10/22
 */
public interface NaturalExpireService {
	/**
	 * 保存自然过期
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean savenaturalExpire(String json, String keys, String topics, String tags)throws Exception;
}
