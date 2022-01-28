package com.topideal.storage.webapi.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 库存调整Form
 * @date 2021-02-04
 */
@ApiModel
public class AdjustmentInventoryForm extends PageForm{
	@ApiModelProperty(value = "令牌",required = true)
	private String token;
    @ApiModelProperty(value = "库存调整单号")  
    private String code;
    @ApiModelProperty(value = "盘点仓库id")  
    private String depotId;
    @ApiModelProperty(value = "商家名称")  
    private String merchantName;
    @ApiModelProperty(value = "业务类别（常量集合adjustmentInventory_modelList）")  
    private String model;
    @ApiModelProperty(value = "状态（常量集合adjustmentType_statusList）")  
    private String status;
    @ApiModelProperty(value = "单据来源（常量集合adjustmentInventory_sourceList））") 
    private String source;
    @ApiModelProperty(value = "单据所属开始日期",hidden = true) 
    private String sourceStartTime;
    @ApiModelProperty(value = "单据所属结束日期",hidden = true) 
    private String sourceEndTime;
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
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSourceStartTime() {
		return sourceStartTime;
	}
	public void setSourceStartTime(String sourceStartTime) {
		this.sourceStartTime = sourceStartTime;
	}
	public String getSourceEndTime() {
		return sourceEndTime;
	}
	public void setSourceEndTime(String sourceEndTime) {
		this.sourceEndTime = sourceEndTime;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
}
