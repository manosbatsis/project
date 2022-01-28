package com.topideal.mapper;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.AdjustmentTypeDTO;
import com.topideal.entity.vo.AdjustmentTypeModel;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 类型调整
 * @author lian_
 */
@MyBatisRepository
public interface AdjustmentTypeMapper extends BaseMapper<AdjustmentTypeModel> {

	/**
	 * 分页
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<AdjustmentTypeDTO> getListByPage(AdjustmentTypeDTO dto) throws SQLException;

	/**
	 * 详情
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	AdjustmentTypeDTO getDetails(AdjustmentTypeDTO dto) throws SQLException;

	AdjustmentTypeDTO getAdjuestDetails(@Param("id") Long id) throws SQLException;

	/**
	 * 根据条件查询导出类型调整单
	 */
	public List<Map<String, Object>> listForExport(AdjustmentTypeDTO dto);

	/**
	 * 根据条件查询导出类型调整单表体
	 */
	public List<Map<String, Object>> listForExportItem(AdjustmentTypeDTO dto);

}

