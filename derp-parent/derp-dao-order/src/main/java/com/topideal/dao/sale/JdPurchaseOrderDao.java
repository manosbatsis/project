package com.topideal.dao.sale;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.sale.JdPurchaseOrderModel;


public interface JdPurchaseOrderDao extends BaseDao<JdPurchaseOrderModel> {
		

	/**
	 * 分页获取京东平台采购订单数据
	 * @return
	 * @throws Exception
	 */
	List <JdPurchaseOrderModel>getPlatformPurchaseList(Map<String, Object> paramMap) throws Exception;







}
