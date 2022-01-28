package com.topideal.mapper.order;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.YunjiReturnDetailModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface YunjiReturnDetailMapper extends BaseMapper<YunjiReturnDetailModel> {

	List<Map<String, Object>> getYjVeriDetail(Map<String, Object> queryMap);

	int changeVeriStatus(Map<String, Object> queryMap);

	Map<String, Object> getReturnAccount(Map<String, Object> queryPlatformMap);



}
