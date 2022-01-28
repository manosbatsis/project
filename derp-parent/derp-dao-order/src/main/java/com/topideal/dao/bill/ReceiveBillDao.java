package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.ReceiveBillDTO;
import com.topideal.entity.dto.sale.SaleReturnIdepotDTO;
import com.topideal.entity.dto.sale.SaleReturnOrderDTO;
import com.topideal.entity.vo.bill.ReceiveBillModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface ReceiveBillDao extends BaseDao<ReceiveBillModel>{


    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
    ReceiveBillDTO listReceiveBillByPage(ReceiveBillDTO dto) throws SQLException;

    /**
     * 统计应收结算
     */
    List<Map<String, Object>> receiveTotalById(Long id) throws SQLException;

    /**
     * 统计抵扣费用
     */
    List<Map<String, Object>> deductionTotalById(Long id) throws SQLException;

    ReceiveBillDTO searchDTOById(Long id) throws SQLException;
    /**
     * 查询已审核的 应收账单
     * @param model
     * @return
     * @throws SQLException
     */
    List<ReceiveBillModel> getReceiveBillList(Map<String, Object> map )throws SQLException;
    /**
     * 获取已经作废/删除的应收账单的收款核销
     * @return
     * @throws SQLException
     */
    List<ReceiveBillModel> getZfAnd006ReceiveBill(List<String> receiveCodes)throws SQLException;
    /**
     * 计算应收金额
     * @param id
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> getSumReceivePrice(Long id) throws SQLException;
    /**
     * 本期已核销金额
     * @param id
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> getSumCollectedPrice(Long id) throws SQLException;

    List<Long> getBuList(List<Long> ids, Long merchantId) throws SQLException;

    /**
     * 根据ids查询应收账单信息
     */
    List<ReceiveBillDTO> listBillByRelIds(List<Long> ids);

    //批量更新应收账单状态
    void batchUpdateBillStatus(List<Long> ids, String billStatus, String invoiceStatus) throws SQLException;

    /**
     * 获取新增选择上架单据分页
     * @param dto
     * @return
     * @throws SQLException
     */
    ReceiveBillDTO listAddShelfOrderByPage(ReceiveBillDTO dto) throws SQLException;

    /**
     * 获取新增选择账单出入库单据分页
     * @param dto
     * @return
     * @throws SQLException
     */
    ReceiveBillDTO listAddBillOutOrderByPage(ReceiveBillDTO dto) throws SQLException;

    /**
     * 获取新增选择预售单据分页
     * @param dto
     * @return
     * @throws SQLException
     */
    ReceiveBillDTO listAddPreOrderByPage(ReceiveBillDTO dto) throws SQLException;

    /**
     * 获取新增选择销售订单分页
     * @param dto
     * @return
     * @throws SQLException
     */
    ReceiveBillDTO listAddSaleOrderByPage(ReceiveBillDTO dto) throws SQLException;

    //批量更新关联应收账单的开票状态更新为“待开票”, 清空发票关联id
    void batchUpdateInvoiceStatus(List<Long> ids, String invoiceStatus) throws SQLException;

    /**
     * 查询状态为：已同步/待审核/待入erp/待入账/已入账的应收账单
     * @return
     */
	List<ReceiveBillModel> getNcBackfillList();

    /**
     * 查询状态不为已作废， 且关联关联单号的应收账单
     * @return
     */
    List<ReceiveBillModel> getRelBill(String code);

    /**
     * 根据页面查询条件查询导出信息
     * @param dto
     * @return
     * @throws SQLException
     */
    List<ReceiveBillDTO> listReceiveBill(ReceiveBillDTO dto) throws SQLException;

    /**
     * 查询应收单当前账单状态不为”已作废“，且NC应收状态为“已入账”、“已关账”的应收单且没有凭证信息
     * 或者应收单当前账单状态为”已作废“，且NC应收状态为“已入账”、“已关账”、“已作废”、“已红冲”的应收账单
     * @return
     */
    List<ReceiveBillModel> getNcVoucherFillBackList();

    /**
     * 获取新增选择采购sd单据分页
     * @param dto
     * @return
     * @throws SQLException
     */
    ReceiveBillDTO listAddPurchaseSDOrderByPage(ReceiveBillDTO dto) throws SQLException;

    /**
     * 获取新增选择销售退货单单据分页
     * @return
     * @throws SQLException
     */
    SaleReturnIdepotDTO listAddSaleReturnOrderByPage(SaleReturnIdepotDTO dto) throws SQLException;

    /**
     * 获取新增选择融资申请单单据分页
     * @param dto
     * @return
     * @throws SQLException
     */
    ReceiveBillDTO listAddFinancingOrderByPage(ReceiveBillDTO dto) throws SQLException;

    /***
     * 项目额度预警获取对应状态为 “已核销” 的应收单 过滤掉 客户为内部公司的数据
     * @param queryBillMap
     * @return
     */
    //List<Long> getProjectQuatoReceiveId(Map<String, Object> queryBillMap);
    /**
     * 待确认
     * @param map
     * @return
     */
    List<Map<String,Object>> getNoConfirmAmount(Map<String, Object> map) throws SQLException;
    /**
     * 待开票
     * @param map
     * @return
     */
    List<Map<String,Object>> getNoInvoiceAmount(Map<String, Object> map) throws SQLException;
    /**
     * 待回款
     * @param map
     * @return
     */
    List<Map<String,Object>> getNoReturnAmount(Map<String, Object> map) throws SQLException;
    /**
     * 待回款
     * @param map
     * @return
     */
    List<Map<String,Object>> getReturnAmount(Map<String, Object> map) throws SQLException;

    /**
     * 获取“待核销、部分核销、已核销”的应收单
     * @param model
     * @return
     */
    List<ReceiveBillModel> getAuditBills(ReceiveBillModel model);

    /**
     * 获取指定公司、应收账单号的未开票、待审核的应收账单
     *
     * @return
     */
    List<ReceiveBillModel> getBillsByCodesAndStatus(String receiveCodes, Long merchantId);

    /**
     * 根据条件查询应收账单
     *
     * @param dto
     * @return
     * @throws SQLException
     */
    List<ReceiveBillModel> listDTO(ReceiveBillDTO dto) throws SQLException;

    /**
     * 查询该公司+事业部、入账月份小于等于月结月份维度下的待核销、部分核销、作废待审的应收账单
     * @param merchantId
     * @param buId
     * @param month
     * @return
     * @throws SQLException
     */
    List<ReceiveBillModel> listByMonthlySnapshot(Long merchantId, Long buId, String month) throws SQLException;


    /**
     * 查询该公司+事业部、入账月份小于等于月结月份、核销月份大于月结月份的已核销的应收账单
     * @param merchantId
     * @param buId
     * @param month
     * @return
     * @throws SQLException
     */
    List<ReceiveBillModel> listByAllVerifyMonthlySnapshot(Long merchantId, Long buId, String month) throws SQLException;
}
