package com.topideal.dao.reporting;

import java.sql.SQLException;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.reporting.BuFinanceLocationAdjustmentDetailsModel;


public interface BuFinanceLocationAdjustmentDetailsDao extends BaseDao<BuFinanceLocationAdjustmentDetailsModel> {
		


	/**
	 * 删除(事业部财务经销存)库位类型调整明细表
	 * 
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delFinanceLocationAdjustmentDetails(Map<String, Object> map) throws SQLException;







}
