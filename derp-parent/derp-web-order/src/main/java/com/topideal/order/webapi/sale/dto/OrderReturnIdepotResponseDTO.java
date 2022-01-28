package com.topideal.order.webapi.sale.dto;

import java.util.List;

import com.topideal.entity.dto.sale.OrderReturnIdepotBatchDTO;
import com.topideal.entity.dto.sale.OrderReturnIdepotDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 电商退返回值 DTO
 * @date 2021-02-08
 */
@ApiModel
public class OrderReturnIdepotResponseDTO {
	@ApiModelProperty(value = "电商退订单")
	private OrderReturnIdepotDTO orderReturnIdepotDTO;
	
	@ApiModelProperty(value = "电商订单退货商品批次详情")
	private List<OrderReturnIdepotBatchDTO> batchAllList;

	public OrderReturnIdepotDTO getOrderReturnIdepotDTO() {
		return orderReturnIdepotDTO;
	}

	public void setOrderReturnIdepotDTO(OrderReturnIdepotDTO orderReturnIdepotDTO) {
		this.orderReturnIdepotDTO = orderReturnIdepotDTO;
	}

	public List<OrderReturnIdepotBatchDTO> getBatchAllList() {
		return batchAllList;
	}

	public void setBatchAllList(List<OrderReturnIdepotBatchDTO> batchAllList) {
		this.batchAllList = batchAllList;
	}
}
