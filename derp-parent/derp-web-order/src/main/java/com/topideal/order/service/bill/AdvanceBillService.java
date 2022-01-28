package com.topideal.order.service.bill;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.bill.AdvanceBillDTO;
import com.topideal.entity.dto.bill.AdvanceBillDataDTO;
import com.topideal.entity.dto.bill.AdvanceBillDatasDTO;
import com.topideal.entity.dto.bill.ReceiveBillToNCDTO;
import com.topideal.entity.dto.bill.ReceiveBillVerifyAdvanceDTO;
import com.topideal.order.webapi.bill.form.AdvanceAuditForm;
import com.topideal.order.webapi.bill.form.InvoiceTemplateYSForm;

/**
 * 预收账单接口
 */
public interface AdvanceBillService {
    /**
     * 保存
     * @param templateForm
     * @param user
     */
    void saveContract(User user, InvoiceTemplateYSForm templateForm) throws Exception;
    /**
     * 生成发票html
     * @param tempId 发票模板id
     * @param ids 应收账单id，多个以英文逗号隔开
     * @param user
     */
    String generateInvoiceHtml(Long tempId, String ids, String invoiceSource, User user) throws Exception;
	/**
	 * 预收开票校验
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	 Map<String, String> validate(List<Long> ids) throws SQLException;
	/**
	 * 预付账单导出
	 * @param dto
	 * @return
	 */
	Map<String, Object>exportAdvanceBill (User user,AdvanceBillDTO dto) throws SQLException;

    /**
     * 分页查询数据
     * @param dto
     * @return
     * @throws SQLException
     */
    AdvanceBillDTO listAdvanceBillByPage(User user, AdvanceBillDTO dto) throws IOException, SQLException;

    /**
     * 新增获取业务单据
     * @param dto
     * @return
     * @throws IOException
     */
    AdvanceBillDataDTO listAddOrderByPage(User user, AdvanceBillDatasDTO dto,String type) throws IOException, SQLException;

    /**
     * 校验是否是同客户+同事业部+同币种
     * @param list
     * @return
     */
    Map checkBuMerchantCurrency(List<AdvanceBillDataDTO> list);


    /**
     * 保存预收账单，不做必填项校验
     * @param dto
     * @return
     */
    Map<String, Object>  saveAdvanceBill(String type,AdvanceBillDTO dto) throws IOException;

    /**
     * 提交申请作废
     * @param id
     * @param invalidRemark
     * @param token
     */
    Map<String, Object>  submitInvalidBill(Long id,String invalidRemark,String token) throws Exception;


    /**
     * 根据id查看详情
     * @param id
     * @return
     */
    AdvanceBillDTO getDetail(Long id) throws SQLException, InvocationTargetException, IllegalAccessException;


    /**
     * 删除预收账单
     * @param userId
     * @param ids
     * @return
     */
    Map<String, Object> delAdvanceBill(long userId,List<Long> ids);


    /**
     * 审核
     * @param from
     * @return
     */
    Map<String,Object> auditAdvanceItem(AdvanceAuditForm from) throws IOException, Exception;


    /**
     * 获取关联预收单提交NC信息
     * @param id
     * @return
     * @throws SQLException
     */
    List<ReceiveBillToNCDTO> listAdvanceBillsToNCById(String token,long id) throws SQLException;


    /**
     * 同步NC
     */
    Map<String, Object> synNC(long id,String token) throws Exception;


    /**
     * 导出预收账单
     * @param list
     * @return
     */
    String exportNcBillPDF(List<Long> list) throws IOException, Exception;


    /**
     * 应收核销勾稽预收单分页查询数据
     * @param dto
     * @return
     * @throws SQLException
     */
    ReceiveBillVerifyAdvanceDTO listVerifyAdvanceByPage(ReceiveBillVerifyAdvanceDTO dto) throws Exception;


    /**
     * 更新凭证信息
     * @param ids
     * @return
     * @throws Exception
     */
    Map<String, Object> updateVoucher(List<Long> ids) throws Exception;
}
