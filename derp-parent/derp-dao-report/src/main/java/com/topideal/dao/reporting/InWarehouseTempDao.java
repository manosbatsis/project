package com.topideal.dao.reporting;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.reporting.InWarehouseTempModel;


public interface InWarehouseTempDao extends BaseDao<InWarehouseTempModel> {

	List<InWarehouseTempModel> getInWarehouseTempDetail(Map<String, Object> queryMap);
		










}
