package com.topideal.mapper.system;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.system.CustomerMerchantRelModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface CustomerMerchantRelMapper extends BaseMapper<CustomerMerchantRelModel> {

	List<CustomerMerchantRelModel> getExtraCustomerList(Map<String, Object> queryMap);



}
