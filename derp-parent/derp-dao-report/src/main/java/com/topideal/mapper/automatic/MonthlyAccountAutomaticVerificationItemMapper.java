package com.topideal.mapper.automatic;

import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.automatic.MonthlyAccountAutomaticVerificationItemModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface MonthlyAccountAutomaticVerificationItemMapper extends BaseMapper<MonthlyAccountAutomaticVerificationItemModel> {

	int deleteByMap(Map<String, Object> params);



}
