package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillCostItemDTO;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillItemDTO;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillCostItemModel;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface TocSettlementReceiveBillCostItemDao extends BaseDao<TocSettlementReceiveBillCostItemModel> {


    Integer batchSave(List<TocSettlementReceiveBillCostItemModel> list) throws SQLException;

    List<Map<String, Object>> deductionTotalById(Long id) throws SQLException;

    /**
     * 获取toc应收费用明细分页数据
     * @param dto
     * @return
     * @throws SQLException
     */
    TocSettlementReceiveBillCostItemDTO listReceiveCostItemByPage(TocSettlementReceiveBillCostItemDTO dto) throws SQLException;

    int delCostItems(Long billId, String dataSource) throws SQLException;

    /**
     * 获取费用明细总金额
     */
    BigDecimal getTotalReceivePrice(Long id) throws SQLException;

    /**
     * 获取费用明细结算币种总金额
     */
    BigDecimal getTotalSettlementPrice(Long id) throws SQLException;

    /**
     * 根据应收账单单号、外部订单号和商品id查询结算金额
     */
    List<TocSettlementReceiveBillCostItemModel> statisticsByBill(List<Long> billIds, String orderCode, Long goodsId) throws SQLException;

    /**
     * 根据母品牌、费项统计应收明细
     */
    List<TocSettlementReceiveBillCostItemModel> listByProject(List<Long> billIds);

    List<TocSettlementReceiveBillCostItemDTO> listForExportItem(Long billId, Integer begin, Integer pageSize) throws SQLException;

    /**
     * 根据账单id统计费用明细数量
     */
    int countByBillId(Long billId, String type) throws SQLException;

    /**
     * 分页根据billId查询数据
     */
    List<TocSettlementReceiveBillCostItemModel> getItemListPage(TocSettlementReceiveBillCostItemModel model) throws SQLException;

    /**
     * 根据电商订单号+商品id 批量统计查询 以“商品id+电商订单号”为维度的结算金额
     */
    List<Map<String, Object>> statisticsByOrderCodesAndBillIds(List<Long> billIds, String type, List<String> orderCodes) throws SQLException;

    /**
     * 分页获取电商订单集合
     */
    List<String> getOrderCodeList(Long billId, String type, Integer begin, Integer pageSize);

    /**
     * 根据账单id统计应收明细数量
     */
    int countAllByBillId(Long billId) throws SQLException;

    /**
     * 统计开票toc账单的结算金额和数量
     */
    List<Map<String, Object>> statisticsByBillIds(List<Long> billIds);

    /**
     * 项目额度预警 查询费用金额
     * @param queryBillMap
     * @return
     */
    List<Map<String, Object>> getProjectWarnList(Map<String, Object> queryBillMap);

    /**
     * 根据母品牌、平台费项、补扣款统计应收费用明细
     */
    List<TocSettlementReceiveBillCostItemModel> listByPlatformProject(List<Long> billIds);

    /**
     * 批量更新暂估费用表头ID
     * @param params
     */
    void batchUpdateTempCostBillId(Map<String, Object> params);

    /**
     * 批量清空暂估费用表头ID
     * @param params
     */
    void batchCleanTempCostBillId(Map<String, Object> params);

    List<TocSettlementReceiveBillCostItemDTO> getExtistTempByMap(Map<String, Object> params);

    int countByDTO(TocSettlementReceiveBillItemDTO itemDTO);

    /**
     * 导出
     * @param costItemDTO
     * @return
     */
    List<Map<String, Object>> listForExportItemByDTO(TocSettlementReceiveBillCostItemDTO costItemDTO);
}
