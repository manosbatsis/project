package com.topideal.dao.purchase;

import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.purchase.PurchaseReturnOdepotDTO;
import com.topideal.entity.dto.purchase.PurchaseReturnOdepotExportDTO;
import com.topideal.entity.vo.purchase.PurchaseReturnOdepotModel;


public interface PurchaseReturnOdepotDao extends BaseDao<PurchaseReturnOdepotModel> {

	PurchaseReturnOdepotDTO getListByPage(PurchaseReturnOdepotDTO dto);

	PurchaseReturnOdepotDTO getDTOById(Long id);

	List<PurchaseReturnOdepotExportDTO> getDetailsByExport(PurchaseReturnOdepotDTO dto);
		










}
