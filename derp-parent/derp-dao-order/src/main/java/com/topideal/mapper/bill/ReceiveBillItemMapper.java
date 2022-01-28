package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.ReceiveBillDTO;
import com.topideal.entity.dto.bill.ReceiveBillItemDTO;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillDTO;
import com.topideal.entity.vo.bill.ReceiveBillItemModel;
import com.topideal.entity.vo.bill.ReceiveBillModel;
import com.topideal.entity.vo.sale.CrawlerVipExtraDataModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface ReceiveBillItemMapper extends BaseMapper<ReceiveBillItemModel> {

    List<ReceiveBillItemDTO> itemListGroupByBillId(@Param("billIds") List<Long> billIds) throws SQLException;

    int delItems(@Param("billId") Long billId, @Param("dataSource") String dataSource) throws SQLException;

    BigDecimal getTotalReceivePrice(@Param("billId")Long billId) throws SQLException;

    List<Map<String, Object>> listInvoiceItem(@Param("ids") List<Long> ids, @Param("merchantId") Long merchantId,
                                              @Param("source") String source) throws SQLException;

    Integer batchSave(List<ReceiveBillItemModel> list) throws SQLException;

    /**
     * 统计应收账单集合表体应收金额
     */
    List<Map<String, Object>> listItemPrice(@Param("ids") List<Long> ids) throws SQLException;

    /**
     * 根据关联的应收账单id以“应收单+品牌+收支费项”为维度统计明细
     */
    List<ReceiveBillItemDTO> synNcItemByIds(@Param("ids") List<Long> ids) throws SQLException;

    /**
     * 根据商品货号、母品牌统计应收明细
     */
    List<Map<String, Object>> listInvoiceItemGroupByParentBrand(@Param("ids") List<Long> ids) throws SQLException;

    /**
     * 以“商品sku+发票描述+费项+po+母品牌”维度统计
     */
    List<Map<String, Object>> listInvoiceItems(@Param("billIds")List<Long> billIds) throws SQLException;

    /**
     * 项目额度预警 查询结算金额
     * @return
     * @param queryBillMap
     */
    List<Map<String, Object>> getProjectWarnList(Map<String, Object> queryBillMap);

    /**
     * 按照PO维度汇总应收明细
     * @return
     * @param billIds
     */
    List<Map<String, Object>> listByPoNo(@Param("billIds") List<Long> billIds) throws SQLException;

    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
    List<ReceiveBillItemDTO> listReceiveBilltem(ReceiveBillDTO dto) throws SQLException;

    /**
     * 获取状态为“待核销、部分核销、已核销“且应收明细“部分核销、未核销”的应收账单明细
     * @param model
     * @return
     * @throws SQLException
     */
    List<ReceiveBillItemDTO> listVerifyItems(ReceiveBillModel model) throws SQLException;

    /**
     * 判断该“上架单”是否已有创建应收单，若有则查询“To B暂估核销表”，找到剩余未核销暂估应收的总金额
     * @param relCodes
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> verifyItems(@Param("relCodes") List<String> relCodes);

    /**
     * 判断该“上架单”是否已有创建应收单，若有则查询“To B暂估核销表”，找到剩余未核销暂估应收的SKU明细金额
     * @param relCode
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> verifyItemList(@Param("relCode") String relCode);

    /**
     * 批量更新应收明细费项
     * @param itemDTOList
     * @return
     */
    void batchUpdate(@Param("itemDTOList") List<ReceiveBillItemDTO> itemDTOList);
}
