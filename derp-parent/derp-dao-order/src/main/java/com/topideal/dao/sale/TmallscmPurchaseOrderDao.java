package com.topideal.dao.sale;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.sale.TmallscmPurchaseOrderModel;


public interface TmallscmPurchaseOrderDao extends BaseDao<TmallscmPurchaseOrderModel> {
		

	/**
	 * 分页获取天猫平台采购订单数据
	 * @return
	 * @throws Exception
	 */
	List <TmallscmPurchaseOrderModel>getTmallPlatformPurchaseList(Map<String, Object> paramMap) throws Exception;








}
