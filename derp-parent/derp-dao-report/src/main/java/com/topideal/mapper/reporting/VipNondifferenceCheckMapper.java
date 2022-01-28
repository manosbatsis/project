package com.topideal.mapper.reporting;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.VipNondifferenceCheckDTO;
import com.topideal.entity.vo.reporting.VipNondifferenceCheckModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface VipNondifferenceCheckMapper extends BaseMapper<VipNondifferenceCheckModel> {

	int delByMap(Map<String, Object> delMap);

	List<VipNondifferenceCheckDTO> getNoDifferenceExportList(VipNondifferenceCheckDTO nondifferenceCheckDTO);



}
