package com.topideal.dao.reporting;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.VipNondifferenceCheckDTO;
import com.topideal.entity.vo.reporting.VipNondifferenceCheckModel;


public interface VipNondifferenceCheckDao extends BaseDao<VipNondifferenceCheckModel> {

	int delByMap(Map<String, Object> delMap);

	List<VipNondifferenceCheckDTO> getNoDifferenceExportList(VipNondifferenceCheckDTO nondifferenceCheckDTO);
		










}
