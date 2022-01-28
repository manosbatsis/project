package com.topideal.order.service.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.BuMoveInventoryDTO;
import com.topideal.entity.dto.sale.BuMoveInventoryExportDTO;
import com.topideal.entity.dto.sale.BuMoveInventoryItemDTO;


/**
 * 事业部移库单 service
 * */
public interface BuMoveInventoryService {

	/**
	 * 列表（分页）
	 * @param dto
	 * @return
	 */
	BuMoveInventoryDTO listBuMoveInventoryByPage(BuMoveInventoryDTO dto,User user)throws SQLException;
	/**
	 * 根据ID获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	BuMoveInventoryDTO searchDetail(Long id) throws SQLException;
	/**
	 * 根据表头ID获取表体
	 * @param id
	 * @return
	 */
	List<BuMoveInventoryItemDTO> listItemByOrderId(Long id)throws SQLException;

	/**
	 * 根据条件获取信息
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<BuMoveInventoryDTO> listBuMoveInventoryDTO(BuMoveInventoryDTO dto,User user) throws SQLException;
	/**
	 * 导出
	 * @param dto
	 * @return
	 */
	List<BuMoveInventoryExportDTO> getDetailsByExport(BuMoveInventoryDTO dto,User user)throws SQLException;
	/**
	 * 物理删除
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	boolean delBuMoveInventory(List ids)throws Exception;
	/**
	 * 导入
	 * @param data
	 * @param user
	 * @return map
	 */
	Map saveImportBuMoveInventory(Map<Integer, List<List<Object>>> data, Long userId, String name, Long merchantId, String merchantName,String userTopidealCode);

	/**
	 * 审核
	 * @param list
	 * @param user
	 * @throws Exception
	 */
	void auditBuMoveInventory(List<Long> list ,User user) throws Exception;
}
