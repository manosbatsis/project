package com.topideal.dao.order;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.InventoryLocationAdjustmentOrderModel;


public interface InventoryLocationAdjustmentOrderDao extends BaseDao<InventoryLocationAdjustmentOrderModel> {
		
	/**
	 * 获取库存调整单
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getLocationAdjustmentOrder(Map<String, Object> param);
	



}
