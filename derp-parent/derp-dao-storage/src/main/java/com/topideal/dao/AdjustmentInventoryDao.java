package com.topideal.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.AdjustmentInventoryDTO;
import com.topideal.entity.vo.AdjustmentInventoryModel;

/**
 * 库存调整
 * @author lian_
 *
 */
public interface AdjustmentInventoryDao extends BaseDao<AdjustmentInventoryModel>{
		
	/**
	 * 获取详情
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	AdjustmentInventoryDTO getDetails(AdjustmentInventoryDTO dto) throws SQLException;


	/**
	 * @Description 根据dto分页
	 * @Date 2019/12/30
	 * @return
	 */
	AdjustmentInventoryDTO searchByDTOPage(AdjustmentInventoryDTO dto) throws SQLException;

	/**
	 * 根据条件查询导出库存调整单
	 */
	public List<Map<String, Object>> listForExport(AdjustmentInventoryDTO dto);

	/**
	 * 根据条件查询导出库存调整单表体
	 */
	public List<Map<String, Object>> listForExportItem(AdjustmentInventoryDTO dto);
}

