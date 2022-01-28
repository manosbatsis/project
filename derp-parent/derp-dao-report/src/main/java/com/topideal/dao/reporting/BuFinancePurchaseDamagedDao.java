package com.topideal.dao.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.reporting.BuFinancePurchaseDamagedModel;


public interface BuFinancePurchaseDamagedDao extends BaseDao<BuFinancePurchaseDamagedModel> {
		
	/**
	 * 清除事业部商家 仓库 月份 (财务经销存)采购残损明细表
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuFinancePurchaseDamaged(Map<String, Object> map) throws SQLException;

	/**
	 * 获取采购残损明细导出
	 */
	List<BuFinancePurchaseDamagedModel>getPurchaseDamagedExport(Map<String, Object> map)throws SQLException;







}
