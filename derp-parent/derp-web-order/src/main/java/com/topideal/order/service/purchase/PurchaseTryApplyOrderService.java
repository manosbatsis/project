package com.topideal.order.service.purchase;


import com.topideal.common.system.auth.User;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.purchase.PurchaseTryApplyOrderDTO;

import java.util.List;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/18 11:58
 * @Description: 采购框架合同
 */
public interface PurchaseTryApplyOrderService {

    PurchaseTryApplyOrderDTO listByPage(User user, PurchaseTryApplyOrderDTO dto);

    /**
     * 获取详情
     * @param user
     * @param dto
     * @return
     */
    PurchaseTryApplyOrderDTO getDetail(User user, PurchaseTryApplyOrderDTO dto);

    /**
     * 采购试单申请编号列表
     * @param user
     * @param dto
     * @return
     */
    List<PurchaseTryApplyOrderDTO> listOaBillCodeSelect(User user, PurchaseTryApplyOrderDTO dto);

    int countByDTO(PurchaseTryApplyOrderDTO dto);

    /**
     * 导出
     * @param user
     * @param dto
     * @return
     */
    List<ExportExcelSheet> exportContractExcel(User user, PurchaseTryApplyOrderDTO dto);

    String getLatestTime(PurchaseTryApplyOrderDTO dto);
}
