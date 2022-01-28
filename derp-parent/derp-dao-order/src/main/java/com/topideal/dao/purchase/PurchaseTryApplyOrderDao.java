package com.topideal.dao.purchase;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.purchase.PurchaseFrameContractDTO;
import com.topideal.entity.dto.purchase.PurchaseTryApplyOrderDTO;
import com.topideal.entity.vo.purchase.PurchaseTryApplyOrderModel;

import java.util.List;


public interface PurchaseTryApplyOrderDao extends BaseDao<PurchaseTryApplyOrderModel> {


    int batchSave(List<PurchaseTryApplyOrderModel> addList);

    PurchaseTryApplyOrderDTO listDTOByPage(PurchaseTryApplyOrderDTO dto);

    PurchaseTryApplyOrderDTO getDetail(PurchaseTryApplyOrderDTO dto);

    List<PurchaseTryApplyOrderDTO> listOaBillCodeSelect(PurchaseTryApplyOrderDTO dto);

    int countByDTO(PurchaseTryApplyOrderDTO dto);

    List<PurchaseFrameContractDTO> listForExport(PurchaseTryApplyOrderDTO dto);

    PurchaseTryApplyOrderDTO getLatestDTO(PurchaseTryApplyOrderDTO dto);
}
