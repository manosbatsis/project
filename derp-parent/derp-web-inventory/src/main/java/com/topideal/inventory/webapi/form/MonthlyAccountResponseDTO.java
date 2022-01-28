package com.topideal.inventory.webapi.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class MonthlyAccountResponseDTO {
	@ApiModelProperty(value = "仓库id")
	private Long depotId ;
	@ApiModelProperty(value = "结转月份")
	private String settlementMonth ;
	public Long getDepotId() {
		return depotId;
	}
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}
	public String getSettlementMonth() {
		return settlementMonth;
	}
	public void setSettlementMonth(String settlementMonth) {
		this.settlementMonth = settlementMonth;
	}


	
	
	
	
}
