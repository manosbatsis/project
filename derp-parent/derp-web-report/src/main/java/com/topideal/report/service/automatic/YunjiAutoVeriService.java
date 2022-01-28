package com.topideal.report.service.automatic;

import java.util.List;

import com.topideal.entity.dto.YunjiAutomaticVerificationDTO;

public interface YunjiAutoVeriService {

	YunjiAutomaticVerificationDTO listYunjiAutoVerification(YunjiAutomaticVerificationDTO dto);

	int getExportCount(YunjiAutomaticVerificationDTO dto);

	List<YunjiAutomaticVerificationDTO> getExportList(YunjiAutomaticVerificationDTO dto);

}
