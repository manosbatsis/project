package com.topideal.dao.order;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.InventoryLocationAdjustmentOrderItemModel;

import java.sql.SQLException;
import java.util.List;


public interface InventoryLocationAdjustmentOrderItemDao extends BaseDao<InventoryLocationAdjustmentOrderItemModel> {


    /**
     * 批量根据表头id删除表体
     * @param list
     * @return
     * @throws SQLException
     */
    int delByIdBatch(List list) throws SQLException;








}
