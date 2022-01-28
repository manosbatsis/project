package com.topideal.report.service.reporting;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.SaleDataDTO;

/**
 * 累计销售汇总
 * */
public interface SaleDataService {

	
	
	/**
	 * 累计汇销售汇总分页
	 */
	public SaleDataDTO getListAddSaleByPage(User user,SaleDataDTO model) throws SQLException;


	/**
	 * 累计销售汇总导出
	 * @param model
	 * @return
	 * @throws SQLException
	 */
    List<SaleDataDTO> getListAddExport(User user,SaleDataDTO model) throws SQLException;

}
