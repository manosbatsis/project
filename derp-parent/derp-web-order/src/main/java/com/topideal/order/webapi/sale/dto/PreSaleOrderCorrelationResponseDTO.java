package com.topideal.order.webapi.sale.dto;

import java.util.List;

import com.topideal.entity.dto.sale.PreSaleOrderCorrelationDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PreSaleOrderCorrelationResponseDTO {
	@ApiModelProperty(value = "预售勾稽列汇总信息") 
	private PreSaleOrderCorrelationDTO mainInfo;
	
	@ApiModelProperty(value = "预售勾稽列明细")  
	private List<PreSaleOrderCorrelationDTO> details;

	public PreSaleOrderCorrelationDTO getMainInfo() {
		return mainInfo;
	}

	public void setMainInfo(PreSaleOrderCorrelationDTO mainInfo) {
		this.mainInfo = mainInfo;
	}

	public List<PreSaleOrderCorrelationDTO> getDetails() {
		return details;
	}

	public void setDetails(List<PreSaleOrderCorrelationDTO> details) {
		this.details = details;
	}

}
