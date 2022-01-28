package com.topideal.dao.inventory;


import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.inventory.BuInventoryItemModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface BuInventoryItemDao extends BaseDao<BuInventoryItemModel> {


    /**
     * 关联表头查询事业部库存明细
     */
    public List<Map<String,Object>> getItemList(Map<String,Object> map) throws SQLException;








}
