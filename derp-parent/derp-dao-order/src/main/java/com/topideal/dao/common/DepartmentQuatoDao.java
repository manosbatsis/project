package com.topideal.dao.common;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.common.DepartmentQuatoDTO;
import com.topideal.entity.vo.common.DepartmentQuatoModel;


public interface DepartmentQuatoDao extends BaseDao<DepartmentQuatoModel>{

	DepartmentQuatoDTO getListByPage(DepartmentQuatoDTO dto);

	DepartmentQuatoModel getLatestDepartmentQuato(Map<String, Object> queryMap);

	/**
	 * 获取是否重合时间段列表
	 * @param queryModel
	 * @return
	 */
	List<DepartmentQuatoModel> getCoincidenceTimeList(DepartmentQuatoModel queryModel);
		










}
