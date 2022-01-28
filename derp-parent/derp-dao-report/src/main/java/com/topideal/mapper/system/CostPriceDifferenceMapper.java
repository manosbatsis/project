package com.topideal.mapper.system;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.system.CostPriceDifferenceModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface CostPriceDifferenceMapper extends BaseMapper<CostPriceDifferenceModel> {


	/**
	 * 事业部财务经销存获取成本差异表数据
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getCostPriceDifferenceMapList(Map<String,Object> map)throws SQLException;


}
