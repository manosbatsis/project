package com.topideal.mapper.system;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface BusinessUnitMapper extends BaseMapper<BusinessUnitModel> {


    List<BusinessUnitModel> getListByMap(Map<String, Object> map);
}
