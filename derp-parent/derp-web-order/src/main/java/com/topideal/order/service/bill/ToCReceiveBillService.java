package com.topideal.order.service.bill;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.bill.*;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillAuditItemModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillVerifyItemModel;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.order.webapi.bill.form.TocReceiveBillInvoiceForm;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * toc应收账单service
 **/
public interface ToCReceiveBillService {

    /**
     * 获取分页
     */
    TocSettlementReceiveBillDTO listReceiveBillByPage(User user,TocSettlementReceiveBillDTO dto) throws SQLException;

    void saveTocSettlementBill(TocSettlementReceiveBillModel model, MultipartFile file) throws Exception;

    List<SelectBean> getCustomerListByShop(MerchantShopRelMongo mongo) throws Exception;

    TocSettlementReceiveBillDTO searchDTOById(Long id) throws SQLException;

    /**
     * 统计应收结算
     */
    List<Map<String, Object>> receiveTotalById(Long id) throws SQLException;

    /**
     * 统计抵扣费用
     */
    List<Map<String, Object>> deductionTotalById(Long id) throws SQLException;

    /**
     * 获取toc应收明细分页数据
     */
    TocSettlementReceiveBillItemDTO listReceiveItemByPage(TocSettlementReceiveBillItemDTO dto) throws SQLException;

    /**
     * 获取toc应收费用明细分页数据
     */
    TocSettlementReceiveBillCostItemDTO listReceiveCostItemByPage(TocSettlementReceiveBillCostItemDTO dto) throws SQLException;

    boolean delToCReceiveBill(List<Long> ids) throws Exception;

    List<SelectBean> getBrandSelectBeans();

    /**
     * 提交
     */
    Map<String, String> confirmReceiveBill(User user,Long id) throws Exception;

    /**
     * 审核
     */
    Map<String, String> auditReceiveBill(User user,TocSettlementReceiveBillAuditItemModel model, String billInDate) throws Exception;

    /**
     * 获取提交NC信息
     */
    List<ToCReceiveBillToNCDTO> listReceiveBillsToNCById(String ids) throws SQLException;

    /**
     * 同步NC
     */
    Map<String, String> synNC(User user,String ids) throws Exception;

    /**
     * 根据条件查询导出账单
     */
    List<Map<String, Object>> listForExport(TocSettlementReceiveBillDTO dto, User user) throws Exception;

    /**
     * 根据条件查询导出账单表体
     */
    List<Map<String, Object>> listForExportItem(Long id, User user)throws SQLException;

    //提交作废申请
    Map<String, String> saveInvalidBill(User user,String id, String invalidRemark) throws Exception;

    /**
     * 修改
     */
    int modify(TocSettlementReceiveBillModel  model) throws SQLException;

    /**
     * 校验toc结算单是否满足开票条件
     * @param ids
     */
    Map<String, String> validate(List<Long> ids) throws SQLException;


    Map<String, Object> getInvoiceInfo(String ids) throws Exception;

    /**
     * 根据json保存发票
     * @param map
     */
    void modifyJSONContract(Map<String, Object> map, User user) throws Exception;

    //根据应收账单id 批量导出PDF
    String exportNcBillPDF(List<Long> ids) throws Exception;

    /**
     * api保存发票
     * @param model
     * @param user
     * @throws Exception
     */
    public void modifyInvoiceContract(TocReceiveBillInvoiceForm model, User user) throws Exception;

    /**
     * 获取核销明细
     * @throws Exception
     */
    List<TocSettlementReceiveBillVerifyItemModel> listVerifyByBillId(Long billId) throws SQLException;

    /**
     * 保存核销明细
     * @throws Exception
     */
    Map<String, String> saveVerifyItem(TocSettlementReceiveBillVerifyItemDTO dto, User user) throws Exception;

    /**
     * 统计toc应收单的rmb金额
     **/
    BigDecimal statisticsRmbAmount(Long id) throws Exception;

    /**
     * 以费项维度汇总导出
     **/
    List<Map<String, Object>> listForSummaryExport(TocSettlementReceiveBillDTO dto, User user) throws Exception;

    /**
     * 生成发票html
     * @param form （tempId） 发票模板id
     * @param form （ids） 应收账单id，多个以英文逗号隔开
     * @param user
     */
    String generateInvoiceHtml(TocReceiveBillInvoiceForm form, User user) throws Exception;

    /**
     * 导出
     * @param task
     * @param basePath
     * @return
     */
    String createReceiveBillExcel(FileTaskMongo task, String basePath) throws Exception;

    /**
     * 结算单汇总导出
     * @param task
     * @param basePath
     * @return
     */
    String createReceiveBillSummaryExcel(FileTaskMongo task, String basePath) throws Exception;
}
