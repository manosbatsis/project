package com.topideal.service.main;

import java.sql.SQLException;

import com.topideal.common.system.auth.User;
import com.topideal.entity.vo.main.CustomerMerchantRelModel;

/**
 * 客户公司中间表信息service
 * @author tsh
 */
public interface CustomerMerchantRelService {
	/**
	 * 根据客户id查询中间表信息
	 * @param id
	 * @return
	 */
	CustomerMerchantRelModel searchDetail(Long id,User user);

	/**
	 * 根据客户供应商Id和商家获取对应税率
	 * @param merchantId
	 * @param customerId
	 * @return
	 * @throws SQLException 
	 */
	CustomerMerchantRelModel getRateByCustomerAndMerchant(Long merchantId, Long customerId) throws SQLException;
}
