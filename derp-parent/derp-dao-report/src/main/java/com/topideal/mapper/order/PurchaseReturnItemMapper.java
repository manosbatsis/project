package com.topideal.mapper.order;

import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.PurchaseReturnItemModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface PurchaseReturnItemMapper extends BaseMapper<PurchaseReturnItemModel> {

	int deleteByMap(Map<String, Object> delMap);



}
