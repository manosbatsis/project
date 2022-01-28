package com.topideal.dao.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.BuFinanceWarehouseDetailsDTO;
import com.topideal.entity.vo.reporting.BuFinanceWarehouseDetailsModel;


public interface BuFinanceWarehouseDetailsDao extends BaseDao<BuFinanceWarehouseDetailsModel> {
		

	/**
	 * 清除事业部商家 仓库 月份 (财务经销存)采购入库明细数据
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuDepotMonthFinanceWarehouseDetails(Map<String, Object> map) throws SQLException;


	/**
	 * 总账导出 获取财务经销存本期入库
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getAllAccountFinPurWarehouse(Map<String, Object> map)throws SQLException;
	
	/**
	 * 获取财务入库成本差异
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getInCostDifferenceExport(Map<String, Object> map)throws SQLException;
	

	/**
	 * 本期采购入库导出
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<BuFinanceWarehouseDetailsModel>getWarehouseDetailExport(Map<String, Object> map)throws SQLException;


	/**
	 * 累计汇采购汇总分页
	 */
	public BuFinanceWarehouseDetailsDTO getListAddWarehouse(BuFinanceWarehouseDetailsDTO model) throws SQLException;

	/**
	 * 累计采购汇总导出
	 * @param model
	 * @return
	 * @throws SQLException
	 */
    List<BuFinanceWarehouseDetailsDTO> getListAddExport(BuFinanceWarehouseDetailsDTO model) throws SQLException;
    
    /**
	 * 财务进销存入账汇总表 (采购)
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getPurchaseSummaryExpotr(Map<String, Object> paramMap);
}
