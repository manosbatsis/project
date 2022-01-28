package com.topideal.dao.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.reporting.BuFinanceDecreaseSaleNoshelfModel;


public interface BuFinanceDecreaseSaleNoshelfDao extends BaseDao<BuFinanceDecreaseSaleNoshelfModel> {
		

	/**
	 * 清除商家 仓库 月份 (事业部财务经销存)本期减少销售在途
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuFinanceDecreaseSaleNoshelf(Map<String, Object> map) throws SQLException;

	/**
	 * 获取本期减少销售在途导出
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<BuFinanceDecreaseSaleNoshelfModel>getdecreaseSaleNoshelfExport(Map<String, Object> map)throws SQLException;






}
