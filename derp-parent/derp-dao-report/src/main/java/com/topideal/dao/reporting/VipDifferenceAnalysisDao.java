package com.topideal.dao.reporting;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.VipDifferenceAnalysisDTO;
import com.topideal.entity.vo.reporting.VipDifferenceAnalysisModel;


public interface VipDifferenceAnalysisDao extends BaseDao<VipDifferenceAnalysisModel> {

	int delByMap(Map<String, Object> delMap);

	List<VipDifferenceAnalysisDTO> getDifferenceExportList(VipDifferenceAnalysisDTO differenceDTO);
		










}
