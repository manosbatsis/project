package com.topideal.storage.webapi.dto;

import java.util.List;

import com.topideal.entity.dto.TakesStockDTO;
import com.topideal.entity.vo.TakesStockItemModel;
import com.topideal.entity.vo.TakesStockModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 盘点指令返回值DTO
 * 
 * @date 2021-02-07
 */
@ApiModel
public class TakesStockResponseDTO {
	@ApiModelProperty(value = "盘点指令Model")
	private TakesStockModel takesStockModel;
	
	@ApiModelProperty(value = "盘点指令DTO")
	private TakesStockDTO takesStockDTO;
	
	@ApiModelProperty(value = "盘点指令表体")
	private List<TakesStockItemModel> itemList;

	public TakesStockModel getTakesStockModel() {
		return takesStockModel;
	}

	public void setTakesStockModel(TakesStockModel takesStockModel) {
		this.takesStockModel = takesStockModel;
	}

	public TakesStockDTO getTakesStockDTO() {
		return takesStockDTO;
	}

	public void setTakesStockDTO(TakesStockDTO takesStockDTO) {
		this.takesStockDTO = takesStockDTO;
	}

	public List<TakesStockItemModel> getItemList() {
		return itemList;
	}

	public void setItemList(List<TakesStockItemModel> itemList) {
		this.itemList = itemList;
	}
}
