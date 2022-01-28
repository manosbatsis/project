package com.topideal.mapper.reporting;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.VipDifferenceAnalysisDTO;
import com.topideal.entity.vo.reporting.VipDifferenceAnalysisModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface VipDifferenceAnalysisMapper extends BaseMapper<VipDifferenceAnalysisModel> {

	int delByMap(Map<String, Object> delMap);

	List<VipDifferenceAnalysisDTO> getDifferenceExportList(VipDifferenceAnalysisDTO differenceDTO);



}
