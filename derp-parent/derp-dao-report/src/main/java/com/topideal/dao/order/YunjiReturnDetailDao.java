package com.topideal.dao.order;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.YunjiReturnDetailModel;


public interface YunjiReturnDetailDao extends BaseDao<YunjiReturnDetailModel> {

	List<Map<String, Object>> getYjVeriDetail(Map<String, Object> queryMap);

	int changeVeriStatus(Map<String, Object> paramsMap);

	Map<String, Object> getReturnAccount(Map<String, Object> queryPlatformMap);
		










}
