package com.topideal.mapper.order;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.LocationAdjustmentOrderModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface LocationAdjustmentOrderMapper extends BaseMapper<LocationAdjustmentOrderModel> {

	/**
	 *库位类型调整明细表
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getLocationAdjustmentOrder(Map<String, Object> map);

}
