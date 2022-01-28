package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.BuFinanceTakesStockResultsModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuFinanceTakesStockResultsMapper extends BaseMapper<BuFinanceTakesStockResultsModel> {
		
	/**
	 * 清除事业部商家 仓库 月份 (财务经销存)盘盈盘亏明细数据
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuFinanceTakesStockResults(Map<String, Object> map) throws SQLException;
	/**
	 * 获取总账导出 盘盈盘亏明细表
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getAllAccountGroupByType(Map<String, Object> map)throws SQLException;
	/**
	 * 获取盘点结果导出
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<BuFinanceTakesStockResultsModel>getTakesStockResultExport(Map<String, Object> map) throws SQLException;



}
