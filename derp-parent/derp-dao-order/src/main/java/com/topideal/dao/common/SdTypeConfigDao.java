package com.topideal.dao.common;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.common.SdTypeConfigDTO;
import com.topideal.entity.vo.common.SdTypeConfigModel;


public interface SdTypeConfigDao extends BaseDao<SdTypeConfigModel> {

	/**
	 * 分页获取
	 * @param dto
	 * @return
	 */
	SdTypeConfigDTO getSdTypeConfigListByPage(SdTypeConfigDTO dto);
		










}
