package com.topideal.dao.reporting;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.SaleTargetAchievementDTO;
import com.topideal.entity.vo.reporting.SaleTargetAchievementModel;


public interface SaleTargetAchievementDao extends BaseDao<SaleTargetAchievementModel> {

	Integer deleteByMap(Map<String, Object> delMap);

	/**
	 * 分页获取
	 * @param dto
	 * @return
	 */
	SaleTargetAchievementDTO getListByPage(SaleTargetAchievementDTO dto);

	/**
	 * 获取导出列表
	 * @param dto
	 * @return
	 */
	List<SaleTargetAchievementDTO> getExportList(SaleTargetAchievementDTO dto);

	/**
	 * 获取存在平台
	 * @return
	 */
	List<String> getExsitplatform();

	/**
	 * 统计分页获取
	 * @param dto
	 * @return
	 */
	List<SaleTargetAchievementDTO> listGroupDto(SaleTargetAchievementDTO dto);
	Integer countGroupDto(SaleTargetAchievementDTO dto);

	/**
	 * 获取存在店铺
	 * @return
	 */
	List<String> getExsitShopName();
		










}
