package com.topideal.dao.order;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.SaleReturnOrderItemModel;

/**
 * 销售退货订单表体  dao
 * @author lian_
 */
public interface SaleReturnOrderItemDao extends BaseDao<SaleReturnOrderItemModel> {

	/**
	 * 获取销售退货量
	 * @param queryMap
	 * @return
	 */
	Integer getSaleReturnAccount(Map<String, Object> queryMap);

	/**
	 * 获取销售退货明细
	 */
	List<Map<String, Object>> getVipSaleReturnOdepot(Map<String, Object> queryMap);
		










}
