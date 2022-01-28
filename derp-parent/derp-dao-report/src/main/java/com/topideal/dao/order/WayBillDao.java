package com.topideal.dao.order;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.WayBillModel;

import java.util.Map;

/**
 * 运单表 dao
 * @author lian_
 */
public interface WayBillDao extends BaseDao<WayBillModel> {

    /**
     * 迁移数据到历史表
     * */
    int synsMoveToHistory(Map<String, Object> map);
    /**
     * 删除已迁移到历史表的数据
     * */
    int delMoveToHistory(Map<String, Object> map);









}
