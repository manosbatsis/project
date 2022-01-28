package com.topideal.mapper.order;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.InventoryLocationAdjustmentOrderModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface InventoryLocationAdjustmentOrderMapper extends BaseMapper<InventoryLocationAdjustmentOrderModel> {

	
/**
 * 获取库存调整单
 * @param param
 * @return
 */
List<Map<String, Object>> getLocationAdjustmentOrder(Map<String, Object> param);

}
