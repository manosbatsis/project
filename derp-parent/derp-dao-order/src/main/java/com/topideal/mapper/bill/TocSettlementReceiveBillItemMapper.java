package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillItemDTO;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface TocSettlementReceiveBillItemMapper extends BaseMapper<TocSettlementReceiveBillItemModel> {

    Integer batchSave(List<TocSettlementReceiveBillItemModel> list) throws SQLException;


    /**
     * 统计应收结算
     */
    List<Map<String, Object>> receiveTotalById(@Param("id") Long id) throws SQLException;

    /**
     * 获取分页
     *
     * @param dto
     * @return
     * @throws SQLException
     */
    PageDataModel<TocSettlementReceiveBillItemDTO> listReceiveItemByPage(TocSettlementReceiveBillItemDTO dto) throws SQLException;

    int delItems(@Param("billId") Long billId) throws SQLException;

    /**
     * 统计toc应收单应收明细总金额
     */
    BigDecimal getTotalReceivePrice(@Param("id") Long id) throws SQLException;

    /**
     * 统计toc应收单应收明细结算币种金额
     */
    BigDecimal getTotalSettlementReceivePrice(@Param("id") Long id) throws SQLException;

    /**
     * 根据应收账单单号、外部订单号和商品id查询结算金额
     */
    List<TocSettlementReceiveBillItemModel> statisticsByBill(@Param("billIds") List<Long> billIds, @Param("orderCode") String orderCode,
                                                             @Param("goodsId") Long goodsId) throws SQLException;

    /**
     * 根据母品牌、费项统计应收明细
     */
    List<TocSettlementReceiveBillItemModel> listByProject(@Param("billIds") List<Long> billIds);

    List<TocSettlementReceiveBillItemDTO> listForExportItem(@Param("id") Long id, @Param("begin") Integer begin, @Param("pageSize") Integer pageSize) throws SQLException;

    /**
     * 根据账单id统计应收明细数量
     */
    int countByBillId(@Param("billId") Long billId) throws SQLException;

    /**
     * 分页根据billId查询数据
     */
    List<TocSettlementReceiveBillItemModel> getItemListPage(TocSettlementReceiveBillItemModel model) throws SQLException;

    /**
     * 根据电商订单号+商品id 批量统计查询 以“商品id+电商订单号”为维度的结算金额
     */
    List<Map<String, Object>> statisticsByOrderCodesAndBillIds(@Param("billIds") List<Long> billIds, @Param("orderCodes") List<String> orderCodes) throws SQLException;

    /**
     * 分页获取电商订单集合
     */
    List<String> getOrderCodeList(@Param("billId") Long billId, @Param("begin") Integer begin, @Param("pageSize") Integer pageSize);

    /**
     * 根据账单id统计应收明细数量
     */
    int countAllByBillId(@Param("billId") Long billId) throws SQLException;

    /**
     * 统计开票toc账单的结算金额和数量
     */
    List<Map<String, Object>> statisticsByBillIds(@Param("billIds") List<Long> billIds);

    /**
     * 项目额度预警 查询结算金额
     * @param queryBillMap
     * @return
     */
    List<Map<String, Object>> getProjectWarnList(Map<String, Object> queryBillMap);

    /**
     * 根据母品牌、平台费项、补扣款维度统计应收明细
     */
    List<TocSettlementReceiveBillItemModel> listByPlatformProject(@Param("billIds") List<Long> billIds);

    int countByDTO(TocSettlementReceiveBillItemDTO itemDTO);

    List<Map<String, Object>> listForExportItemByDTO(TocSettlementReceiveBillItemDTO itemDTO);

    Map<String, Object> getTotalPriceById(Long id);

    List<String> getExternalCodeList(@Param("billId")Long billId, @Param("begin")Integer begin, @Param("pageSize")Integer pageSize);

    void batchCleanTempBillId(Map<String, Object> params);

    void batchUpdateTempBillId(Map<String, Object> params);
}
