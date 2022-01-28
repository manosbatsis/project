package com.topideal.mapper.common;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.common.DepartmentQuatoDTO;
import com.topideal.entity.vo.common.DepartmentQuatoModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface DepartmentQuatoMapper extends BaseMapper<DepartmentQuatoModel> {

	PageDataModel<DepartmentQuatoDTO> getListByPage(DepartmentQuatoDTO dto);

	DepartmentQuatoModel getLatestDepartmentQuato(Map<String, Object> queryMap);

	List<DepartmentQuatoModel> getCoincidenceTimeList(DepartmentQuatoModel queryModel);



}
