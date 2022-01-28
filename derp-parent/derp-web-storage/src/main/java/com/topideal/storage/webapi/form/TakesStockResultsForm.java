package com.topideal.storage.webapi.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 盘点结果表Form
 * @date 2021-02-05
 */
@ApiModel
public class TakesStockResultsForm  extends PageForm{
	
	@ApiModelProperty(value = "令牌",required = true)
	 private String token;
	@ApiModelProperty(value = "盘点单号")
    private String code;
    @ApiModelProperty(value = "盘点仓库id")
    private String depotId;
    @ApiModelProperty(value = "服务类型")
    private String serverType;
    @ApiModelProperty(value = "业务场景")
    private String model;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "商家名称")
    private String merchantName;
	@ApiModelProperty(value = "单据id集合，多个用逗号隔开")
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
	public String getDepotId() {
		return depotId;
	}
	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
}

