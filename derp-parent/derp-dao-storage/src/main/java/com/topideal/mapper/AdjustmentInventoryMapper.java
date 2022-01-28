package com.topideal.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.AdjustmentInventoryDTO;
import com.topideal.entity.vo.AdjustmentInventoryModel;

/**
 * 库存调整
 * @author lian_
 *
 */
@MyBatisRepository
public interface AdjustmentInventoryMapper extends BaseMapper<AdjustmentInventoryModel> {

	/**
	 * 获取详情
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	AdjustmentInventoryDTO getDetails(AdjustmentInventoryDTO dto) throws SQLException;
    
	
	/**
     * 查询所有数据
     * @return
     */
    PageDataModel<AdjustmentInventoryModel> getListByPage(AdjustmentInventoryModel model)throws SQLException;

	/**
	 * 查询所有数据
	 * @return dto
	 */
	PageDataModel<AdjustmentInventoryDTO> getDTOListByPage(AdjustmentInventoryDTO dto)throws SQLException;

	/**
	 * 根据条件查询导出库存调整单
	 */
	public List<Map<String, Object>> listForExport(AdjustmentInventoryDTO dto);

	/**
	 * 根据条件查询导出库存调整单表体
	 */
	public List<Map<String, Object>> listForExportItem(AdjustmentInventoryDTO dto);
}

