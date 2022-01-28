package com.topideal.dao.purchase;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.purchase.PurchaseFrameContractDTO;
import com.topideal.entity.vo.purchase.PurchaseFrameContractModel;

import java.sql.SQLException;
import java.util.List;


public interface PurchaseFrameContractDao extends BaseDao<PurchaseFrameContractModel> {

	/**
	 * 获取采购合同下拉框
	 * @param user
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseFrameContractDTO>listFrameContracSelect(PurchaseFrameContractDTO dto)throws SQLException;
    /**
     * 批量新增
     * @param addList
     */
    int batchSave(List<PurchaseFrameContractModel> addList);

    /**
     * 分页查询DTO
     * @param dto
     * @return
     */
    PurchaseFrameContractDTO listDTOByPage(PurchaseFrameContractDTO dto);

    /**
     * 获取详情
     * @param dto
     * @return
     */
    PurchaseFrameContractDTO getDetail(PurchaseFrameContractDTO dto);

    int countByDTO(PurchaseFrameContractDTO dto);

    List<PurchaseFrameContractDTO> listForExport(PurchaseFrameContractDTO dto);

    PurchaseFrameContractDTO getLatestDTO(PurchaseFrameContractDTO dto);
}
