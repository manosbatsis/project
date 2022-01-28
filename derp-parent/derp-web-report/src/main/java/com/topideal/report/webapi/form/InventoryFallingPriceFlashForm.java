package com.topideal.report.webapi.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel
public class InventoryFallingPriceFlashForm{

	@ApiModelProperty(value = "令牌")
	private String token;
	@ApiModelProperty(value = "报表月份")
    private String reportMonth;
	@ApiModelProperty(value = "仓库ids 多个用,号隔开")
	private String depotIds ;

	@ApiModelProperty(value = "事业部id 多个用,号隔开")
	private String buIds;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getReportMonth() {
		return reportMonth;
	}

	public void setReportMonth(String reportMonth) {
		this.reportMonth = reportMonth;
	}

	public String getDepotIds() {
		return depotIds;
	}

	public void setDepotIds(String depotIds) {
		this.depotIds = depotIds;
	}

	public String getBuIds() {
		return buIds;
	}

	public void setBuIds(String buIds) {
		this.buIds = buIds;
	}
}
