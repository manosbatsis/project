package com.topideal.dao.order;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.LocationAdjustmentOrderModel;


public interface LocationAdjustmentOrderDao extends BaseDao<LocationAdjustmentOrderModel> {
		
	/**
	 *库位类型调整明细表
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getLocationAdjustmentOrder(Map<String, Object> map);









}
