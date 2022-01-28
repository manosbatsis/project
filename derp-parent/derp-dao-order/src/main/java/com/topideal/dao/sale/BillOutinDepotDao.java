package com.topideal.dao.sale;

import java.sql.Timestamp;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.BillOutinDepotDTO;
import com.topideal.entity.dto.sale.BillOutinDepotItemDTO;
import com.topideal.entity.vo.sale.BillOutinDepotModel;


public interface BillOutinDepotDao extends BaseDao<BillOutinDepotModel> {

	BillOutinDepotDTO listBillOutinDepotByPage(BillOutinDepotDTO dto);

	BillOutinDepotDTO searchDTOById(Long id);

	BillOutinDepotModel searchUnDelModel(BillOutinDepotModel tempModel);

	Integer getOrderCount(BillOutinDepotDTO dto);

	List<BillOutinDepotDTO> listDto(BillOutinDepotDTO dto);

	List<BillOutinDepotItemDTO> getExportItemList(BillOutinDepotDTO dto);

	/**
	 * 查询出业务单号集合和同个事业部的账单出库单的最大出库时间
	 */
	Timestamp getMaxOutDepotDate(List<String> billCodes, Long buId);









}
