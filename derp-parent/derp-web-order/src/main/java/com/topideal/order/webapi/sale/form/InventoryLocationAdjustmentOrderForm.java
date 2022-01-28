package com.topideal.order.webapi.sale.form;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class InventoryLocationAdjustmentOrderForm extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	
	@ApiModelProperty(value = "库位调整单号")
    private String code;
 
	@ApiModelProperty(value = "仓库ID")
    private String depotId;
  
	@ApiModelProperty(value = "事业部id")
    private String buId;
   
	@ApiModelProperty(value = "单据类型 DSDD-电商订单 DBRK-调拨入库 DBCK-调拨出库 XSCK-销售出库")
    private String type;
	 
	@ApiModelProperty(value = "主键集合ids")
	private String ids;
    @ApiModelProperty(value = "客户id")
    private Long customerId;
    @ApiModelProperty(value = "客户名称")
    private String customerName; 
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

	public String getDepotId() {
		return depotId;
	}

	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}

	public String getBuId() {
		return buId;
	}

	public void setBuId(String buId) {
		this.buId = buId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

}
