package com.topideal.order.webapi.purchase.form;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel
public class PurchaseReturnOrderForm extends PageForm{

	@ApiModelProperty(value = "令牌", required = true)
    private String token;
    /**
    * 采购退货订单号
    */
    @ApiModelProperty(value="采购退货订单号", required=false)
    private String code;
    /**
    * 退出仓库id
    */
    @ApiModelProperty(value="退出仓库id", required=false)
    private Long outDepotId;
    /**
    * 状态：001-待审核 ,003-已审核 ,006-已删除 ,016已出仓 ,007已完结
    */
    @ApiModelProperty(value="状态：001-待审核 ,003-已审核 ,006-已删除 ,016已出仓 ,007已完结", required=false)
    private String status;
    /**
    * 客户id
    */
    @ApiModelProperty(value="供应商id", required=false)
    private Long supplierId;
    
    /**
    *  事业部id
    */
    @ApiModelProperty(value="事业部id", required=false)
    private Long buId;
    
    /**
    * po号
    */
    @ApiModelProperty(value="po号", required=false)
    private String poNo;

	@ApiModelProperty(value = "订单ID,多个以‘,’隔开", required = false)
	private String ids;
    
    @ApiModelProperty(value="创建开始时间", required=false)
    private String createStartDate ;
    @ApiModelProperty(value="创建结束时间", required=false)
    private String createEndDate ;
    /**
     * 关联销售单单号
     */
     @ApiModelProperty(value="关联采购单单号", required=false)
     private String purchaseOrderRelCode;
    
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
	public Long getOutDepotId() {
		return outDepotId;
	}
	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public Long getBuId() {
		return buId;
	}
	public void setBuId(Long buId) {
		this.buId = buId;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
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

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getPurchaseOrderRelCode() {
		return purchaseOrderRelCode;
	}
	public void setPurchaseOrderRelCode(String purchaseOrderRelCode) {
		this.purchaseOrderRelCode = purchaseOrderRelCode;
	}
	
	
}