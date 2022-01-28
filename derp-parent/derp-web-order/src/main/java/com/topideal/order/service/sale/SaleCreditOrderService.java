package com.topideal.order.service.sale;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.SaleCreditBillOrderDTO;
import com.topideal.entity.dto.sale.SaleCreditOrderDTO;
import com.topideal.entity.dto.sale.SaleCreditOrderExportDTO;
import com.topideal.entity.dto.sale.SaleCreditOrderItemDTO;
import com.topideal.entity.vo.sale.SaleCreditBillOrderItemModel;
import com.topideal.entity.vo.sale.SaleCreditBillOrderModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SaleCreditOrderService {

	/**
	 * 获取列表 分页
	 * @param dto
	 * @return
	 * @throws Exception
	 */
    SaleCreditOrderDTO listDTOByPage(SaleCreditOrderDTO dto, User user) throws Exception;

	/**
	 * 统计类型数量
	 * @param dto
	 * @return
	 */
	List<Map<String, Object>> getCreditTypeNum(SaleCreditOrderDTO dto,User user) throws Exception ;
	/**
	 * 获取详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
    SaleCreditOrderDTO searchDetail(Long id) throws Exception;
	/**
	 * 获取表体详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
    List<SaleCreditOrderItemDTO> searchItemsByOrderId(Long id) throws Exception;
	/**
	 * 获取收款单详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
    List<SaleCreditBillOrderModel> searchBillOrderByOrderId(Long id,String status) throws Exception;
	/**
	 * 获取收款单表体详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
    List<SaleCreditBillOrderItemModel> searchBillItemsByOrderId(Long id) throws Exception;
	/**
	 * 保存赊销单
	 * @param id
	 * @throws Exception
	 */
    void saveCreditOrder(SaleCreditOrderDTO dto, User user) throws Exception;
	/**
	 * 提交
	 * @param id
	 * @throws Exception
	 */
    void submitOrder(Long id, User user) throws Exception;
	/**
	 * 收到保证金
	 * @param id
	 * @throws Exception
	 */
    void updateMarginOrder(Long id, String receiveMarginDate, String actualMarginAmount, String remark, User user) throws Exception;
	/**
	 * 确认放款
	 * @param id
	 * @throws Exception
	 */
    void confirmOrder(Long id, String loanDate, String valueDate, String remark, User user, String sapienceLoanDate) throws Exception;
	/**
	 * 访问申请还款页面
	 */
    SaleCreditOrderDTO getRepayDetail(Long id) throws Exception;
	/**
	 * 还款试算
	 * @return
	 * @throws Exception
	 */
    SaleCreditBillOrderDTO calRepayAmount(SaleCreditOrderDTO dto, User user, String receiveDate, BigDecimal discountDelayAmount) throws Exception;
	/**
	 * 申请还款
	 * @param dto
	 * @param user
	 * @param receiptDate
	 * @return
	 * @throws Exception
	 */
    SaleCreditBillOrderDTO applyRefund(SaleCreditOrderDTO dto, User user, String receiveDate, BigDecimal discountDelayAmount, String discountReason) throws Exception;
	/**
	 * 提交还款
	 * @param id
	 * @throws Exception
	 */
    void saveCreditBill(SaleCreditOrderDTO dto, User user, String receiveDate,BigDecimal discountDelayAmount, String discountReason) throws Exception;
	/**
	 * 确认还款
	 * @param id
	 * @throws Exception
	 */
    void confirmCreditBill(Long billId, String accountDate, String remark, User user) throws Exception;

	/**
	 * 删除赊销单
	 * @param id
	 * @throws Exception
	 */
    void deleteCreditOrder(String ids, User user) throws Exception;
	/**
	 * 导出结算单
	 * @param id
	 * @throws Exception
	 */
    SaleCreditOrderExportDTO exportCreditOrder(Long id) throws Exception;
	/**
	 * 导出试算结果
	 * @param id
	 * @throws Exception
	 */
    SaleCreditOrderExportDTO exportCalResult(String json) throws Exception;
}
