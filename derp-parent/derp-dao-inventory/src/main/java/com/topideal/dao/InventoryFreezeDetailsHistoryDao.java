package com.topideal.dao;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.vo.InventoryFreezeDetailsHistoryModel;

/**
 * 库存冻结明细 dao
 * @author lian_
 *
 */
public interface InventoryFreezeDetailsHistoryDao extends BaseDao<InventoryFreezeDetailsHistoryModel>{
		
	/**
	 * 新增历史记录
	 * @return
	 * @throws SQLException
	 */
	int insertHistory() throws SQLException;
	/**
	 * 根据ids批量新增历史记录
	 * @return
	 * @throws SQLException
	 */
	int insertBathHistory(List ids) throws SQLException;

}
