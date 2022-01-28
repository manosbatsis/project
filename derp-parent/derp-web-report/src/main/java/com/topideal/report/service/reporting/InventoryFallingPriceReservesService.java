package com.topideal.report.service.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.InventoryFallingPriceReservesDTO;
import com.topideal.entity.dto.InventoryWarningCountDto;
import com.topideal.entity.vo.reporting.InventoryFallingPriceReservesModel;

public interface InventoryFallingPriceReservesService {

	/**
	 * 分页获取
	 * @param model
	 * @return
	 */
	InventoryFallingPriceReservesDTO listInventoryFallingPriceReservesPage(InventoryFallingPriceReservesDTO model);

	int getCount(InventoryFallingPriceReservesDTO dto);
	/**
	 * 根据IDS查询
	 * @param dto
	 * @return
	 * @throws SQLException 
	 */
	List<InventoryFallingPriceReservesDTO> getExportList(InventoryFallingPriceReservesDTO dto) throws SQLException;


	/**
	 * 根据商家、仓库、日期统计效期预警
	 * @Param
	 * @return
	 */
	List<InventoryWarningCountDto> countInventoryWarning(Map<String, Object> params) throws SQLException;

}
