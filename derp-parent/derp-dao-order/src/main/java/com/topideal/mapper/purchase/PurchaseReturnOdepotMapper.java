package com.topideal.mapper.purchase;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.purchase.PurchaseReturnOdepotDTO;
import com.topideal.entity.dto.purchase.PurchaseReturnOdepotExportDTO;
import com.topideal.entity.vo.purchase.PurchaseReturnOdepotModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface PurchaseReturnOdepotMapper extends BaseMapper<PurchaseReturnOdepotModel> {

	PageDataModel<PurchaseReturnOdepotDTO> getListByPage(PurchaseReturnOdepotDTO dto);

	PurchaseReturnOdepotDTO getDTOById(@Param("id")Long id);

	List<PurchaseReturnOdepotExportDTO> getDetailsByExport(PurchaseReturnOdepotDTO dto);



}
