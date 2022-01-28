package com.topideal.dao.order;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.BuMoveInventoryItemModel;

import java.sql.SQLException;
import java.util.List;


public interface BuMoveInventoryItemDao extends BaseDao<BuMoveInventoryItemModel> {



    /**
     * 批量根据表头id删除表体
     * @param list
     * @return
     * @throws SQLException
     */
    int delByIdBatch(List list) throws SQLException;








}
