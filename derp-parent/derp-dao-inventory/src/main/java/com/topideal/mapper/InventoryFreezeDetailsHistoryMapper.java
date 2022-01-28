package com.topideal.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.InventoryFreezeDetailsHistoryModel;

/**
 * 库存冻结明细 mapper
 * @author lian_
 */
@MyBatisRepository
public interface InventoryFreezeDetailsHistoryMapper extends BaseMapper<InventoryFreezeDetailsHistoryModel> {

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
	int insertBathHistory(@Param("list")List list) throws SQLException;

}
