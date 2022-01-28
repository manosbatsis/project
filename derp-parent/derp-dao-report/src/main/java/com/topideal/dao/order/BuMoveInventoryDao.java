package com.topideal.dao.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.BuMoveInventoryModel;


public interface BuMoveInventoryDao extends BaseDao<BuMoveInventoryModel> {
		

	/**
	 * 获取(事业部财务经销存) 本期事业部移库明细
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getBuFinanceMoveDetails(Map<String, Object> map) throws SQLException;
	/**
	 * 获取(事业部财务经销存) 本期事业部移库明细加权
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getBuFinanceMoveDetailsWeighted(Map<String, Object> map) throws SQLException;

	
	/**
	 * (事业部财务经销存)本期事业部移库明细总数数量 (入)
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int getBuFinanceMoveInDetailsCount(Map<String, Object> map) throws SQLException;
	/**
	 * (事业部财务经销存)本期事业部移库明细总数数量 (入)加权
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int getBuFinanceMoveInDetailsCountWeighted(Map<String, Object> map) throws SQLException;
	
	/**
	 * (事业部财务经销存)本期事业部移库明细总数数量 (出)
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int getBuFinanceMoveOutDetailsCount(Map<String, Object> map) throws SQLException;
	
	/**
	 * 事业部移库单数据(入)
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getBuFinanceMoveDetailsInNum(Map<String, Object> map) throws SQLException;
	

	/**
	 * 事业部移库单数据(出)
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getBuFinanceMoveDetailsOutNum(Map<String, Object> map) throws SQLException;
	

	


}
