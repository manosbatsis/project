package com.topideal.mapper.reporting;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.SaleTargetAchievementDTO;
import com.topideal.entity.vo.reporting.SaleTargetAchievementModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface SaleTargetAchievementMapper extends BaseMapper<SaleTargetAchievementModel> {

	Integer deleteByMap(Map<String, Object> delMap);

	PageDataModel<SaleTargetAchievementDTO> getListByPage(SaleTargetAchievementDTO dto);

	List<SaleTargetAchievementDTO> getExportList(SaleTargetAchievementDTO dto);

	List<String> getExsitplatform();

	List<SaleTargetAchievementDTO> listGroupDto(SaleTargetAchievementDTO dto);

	Integer countGroupDto(SaleTargetAchievementDTO dto);

	List<String> getExsitShopName();



}
