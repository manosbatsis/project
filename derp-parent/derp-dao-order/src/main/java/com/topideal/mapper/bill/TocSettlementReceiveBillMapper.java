package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.ReceiveBillVerificationDTO;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillDTO;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface TocSettlementReceiveBillMapper extends BaseMapper<TocSettlementReceiveBillModel> {

    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
    PageDataModel<TocSettlementReceiveBillDTO> listReceiveBillByPage(TocSettlementReceiveBillDTO dto) throws SQLException;

    TocSettlementReceiveBillDTO searchDTOById(Long id) throws SQLException;

    /**
     * 查询状态为：已同步/待审核/待入erp/待入账/已入账的toc应收账单
     * @return
     */
    List<TocSettlementReceiveBillModel> getNcBackfillList();

    /**
     * 查询Toc应收单当前账单状态不为”已作废“，且NC应收状态为“已入账”、“已关账”的应收单且没有凭证信息
     * 或者Toc应收单当前账单状态为”已作废“，且NC应收状态为“已入账”、“已关账”、“已作废”、“已红冲”的应收账单
     * @return
     */
    List<TocSettlementReceiveBillModel> getNcVoucherFillBackList();

    List<TocSettlementReceiveBillDTO> listForExport(TocSettlementReceiveBillDTO dto);

    /**
     * 修改toc应收账单，其中错误文件地址字段为空也更新
     * @return
     */
    int updateWithNull(TocSettlementReceiveBillModel model);

    List<TocSettlementReceiveBillModel> listByIds(@Param("ids")List<Long> ids);

    //批量更新关联应收账单的开票状态更新为“待开票”, 清空发票关联id
    void batchUpdateInvoiceStatus(@Param("ids") List<Long> ids, @Param("invoiceStatus") String invoiceStatus) throws SQLException;

    /**
     * 获取应收金额 （应收账龄报告）
     */
    Map<String, Object> getRecevieByBillStatus(Map<String, Object> queryMap);

    /**
     * 应收账龄报告 根据商家+事业部+客户+币种 汇总类型为POP
     * @param queryMap
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> getSummarySettlement(Map<String, Object> queryMap) throws SQLException;


    /**
     * 根据商家+事业部+客户+币种以及月份的维度获取未核销的金额
     *
     * @param queryMap
     * @return
     */
    List<Map<String, Object>> getItemBySearch(Map<String, Object> queryMap);

    /**
     * 查询toc应收账单
     */
    List<TocSettlementReceiveBillModel> listTocBill(TocSettlementReceiveBillDTO dto);

    /**
     * 批量更新关联应收账单的账单状态
     */
    void batchUpdateBillStatus(@Param("ids") List<Long> ids, @Param("billStatus") String billStatus) throws SQLException;


    /**
     * 查询已审核未作废的toc应收账单
     * @param
     * @return
     * @throws SQLException
     */
    List<TocSettlementReceiveBillModel> getReceiveBillList(Map<String, Object> map)throws SQLException;

    /**
     * 查询作废通过且已生成核销跟踪的toc应收账单
     * @param
     * @return
     * @throws SQLException
     */
    List<TocSettlementReceiveBillModel> getZfAnd006ReceiveBill(@Param("receiveCodes") List<String> receiveCodes)throws SQLException;

    /**
     * 查询该公司+事业部、入账月份小于等于月结月份维度下的待核销、部分核销、作废待审的toc应收账单
     * @param merchantId
     * @param buId
     * @param month
     * @return
     * @throws SQLException
     */
    List<TocSettlementReceiveBillModel> listByMonthlySnapshot(@Param("merchantId") Long merchantId, @Param("buId") Long buId, @Param("month") String month) throws SQLException;


    /**
     * 查询该公司+事业部、入账月份小于等于月结月份、核销月份大于月结月份的已核销的toc应收账单
     * @param merchantId
     * @param buId
     * @param month
     * @return
     * @throws SQLException
     */
    List<TocSettlementReceiveBillModel> listByAllVerifyMonthlySnapshot(@Param("merchantId") Long merchantId, @Param("buId") Long buId, @Param("month") String month) throws SQLException;

}
