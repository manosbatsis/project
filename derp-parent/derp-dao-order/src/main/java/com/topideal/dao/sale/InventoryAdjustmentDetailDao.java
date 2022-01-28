package com.topideal.dao.sale;

import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.sale.InventoryAdjustmentDetailModel;


public interface InventoryAdjustmentDetailDao extends BaseDao<InventoryAdjustmentDetailModel> {
		
    /**
     * 通过商家id、商品货号、po单号及业务类型统计数量
     */
    Integer countByDetailModel(InventoryAdjustmentDetailModel model);


	/**
	 * 根据条件获取国检抽样数量/唯品红冲值
	 * @param map
	 * @return
	 */
	Integer getModelCount(Map<String, Object> map);






}
