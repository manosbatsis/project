package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillCostItemDTO;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillItemDTO;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillCostItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface TocSettlementReceiveBillCostItemMapper extends BaseMapper<TocSettlementReceiveBillCostItemModel> {

    Integer batchSave(List<TocSettlementReceiveBillCostItemModel> list) throws SQLException;

    /**
     * 统计抵扣费用
     */
    List<Map<String, Object>> deductionTotalById(@Param("id") Long id) throws SQLException;

    PageDataModel<TocSettlementReceiveBillCostItemDTO> listReceiveCostItemByPage(TocSettlementReceiveBillCostItemDTO dto) throws SQLException;

    int delCostItems(@Param("billId") Long billId, @Param("dataSource") String dataSource) throws SQLException;

    /**
     * 获取费用明细总金额
     */
    BigDecimal getTotalReceivePrice(@Param("id") Long id) throws SQLException;

    /**
     * 获取费用明细结算币种总金额
     */
    BigDecimal getTotalSettlementPrice(@Param("id") Long id) throws SQLException;

    /**
     * 根据应收账单单号、外部订单号和商品id查询结算金额
     */
    List<TocSettlementReceiveBillCostItemModel> statisticsByBill(@Param("billIds") List<Long> billIds, @Param("orderCode") String orderCode,
                                         @Param("goodsId") Long goodsId) throws SQLException;

    /**
     * 根据母品牌、平台费项统计应收明细
     */
    List<TocSettlementReceiveBillCostItemModel> listByProject(@Param("billIds") List<Long> billIds);

    List<TocSettlementReceiveBillCostItemDTO> listForExportItem(@Param("billId") Long billId, @Param("begin") Integer begin, @Param("pageSize") Integer pageSize) throws SQLException;

    /**
     * 根据账单id统计费用明细数量
     */
    int countByBillId(@Param("billId") Long billId, @Param("type")String type) throws SQLException;

    /**
     * 分页根据billId查询数据
     */
    List<TocSettlementReceiveBillCostItemModel> getItemListPage(TocSettlementReceiveBillCostItemModel model) throws SQLException;

    /**
     * 根据电商订单号+商品id 批量统计查询 以“商品id+电商订单号”为维度的结算金额
     */
    List<Map<String, Object>> statisticsByOrderCodesAndBillIds(@Param("billIds") List<Long> billIds,@Param("type") String type, @Param("orderCodes") List<String> orderCodes) throws SQLException;

    /**
     * 分页获取电商订单集合
     */
    List<String> getOrderCodeList(@Param("billId") Long billId, @Param("type")String type, @Param("begin") Integer begin, @Param("pageSize") Integer pageSize);

    /**
     * 根据账单id统计应收明细数量
     */
    int countAllByBillId(@Param("billId") Long billId) throws SQLException;

    /**
     * 统计开票toc账单的结算金额和数量
     */
    List<Map<String, Object>> statisticsByBillIds(@Param("billIds") List<Long> billIds);

    /**
     * 项目额度预警 查询费用金额
     * @param queryBillMap
     * @return
     */
    List<Map<String, Object>> getProjectWarnList(Map<String, Object> queryBillMap);

    /**
     * 根据母品牌、平台费项、补扣款统计应收费用明细
     */
    List<TocSettlementReceiveBillCostItemModel> listByPlatformProject(@Param("billIds") List<Long> billIds);

    /**
     * 批量更新TempCostBillId
     * @param params
     */
    void batchUpdateTempCostBillId(Map<String, Object> params);

    /**
     * 批量清空TempCostBillId
     * @param params
     */
    void batchCleanTempCostBillId(Map<String, Object> params);

    List<TocSettlementReceiveBillCostItemDTO> getExtistTempByMap(Map<String, Object> params);

    int countByDTO(TocSettlementReceiveBillItemDTO itemDTO);

    List<Map<String, Object>> listForExportItemByDTO(TocSettlementReceiveBillCostItemDTO costItemDTO);
}
