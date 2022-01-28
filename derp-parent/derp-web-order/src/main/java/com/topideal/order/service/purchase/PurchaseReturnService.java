package com.topideal.order.service.purchase;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.purchase.PurchaseReturnItemDTO;
import com.topideal.entity.dto.purchase.PurchaseReturnOdepotItemDTO;
import com.topideal.entity.dto.purchase.PurchaseReturnOrderDTO;
import com.topideal.entity.dto.purchase.PurchaseReturnOrderExportDTO;
import com.topideal.entity.vo.purchase.PurchaseReturnItemModel;
import com.topideal.entity.vo.purchase.PurchaseReturnOdepotModel;
import com.topideal.entity.vo.purchase.PurchaseReturnOrderModel;
import com.topideal.order.webapi.sale.form.MerchandiseForm;

import java.sql.SQLException;
import java.util.List;

public interface PurchaseReturnService {

	PurchaseReturnOrderDTO listPurchaseReturnPage(PurchaseReturnOrderDTO dto, User user) throws SQLException;

	void isSameParameter(String ids) throws SQLException, RuntimeException;

	PurchaseReturnOrderModel listPurchaseOrderAndDepotOrder(List<Long> parseIds, Long merchantId) throws SQLException;

	String getDepotTypeByDepotId(Long outDepotId);

	/**
	 * 保存
	 * @param model
	 * @param itemList
	 * @param user
	 * @throws Exception
	 */
	Long saveOrModifyPurchaseReturnOrder(PurchaseReturnOrderModel model, User user, String operate) throws Exception;

	/**
	 * 保存并审核
	 * @param model
	 * @param itemList
	 * @param user
	 * @throws Exception
	 */
	void auditPurchaseReturnOrder(PurchaseReturnOrderModel model, User user) throws Exception;

	PurchaseReturnOrderDTO getDTOById(Long id);

	List<PurchaseReturnItemModel> getItemListByOrderId(Long id) throws SQLException;

	/**
	 * 删除
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	boolean delPurchaseReturnOrder(List<Long> list) throws SQLException;

	/**
	 * 采购退货订到导出
	 * @param dto
	 * @return
	 */
	List<PurchaseReturnOrderExportDTO> getDetailsByExport(PurchaseReturnOrderDTO dto, User user);

	/**
	 * 打托出库
	 * @param returnModel
	 * @param itemList
	 * @param user
	 * @throws SQLException
	 * @throws Exception
	 */
	void savePurchaseReturnOdepot(PurchaseReturnOdepotModel returnModel, List<PurchaseReturnOdepotItemDTO> itemList, User user) throws SQLException, Exception;

	/**
	 * 采购退选择商品弹窗
	 * @param form
	 * @return
	 * @throws Exception
	 */
	PurchaseReturnItemDTO getPurchaseItemPopupByPage(MerchandiseForm form, User user) throws Exception;

}
