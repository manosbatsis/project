package com.topideal.mapper.reporting;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.InWarehouseTempModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface InWarehouseTempMapper extends BaseMapper<InWarehouseTempModel> {

	List<InWarehouseTempModel> getInWarehouseTempDetail(Map<String, Object> queryMap);



}
