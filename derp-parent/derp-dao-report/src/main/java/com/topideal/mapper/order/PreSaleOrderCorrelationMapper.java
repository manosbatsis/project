package com.topideal.mapper.order;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.PreSaleOrderCorrelationModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface PreSaleOrderCorrelationMapper extends BaseMapper<PreSaleOrderCorrelationModel> {

	/**
	 * 在库天数统计预售库存余量
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getInWarehouseSumAccount(Map<String, Object> queryMap);



}
