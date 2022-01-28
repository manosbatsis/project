package com.topideal.service.main.impl;

import com.topideal.dao.main.CustomerInfoDao;
import com.topideal.dao.main.CustomerMainDao;
import com.topideal.entity.dto.main.CustomerMainDTO;
import com.topideal.service.main.CustomerMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CustomerMainServiceImpl implements CustomerMainService{

	@Autowired
	CustomerMainDao customerMainDao ;
	
	@Autowired
	CustomerInfoDao customerInfoDao ;

	@Override
	public CustomerMainDTO listCustomerMain(CustomerMainDTO dto) throws SQLException {
		CustomerMainDTO customerMainDTO = customerMainDao.listCustomerMain(dto);
		List<CustomerMainDTO> list = customerMainDTO.getList();
		list.forEach((entity) -> {
			entity.setCcodetypeLabel(entity.getCcodetypes());
		});
		return customerMainDTO;
	}

	@Override
	public CustomerMainDTO searchDetail(Long id) {
		CustomerMainDTO customerMainDTO = customerMainDao.searchDTOById(id);
		if(customerMainDTO != null) {
			customerMainDTO.setCcodetypeLabel(customerMainDTO.getCcodetypes());
		}
		return customerMainDTO;
	}

}
