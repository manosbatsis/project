package com.topideal.order.webapi.purchase.form;

import com.topideal.entity.vo.purchase.PurchaseReturnItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ApiModel
public class PurchaseReturnOrderAddForm implements Serializable{

	@ApiModelProperty(value="令牌", required=true)
    private String token;
    /**
    * id
    */
	@ApiModelProperty(value="记录ID", required=false)
    private Long id;
    /**
    * 采购退货订单号
    */
	@ApiModelProperty(value="采购退货订单号", required=false)
    private String code;
    /**
    * 合同号
    */
	@ApiModelProperty(value="合同号", required=false)
    private String contractNo;
    /**
    * 退入仓库id
    */
	@ApiModelProperty(value="退入仓库id", required=false)
    private Long inDepotId;
    /**
    * 退入仓库名称
    */
	@ApiModelProperty(value="退入仓库名称", required=false)
    private String inDepotName;
    /**
    * 退出仓库id
    */
	@ApiModelProperty(value="退出仓库id", required=false)
    private Long outDepotId;
    /**
    * 退出仓库名称
    */
	@ApiModelProperty(value="退出仓库名称", required=false)
    private String outDepotName;
    /**
    * 备注
    */
	@ApiModelProperty(value="备注", required=false)
    private String remark;
    /**
    * 公司ID
    */
	@ApiModelProperty(value="公司ID", required=false)
    private Long merchantId;
    /**
    * 公司名称
    */
	@ApiModelProperty(value="公司名称", required=false)
    private String merchantName;
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
    * 客户名称
    */
	@ApiModelProperty(value="供应商名称", required=false)
    private String supplierName;
    /**
    * 计划退货数量
    */
	@ApiModelProperty(value="计划退货数量", required=false)
    private Integer totalReturnNum;
    /**
    * 是否同关区（0-否，1-是）
    */
	@ApiModelProperty(value="是否同关区（0-否，1-是）", required=false)
    private String isSameArea;
    /**
    * 币种 01 人民币 02 日元 03 澳元 04 美元 05 港币 06 欧元  07 英镑
    */
	@ApiModelProperty(value="币种", required=false)
    private String currency;

    /**
    * 海外仓理货单位 00-托盘 01-箱 02-件
    */
	@ApiModelProperty(value="海外仓理货单位", required=false)
    private String tallyingUnit;
    /**
    * 关联销售单单号
    */
	@ApiModelProperty(value="关联采购单号,多个以';'分隔", required=false)
    private String purchaseOrderRelCode;
    /**
    * 事业部名称
    */
    @ApiModelProperty(value="事业部名称", required=false)
    private String buName;
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
    /**
    * 提货方式 1-邮寄 2-自提
    */
    @ApiModelProperty(value="提货方式 1-邮寄 2-自提", required=false)
    private String deliveryMethod;
    /**
    * 收货地址
    */
    @ApiModelProperty(value="收货地址", required=false)
    private String deliveryAddr;
    /**
    * 国家
    */
    @ApiModelProperty(value="国家", required=false)
    private String country;
    /**
    * 收货人名称
    */
    @ApiModelProperty(value="收货人名称", required=false)
    private String deliveryName;
    /**
    * 目的地
    */
    @ApiModelProperty(value="目的地", required=false)
    private String destinationName;

    //本位币
    @ApiModelProperty(value="本位币", required=false)
    private String tgtCurrency;
    //汇率
    @ApiModelProperty(value="汇率", required=false)
    private BigDecimal rate;
    //目的地地址（内部使用）
    @ApiModelProperty(value="目的地地址（内部使用）", required=false)
    private String destinationAddress;

    @ApiModelProperty(value="表体明细")
    private List<PurchaseReturnItemModel> itemList ;

	@ApiModelProperty(value = "库位类型Id")
	private Long  stockLocationTypeId;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public Long getInDepotId() {
		return inDepotId;
	}
	public void setInDepotId(Long inDepotId) {
		this.inDepotId = inDepotId;
	}
	public String getInDepotName() {
		return inDepotName;
	}
	public void setInDepotName(String inDepotName) {
		this.inDepotName = inDepotName;
	}
	public Long getOutDepotId() {
		return outDepotId;
	}
	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}
	public String getOutDepotName() {
		return outDepotName;
	}
	public void setOutDepotName(String outDepotName) {
		this.outDepotName = outDepotName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public Integer getTotalReturnNum() {
		return totalReturnNum;
	}
	public void setTotalReturnNum(Integer totalReturnNum) {
		this.totalReturnNum = totalReturnNum;
	}
	public String getIsSameArea() {
		return isSameArea;
	}
	public void setIsSameArea(String isSameArea) {
		this.isSameArea = isSameArea;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getTallyingUnit() {
		return tallyingUnit;
	}
	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}
	public String getPurchaseOrderRelCode() {
		return purchaseOrderRelCode;
	}
	public void setPurchaseOrderRelCode(String purchaseOrderRelCode) {
		this.purchaseOrderRelCode = purchaseOrderRelCode;
	}
	public String getBuName() {
		return buName;
	}
	public void setBuName(String buName) {
		this.buName = buName;
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
	public String getDeliveryMethod() {
		return deliveryMethod;
	}
	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}
	public String getDeliveryAddr() {
		return deliveryAddr;
	}
	public void setDeliveryAddr(String deliveryAddr) {
		this.deliveryAddr = deliveryAddr;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getDeliveryName() {
		return deliveryName;
	}
	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}
	public String getDestinationName() {
		return destinationName;
	}
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
	public String getTgtCurrency() {
		return tgtCurrency;
	}
	public void setTgtCurrency(String tgtCurrency) {
		this.tgtCurrency = tgtCurrency;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public String getDestinationAddress() {
		return destinationAddress;
	}
	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public List<PurchaseReturnItemModel> getItemList() {
		return itemList;
	}

	public void setItemList(List<PurchaseReturnItemModel> itemList) {
		this.itemList = itemList;
	}

	public Long getStockLocationTypeId() {
		return stockLocationTypeId;
	}

	public void setStockLocationTypeId(Long stockLocationTypeId) {
		this.stockLocationTypeId = stockLocationTypeId;
	}
}
