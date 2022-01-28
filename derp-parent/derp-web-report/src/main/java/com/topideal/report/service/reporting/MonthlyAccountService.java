package com.topideal.report.service.reporting;

import java.sql.SQLException;

import com.topideal.entity.vo.inventory.MonthlyAccountModel;

public interface MonthlyAccountService {
   
	public MonthlyAccountModel searchByModel(MonthlyAccountModel model) throws SQLException;
}
