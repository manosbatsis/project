package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.common.PlatformTemporaryCostOrderItemDTO;
import com.topideal.entity.vo.sale.PlatformTemporaryCostOrderItemModel;

import java.sql.SQLException;
import java.util.List;


public interface PlatformTemporaryCostOrderItemDao extends BaseDao<PlatformTemporaryCostOrderItemModel> {

    /**
     * 删除
     * @param id
     * @return
     */
    void deleteCostOrderItemById(List<Long> idList);

    /**
     * 批量新增
     * @param list
     * @return
     */
    Integer batchSave(List<PlatformTemporaryCostOrderItemModel> list) throws SQLException;

    /**
     * 查询
     * @param dto
     * @return
     */
    public List<PlatformTemporaryCostOrderItemDTO> listPlatformTemporaryCostItemDTO(PlatformTemporaryCostOrderItemDTO dto);

    /**
     * 根据表头表体的电商订单号批量更新暂估费用明细关联的费用明细id
     */
    Integer batchUpdateIds(List<String> orderCodes, String orderType);

    /**
     * 查询
     * @param platformIds
     * @return
     */
    List<PlatformTemporaryCostOrderItemModel> listItemByPlatformIds(List<Long> platformIds);


    /**
     * 根据orderId list获取各个订单金额合计
     * @param longs
     * @return
     */
    List<PlatformTemporaryCostOrderItemDTO> sumAmountByOrderIds(List<Long> platformIds);
    /**
     * 根据orderCode单号删除 
     * @param orderCodeList
     * @return
     * @throws SQLException
     */
    int deleteByOrderCode(List<String>orderCodeList)throws SQLException;
}
