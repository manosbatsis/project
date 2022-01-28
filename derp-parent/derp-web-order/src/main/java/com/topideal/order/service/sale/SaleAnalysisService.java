package com.topideal.order.service.sale;

import java.sql.SQLException;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.SaleAnalysisDTO;



/**
 * 销售出库分析 service
 * */
public interface SaleAnalysisService {

	/**
	 * 列表（分页）
	 * @param model 
	 * @return
	 */
	SaleAnalysisDTO listSaleAnalysisDTO(SaleAnalysisDTO dto,User user)throws SQLException;
}
