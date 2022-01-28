package com.topideal.storage.service.adjustmenttype;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.AdjustmentInventoryDTO;
import com.topideal.entity.vo.AdjustmentInventoryModel;

/**
 * 库存调整
 * @author zhanghx
 */
public interface AdjustmentInventoryService {

	/**
	 * 获取详情
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	AdjustmentInventoryDTO getDetails(AdjustmentInventoryDTO dto) throws SQLException;
	
	/**
	 * 获取详情
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	AdjustmentInventoryModel searchByModel(AdjustmentInventoryModel model) throws SQLException;
	
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	AdjustmentInventoryDTO listByPage(AdjustmentInventoryDTO model) throws SQLException;
	
	/**
	 * 审核
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> auditAdjustment(List<Long> ids, User user) throws Exception;
	
	/**
	 * 批量删除
	 * @param list
	 * @return
	 */
	boolean deleteByIds(List<Long> list) throws Exception;
	
	/**
	 * 导入
	 * @param data
	 * @param user
	 * @return map
	 */
	Map importAdjustment(List<List<Map<String, String>>> data, User user) throws Exception;
	/**
	 * 分组统计调整单调减商品调整数量
	 * 
	 * */
	public List<Map<String, Object>> getItemByInventoryIds(List<Long> inventoryIds, User user) throws Exception;
   
	public int modify(AdjustmentInventoryModel model) throws SQLException;

	/**
	 * 通过id批量获取库存调整单详情信息
	 */
	public List<Map<String, Object>> getByInventoryIds(List<Long> inventoryIds);

	/**
	 * @Description 保存库存调整单表体事业部信息，并校验是否推送库存
	 */
	Map<String, String> saveBuDetails(AdjustmentInventoryDTO dto, User user) throws Exception;

	/**
	 * 根据条件查询导出库存调整单
	 */
	public List<Map<String, Object>> listForExport(AdjustmentInventoryDTO dto);

	/**
	 * 根据条件查询导出库存调整单表体
	 */
	public List<Map<String, Object>> listForExportItem(AdjustmentInventoryDTO dto);
}
