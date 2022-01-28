package com.topideal.service.main;

import java.sql.SQLException;

import com.topideal.entity.dto.main.CustomerMainDTO;
import com.topideal.entity.vo.main.CustomerMainModel;

public interface CustomerMainService {

	/**
	 * 获取主数据分页
	 * @param dto
	 * @return
	 * @throws SQLException 
	 */
	CustomerMainDTO listCustomerMain(CustomerMainDTO dto) throws SQLException;

	/**
	 * 获取详情
	 * @param id
	 * @return
	 */
	CustomerMainDTO searchDetail(Long id);

}
