package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 账单出库单批次Form
 * @date 2021-02-05
 */
@ApiModel
public class BillOutinDepotBatchForm extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token; 
	
	@ApiModelProperty(value = "账单出入库单id",required = true)
    private String outinDepotId;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOutinDepotId() {
		return outinDepotId;
	}

	public void setOutinDepotId(String outinDepotId) {
		this.outinDepotId = outinDepotId;
	}
	
}
