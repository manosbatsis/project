package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillItemDTO;
import com.topideal.entity.vo.bill.TocTemporaryReceiveBillItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface TocTemporaryReceiveBillItemMapper extends BaseMapper<TocTemporaryReceiveBillItemModel> {

    Integer batchSave(List<TocTemporaryReceiveBillItemModel> list) throws SQLException;

    Integer deleteByModel(TocTemporaryReceiveBillItemModel model) throws SQLException;

    PageDataModel<TocTemporaryReceiveBillItemDTO> getListByPage(TocTemporaryReceiveBillItemDTO dto);

    int countBillNum(@Param("ids") List<Long> ids, @Param("merchantId")Long merchantId, @Param("buIds")List<Long> buIds) throws SQLException;

    int countTempBillNum(TocTemporaryReceiveBillItemDTO dto) throws SQLException;

    /**
     * 统计toc应收账单集合表体应收金额
     */
    List<Map<String, Object>> listItemPrice(@Param("billId") Long billId,@Param("buIds") List<Long> buIds) throws SQLException;

    List<TocTemporaryReceiveBillItemDTO> listForExportItem(@Param("ids")List<Long> ids, @Param("buIds")List<Long> buIds
            , @Param("begin") int begin, @Param("pageSize") int pageSize) throws SQLException;

    /**
     * 批量更新toc暂估应收明细金额
     * @param billItemModelList
     * @return
     */
    void batchUpdate(@Param("billItemModelList") List<TocTemporaryReceiveBillItemModel> billItemModelList);

    /**
     * 查询toc暂估应收单未结算的数量
     * @param id
     * @return
     */
    Map<String, Object> countByBillIdAndStatus(@Param("id") Long id, @Param("settlementStatus") String settlementStatus);

    /**
     * 导出截至当前时间，表头状态为“部分结算”和“未结算”，表体电商订单为“未结算”的数据；
     */
    List<Map<String, Object>> listForExportTempItemPage(TocTemporaryReceiveBillItemDTO dto) throws SQLException;

    /**
     * 获取结算单的最大结算时间
     */
    Timestamp getMaxSettlementDate(@Param("billId") Long billId) throws SQLException;

    /**
     * 分页根据billId查询数据
     */
    List<TocTemporaryReceiveBillItemModel> getItemListPage(TocTemporaryReceiveBillItemDTO dto) throws SQLException;

    /**
     * 根据电商订单号集合查询暂估明细
     */
    List<TocTemporaryReceiveBillItemModel> getItemListByOrderList(@Param("orderCodes") List<String> orderCodes, @Param("externalCodes") List<String> externalCodes, @Param("settlementMark") String settlementMark, @Param("merchantId")Long merchantId, @Param("buId")Long buId, @Param("storePlatformCode")String storePlatformCode) throws SQLException;

    /**
     * 分页根据billId查询电商订单单号
     */
    List<String> getOrderCodesPage(TocTemporaryReceiveBillItemDTO dto) throws SQLException;

    /**
     * 根据订单号+商品id的维度核销toc应收明细
     */
    void updateVerifyItems(@Param("orderCodes") List<String> orderCodes, @Param("merchantId") Long merchantId, @Param("buId") Long buId, @Param("storePlatformCode") String storePlatformCode, @Param("settlementId") Long settlementId) throws SQLException;

    /**
     * 根据toc暂估收入明细统计金额、数量以及未核销的金额和数量
     */
    List<Map<String, Object>> countByBillIds(@Param("billIds") List<Long> billIds);

    /**
     * 根据暂估id统计发货订单与退款订单正负红冲数量
     */
    Integer countPunchNum(@Param("billId") Long billId, @Param("punchType") String punchType);

    /**
     * 更新暂估收入明细完结核销
     */
    void updateEndItemBill(@Param("billId") Long billId, @Param("punchType") String punchType);

    List<TocTemporaryReceiveBillDTO> summaryByDTO(TocTemporaryReceiveBillItemDTO dto);

    List<String> getExternalCodesPage(TocTemporaryReceiveBillItemDTO itemDTO);
}
