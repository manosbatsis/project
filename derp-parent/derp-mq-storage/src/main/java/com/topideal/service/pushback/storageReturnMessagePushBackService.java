package com.topideal.service.pushback;
/**
 * 仓储退运信息库存加减成功回推
 * @author 杨创
 *2019/02/26
 */
public interface storageReturnMessagePushBackService {
	/**
	 * 仓储退运信息库存加减成功回推
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean updatestorageReturnMessagePushBackInfo(String json, String keys, String topics, String tags)throws Exception;
}
