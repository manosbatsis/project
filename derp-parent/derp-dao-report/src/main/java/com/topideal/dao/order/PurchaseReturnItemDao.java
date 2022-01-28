package com.topideal.dao.order;

import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.PurchaseReturnItemModel;


public interface PurchaseReturnItemDao extends BaseDao<PurchaseReturnItemModel> {

	int deleteByMap(Map<String, Object> delMap);
		










}
