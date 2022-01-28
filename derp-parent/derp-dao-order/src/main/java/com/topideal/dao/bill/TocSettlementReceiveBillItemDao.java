package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillItemDTO;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillItemModel;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface TocSettlementReceiveBillItemDao extends BaseDao<TocSettlementReceiveBillItemModel> {


    Integer batchSave(List<TocSettlementReceiveBillItemModel> list) throws SQLException;



    /**
     * 统计应收结算
     */
    List<Map<String, Object>> receiveTotalById(Long id) throws SQLException;

    /**
     * 获取toc应收明细分页数据
     * @param dto
     * @return
     * @throws SQLException
     */
    TocSettlementReceiveBillItemDTO listReceiveItemByPage(TocSettlementReceiveBillItemDTO dto) throws SQLException;

    int delItems(Long billId) throws SQLException;

    /**
     * 统计toc应收单应收明细总金额
     */
    BigDecimal getTotalReceivePrice(Long id) throws SQLException;

    /**
     * 统计toc应收单应收明细结算币种金额
     */
    BigDecimal getTotalSettlementReceivePrice(Long id) throws SQLException;

    /**
     * 根据应收账单单号、外部订单号和商品id查询结算金额
     */
    List<TocSettlementReceiveBillItemModel> statisticsByBill(List<Long> billIds, String orderCode, Long goodsId) throws SQLException;

    /**
     * 根据母品牌、费项统计应收明细
     */
    List<TocSettlementReceiveBillItemModel> listByProject(List<Long> billIds);

    List<TocSettlementReceiveBillItemDTO> listForExportItem(Long id, Integer begin, Integer pageSize) throws SQLException;

    /**
     * 根据账单id、电商订单不为空统计应收明细数量
     */
    int countByBillId(Long billId) throws SQLException;

    /**
     * 分页根据billId查询数据
     */
    List<TocSettlementReceiveBillItemModel> getItemListPage(TocSettlementReceiveBillItemModel model) throws SQLException;

    /**
     * 根据电商订单号+商品id 批量统计查询 以“商品id+电商订单号”为维度的结算金额
     */
    List<Map<String, Object>> statisticsByOrderCodesAndBillIds(List<Long> billIds, List<String> orderCodes) throws SQLException;

    /**
     * 分页获取电商订单集合
     */
    List<String> getOrderCodeList(Long billId, Integer begin, Integer pageSize);

    /**
     * 根据账单id统计应收明细数量
     */
    int countAllByBillId(Long billId) throws SQLException;

    /**
     * 统计开票toc账单的结算金额和数量
     */
    List<Map<String, Object>> statisticsByBillIds(List<Long> billIds);

    /**
     * 项目额度预警 查询结算金额
     * @param queryBillMap
     * @return
     */
    List<Map<String, Object>> getProjectWarnList(Map<String, Object> queryBillMap);

    /**
     * 根据母品牌、平台费项、补扣款维度统计应收明细
     */
    List<TocSettlementReceiveBillItemModel> listByPlatformProject(List<Long> billIds);

    int countByDTO(TocSettlementReceiveBillItemDTO itemDTO);

    List<Map<String, Object>> listForExportItemByDTO(TocSettlementReceiveBillItemDTO itemDTO);

    /**
     * 获取总金额
     * @param id
     * @return
     */
    Map<String, Object> getTotalPriceById(Long id);

    /**
     * 获取外部订单号集合
     */
    List<String> getExternalCodeList(Long billId, Integer begin, Integer pageSize);

    void batchCleanTempBillId(Map<String, Object> params);

    /**
     * 更新tempBillId
     * @param params
     */
    void batchUpdateTempBillId(Map<String, Object> params);
}
