package com.topideal.service;

import java.util.List;
import java.util.Map;

/**
 * 数据处理
 * @author 杨创
 *
 */
public interface DataOperationsService {
	
	/**
	 * 发送消息
	 * @return
	 * @throws Exception
	 */
	boolean sendMS(String json, String topic, String tags) throws Exception;

	/**
	 * 手动导入订单100
	 * @param data
	 * @return
	 */
	Map importOrder100Data(Map<Integer, List<List<Object>>> data);

	/**
	 * 手动导入库存回滚订单
	 * @param data
	 * @return
	 */
	Map importInventoryRollbackData(Map<Integer, List<List<Object>>> data);
	
	/**
	 * 获取回滚失败数据
	 * @param orderCodes
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getRollbackList(String orderCodes) throws Exception;
	/**
	 * 查询电商订单金额更新日志list
	 * */
	Map<String, Object> getAmountCoverLogList(String indexCode) throws Exception;

	/**
	 * 电商金额导入覆盖
	 * @param data
	 * @return
	 */
    Map importAmountCover(List<Map<String, String>> data) throws Exception;
}
