package com.topideal.service.timer;

/**
 * 收款核销跟踪 生成和刷新Service
 * @author 杨创
 *2020/07/30
 */
public interface SaveReceiveBillVerificationService {
	/**
	 * 收款核销跟踪 生成和刷新MQ Service
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @return
	 * @throws Exception
	 */
	public boolean saveReceiveBillVerification (String json,String keys,String topics,String tags) throws Exception;


	/**
	 * 收款核销跟踪 作废删除
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @return
	 * @throws Exception
	 */
	public boolean delReceiveBillVerification(String json,String keys,String topics,String tags) throws Exception;
}
