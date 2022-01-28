package com.topideal.dao.common;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.common.PlatformSettlementConfigDTO;
import com.topideal.entity.vo.common.PlatformSettlementConfigModel;


public interface PlatformSettlementConfigDao extends BaseDao<PlatformSettlementConfigModel>{
		

	/**
	 * 分页查询
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	PlatformSettlementConfigDTO getPlatSettlementListByPage(PlatformSettlementConfigDTO dto) throws SQLException;
	
	/**
	 * 导出
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<PlatformSettlementConfigDTO> exportSettlementList(PlatformSettlementConfigDTO dto) throws SQLException;

	/**
	 * 获取状态为启用的
	 * @return
	 */
	List<PlatformSettlementConfigDTO> getSelectBean(String storePlatformCode);



}
