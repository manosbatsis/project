package com.topideal.entity.dto.bill;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 开票模板表体dto
 * @Author: Chen Yiluan
 * @Date: 2021/09/07 18:19
 **/
@ApiModel
public class InvoiceTemplateItemYSDTO implements Serializable {

    @ApiModelProperty("序号")
    private String index;
    @ApiModelProperty("poNo号")
    private String poNo;
    @ApiModelProperty("单位")
    private String unit;
    @ApiModelProperty("数量")
    private String quantity;    
    @ApiModelProperty("金额")
    private String amount;
    @ApiModelProperty("备注")
    private String remark;
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}



  
}
