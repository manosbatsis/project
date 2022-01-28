package com.topideal.report.service.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.SaleAmountTargetDTO;

public interface SaleAmountTargetService {

	SaleAmountTargetDTO listSaleAmountTarget(SaleAmountTargetDTO dto, User user) throws SQLException;

	Integer getOrderCount(SaleAmountTargetDTO dto, User user) throws SQLException;

	List<SaleAmountTargetDTO> exportAmountList(SaleAmountTargetDTO dto, User user);

	Map importAmountTarget(List<Map<String, String>> data, User user) throws SQLException;

	Integer delAmountTarget(String[] paramsArr) throws SQLException;


}
