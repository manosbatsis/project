package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 领料单
 *
 */
@ApiModel
public class MaterialOrderForm  extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token; 
	@ApiModelProperty(value = "领料单号")
	private String code;
	@ApiModelProperty(value = "调出仓库ID") 
	private String outDepotId;	
	@ApiModelProperty(value = "po单号") 
	private String poNo;
	@ApiModelProperty(value = "客户ID(供应商)") 
	private String customerId;
	@ApiModelProperty(value = "事业部id")
	private String buId;
	@ApiModelProperty(value = "订单状态:001:待审核,002:审核中,003:已审核,004:出库中,005:已出库,006:已删除")
	private String state; 
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
	public String getOutDepotId() {
		return outDepotId;
	}
	public void setOutDepotId(String outDepotId) {
		this.outDepotId = outDepotId;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getBuId() {
		return buId;
	}
	public void setBuId(String buId) {
		this.buId = buId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
}