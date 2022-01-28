package com.topideal.dao.main;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.CustomerMainDTO;
import com.topideal.entity.vo.main.CustomerMainModel;


public interface CustomerMainDao extends BaseDao<CustomerMainModel> {

	CustomerMainDTO listCustomerMain(CustomerMainDTO dto);

	/**
	 * 获取dto
	 * @param id
	 * @return
	 */
	CustomerMainDTO searchDTOById(Long id);
		










}
