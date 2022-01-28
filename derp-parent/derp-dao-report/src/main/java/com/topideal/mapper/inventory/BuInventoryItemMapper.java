package com.topideal.mapper.inventory;


import com.topideal.entity.vo.inventory.BuInventoryItemModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface BuInventoryItemMapper extends BaseMapper<BuInventoryItemModel> {


    /**
     * 关联表头查询事业部库存明细
     */
    public List<Map<String,Object>> getItemList(Map<String,Object> map) throws SQLException;
}
