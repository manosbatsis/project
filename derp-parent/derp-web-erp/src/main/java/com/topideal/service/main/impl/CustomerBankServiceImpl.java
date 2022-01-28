package com.topideal.service.main.impl;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.vo.main.CustomerBankModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.main.CustomerBankDao;
import com.topideal.dao.main.CustomerBankMerchantRelDao;
import com.topideal.entity.dto.main.CustomerBankDTO;
import com.topideal.entity.dto.main.CustomerBankMerchantRelDTO;
import com.topideal.entity.vo.main.CustomerBankMerchantRelModel;
import com.topideal.service.main.CustomerBankService;

@Service
public class CustomerBankServiceImpl implements CustomerBankService{

	@Autowired
	private CustomerBankDao customerBankDao ;
	
	@Autowired
	private CustomerBankMerchantRelDao CustomerBankMerchantRelDao;
	/**
	 * 获取客户银行信息回显
	 */
	@Override
	public List<CustomerBankDTO>  getCustomerBankList(CustomerBankDTO dto) throws SQLException {
		List<CustomerBankDTO> bankList =customerBankDao.getCustomerBankList(dto);
		
				
		return bankList;
	}

	@Override
	public List<CustomerBankDTO> getCustomerBySupplierId(Long customerId, Long merchantId) {
		return customerBankDao.getCustomerBySupplierId(customerId,merchantId);
	}

	@Override
	public CustomerBankDTO getCustomerBankById(Long id) throws SQLException {
		CustomerBankModel model=new CustomerBankModel();
		model.setId(id);
		model=customerBankDao.searchByModel(model);

		CustomerBankDTO dto=new CustomerBankDTO();
		if(model!=null){
			BeanUtils.copyProperties(model, dto);
		}
		return dto;
	}


}
