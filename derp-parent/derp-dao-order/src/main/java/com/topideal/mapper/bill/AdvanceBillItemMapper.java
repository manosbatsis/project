package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.bill.AdvanceBillDTO;
import com.topideal.entity.dto.bill.AdvanceBillItemDTO;
import com.topideal.entity.vo.bill.AdvanceBillItemModel;
import com.topideal.entity.vo.bill.AdvanceBillModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface AdvanceBillItemMapper extends BaseMapper<AdvanceBillItemModel> {

    /**
     * 根据ids查询预收账单表体信息
     */
	List<Map<String, Object>> getAdvanceBillItemList(@Param("ids") List<Long> ids);
    /**
     * 根据预收账单id查看表体明细
     * @param advanceId
     * @return
     */
    List<AdvanceBillItemDTO> getAdvanceById(@Param("advanceId") Long advanceId);


    /**
     * 根据预收账单id删除预收详情
     * @param advanceId
     * @return
     * @throws SQLException
     */
    int delItems(@Param("advanceId") Long advanceId) throws SQLException;


    /**
     * 根据费项维度查询预收总金额
     * @param advanceIds
     * @return
     */
    List<AdvanceBillItemDTO> synNcItemByIds(@Param("ids") List<Long> advanceIds);


    /**
     * 获取预收账单总金额
     * @param ids
     * @return
     */
    List<Map<String, Object>> listItemPrice(@Param("ids") List<Long> ids);

    /**
     * 根据关联的销售订单号统计各预收单的预收款
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> listItemPriceByOrderCodes(@Param("ids")List<Long> ids, @Param("orderCodes")List<String> orderCodes) throws SQLException;

    /**
     * 根据销售订单号查询未被勾稽的已审核预收单明细
     * @param orderCodes 销售订单号集合
     * @return
     */
    List<AdvanceBillItemDTO> listWithoutVerify(@Param("orderCodes") List<String> orderCodes);

    /**
     * 账单明细id集合查询预收账单明细
     * @param ids 账单明细id
     * @return
     */
    List<AdvanceBillItemModel> listByIds(@Param("ids")List<Long> ids);
}
