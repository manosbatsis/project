package com.topideal.order.service.purchase;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.purchase.PurchaseInvoiceDTO;

import java.sql.SQLException;

public interface PurchaseInvoiceService {

	PurchaseInvoiceDTO getInvocieByPurchaseId(Long id) throws Exception;

	/**
	 * 新增
	 * @param dto
	 * @param user
	 * @throws SQLException
	 */
	void saveInvoice(PurchaseInvoiceDTO dto, User user) throws SQLException;

	/**
	 * 获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	PurchaseInvoiceDTO getDetailsById(Long id) throws SQLException;

	/**
	 * 分页获取
	 * @param dto
	 * @return
	 */
	PurchaseInvoiceDTO getListByPage(PurchaseInvoiceDTO dto, User user);

	/**
	 * 删除
	 * @param ids
	 * @param user
	 * @throws SQLException
	 */
	void delInvoice(String ids, User user) throws SQLException;

}
