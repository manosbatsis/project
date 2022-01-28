package com.topideal.report.service.reporting.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.system.CustomerInfoDao;
import com.topideal.entity.vo.system.CustomerInfoModel;
import com.topideal.report.service.reporting.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService{

	// 客户信息
	@Autowired
	private CustomerInfoDao customerInfoDao;
	
	/**
	 * 根据id获取客户详情
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public CustomerInfoModel getDetails(Long id) throws SQLException {
		CustomerInfoModel model = new CustomerInfoModel();
		model.setId(id);
		return customerInfoDao.getDetails(model);
	}

	/**
	 * 获取使用中的客户列表
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SelectBean> listAllCustomer() throws SQLException {
		return customerInfoDao.listAllCustomer();
	}


}
