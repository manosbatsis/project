package com.topideal.order.webapi.platformdata.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/***
 * 补货动销报表查询form
 * @author Guobs
 *
 */
@ApiModel
public class PurchasingReportsForm extends PageForm{

	@ApiModelProperty(value = "令牌", required = true)
	private String token;
	
	@ApiModelProperty(value = "仓库", required = false)
	private String warehouse;
	
	@ApiModelProperty(value = "商品编码", required = false)
	private String wareId;
	
	@ApiModelProperty(value = "平台", required = false)
	private String platform;
	
	@ApiModelProperty(value = "报表日期 yyyy-MM-dd", required = false)
	private String reportDate;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getWareId() {
		return wareId;
	}

	public void setWareId(String wareId) {
		this.wareId = wareId;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	
	

}
