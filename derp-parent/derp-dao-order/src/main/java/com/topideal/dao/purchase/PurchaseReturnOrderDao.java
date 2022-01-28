package com.topideal.dao.purchase;

import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.purchase.PurchaseReturnOrderDTO;
import com.topideal.entity.dto.purchase.PurchaseReturnOrderExportDTO;
import com.topideal.entity.vo.purchase.PurchaseReturnOrderModel;


public interface PurchaseReturnOrderDao extends BaseDao<PurchaseReturnOrderModel> {

	PurchaseReturnOrderDTO listPurchaseReturnPage(PurchaseReturnOrderDTO dto);

	PurchaseReturnOrderDTO getDTOById(Long id);

	List<PurchaseReturnOrderExportDTO> getDetailsByExport(PurchaseReturnOrderDTO dto);
		










}
