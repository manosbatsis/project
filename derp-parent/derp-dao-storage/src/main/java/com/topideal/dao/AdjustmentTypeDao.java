package com.topideal.dao;

import com.topideal.entity.dto.AdjustmentTypeDTO;
import com.topideal.entity.vo.AdjustmentTypeModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 类型调整
 * @author lian_
 *
 */
public interface AdjustmentTypeDao extends BaseDao<AdjustmentTypeModel>{
		
	/**
	 * 分页
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	AdjustmentTypeDTO getListByPage(AdjustmentTypeDTO dto) throws SQLException;

	/**
	 * 详情
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	AdjustmentTypeDTO getDetails(AdjustmentTypeDTO dto) throws SQLException;

	AdjustmentTypeDTO getAdjustDetails(Long id) throws SQLException;

	/**
	 * 根据条件查询导出类型调整单
	 */
	public List<Map<String, Object>> listForExport(AdjustmentTypeDTO dto);

	/**
	 * 根据条件查询导出类型调整单表体
	 */
	public List<Map<String, Object>> listForExportItem(AdjustmentTypeDTO dto);

}

