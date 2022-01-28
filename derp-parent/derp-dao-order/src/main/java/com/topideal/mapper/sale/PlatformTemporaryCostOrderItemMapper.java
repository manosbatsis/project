package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.common.PlatformTemporaryCostOrderItemDTO;
import com.topideal.entity.vo.sale.PlatformTemporaryCostOrderItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface PlatformTemporaryCostOrderItemMapper extends BaseMapper<PlatformTemporaryCostOrderItemModel> {

    //批量新增
    Integer batchSave(List<PlatformTemporaryCostOrderItemModel> list) throws SQLException;

    //根据表头表体的电商订单号批量更新暂估费用明细关联的费用明细id
    Integer batchUpdateIds(@Param("orderCodes") List<String> orderCodes, @Param("orderType") String orderType);

    //删除
    void deleteCostOrderItemById(@Param("idList") List<Long> idList);

    /**
     * 分页查询
     * @param dto
     * @return
     */
    List<PlatformTemporaryCostOrderItemDTO> listPlatformTemporaryCostItemDTO(PlatformTemporaryCostOrderItemDTO dto);

    /**
     * 查询
     * @param platformIds
     * @return
     */
    List<PlatformTemporaryCostOrderItemModel> listItemByPlatformIds(@Param("platformIds") List<Long> platformIds);

    List<PlatformTemporaryCostOrderItemDTO> sumAmountByOrderIds(@Param("platformIds") List<Long> platformIds);
    /**
     * 根据orderCode单号删除 
     * @param orderCodeList
     * @return
     * @throws SQLException
     */
    int deleteByOrderCode(List<String>list)throws SQLException;

}
