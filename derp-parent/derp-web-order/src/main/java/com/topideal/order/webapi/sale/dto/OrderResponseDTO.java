package com.topideal.order.webapi.sale.dto;

import java.util.List;

import com.topideal.entity.dto.sale.OrderDTO;
import com.topideal.entity.dto.sale.WayBillItemDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 电商订单返回值 dto
 * @date 2021-02-07
 */
@ApiModel
public class OrderResponseDTO {
	@ApiModelProperty(value = "电商订单信息")
	OrderDTO orderDTO;
	@ApiModelProperty(value = "运单信息")
	List<WayBillItemDTO> wayBillList;
	
	public OrderDTO getOrderDTO() {
		return orderDTO;
	}
	public void setOrderDTO(OrderDTO orderDTO) {
		this.orderDTO = orderDTO;
	}
	public List<WayBillItemDTO> getWayBillList() {
		return wayBillList;
	}
	public void setWayBillList(List<WayBillItemDTO> wayBillList) {
		this.wayBillList = wayBillList;
	}
	
	
}
