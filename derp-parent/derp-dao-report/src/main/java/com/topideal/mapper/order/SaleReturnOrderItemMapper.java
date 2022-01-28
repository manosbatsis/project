package com.topideal.mapper.order;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.SaleReturnOrderItemModel;
import com.topideal.mapper.BaseMapper;

/**
 * 销售退货订单表体  mapper
 * @author lian_
 */
@MyBatisRepository
public interface SaleReturnOrderItemMapper extends BaseMapper<SaleReturnOrderItemModel> {

	/**
	 * 获取唯品PO销售退数量
	 * @param queryMap
	 * @return
	 */
	Integer getSaleReturnAccount(Map<String, Object> queryMap);

	/**
	 * 获取唯品PO销售退明细
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getVipSaleReturnOdepot(Map<String, Object> queryMap);



}
