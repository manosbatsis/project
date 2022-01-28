package com.topideal.dao.system;


import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.system.BusinessUnitModel;

import java.util.List;
import java.util.Map;

public interface BusinessUnitDao extends BaseDao<BusinessUnitModel> {


    List<BusinessUnitModel> getListByMap(Map<String, Object> map);
}
