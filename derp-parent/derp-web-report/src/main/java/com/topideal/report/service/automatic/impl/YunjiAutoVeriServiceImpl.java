package com.topideal.report.service.automatic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.automatic.YunjiAutomaticVerificationDao;
import com.topideal.entity.dto.YunjiAutomaticVerificationDTO;
import com.topideal.report.service.automatic.YunjiAutoVeriService;

@Service
public class YunjiAutoVeriServiceImpl implements YunjiAutoVeriService{

	@Autowired
	private YunjiAutomaticVerificationDao veriDao ;
	
	@Override
	public YunjiAutomaticVerificationDTO listYunjiAutoVerification(YunjiAutomaticVerificationDTO dto) {
		dto = veriDao.listByPage(dto) ;
		return dto;
	}

	@Override
	public int getExportCount(YunjiAutomaticVerificationDTO dto) {
		return veriDao.getExportCount(dto);
	}

	@Override
	public List<YunjiAutomaticVerificationDTO> getExportList(YunjiAutomaticVerificationDTO dto) {
		return veriDao.getExportList(dto);
	}

	
}
