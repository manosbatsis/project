package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.sale.OrderItemHistoryDTO;
import com.topideal.entity.vo.sale.OrderItemHistoryModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;


@MyBatisRepository
public interface OrderItemHistoryMapper extends BaseMapper<OrderItemHistoryModel> {

    List<OrderItemHistoryDTO> listItemDTO(OrderItemHistoryDTO dto);


}
