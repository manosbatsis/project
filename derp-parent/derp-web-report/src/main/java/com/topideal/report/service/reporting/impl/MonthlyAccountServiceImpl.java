package com.topideal.report.service.reporting.impl;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.inventory.MonthlyAccountDao;
import com.topideal.entity.vo.inventory.MonthlyAccountModel;
import com.topideal.report.service.reporting.MonthlyAccountService;

@Service
public class MonthlyAccountServiceImpl implements MonthlyAccountService{
   
	@Autowired
	private MonthlyAccountDao monthlyAccountDao;
	
	public MonthlyAccountModel searchByModel(MonthlyAccountModel model) throws SQLException{
		return monthlyAccountDao.searchByModel(model);
	}
}
