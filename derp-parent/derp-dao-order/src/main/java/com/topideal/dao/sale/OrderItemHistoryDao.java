package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.OrderItemDTO;
import com.topideal.entity.dto.sale.OrderItemHistoryDTO;
import com.topideal.entity.vo.sale.OrderItemHistoryModel;

import java.util.List;


public interface OrderItemHistoryDao extends BaseDao<OrderItemHistoryModel> {


    List<OrderItemHistoryDTO> listItemDTO(OrderItemHistoryDTO dto);









}
