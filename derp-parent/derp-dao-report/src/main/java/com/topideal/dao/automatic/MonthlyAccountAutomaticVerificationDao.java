package com.topideal.dao.automatic;

import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.MonthlyAccountAutomaticVerificationDTO;
import com.topideal.entity.vo.automatic.MonthlyAccountAutomaticVerificationModel;


public interface MonthlyAccountAutomaticVerificationDao extends BaseDao<MonthlyAccountAutomaticVerificationModel> {

	int deleteByMap(Map<String, Object> params);

	MonthlyAccountAutomaticVerificationDTO listAutomaticVeriByPage(MonthlyAccountAutomaticVerificationDTO dto);

	Integer countList(MonthlyAccountAutomaticVerificationDTO dto);

	int modifyNullValue(MonthlyAccountAutomaticVerificationModel model);
		










}
