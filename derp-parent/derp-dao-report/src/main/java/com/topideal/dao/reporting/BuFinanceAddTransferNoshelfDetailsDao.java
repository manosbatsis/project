package com.topideal.dao.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.reporting.BuFinanceAddTransferNoshelfDetailsModel;


public interface BuFinanceAddTransferNoshelfDetailsDao extends BaseDao<BuFinanceAddTransferNoshelfDetailsModel> {
		

	/**
	 * 清除商家 仓库 月份 (事业部财务经销存)累计调拨明细
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuFinanceAddTransferNoshelfDetail(Map<String, Object> map) throws SQLException;

	/**
	 * 累计调拨在途导出
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<BuFinanceAddTransferNoshelfDetailsModel>getAddTransferNoshelfExport(Map<String, Object> map) throws SQLException;;






}
