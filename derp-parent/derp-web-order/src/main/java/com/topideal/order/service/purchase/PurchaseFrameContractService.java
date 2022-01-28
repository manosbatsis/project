package com.topideal.order.service.purchase;


import com.topideal.common.system.auth.User;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.purchase.PurchaseFrameContractDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/18 11:58
 * @Description: 采购框架合同
 */
public interface PurchaseFrameContractService {
	
	/**
	 * 获取框架合同或者试单的币种和交货方式
	 * @param purchaseFrameContractId
	 * @param purchaseTryApplyOrderId
	 * @return
	 * @throws SQLException
	 */
	PurchaseFrameContractDTO getContracOrTryApplyOrder(User user,String frameContractNo,String tryApplyCode)throws SQLException;
	
	/**
	 * 获取采购合同下拉框
	 * @param user
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseFrameContractDTO>listFrameContracSelect(PurchaseFrameContractDTO dto)throws SQLException;

    PurchaseFrameContractDTO listByPage(User user, PurchaseFrameContractDTO dto);

    /**
     * 获取详情
     * @param user
     * @param dto
     * @return
     */
    PurchaseFrameContractDTO getDetail(User user, PurchaseFrameContractDTO dto);

    int countByDTO(PurchaseFrameContractDTO dto);

    /**
     *
     * 采购框架合同导出
     * @param user
     * @param dto
     * @return
     */
    List<ExportExcelSheet> exportContractExcel(User user, PurchaseFrameContractDTO dto);

    String getLatestTime(PurchaseFrameContractDTO dto);
}
