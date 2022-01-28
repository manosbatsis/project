package com.topideal.order.webapi.purchase.form;

import com.topideal.entity.dto.purchase.PurchaseReturnOdepotItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel
public class PurchaseReturnOdepotAddForm implements Serializable{

	@ApiModelProperty(value="令牌", required=true)
    private String token;
    /**
    * id
    */
	@ApiModelProperty(value="记录ID", required=false)
    private Long id;
    /**
    * 采购退货出库订单号
    */
	@ApiModelProperty(value="采购退货出库订单号", required=false)
    private String code;
    /**
    * 采购退订单号
    */
	@ApiModelProperty(value="采购退订单号", required=false)
    private String purchaseReturnCode;
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
    * 出库时间
    */
	@ApiModelProperty(value="出库时间", required=false)
    private String returnOutDate;
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
    * 状态：001-待审核 ,003-已审核 ,015:待出仓,016:已出仓,027:出库中 ,007已完结
    */
	@ApiModelProperty(value="状态：001-待审核 ,003-已审核 ,015:待出仓,016:已出仓,027:出库中 ,007已完结", required=false)
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
    * 币种 01 人民币 02 日元 03 澳元 04 美元 05 港币 06 欧元  07 英镑
    */
	@ApiModelProperty(value="币种", required=false)
    private String currency;
	@ApiModelProperty(value="币种中文", required=false)
    private String currencyLabel;
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
    * 物流企业名称
    */
	@ApiModelProperty(value="物流企业名称", required=false)
    private String logisticsName;
    /**
    * 进口模式10：BBC;20：BC;30：保留; 40：CC
    */
	@ApiModelProperty(value="进口模式10：BBC;20：BC;30：保留; 40：CC", required=false)
    private String importMode;
    /**
    * LBX单号
    */
	@ApiModelProperty(value="LBX单号", required=false)
    private String lbxNo;
    /**
    * 提单号
    */
	@ApiModelProperty(value="提单号", required=false)
    private String blNo;
    /**
    * 运单号
    */
	@ApiModelProperty(value="运单号", required=false)
    private String wayBillNo;

    /**
     * 出库时间
     */
	@ApiModelProperty(value="出库时间开始", required=false)
    private String returnOutStartDate;
	@ApiModelProperty(value="出库时间结束", required=false)
    private String returnOutEndDate;

    //理货单位
	@ApiModelProperty(value="理货单位", required=false)
    private String tallyingUnit;
	@ApiModelProperty(value="理货单位中文", required=false)
    private String tallyingUnitLabel;

    //采购退货ID
	@ApiModelProperty(value="采购退货订单ID", required=false)
    private Long purchaseReturnId;

    /**
     * 外部订单号
     */
	@ApiModelProperty(value="外部订单号", required=false)
    private String extraCode;

	@ApiModelProperty(value="表体明细")
    private List<PurchaseReturnOdepotItemDTO> itemList ;

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

	public String getPurchaseReturnCode() {
		return purchaseReturnCode;
	}

	public void setPurchaseReturnCode(String purchaseReturnCode) {
		this.purchaseReturnCode = purchaseReturnCode;
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

	public String getReturnOutDate() {
		return returnOutDate;
	}

	public void setReturnOutDate(String returnOutDate) {
		this.returnOutDate = returnOutDate;
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrencyLabel() {
		return currencyLabel;
	}

	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
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

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	public String getImportMode() {
		return importMode;
	}

	public void setImportMode(String importMode) {
		this.importMode = importMode;
	}

	public String getLbxNo() {
		return lbxNo;
	}

	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}

	public String getBlNo() {
		return blNo;
	}

	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}

	public String getWayBillNo() {
		return wayBillNo;
	}

	public void setWayBillNo(String wayBillNo) {
		this.wayBillNo = wayBillNo;
	}

	public String getReturnOutStartDate() {
		return returnOutStartDate;
	}

	public void setReturnOutStartDate(String returnOutStartDate) {
		this.returnOutStartDate = returnOutStartDate;
	}

	public String getReturnOutEndDate() {
		return returnOutEndDate;
	}

	public void setReturnOutEndDate(String returnOutEndDate) {
		this.returnOutEndDate = returnOutEndDate;
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}

	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}

	public void setTallyingUnitLabel(String tallyingUnitLabel) {
		this.tallyingUnitLabel = tallyingUnitLabel;
	}

	public Long getPurchaseReturnId() {
		return purchaseReturnId;
	}

	public void setPurchaseReturnId(Long purchaseReturnId) {
		this.purchaseReturnId = purchaseReturnId;
	}

	public String getExtraCode() {
		return extraCode;
	}

	public void setExtraCode(String extraCode) {
		this.extraCode = extraCode;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<PurchaseReturnOdepotItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<PurchaseReturnOdepotItemDTO> itemList) {
		this.itemList = itemList;
	}
}
