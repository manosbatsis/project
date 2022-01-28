package com.topideal.report.service.automatic;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.VipAutomaticVerificationDTO;
import com.topideal.entity.dto.VipDifferenceAnalysisDTO;
import com.topideal.entity.dto.VipNondifferenceCheckDTO;

public interface VipAutoVeriService {

	/**
	 * 获取分页信息
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	VipAutomaticVerificationDTO listVipAutoVerification(VipAutomaticVerificationDTO dto) throws SQLException;

	/**
	 * 获取导出列表
	 * @param dto
	 * @return
	 */
	List<VipAutomaticVerificationDTO> getExportList(VipAutomaticVerificationDTO dto);

	/**
	 * 获取差异导出列表
	 * @param differenceDTO
	 * @return
	 */
	List<VipDifferenceAnalysisDTO> getDifferenceExportList(VipDifferenceAnalysisDTO differenceDTO);

	/**
	 * 无差异分析导出
	 * @param nondifferenceCheckDTO
	 * @return
	 */
	List<VipNondifferenceCheckDTO> getNoDifferenceExportList(VipNondifferenceCheckDTO nondifferenceCheckDTO);

}
