package com.topideal.report.service.reporting;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.SaleTargetAchievementDTO;
import com.topideal.entity.vo.system.BusinessUnitModel;

public interface SaleTargetAchievementService {

	List<BusinessUnitModel> getAllBuUnit() throws SQLException;

	/**
	 * 分页列表查询
	 * @param dto
	 * @return
	 */
	SaleTargetAchievementDTO getListByPage(User user,SaleTargetAchievementDTO dto);

	/**
	 * 获取导出列表
	 * @param dto
	 * @return
	 */
	List<SaleTargetAchievementDTO> getExportList(User user,SaleTargetAchievementDTO dto);

	/**
	 * 获取存在电商平台
	 * @return
	 */
	List<String> getExsitplatform();

	/**
	 * 获取存在电商店铺
	 * @return
	 */
	List<String> getExsitShopName();

}
