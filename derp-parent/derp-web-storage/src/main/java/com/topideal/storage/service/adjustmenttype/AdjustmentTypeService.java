package com.topideal.storage.service.adjustmenttype;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.AdjustmentInventoryDTO;
import com.topideal.entity.dto.AdjustmentTypeDTO;
import com.topideal.entity.dto.AdjustmentTypeItemDTO;
import com.topideal.entity.dto.TakesStockResultsDTO;
import com.topideal.entity.vo.AdjustmentTypeModel;

/**
 * 类型调整
 * @author zhanghx
 */
public interface AdjustmentTypeService {

	/**
	 * 分页
	 * @param dto
	 * @return
	 */
	AdjustmentTypeDTO listByPage(AdjustmentTypeDTO dto) throws SQLException;
	
	/**
	 * 详情
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	AdjustmentTypeDTO getDetails(AdjustmentTypeDTO dto) throws SQLException;

	AdjustmentTypeDTO getAdjustDetails(Long id) throws SQLException;

	Map<String, String> confirmInDepot(User user, AdjustmentTypeDTO dto) throws Exception;
	/**
	 * 导入
	 * @param data
	 * @param userId
	 * @param userName
	 * @param merchantId
	 * @param merchantName
	 * @param relMerchantIds
	 * @return
	 * @throws Exception
	 */
	Map importAdjustment(List<List<Map<String, String>>> data, User user) throws Exception;
	/**
	 * 批量删除
	 * @param list
	 * @return
	 * @throws Exception
	 */
	boolean deleteByIds(List<Long> list) throws Exception;
	/**
	 * 确认调整（单据来源为导入的）
	 * @param id
	 * @param user
	 * @return
	 * @throws Exception
	 */
	boolean confirmAdjustment(Long id, User user) throws Exception;

	/**
	 * 根据条件查询导出类型调整单
	 */
	public List<Map<String, Object>> listForExport(AdjustmentTypeDTO dto);

	/**
	 * 根据条件查询导出类型调整单表体
	 */
	public List<Map<String, Object>> listForExportItem(AdjustmentTypeDTO dto);
}
