package com.topideal.order.webapi.sale.dto;

import com.topideal.entity.dto.sale.SaleShelfIdepotDTO;
import com.topideal.entity.dto.sale.SaleShelfIdepotItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class SaleShelfIdepotResponseDTO {
	@ApiModelProperty(value="上架入库单信息")
	private SaleShelfIdepotDTO saleShelfIdepotDTO ;

	@ApiModelProperty(value="上架入库单表体信息")
	private List<SaleShelfIdepotItemDTO> itemList;

	public SaleShelfIdepotDTO getSaleShelfIdepotDTO() {
		return saleShelfIdepotDTO;
	}
	public void setSaleShelfIdepotDTO(SaleShelfIdepotDTO saleShelfIdepotDTO) {
		this.saleShelfIdepotDTO = saleShelfIdepotDTO;
	}
	public List<SaleShelfIdepotItemDTO> getItemList() {
		return itemList;
	}
	public void setItemList(List<SaleShelfIdepotItemDTO> itemList) {
		this.itemList = itemList;
	}
}
