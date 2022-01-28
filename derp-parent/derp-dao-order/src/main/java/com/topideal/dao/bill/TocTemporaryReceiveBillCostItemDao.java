package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.TocTemporaryCostBillDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillCostItemDTO;
import com.topideal.entity.vo.bill.TocTemporaryReceiveBillCostItemModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface TocTemporaryReceiveBillCostItemDao extends BaseDao<TocTemporaryReceiveBillCostItemModel> {

    /**
     * 批量新增
     * @param list
     * @return
     */
    Integer batchSave(List<TocTemporaryReceiveBillCostItemModel> list) throws SQLException;

    /**
     * 统计暂估费用明细数量
     * @param dto
     * @return
     */
    int countTempBillNum(TocTemporaryReceiveBillCostItemDTO dto) throws SQLException;



    /**
     * 分页根据billId查询数据
     * @param dto
     * @return
     */
    List<TocTemporaryReceiveBillCostItemModel> getItemListPage(TocTemporaryReceiveBillCostItemDTO dto) throws SQLException;

    /**
     * 分页根据billId查询电商订单单号
     */
    List<String> getOrderCodesPage(TocTemporaryReceiveBillCostItemDTO dto) throws SQLException;

    /**
     * 分页根据billId查询外部订单号
     */
    List<String> getExternalCodesPage(TocTemporaryReceiveBillCostItemDTO dto) throws SQLException;

    /**
     * 批量更新toc暂估应收明细金额
     * @param billItemModelList
     * @return
     */
    void batchUpdate(List<TocTemporaryReceiveBillCostItemModel> billItemModelList);

    /**
     * 根据电商订单号集合查询暂估费用明细
     */
    List<TocTemporaryReceiveBillCostItemModel> getItemListByOrderList(List<String> orderCodes, String type, Long merchantId, Long buId, String storePlatformCode) throws SQLException;

    /**
     * 分页获取费用明细
     */
    TocTemporaryReceiveBillCostItemDTO getListByPage(TocTemporaryReceiveBillCostItemDTO dto);

    /**
     * 导出截至当前时间，表头状态为“部分结算”和“未结算”，表体电商订单为“未结算”的数据；
     */
    List<Map<String, Object>> listForExportTempCostItemPage(TocTemporaryReceiveBillCostItemDTO dto) throws SQLException;

    Integer deleteByModel(TocTemporaryReceiveBillCostItemModel model) throws SQLException;

    /**
     * @Description 根据订单号+平台费项的维度核销toc应收明细
     * @param orderCodes
     * @param type 类型 0-非天猫平台（以系统单号） 1-天猫平台（以外部单号）
     */
    void updateVerifyCostItems(List<String> orderCodes, String type, Long merchantId, Long buId, String storePlatformCode, Long settlementId) throws SQLException;

    /**
     * 根据toc暂估费用明细统计金额、数量以及未核销的金额和数量
     */
    List<Map<String, Object>> countByBillIds(List<Long> billIds);

    /**
     * 根据暂估id统计发货订单与退款订单正负红冲数量
     */
    Integer countPunchNum(Long billId, String punchType);

    /**
     * 更新暂估收入明细完结核销
     */
    void updateEndItemBill(Long billId, String punchType);

    /**
     * 汇总花费
     * @param dto
     * @return
     */
    List<TocTemporaryCostBillDTO> summaryByDTO(TocTemporaryReceiveBillCostItemDTO dto);

    List<TocTemporaryReceiveBillCostItemDTO> sumCostGroupByModel(TocTemporaryReceiveBillCostItemModel queryItemModel);

    /**
     * 获取费用差异调整明细
     * @param params
     * @return
     */
    List<TocTemporaryReceiveBillCostItemDTO> getCostDiffItems(Map<String, Object> params);

    /**
     * 根据差异调整单更新暂估核销
     * @param diffItems
     */
    void batchUpdateVerifyCostItemsByDiffItem(List<TocTemporaryReceiveBillCostItemDTO> diffItems);

    /**
     * 获取未核销明细
     * @param params
     * @return
     */
    List<TocTemporaryReceiveBillCostItemDTO> getUnVerifyCostItem(Map<String, Object> params);

    /**
     * 根据差异调整单更新暂估核销2
     * @param diffItems
     */
    void batchUpdateVerifyCostItemsByDiffItem2(Map<String, Object> params);

    TocTemporaryReceiveBillCostItemDTO getCostDiffListByPage(TocTemporaryReceiveBillCostItemDTO dto);

    List<TocTemporaryReceiveBillCostItemDTO> getUnVerifyCostWithoutSettlementItem(Map<String, Object> params);

    void updateWithoutSettleItems(List<TocTemporaryReceiveBillCostItemDTO> withoutSettlementItemList);

    void deletebyModelExcludeAdjustment(TocTemporaryReceiveBillCostItemModel alreadyItemModel);

    /**
     * 根据Map删除
     * @param param
     */
    void deleteByMap(Map<String, Object> param);

    /**
     * 根据Map 查询List
     * @param param
     * @return
     */
    List<TocTemporaryReceiveBillCostItemDTO> getItemListByMap(Map<String, Object> param);

    void updateAdjustmentByDTO(TocTemporaryReceiveBillCostItemDTO entity);

    void updateByMap(Map<String, Object> param);
}
