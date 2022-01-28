package com.topideal.order.service.purchase;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.purchase.PurchaseWarehouseDTO;
import com.topideal.entity.dto.purchase.PurchaseWarehouseExportBean;
import com.topideal.entity.dto.purchase.PurchaseWarehouseExportDTO;
import com.topideal.entity.vo.purchase.PurchaseWarehouseModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 采购入库单 service
 * */
public interface PurchaseWarehouseService {

	/**
	 * 列表（分页）
	 * @param model
	 * @return
	 */
	PurchaseWarehouseDTO listPurchaseWarehousePage(PurchaseWarehouseDTO dto, User user)throws SQLException;
	/**
	 * 根据ID获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	PurchaseWarehouseModel searchDetail(Long id) throws SQLException;

	/**
	 * 导入关联采购单
	 * @param data
	 * @param user
	 * @return map
	 * @throws SQLException
	 * @throws Exception
	 */
	Map<String, Object> importRelation(List<Map<String, String>> data, User user) throws Exception;

	/**
	 * 导入入库单
	 * @param data
	 * @param user
	 * @return map
	 * @throws Exception
	 */
	Map<String, Object> saveImportWarehouse(List<Map<String, String>> data, User user) throws Exception;

	/**
	 * 确认入库
	 * @param ids
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	List<InvetAddOrSubtractRootJson> confirmDepot(List<Long> ids, Long userId, String name, String topidealCode) throws SQLException, Exception;

	/**
	 * 入库明细导出
	 * @param ids
	 * @param dto
	 * @return
	 */
	List<PurchaseWarehouseExportDTO> getExportDetails(List ids, PurchaseWarehouseDTO dto,User user) throws SQLException;

	/**
	 * 入库勾稽明细导出
	 * @param ids
	 * @return
	 */
	List<PurchaseWarehouseExportBean> getPurchaseExportDetails(List ids, Long userId) throws SQLException;

	/**
	 * 入库勾稽明细校验仓库类型
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	String checkWarehouseDepotType(List<Long> ids) throws SQLException;
	/**
	 * 根据id删除(支持批量)
	 * @param ids
	 * @return
	 */
	public void delBatchByIds(List<Long> ids) throws SQLException;

	PurchaseWarehouseDTO searchDTODetail(Long id) throws SQLException;

	/**
	 * 采购入库回推库存
	 * @param jsonList
	 * @throws SQLException
	 */
	void pushInventory(List<InvetAddOrSubtractRootJson> jsonList, User user) throws SQLException;

}
