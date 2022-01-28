package com.topideal.dao.main;

import java.sql.SQLException;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.main.CustomerBankMerchantRelModel;


public interface CustomerBankMerchantRelDao extends BaseDao<CustomerBankMerchantRelModel> {
		


	/**
	 * 根据条件删除 商家客户银行信息
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int deleteByParam(Map<String, Object> map)throws SQLException;







}
