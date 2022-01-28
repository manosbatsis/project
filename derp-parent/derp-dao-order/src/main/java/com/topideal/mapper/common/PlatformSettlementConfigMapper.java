package com.topideal.mapper.common;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.common.PlatformSettlementConfigDTO;
import com.topideal.entity.vo.common.PlatformSettlementConfigModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


@MyBatisRepository
public interface PlatformSettlementConfigMapper extends BaseMapper<PlatformSettlementConfigModel> {

	/**
	 * 分页查询
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<PlatformSettlementConfigDTO>  getPlatSettlementListByPage(PlatformSettlementConfigDTO dto) throws SQLException;
	
	/**
	 * 导出
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<PlatformSettlementConfigDTO> exportSettlementList(PlatformSettlementConfigDTO dto) throws SQLException;


	/**
	 * 获取
	 * @return
	 */
	List<PlatformSettlementConfigDTO> getSelectBean(@Param("storePlatformCode") String storePlatformCode);
}
