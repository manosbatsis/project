package com.topideal.order.webapi.sale.dto;

import com.topideal.entity.dto.sale.SaleShelfDTO;
import com.topideal.entity.dto.sale.ShelfDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class ShelfResponseDTO {
	@ApiModelProperty(value = "上架单信息")
	private ShelfDTO shelfDTO ;
	@ApiModelProperty(value = "上架单明细")
	private List<SaleShelfDTO> itemList;

	public ShelfDTO getShelfDTO() {
		return shelfDTO;
	}
	public void setShelfDTO(ShelfDTO shelfDTO) {
		this.shelfDTO = shelfDTO;
	}
	public List<SaleShelfDTO> getItemList() {
		return itemList;
	}
	public void setItemList(List<SaleShelfDTO> itemList) {
		this.itemList = itemList;
	}


}
