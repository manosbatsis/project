package com.topideal.order.service.purchase;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.purchase.PurchaseReturnOdepotDTO;
import com.topideal.entity.dto.purchase.PurchaseReturnOdepotExportDTO;
import com.topideal.entity.vo.purchase.PurchaseReturnOdepotItemModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface PurchaseReturnOdepotService {

	/**
	 * 获取分页数据
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	PurchaseReturnOdepotDTO listPurchaseReturnOdepotPage(PurchaseReturnOdepotDTO dto, User user) throws SQLException;

	/**
	 * 导入
	 * @param data
	 * @param user
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	Map<String, Object> importPurchaseReturnOdepot(List<Map<String, String>> data, User user) throws SQLException, Exception;

	/**
	 * 查询dto
	 * @param id
	 * @return
	 */
	PurchaseReturnOdepotDTO getDTOById(Long id);

	/**
	 * 根据ID查询明细
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseReturnOdepotItemModel> getItemListByOrderId(Long id) throws SQLException;

	/**
	 * 获取导出
	 * @param dto
	 * @return
	 */
	List<PurchaseReturnOdepotExportDTO> getDetailsByExport(PurchaseReturnOdepotDTO dto, User user);

	/**
	 * 审核
	 * @param list
	 * @param user
	 * @return
	 */
	boolean auditPurchaseReturnOrder(List<Long> list, User user) throws SQLException;

}
