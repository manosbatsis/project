package com.topideal.order.webapi.sale.form;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class BuMoveInventoryForm  extends PageForm{
	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	
	@ApiModelProperty(value = "移库单号")
    private String code;
   
	@ApiModelProperty(value = "来源业务单号")
    private String businessNo;
    
	@ApiModelProperty(value = "移库状态 017-待移库,018-已移库,027-移库中")
    private String status;
	 
	@ApiModelProperty(value = "出库仓库ID")
    private String depotId;
   
	@ApiModelProperty(value = "移入事业部ID")
    private String inBuId;
   
	@ApiModelProperty(value = "移出事业部id")
    private String outBuId;
	
	@ApiModelProperty(value = "创建时间开始",hidden=true)
    private String createStartDate;

	@ApiModelProperty(value = "创建时间结束",hidden=true)
    private String createEndDate;

	@ApiModelProperty(value = "移库日期开始",hidden=true)
    private String moveStartDate;
	
	@ApiModelProperty(value = "移库日期结束",hidden=true)
    private String moveEndDate;

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
	public String getBusinessNo() {
		return businessNo;
	}
	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDepotId() {
		return depotId;
	}
	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}
	public String getInBuId() {
		return inBuId;
	}
	public void setInBuId(String inBuId) {
		this.inBuId = inBuId;
	}
	public String getOutBuId() {
		return outBuId;
	}
	public void setOutBuId(String outBuId) {
		this.outBuId = outBuId;
	}
	public String getCreateStartDate() {
		return createStartDate;
	}
	public void setCreateStartDate(String createStartDate) {
		this.createStartDate = createStartDate;
	}
	public String getCreateEndDate() {
		return createEndDate;
	}
	public void setCreateEndDate(String createEndDate) {
		this.createEndDate = createEndDate;
	}
	public String getMoveStartDate() {
		return moveStartDate;
	}
	public void setMoveStartDate(String moveStartDate) {
		this.moveStartDate = moveStartDate;
	}
	public String getMoveEndDate() {
		return moveEndDate;
	}
	public void setMoveEndDate(String moveEndDate) {
		this.moveEndDate = moveEndDate;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
}
