package com.topideal.dao.reporting;

import java.sql.SQLException;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.reporting.BuFinanceSaleNotshelfModel;


public interface BuFinanceSaleNotshelfDao extends BaseDao<BuFinanceSaleNotshelfModel> {
		

	/**
	 * 清除商家 仓库 月份 (事业部财务经销存)销售未上架明细表
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuFinanceSaleNotshelf(Map<String, Object> map) throws SQLException;








}
