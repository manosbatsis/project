package com.topideal.order.webapi.sale.dto;

import com.topideal.entity.dto.sale.PreSaleOrderDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 预售单返回值 DTO
 * @date 2021-02-08
 */
@ApiModel
public class PreSaleOrderResponseDTO {
	@ApiModelProperty(value = "预售单信息")
	private PreSaleOrderDTO preSaleOrderDTO;
	@ApiModelProperty(value = "预售单表体数量")
	private int itemCount;
	@ApiModelProperty(value = "理货单位是否必填标识")
	private int isTallyingRequired;
	public PreSaleOrderDTO getPreSaleOrderDTO() {
		return preSaleOrderDTO;
	}
	public void setPreSaleOrderDTO(PreSaleOrderDTO preSaleOrderDTO) {
		this.preSaleOrderDTO = preSaleOrderDTO;
	}
	public int getItemCount() {
		return itemCount;
	}
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	public int getIsTallyingRequired() {
		return isTallyingRequired;
	}
	public void setIsTallyingRequired(int isTallyingRequired) {
		this.isTallyingRequired = isTallyingRequired;
	}
	
}
