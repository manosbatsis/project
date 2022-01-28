package com.topideal.mapper.automatic;

import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.MonthlyAccountAutomaticVerificationDTO;
import com.topideal.entity.vo.automatic.MonthlyAccountAutomaticVerificationModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface MonthlyAccountAutomaticVerificationMapper extends BaseMapper<MonthlyAccountAutomaticVerificationModel> {

	int deleteByMap(Map<String, Object> params);

	PageDataModel<MonthlyAccountAutomaticVerificationDTO> getListByPage(MonthlyAccountAutomaticVerificationDTO dto);

	Integer countList(MonthlyAccountAutomaticVerificationDTO dto);

	int modifyNullValue(MonthlyAccountAutomaticVerificationModel model);



}
