package com.topideal.mapper.purchase;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.purchase.PurchaseFrameContractDTO;
import com.topideal.entity.vo.purchase.PurchaseFrameContractModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface PurchaseFrameContractMapper extends BaseMapper<PurchaseFrameContractModel> {

	/**
	 * 获取采购合同下拉框
	 * @param user
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseFrameContractDTO>listFrameContracSelect(PurchaseFrameContractDTO dto)throws SQLException;
	
    int batchSave(List<PurchaseFrameContractModel> addList);

    PageDataModel<PurchaseFrameContractDTO> listDTOByPage(PurchaseFrameContractDTO dto);

    PurchaseFrameContractDTO getDetail(PurchaseFrameContractDTO dto);

    int countByDTO(PurchaseFrameContractDTO dto);

    List<PurchaseFrameContractDTO> listForExport(PurchaseFrameContractDTO dto);

    PurchaseFrameContractDTO getLatestDTO(PurchaseFrameContractDTO dto);
}
