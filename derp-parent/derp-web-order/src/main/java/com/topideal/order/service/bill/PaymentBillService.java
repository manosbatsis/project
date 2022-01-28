package com.topideal.order.service.bill;

import com.topideal.common.system.auth.User;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.bill.*;
import com.topideal.entity.dto.purchase.PurchaseOrderDTO;
import com.topideal.mongo.entity.FileTaskMongo;

import java.sql.SQLException;
import java.util.List;

public interface PaymentBillService {

	/**
	 * 查询分页数量
	 * @param dto
	 * @param user
	 * @return
	 */
	PaymentBillDTO listPaymentBill(PaymentBillDTO dto, User user);

	/**
	 * 查询采购订单
	 * @param dto
	 * @param user
	 * @return
	 * @throws SQLException 
	 */
	PurchaseOrderDTO listPurchaseOrderPage(PurchaseOrderDTO dto, User user) throws SQLException;

	/**
	 * 生成前检验
	 * @param idList
	 * @throws SQLException 
	 */
	void getCheckData(List<Long> idList) throws SQLException;

	/**
	 * 获取新增信息
	 * @param idList
	 * @return
	 * @throws SQLException 
	 * @throws Exception 
	 */
	PaymentBillDTO getAddPageInfo(List<Long> idList,String type) throws Exception;

	/**
	 * 新增或编辑
	 * @param dto
	 * @param user
	 * @return
	 * @throws SQLException 
	 * @throws Exception 
	 */
	Long saveOrModifyPaymentBill(PaymentBillDTO dto, User user) throws SQLException, Exception;

	/**
	 * 作废
	 * @param list
	 * @param invalidRemark
	 * @param user
	 * @throws SQLException 
	 */
	void submitInvalidBill(List<Long> list, String invalidRemark, User user) throws SQLException;

	/**
	 * 查询详情
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	PaymentBillDTO getDetail(long id) throws SQLException;

	/**
	 * 删除
	 * @param user
	 * @param list
	 * @throws SQLException 
	 */
	void delPaymentBill(User user, List<Long> list) throws SQLException;

	/**
	 * 核销
	 * @param dto
	 * @param user
	 * @throws SQLException 
	 */
	void saveVerificate(PaymentVerificateItemDTO dto, User user) throws SQLException;

	/**
	 * 获取应付明细
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	List<PaymentItemDTO> getPaymentItems(Long id) throws SQLException;

	/**
	 * 获取操作明细
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	List<OperateLogDTO> listOperateLog(Long id) throws SQLException;

	/**
	 * 获取费用明细
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	List<PaymentCostItemDTO> getCostItems(Long id) throws SQLException;

	/**
	 * 获取核销记录
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	List<PaymentVerificateItemDTO> getVerificateList(Long id) throws SQLException;

	/**
	 * 审核
	 * @param user
	 * @param id
	 * @param invalidRemark
	 * @param isPassed
	 * @throws SQLException 
	 */
	void auditPayment(User user, Long id, String invalidRemark, String isPassed) throws SQLException;

	/**
	 * 获取勾稽预付款单
	 * @param billDTO
	 * @return
	 * @throws SQLException 
	 */
	PaymentVerificateItemDTO getVeriAdvancePaymentList(AdvancePaymentBillDTO billDTO) throws SQLException;

	/**
	 * 编辑提交，修改选择审批方式
	 * @param user
	 * @param id
	 * @param auditMethod
	 * @throws SQLException 
	 * @throws Exception 
	 */
	void modifyAuditMethod(User user, Long id, String auditMethod) throws Exception;

	/**
	 * 同步NC
	 * @param id
	 * @throws Exception 
	 */
	void syncNC(Long id, User user) throws Exception;

	/**
	 * 获取PO号
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	String getDetailPoNo(Long id) throws SQLException;

	/**
	 * 获取汇总明细
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	List<PaymentSummaryDTO> getPaymentSummary(Long id) throws SQLException;

	/**
	 * 获取打印明细
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	List<PaymentPrintSummaryDTO> getListPrintSummary(Long id) throws SQLException;

	/**
	 * 打印修改公司主体
	 * @param detail
	 * @return
	 */
	PaymentBillDTO changeMerchantInfo(PaymentBillDTO detail);

	/**
	 * 更新打印信息
	 * @param id
	 * @param user
	 * @throws SQLException 
	 */
	void updatePrintingInfo(Long id, User user) throws SQLException;

	/**
	 * 应付帐单导出
	 * @param task
	 * @param basePath
	 * @return
	 */
	String createPaymentBillExcel(FileTaskMongo task, String basePath) throws Exception;

	/**
	 * count
	 * @return
	 */
    int countByDTO(PaymentBillDTO dto);

	/**
	 * 页面直接导出
	 * @param user
	 * @param dto
	 * @return
	 */
    List<ExportExcelSheet> exportPaymentBillExcel(User user, PaymentBillDTO dto);
}
