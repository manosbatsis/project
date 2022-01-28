package com.topideal.order.service.bill;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.bill.AdvancePaymentBillDTO;
import com.topideal.entity.dto.bill.AdvancePaymentBillExportDTO;
import com.topideal.entity.dto.bill.AdvancePaymentBillItemDTO;
import com.topideal.entity.dto.bill.AdvancePaymentRecordItemDTO;
import com.topideal.entity.dto.bill.OperateLogDTO;
import com.topideal.entity.dto.purchase.PurchaseOrderDTO;

import java.sql.SQLException;
import java.util.List;

public interface AdvancePaymentBillService {
    /**
     * 获取分页
     * @param dto
     * @param user
     * @return
     */
    AdvancePaymentBillDTO listAdvancePaymentBill(AdvancePaymentBillDTO dto, User user);

    /**
     * 生成前检查
     * @param idList
     */
    void getCheckData(List<Long> idList) throws SQLException;

    /**
     * 新增或保存
     * @param dto
     * @param user
     * @return 
     */
    Long saveOrModifyAdvancePaymentBill(AdvancePaymentBillDTO dto, User user) throws SQLException;

    /**
     * 跳转新增页获取信息
     * @param idList
     * @return
     * @throws Exception 
     */
    AdvancePaymentBillDTO getAddPageInfo(List<Long> idList) throws SQLException, Exception;

	/**
	 * 获取详情
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	AdvancePaymentBillDTO getDetail(long id) throws SQLException;

	/**
	 * 删除
	 * @param user
	 * @param list
	 * @throws SQLException 
	 */
	void delAdvanceBill(User user, List<Long> list) throws SQLException;

	/**
	 * 作废申请
	 * @param list
	 * @param invalidRemark
	 * @param user
	 * @throws SQLException 
	 */
	void submitInvalidBill(List<Long> list, String invalidRemark, User user) throws SQLException;

	/**
	 * 付款
	 * @param dto
	 * @throws SQLException 
	 */
	void savePayment(AdvancePaymentRecordItemDTO dto, User user) throws SQLException;

	/**
	 * 编辑提交，修改选择审批方式
	 * @param user
	 * @param id
	 * @param auditMethod
	 * @throws SQLException 
	 * @throws Exception 
	 */
	void modifyAuditMethod(User user, Long id, String auditMethod) throws SQLException, Exception;

	/**
	 * 审核
	 * @param user
	 * @param id
	 * @param invalidRemark
	 * @param isPassed
	 * @throws SQLException 
	 */
	void auditAdvancePayment(User user, Long id, String invalidRemark, String isPassed) throws SQLException;

	/**
	 * 获取付款明细
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	List<AdvancePaymentBillItemDTO> getPaymentItems(Long id) throws SQLException;

	/**
	 * 获取操作日志
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	List<OperateLogDTO> listOperateLog(Long id) throws SQLException;

	/**
	 * 获取付款记录
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	List<AdvancePaymentRecordItemDTO> getRecordItemList(Long id) throws SQLException;

	/**
	 * 获取采购订单数据
	 * @param dto
	 * @param user
	 * @return
	 * @throws SQLException 
	 */
	PurchaseOrderDTO listPurchaseOrderPage(PurchaseOrderDTO dto, User user) throws SQLException;

	/**
	 * 导出excel
	 * @param exportDTO
	 * @param user 
	 * @return
	 * @throws SQLException 
	 */
	List<AdvancePaymentBillExportDTO> exportExcel(AdvancePaymentBillExportDTO exportDTO, User user) throws SQLException;

	/**
	 * 获取PO号
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	String getDetailPoNo(Long id) throws SQLException;

	/**
	 * 打印修改公司主体
	 * @param detail
	 * @return
	 */
	AdvancePaymentBillDTO changeMerchantInfo(AdvancePaymentBillDTO detail);
}
