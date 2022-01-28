package com.topideal.report.service.automatic.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.automatic.VipAutomaticVerificationDao;
import com.topideal.dao.reporting.VipDifferenceAnalysisDao;
import com.topideal.dao.reporting.VipNondifferenceCheckDao;
import com.topideal.entity.dto.VipAutomaticVerificationDTO;
import com.topideal.entity.dto.VipDifferenceAnalysisDTO;
import com.topideal.entity.dto.VipNondifferenceCheckDTO;
import com.topideal.report.service.automatic.VipAutoVeriService;

@Service
public class VipAutoVeriServiceImpl implements VipAutoVeriService{

	@Autowired
	VipAutomaticVerificationDao vipAutomaticVerificationDao ;
	@Autowired
	VipDifferenceAnalysisDao vipDifferenceAnalysisDao ;
	@Autowired
	VipNondifferenceCheckDao vipNondifferenceCheckDao ;

	@Override
	public VipAutomaticVerificationDTO listVipAutoVerification(VipAutomaticVerificationDTO dto) throws SQLException {
		return vipAutomaticVerificationDao.getListByPage(dto);
	}

	@Override
	public List<VipAutomaticVerificationDTO> getExportList(VipAutomaticVerificationDTO dto) {
		return vipAutomaticVerificationDao.getExportList(dto);
	}

	@Override
	public List<VipDifferenceAnalysisDTO> getDifferenceExportList(VipDifferenceAnalysisDTO differenceDTO) {
		return vipDifferenceAnalysisDao.getDifferenceExportList(differenceDTO);
	}

	@Override
	public List<VipNondifferenceCheckDTO> getNoDifferenceExportList(VipNondifferenceCheckDTO nondifferenceCheckDTO) {
		return vipNondifferenceCheckDao.getNoDifferenceExportList(nondifferenceCheckDTO);
	}
}
