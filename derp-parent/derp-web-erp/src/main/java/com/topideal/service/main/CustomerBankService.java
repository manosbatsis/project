package com.topideal.service.main;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.main.CustomerBankDTO;

public interface CustomerBankService {



	/**
	 * 获取客户银行信息 (供应商银行信息回显)
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	List<CustomerBankDTO>  getCustomerBankList(CustomerBankDTO dto) throws SQLException;

	/**
	 * 根据商家+客户查询客户银行信息
	 * @param customerId
	 * @param merchantId
	 * @return
	 */
	List<CustomerBankDTO> getCustomerBySupplierId(Long customerId, Long merchantId);


	/**
	 * 根据客户+银行账户获取客户银行信息
	 * @return
	 */
	CustomerBankDTO getCustomerBankById(Long id) throws SQLException;
}
