package com.topideal.dao.system;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.system.CustomerMerchantRelModel;


public interface CustomerMerchantRelDao extends BaseDao<CustomerMerchantRelModel> {

	List<CustomerMerchantRelModel> getExtraCustomerList(Map<String, Object> queryMap);
		










}
