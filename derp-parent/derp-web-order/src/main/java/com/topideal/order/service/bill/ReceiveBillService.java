package com.topideal.order.service.bill;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.bill.*;
import com.topideal.entity.dto.transfer.TransferOrderDTO;
import com.topideal.entity.dto.transfer.TransferOrderItemDTO;
import com.topideal.entity.vo.bill.ReceiveBillAuditItemModel;
import com.topideal.entity.vo.bill.ReceiveBillCostItemModel;
import com.topideal.entity.vo.bill.ReceiveBillModel;
import com.topideal.entity.vo.sale.SaleShelfModel;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.order.webapi.bill.form.*;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 应收账单service
 * @Author: Chen Yiluan
 * @Date: 2020-07-27 15:01
 **/
public interface ReceiveBillService {

    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
    ReceiveBillDTO listReceiveBillByPage(ReceiveBillDTO dto, User user) throws SQLException;

    /**
     * 获取新增选择单据分页
     * @param dto
     * @return
     * @throws SQLException
     */
    ReceiveBillDTO listAddOrderByPage(ReceiveBillDTO dto, User user) throws SQLException;

    /**
     * 根据业务单号查询对应应收账单是否存在
     */
    Map<String, String> isExistOrder(ReceiveBillDTO dto) throws SQLException;

    /**
     * 保存
     */
    Map<String, String> saveReceiveBill(ReceiveBillDTO dto, User user) throws Exception;

    /**
     * 校验并保存
     */
    Map<String, String> verifyAndSaveReceiveBill(List<ReceiveBillAddForm> addForms, User user) throws Exception;


    ReceiveBillDTO searchDTOById(Long id) throws SQLException;

    /**
     * 统计应收结算
     */
    List<Map<String, Object>> receiveTotalById(Long id) throws SQLException;

    /**
     * 统计抵扣费用
     */
    List<ReceiveBillCostItemDTO> deductionTotalById(Long id) throws SQLException;

    Map<String, String> confirmReceiveBill(ReceiveBillSubmitForm form, User user) throws Exception;


    Map<String, String> auditReceiveBill(ReceiveBillAuditForm form, User user) throws Exception;

    /**
     * 刷新
     * */
    Map<String, String> refreshReceiveBill(Long id, User user) throws Exception;

    /**
     * 根据id删除(支持批量)
     * @param ids
     * @return
     */
    public boolean delReceiveBill(List<Long> ids) throws Exception;

    List<Map<String, Object>> listItemInfos(List<Long> ids, Long merchantId, User user) throws Exception;

    MerchantInfoMongo getMerchantInfo(Long merchantId) throws SQLException;

    CustomerInfoMogo getCustomer(Long id) throws SQLException;

    /**
     * 根据json保存发票
     * @param map
     */
    void modifyJSONContract(Map<String, Object> map, String invoiceSource) throws Exception;

    List<String> getCodes(List<Long> ids) throws SQLException;

    List<String> getRelCodes(List<Long> ids) throws SQLException;

    ByteArrayOutputStream exportTempDateFile(String orderCode, String fileTempCode) throws Exception;

    Map<String, String> validate(List<Long> ids) throws SQLException;

    List<Map<String, Object>> listWPItemInfos(List<Long> ids, Long merchantId) throws SQLException;

    List<Map<String, Object>> listSaleShelfInfo(String orderCode) throws SQLException;

    Map<String, String> isExistRelInvoice(String id) throws SQLException;

    //提交作废申请
    Map<String, String> saveInvalidBill(String ids, String invalidRemark, User user) throws Exception;

    //根据id集合查询应收账单
    List<ReceiveBillDTO> listByIds(List<Long> ids) throws SQLException;

    Map<String, String> validateRelCodeInfo(List<Long> ids) throws SQLException;

    /**
     * 根据应收账单单号查询对应应收账单是否存在
     */
    Map<String, String> isBillSave(String billCode) throws SQLException;

    /**
     * 根据条件查询导出账单账单
     */
    public Map<Long, ReceiveBillDTO> listForExport(ReceiveBillDTO dto, User user) throws SQLException;

    //京东唯品开票信息
    List<Map<String, Object>> listJDItemInfos(List<Long> ids, Long merchantId, User user) throws Exception;

    Map<String, String> updateVoucher(List<Long> ids) throws Exception;

    /**
     * 根据条件查询导出应收明细
     */
    List<Map<String, Object>> listForItemExport(List<Long> ids, Map<Long, String> idsMap, Map<Long, String> customerNameMap) throws SQLException;

    /**
     * 根据条件查询导出应收费用明细
     */
    List<Map<String, Object>> listForCostItemExport(List<Long> ids, Map<Long, String> idsMap, Map<Long, String> customerNameMap) throws SQLException;

    /**
     * 保存创建应收
     */
    Map<String, Object> saveApiAddReceiveBill(ReceiveBillDTO dto, User user) throws Exception;

    //考拉开票信息
    List<Map<String, Object>> listKLItemInfos(List<Long> ids, Long merchantId) throws Exception;
    List<Map<String, Object>> listYZItemInfos(List<Long> ids, Long merchantId, User user) throws Exception;

    /**
     * 获取应收明细分页数据
     */
    List<ReceiveBillItemDTO> listReceiveItem(ReceiveBillDTO dto) throws SQLException;

    /**
     * 获取应收费用明细分页数据
     */
    List<ReceiveBillCostItemDTO> listReceiveCostItem(ReceiveBillDTO dto) throws SQLException;

    /**
     * 应收明细导入
     * @param data
     * @param billId
     * @param user
     * @return
     */
    Map importReceiveItems(List<List<Map<String, String>>> data, Long billId, User user) throws SQLException;

    /**
     * 应收明细导出
     * @param json
     * @return
     */
    List<ReceiveBillItemDTO> exportListItem(String json);

    Map<String, String> saveReceiveBillFromEdit(ReceiveBillSubmitForm form, User user) throws Exception;

    /**
     * 费用明细导入
     * @param data
     * @param billId
     * @param user
     * @return
     */
    Map importReceiveCostItems(List<List<Map<String, String>>> data, Long billId, User user) throws SQLException;

    /**
     * 创建应收页面明细导入
     * @param data
     * @param customerId
     * @param user
     * @return
     */
    Map importAddReceiveItems(List<List<Map<String, String>>> data, Long customerId, User user) throws SQLException;

    /**
     * 费用明细导出
     * @param json
     * @return
     */
    List<ReceiveBillCostItemDTO> exportListCostItem(String json) throws SQLException;

    /**
     * 生成发票html
     * @param tempId 发票模板id
     * @param ids 应收账单id，多个以英文逗号隔开
     * @param user
     */
    String generateInvoiceHtml(Long tempId, String ids, String invoiceSource, User user) throws Exception;

    /**
     * 保存
     * @param templateForm
     * @param user
     */
    void saveContract(User user, InvoiceTemplateForm templateForm) throws Exception;

    /**
     * 保存创建应收
     */
    Map<String, String> saveApiAddReceiveBill(ReceiveBillForm form, User user) throws Exception;

    /**
     * 选择单据
     * @param orderType
     * @param relCodes
     * @return
     */
    List<ReceiveBillItemDTO> chooseOrder(User user, Long billId, String relCodes) throws SQLException;
}
