package com.topideal.order.webapi.sale.dto;

import com.topideal.entity.dto.sale.SaleOutDepotDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SaleOutDepotResponseDTO {
	@ApiModelProperty(value = "销售出库单信息")
	private SaleOutDepotDTO saleOutDepotDTO;
	
	@ApiModelProperty(value = "是否有商品已上架 yes：该销售订单有商品上架，no:该销售订单还没有商品上架")
	private String isNotShelf;
	
	@ApiModelProperty(value = "pageSource")
	private String pageSource;

	public SaleOutDepotDTO getSaleOutDepotDTO() {
		return saleOutDepotDTO;
	}

	public void setSaleOutDepotDTO(SaleOutDepotDTO saleOutDepotDTO) {
		this.saleOutDepotDTO = saleOutDepotDTO;
	}

	public String getIsNotShelf() {
		return isNotShelf;
	}

	public void setIsNotShelf(String isNotShelf) {
		this.isNotShelf = isNotShelf;
	}

	public String getPageSource() {
		return pageSource;
	}

	public void setPageSource(String pageSource) {
		this.pageSource = pageSource;
	}
	
}
