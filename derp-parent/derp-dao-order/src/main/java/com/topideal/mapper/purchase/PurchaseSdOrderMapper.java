package com.topideal.mapper.purchase;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.purchase.PurchaseSdOrderDTO;
import com.topideal.entity.dto.purchase.PurchaseSdOrderPageDTO;
import com.topideal.entity.vo.purchase.PurchaseSdOrderModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface PurchaseSdOrderMapper extends BaseMapper<PurchaseSdOrderModel> {

	List<PurchaseSdOrderPageDTO> getPurchaseSdOrderPageList(PurchaseSdOrderPageDTO dto);

	Integer countPurchaseSdOrderPageNum(PurchaseSdOrderPageDTO dto);

	PurchaseSdOrderDTO searchByDTO(PurchaseSdOrderDTO dto);

	List<PurchaseSdOrderDTO> listDTO(PurchaseSdOrderPageDTO dto);

	List<PurchaseSdOrderPageDTO> getExportSdOrder(PurchaseSdOrderPageDTO dto);
	
	List<PurchaseSdOrderModel> getBXListDTO(@Param("model")PurchaseSdOrderModel model,@Param("orderId")Long orderId);


}
