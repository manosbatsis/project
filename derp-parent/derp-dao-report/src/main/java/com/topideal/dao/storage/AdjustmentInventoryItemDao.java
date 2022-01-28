package com.topideal.dao.storage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.storage.AdjustmentInventoryItemModel;

/**
 * 库存调整详情表
 * @author zhanghx
 */
public interface AdjustmentInventoryItemDao extends BaseDao<AdjustmentInventoryItemModel> {

	/**
	 * 获取库存调整数量
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> getAdjustmentNum(Map<String, Object> map) throws SQLException;
	
	/**
	 * 根据库存调整ids 获取获取商品
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	List<Map<String,Object>> getList(List ids)throws SQLException;

	/**
	 * 根据业务类型获取数量
	 * @param queryMap
	 * @return
	 */
	Integer getInventoryNumByModel(Map<String, Object> queryMap);

	/**
	 * 获取唯品PO库存调整明细
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getVipAjuInvenDetails(Map<String, Object> queryMap);

	/**
	 * 获取唯品自动校验库存调整数
	 * @param queryMap
	 * @return
	 */
	Integer getInventorySystemNumByModel(Map<String, Object> queryMap);
	
}

