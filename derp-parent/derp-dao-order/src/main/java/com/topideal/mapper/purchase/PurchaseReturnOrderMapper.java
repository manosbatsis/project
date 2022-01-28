package com.topideal.mapper.purchase;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.purchase.PurchaseReturnOrderDTO;
import com.topideal.entity.dto.purchase.PurchaseReturnOrderExportDTO;
import com.topideal.entity.vo.purchase.PurchaseReturnOrderModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface PurchaseReturnOrderMapper extends BaseMapper<PurchaseReturnOrderModel> {

	PageDataModel<PurchaseReturnOrderDTO> getListByPage(PurchaseReturnOrderDTO dto);

	PurchaseReturnOrderDTO getDTOById(@Param("id")Long id);

	List<PurchaseReturnOrderExportDTO> getDetailsByExport(PurchaseReturnOrderDTO dto);



}
