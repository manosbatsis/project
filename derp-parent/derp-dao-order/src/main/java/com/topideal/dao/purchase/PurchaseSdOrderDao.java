package com.topideal.dao.purchase;

import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.purchase.PurchaseSdOrderDTO;
import com.topideal.entity.dto.purchase.PurchaseSdOrderPageDTO;
import com.topideal.entity.vo.purchase.PurchaseSdOrderModel;


public interface PurchaseSdOrderDao extends BaseDao<PurchaseSdOrderModel>{

	PurchaseSdOrderPageDTO getPurchaseSdOrderPageList(PurchaseSdOrderPageDTO dto);

	PurchaseSdOrderDTO searchDTOById(Long id);

	List<PurchaseSdOrderDTO> listDTO(PurchaseSdOrderPageDTO dto);

	List<PurchaseSdOrderPageDTO> getExportSdOrder(PurchaseSdOrderPageDTO dto);
		
	List<PurchaseSdOrderModel> getBXListDTO(PurchaseSdOrderModel model,Long orderId);









}
