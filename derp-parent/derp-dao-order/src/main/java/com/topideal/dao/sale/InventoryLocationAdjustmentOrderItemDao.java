package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.InventoryLocationAdjustmentOrderItemDTO;
import com.topideal.entity.dto.sale.PreSaleOrderItemDTO;
import com.topideal.entity.vo.sale.InventoryLocationAdjustmentOrderItemModel;

import java.sql.SQLException;
import java.util.List;


public interface InventoryLocationAdjustmentOrderItemDao extends BaseDao<InventoryLocationAdjustmentOrderItemModel> {

    /**
     * 列表查询
     * @param dto
     * @return
     * @throws SQLException
     */
    List<InventoryLocationAdjustmentOrderItemDTO> listInventoryLocationAdjustDTO(InventoryLocationAdjustmentOrderItemDTO dto)throws SQLException;

    /**
     * 批量根据表头id删除表体
     * @param list
     * @return
     * @throws SQLException
     */
    int delByIdBatch(List list) throws SQLException;






}
