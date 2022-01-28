package com.topideal.mapper.order;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.WayBillModel;
import com.topideal.mapper.BaseMapper;

import java.util.Map;

/**
 * 运单表 mapper
 * @author lian_
 */
@MyBatisRepository
public interface WayBillMapper extends BaseMapper<WayBillModel> {

    /**
     * 迁移数据到历史表
     * */
    int synsMoveToHistory(Map<String, Object> map);
    /**
     * 删除已迁移到历史表的数据
     * */
    int delMoveToHistory(Map<String, Object> map);

}
