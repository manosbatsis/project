package com.topideal.order.webapi.platformdata.form;
import java.io.Serializable;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class OrealPurchaseOrderForm extends PageForm implements Serializable{

    
	@ApiModelProperty(value = "令牌",required = true)
	private String token ;
	@ApiModelProperty(value = "订单编号")
    private String vordercode;
	@ApiModelProperty(value = "CSR确认单号")
    private String vdef7;
	@ApiModelProperty(value = "订单开始时间")
    private String dorderdateStartDate;
	@ApiModelProperty(value = "订单开始结束时间")
    private String dorderdateEndDate;
	@ApiModelProperty(value = "事业部")
    private Long  buId;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getVordercode() {
		return vordercode;
	}

	public void setVordercode(String vordercode) {
		this.vordercode = vordercode;
	}

	public String getVdef7() {
		return vdef7;
	}

	public void setVdef7(String vdef7) {
		this.vdef7 = vdef7;
	}

	public String getDorderdateStartDate() {
		return dorderdateStartDate;
	}

	public void setDorderdateStartDate(String dorderdateStartDate) {
		this.dorderdateStartDate = dorderdateStartDate;
	}

	public String getDorderdateEndDate() {
		return dorderdateEndDate;
	}

	public void setDorderdateEndDate(String dorderdateEndDate) {
		this.dorderdateEndDate = dorderdateEndDate;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}
   

   






}
