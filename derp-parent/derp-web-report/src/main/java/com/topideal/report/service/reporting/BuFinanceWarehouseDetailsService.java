package com.topideal.report.service.reporting;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.BuFinanceWarehouseDetailsDTO;

/**
 * 累计汇采购汇
 * */
public interface BuFinanceWarehouseDetailsService {

	
	
	/**
	 * 累计汇采购汇总分页
	 */
	public BuFinanceWarehouseDetailsDTO getListAddWarehouseByPage(User user,BuFinanceWarehouseDetailsDTO model) throws SQLException;


	/**
	 * 累计采购汇总导出
	 * @param model
	 * @return
	 * @throws SQLException
	 */
    List<BuFinanceWarehouseDetailsDTO> getListAddExport(User user,BuFinanceWarehouseDetailsDTO model) throws SQLException;

}
