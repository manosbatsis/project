package com.topideal.mapper.order;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.OrderReturnIdepotModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface OrderReturnIdepotMapper extends BaseMapper<OrderReturnIdepotModel> {

	OrderReturnIdepotModel getVeriNotDelList(OrderReturnIdepotModel orderReturnIdepotModel);



}
