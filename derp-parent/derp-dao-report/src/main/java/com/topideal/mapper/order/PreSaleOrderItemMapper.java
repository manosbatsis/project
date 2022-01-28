package com.topideal.mapper.order;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.PreSaleOrderItemModel;
import com.topideal.mapper.BaseMapper;

import java.util.Map;


@MyBatisRepository
public interface PreSaleOrderItemMapper extends BaseMapper<PreSaleOrderItemModel> {

    int deleteByMap(Map<String, Object> delMap);
}
