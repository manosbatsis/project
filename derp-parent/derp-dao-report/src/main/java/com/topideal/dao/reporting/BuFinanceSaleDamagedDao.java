package com.topideal.dao.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.reporting.BuFinanceSaleDamagedModel;


public interface BuFinanceSaleDamagedDao extends BaseDao<BuFinanceSaleDamagedModel> {
		
	/**
	 * 清除事业部商家 仓库 月份 (财务经销存)销售残损明细表
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuFinanceSaleDamaged(Map<String, Object> map) throws SQLException;

	/**
	 * 获取销售残损明细导出
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<BuFinanceSaleDamagedModel>getsaleDamagedExport(Map<String, Object> map)throws SQLException;







}
