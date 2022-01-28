package com.topideal.mapper.sale;

import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.sale.InventoryAdjustmentDetailModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface InventoryAdjustmentDetailMapper extends BaseMapper<InventoryAdjustmentDetailModel> {

    Integer countByDetailModel(InventoryAdjustmentDetailModel model);

    
    
    
	/**
	 * 根据条件获取国检抽样数量/唯品红冲值
	 * @param map
	 * @return
	 */
	Integer getModelCount(Map<String, Object> map);
}
