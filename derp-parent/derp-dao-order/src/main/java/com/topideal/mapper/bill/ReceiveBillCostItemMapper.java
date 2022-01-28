package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.ReceiveBillCostItemDTO;
import com.topideal.entity.dto.bill.ReceiveBillDTO;
import com.topideal.entity.dto.bill.ReceiveBillItemDTO;
import com.topideal.entity.vo.bill.ReceiveBillCostItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface ReceiveBillCostItemMapper extends BaseMapper<ReceiveBillCostItemModel> {

    /**
     * 根据商品、po号、项目类型统计应收账单费用明细
     */
    List<ReceiveBillCostItemDTO> itemListByBillIds(@Param("billIds") List<Long> billIds) throws SQLException;

    int delCostItem(Long billId) throws SQLException;

    BigDecimal getTotalReceivePrice(@Param("billId")Long billId) throws SQLException;

    /**
     * 以“商品sku+发票描述+费项+po+母品牌”维度统计
     */
    List<Map<String, Object>> listInvoiceCostItem(@Param("billIds") List<Long> billIds) throws SQLException;

    List<Map<String, Object>> listInvoiceCostItemByGoodsNo(@Param("ids") List<Long> ids, @Param("merchantId") Long merchantId,@Param("source") String source) throws SQLException;
    /**
     * 统计应收账单集合费用金额
     */
    List<Map<String, Object>> listCostPrice(@Param("ids") List<Long> ids) throws SQLException;

    Integer batchSave(List<ReceiveBillCostItemModel> list) throws SQLException;

    /**
     * 根据关联的应收账单id以“应收单+品牌+收支费项”为维度统计明细
     */
    List<ReceiveBillCostItemDTO> synNcItemByIds(@Param("ids") List<Long> ids) throws SQLException;

    /**
     * 以商品维度统计指定费项的费用明细
     */
    List<Map<String, Object>> listInvoiceCostItemByProject(@Param("ids") List<Long> ids, @Param("projectId")Long projectId) throws SQLException;

    /**
     * 项目额度预警 查询费用金额
     * @param queryBillMap
     * @return
     */
    List<Map<String, Object>> getProjectWarnList(Map<String, Object> queryBillMap);

    /**
     * 按照PO和费项维度汇总应收明细
     * @return
     * @param billIds
     */
    List<Map<String, Object>> listByPoNoAndProject(@Param("billIds") List<Long> billIds) throws SQLException;

    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
    List<ReceiveBillCostItemDTO> listReceiveCostItem(ReceiveBillDTO dto) throws SQLException;

    /**
     * 获取指定应收账单的未核销费用明细
     * @return
     * @param billIds
     */
    List<ReceiveBillCostItemDTO> getBeVerifyCostItems(@Param("billIds") List<Long> billIds,@Param("billType")String billType) throws SQLException;

    /**
     * 批量更新费用明细费项
     * @param itemDTOList
     * @return
     */
    void batchUpdate(@Param("itemDTOList") List<ReceiveBillCostItemDTO> itemDTOList);
}
