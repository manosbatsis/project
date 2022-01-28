package com.topideal.report.service.automatic;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.MonthlyAccountAutomaticVerificationDTO;
import com.topideal.entity.vo.automatic.MonthlyAccountAutomaticVerificationItemModel;
import com.topideal.entity.vo.automatic.MonthlyAccountAutomaticVerificationModel;

public interface MonthlyAccountAutoVeriService {

	MonthlyAccountAutomaticVerificationDTO listAutomaticVeriByPage(MonthlyAccountAutomaticVerificationDTO dto);

	List<MonthlyAccountAutomaticVerificationItemModel> listForExport(MonthlyAccountAutomaticVerificationDTO dto) throws SQLException;

	MonthlyAccountAutomaticVerificationModel searchById(Long id) throws SQLException;

	int modifyNullValue(MonthlyAccountAutomaticVerificationModel model);

}
