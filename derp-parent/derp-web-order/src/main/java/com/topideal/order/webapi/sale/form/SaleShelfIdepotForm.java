package com.topideal.order.webapi.sale.form;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class SaleShelfIdepotForm extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;

	@ApiModelProperty(value = "上架入库单号")
    private String code;

	@ApiModelProperty(value = "销售订单编号")
    private String saleOrderCode;

	@ApiModelProperty(value = "销售出库清单编号")
    private String saleOutCode;

	@ApiModelProperty(value = "入仓仓库id")
    private String inDepotId;

	@ApiModelProperty(value = "出仓仓库ID")
    private String outDepotId;

	@ApiModelProperty(value = "po单号")
    private String poNo;

	@ApiModelProperty(value = "单据状态:  011-待入仓,012-已入仓,028-入库中")
    private String state;

	@ApiModelProperty(value = "入库开始时间")
    private String shelfStartDate;

	@ApiModelProperty(value = "入库结束时间")
    private String shelfEndDate;

	@ApiModelProperty(value = "上架单号")
    private String code1;

	@ApiModelProperty(value = "是否红冲单：0-否，1-是")
	private String isWriteOff;

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

	public String getSaleOrderCode() {
		return saleOrderCode;
	}

	public void setSaleOrderCode(String saleOrderCode) {
		this.saleOrderCode = saleOrderCode;
	}

	public String getSaleOutCode() {
		return saleOutCode;
	}

	public void setSaleOutCode(String saleOutCode) {
		this.saleOutCode = saleOutCode;
	}

	public String getInDepotId() {
		return inDepotId;
	}

	public void setInDepotId(String inDepotId) {
		this.inDepotId = inDepotId;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getShelfStartDate() {
		return shelfStartDate;
	}

	public void setShelfStartDate(String shelfStartDate) {
		this.shelfStartDate = shelfStartDate;
	}

	public String getShelfEndDate() {
		return shelfEndDate;
	}

	public void setShelfEndDate(String shelfEndDate) {
		this.shelfEndDate = shelfEndDate;
	}

	public String getCode1() {
		return code1;
	}

	public void setCode1(String code1) {
		this.code1 = code1;
	}

	public String getIsWriteOff() {
		return isWriteOff;
	}

	public void setIsWriteOff(String isWriteOff) {
		this.isWriteOff = isWriteOff;
	}
}
