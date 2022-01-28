package com.topideal.order.webapi.sale.form;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class BillOutinDepotItemForm  extends PageForm{
	
	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	
	@ApiModelProperty(value = "出入库单id")
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
