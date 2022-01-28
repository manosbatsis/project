package com.topideal.order.webapi.sale.form;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class PreSaleOrderForm  extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token; 	

	@ApiModelProperty(value = "预售单编号")
    private String code;

	@ApiModelProperty(value = "订单状态:001:待审核,003:已审核,007:已完结")
    private String state;	

	@ApiModelProperty(value = "客户id")
    private String customerId;
	
	@ApiModelProperty(value = "出仓仓库ID")
    private String outDepotId;

	@ApiModelProperty(value = "事业部id")
    private String buId;

	@ApiModelProperty(value = "po单号")
    private String poNo;
	
	@ApiModelProperty(value = "订单日期时间开始")
    private String createDateStartDate;
	
	@ApiModelProperty(value = "订单日期时间结束")
    private String createDateEndDate;
	 
	@ApiModelProperty(value = "主键集合ids")
	private String ids;
		 
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getOutDepotId() {
		return outDepotId;
	}

	public void setOutDepotId(String outDepotId) {
		this.outDepotId = outDepotId;
	}

	public String getBuId() {
		return buId;
	}

	public void setBuId(String buId) {
		this.buId = buId;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getCreateDateStartDate() {
		return createDateStartDate;
	}

	public void setCreateDateStartDate(String createDateStartDate) {
		this.createDateStartDate = createDateStartDate;
	}

	public String getCreateDateEndDate() {
		return createDateEndDate;
	}

	public void setCreateDateEndDate(String createDateEndDate) {
		this.createDateEndDate = createDateEndDate;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}
