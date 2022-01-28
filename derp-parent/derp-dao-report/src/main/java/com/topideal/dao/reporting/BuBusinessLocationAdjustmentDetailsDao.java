package com.topideal.dao.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.reporting.BuBusinessLocationAdjustmentDetailsModel;


public interface BuBusinessLocationAdjustmentDetailsDao extends BaseDao<BuBusinessLocationAdjustmentDetailsModel> {
		

	/**
	 * 删除(事业部业务经销存)库位类型调整明细表
	 * 
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuBusinessLocationAdjustmentDetails(Map<String, Object> map) throws SQLException;


	/**
	 * 业务经销存库位类型调整明细
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getLocationAdjustmentDetailsForMap(Map<String, Object> map);




}
