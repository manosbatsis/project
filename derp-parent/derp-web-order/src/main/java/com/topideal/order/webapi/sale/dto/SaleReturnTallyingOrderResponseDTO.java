package com.topideal.order.webapi.sale.dto;

import java.util.List;

import com.topideal.entity.dto.purchase.TallyingItemBatchDTO;
import com.topideal.entity.dto.purchase.TallyingOrderDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SaleReturnTallyingOrderResponseDTO {
	@ApiModelProperty(value = "理货单信息")
	TallyingOrderDTO tallyingOrderDTO;
	@ApiModelProperty(value = "理货单商品批次信息")
	List<TallyingItemBatchDTO> tallyingItemBatchList;
	
	public TallyingOrderDTO getTallyingOrderDTO() {
		return tallyingOrderDTO;
	}
	public void setTallyingOrderDTO(TallyingOrderDTO tallyingOrderDTO) {
		this.tallyingOrderDTO = tallyingOrderDTO;
	}
	public List<TallyingItemBatchDTO> getTallyingItemBatchList() {
		return tallyingItemBatchList;
	}
	public void setTallyingItemBatchList(List<TallyingItemBatchDTO> tallyingItemBatchList) {
		this.tallyingItemBatchList = tallyingItemBatchList;
	}
	
	
}
