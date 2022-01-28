package com.topideal.dao.system;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.system.CostPriceDifferenceModel;


public interface CostPriceDifferenceDao extends BaseDao<CostPriceDifferenceModel> {
		


	/**
	 * 事业部财务经销存获取成本差异表数据
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getCostPriceDifferenceMapList(Map<String,Object> map)throws SQLException;







}
