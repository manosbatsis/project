package com.topideal.mapper.purchase;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.purchase.PurchaseFrameContractDTO;
import com.topideal.entity.dto.purchase.PurchaseTryApplyOrderDTO;
import com.topideal.entity.vo.purchase.PurchaseTryApplyOrderModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;


@MyBatisRepository
public interface PurchaseTryApplyOrderMapper extends BaseMapper<PurchaseTryApplyOrderModel> {


    int batchSave(List<PurchaseTryApplyOrderModel> addList);

    PageDataModel<PurchaseTryApplyOrderDTO> listDTOByPage(PurchaseTryApplyOrderDTO dto);

    PurchaseTryApplyOrderDTO getDetail(PurchaseTryApplyOrderDTO dto);

    List<PurchaseTryApplyOrderDTO> listOaBillCodeSelect(PurchaseTryApplyOrderDTO dto);

    int countByDTO(PurchaseTryApplyOrderDTO dto);

    List<PurchaseFrameContractDTO> listForExport(PurchaseTryApplyOrderDTO dto);

    PurchaseTryApplyOrderDTO getLatestDTO(PurchaseTryApplyOrderDTO dto);
}
