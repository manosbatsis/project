package com.topideal.storage.webapi.dto;

import java.util.List;

import com.topideal.entity.dto.TakesStockResultItemDTO;
import com.topideal.entity.dto.TakesStockResultsDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 盘点结果返回值DTO
 * @date 2021-02-07
 */
@ApiModel
public class TakesStockResultResponseDTO {
	@ApiModelProperty(value = "盘点结果DTO")
	private TakesStockResultsDTO takesStockResultDTO;
	
	@ApiModelProperty(value = "盘点结果表体")
	private List<TakesStockResultItemDTO> itemList;

	public TakesStockResultsDTO getTakesStockResultDTO() {
		return takesStockResultDTO;
	}

	public void setTakesStockResultDTO(TakesStockResultsDTO takesStockResultDTO) {
		this.takesStockResultDTO = takesStockResultDTO;
	}

	public List<TakesStockResultItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<TakesStockResultItemDTO> itemList) {
		this.itemList = itemList;
	}
	
}
