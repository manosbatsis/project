package com.topideal.storage.webapi.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 盘点指令Form
 * @date 2021-02-05
 */
@ApiModel
public class TakesStockForm extends PageForm {

	 @ApiModelProperty(value = "令牌",required = true)
	 private String token;
	 @ApiModelProperty(value = "盘点单号")  
     private String code;
	 @ApiModelProperty(value = "盘点仓库id") 
     private String depotId;
	 @ApiModelProperty(value = "商家名称") 
     private String merchantName;    
	 @ApiModelProperty(value = "服务类型")
     private String serverType;
	 @ApiModelProperty(value = "状态")
	 private String status;
	 
	 @ApiModelProperty(value = "业务场景")
	 private String model;
	 @ApiModelProperty(value = "备注")
	 private String remark;
	 @ApiModelProperty(value = "盘点申请id")
	 private String id;
	 @ApiModelProperty(value = "商品信息json")
	 private String goodsList;
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
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(String goodsList) {
		this.goodsList = goodsList;
	}

}

