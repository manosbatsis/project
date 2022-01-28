package com.topideal.entity.dto.main;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 客户销售价格表
 * @author lian_
 */
public class CustomerSalePriceDTO extends PageModel implements Serializable{
    //id
	@ApiModelProperty(value = "id")
    private Long id;
    //标准条形码
	@ApiModelProperty(value = "标准条形码")
    private String commbarcode;
	//条形码
	@ApiModelProperty(value = "条形码")
	private String barcode;
    //商品名称
	@ApiModelProperty(value = "商品名称")
    private String goodsName;
    //商家ID
	@ApiModelProperty(value = "商家ID")
    private Long merchantId;
    //商家名称
	@ApiModelProperty(value = "商家名称")
    private String merchantName;
    //客户ID
	@ApiModelProperty(value = "客户ID")
    private Long customerId;
    //客户名称
	@ApiModelProperty(value = "客户名称")
    private String customerName;
 	//客户编码
	@ApiModelProperty(value = "客户编码")
 	private String customerCode;
    //状态 006删除 001 待审核 003已审核
	@ApiModelProperty(value = "状态 006删除 001 待审核 003已审核")
    private String status;
	@ApiModelProperty(value = "状态 006删除 001 待审核 003已审核label")
    private String statusLabel;
    //品牌id
	@ApiModelProperty(value = "品牌id")
    private Long brandId;
    //品牌名称
	@ApiModelProperty(value = "品牌名称")
    private String brandName;   
    //价格失效时间
	@ApiModelProperty(value = "价格失效时间")
    private Timestamp expiryDate;
	//价格失效时间Str
	@ApiModelProperty(value = "价格失效时间")
	private String expiryDateStr;
    //价格生效时间
	@ApiModelProperty(value = "价格生效时间")
    private Timestamp effectiveDate;
	//价格生效时间Str
	@ApiModelProperty(value = "价格生效时间")
	private String effectiveDateStr;
    // 币种 
	@ApiModelProperty(value = "币种")
    private String currency;
	@ApiModelProperty(value = "币种label")
    private String currencyLabel;
    //销售价格（RMB）
	@ApiModelProperty(value = "销售价格（RMB）")
    private BigDecimal salePrice;
	private String salePriceLabel;
    //规格型号
	@ApiModelProperty(value = "规格型号")
    private String spec;
    //修改日期
	@ApiModelProperty(value = "修改日期")
    private Timestamp modifyDate;
    //创建时间
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    // 审核时间 
	@ApiModelProperty(value = "审核时间")
    private Timestamp auditDate;
    //创建人id
	@ApiModelProperty(value = "创建人id")
    private Long creater;
    //创建人名称
	@ApiModelProperty(value = "创建人名称")
    private String createName;
    //审核人id
	@ApiModelProperty(value = "审核人id")
    private Long auditor;
    //审核人名称
	@ApiModelProperty(value = "审核人名称")
    private String auditName;
    //修改人id
	@ApiModelProperty(value = "修改人id")
    private Long modifier;
    //修改人名称
	@ApiModelProperty(value = "修改人名称")
    private String modifierName;
	//事业部名称
	@ApiModelProperty(value = "事业部名称")
	private String buName;
	//提交人名称
	@ApiModelProperty(value = "提交人名称")
	private String submitName;
	//提交人id
	@ApiModelProperty(value = "提交人id")
	private Long submitId;

	//提交时间
	private Timestamp submitDate;
	/**
	 *  事业部id
	 */
	@ApiModelProperty(value = "事业部id")
	private Long buId;
	@ApiModelProperty(value = "事业部idList")
	private List buIdList;
	private List<Long> customerList;

	//备注
	@ApiModelProperty(value = "备注")
	private String remark;
	//编码(暂时用于关联附件)
	@ApiModelProperty(value = "编码(暂时用于关联附件)")
	private String code;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCommbarcode() {
		return commbarcode;
	}
	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
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
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
		if(status != null) {
			this.statusLabel = DERP_SYS.getLabelByKey(DERP_SYS.customer_sale_price_statusList, status);
		}
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public Timestamp getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Timestamp expiryDate) {
		this.expiryDate = expiryDate;
	}
	public Timestamp getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Timestamp effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
		if(currency != null) {
			this.currencyLabel = DERP_SYS.getLabelByKey(DERP.currencyCodeList, currency);
		}
	}
	public BigDecimal getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
		this.salePriceLabel = salePrice.toPlainString();
	}

	public String getSalePriceLabel() {
		return salePriceLabel;
	}

	public void setSalePriceLabel(String salePriceLabel) {
		this.salePriceLabel = salePriceLabel;
	}

	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	public String getCurrencyLabel() {
		return currencyLabel;
	}
	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
	}
	public Timestamp getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Timestamp auditDate) {
		this.auditDate = auditDate;
	}
	public Long getCreater() {
		return creater;
	}
	public void setCreater(Long creater) {
		this.creater = creater;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public Long getAuditor() {
		return auditor;
	}
	public void setAuditor(Long auditor) {
		this.auditor = auditor;
	}
	public String getAuditName() {
		return auditName;
	}
	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}
	public Long getModifier() {
		return modifier;
	}
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	public String getModifierName() {
		return modifierName;
	}
	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
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

	public String getSubmitName() {
		return submitName;
	}

	public void setSubmitName(String submitName) {
		this.submitName = submitName;
	}

	public Long getSubmitId() {
		return submitId;
	}

	public void setSubmitId(Long submitId) {
		this.submitId = submitId;
	}

	public List getBuIdList() {
		return buIdList;
	}

	public void setBuIdList(List buIdList) {
		this.buIdList = buIdList;
	}

	public List<Long> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List<Long> customerList) {
		this.customerList = customerList;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getExpiryDateStr() {
		return expiryDateStr;
	}

	public void setExpiryDateStr(String expiryDateStr) {
		this.expiryDateStr = expiryDateStr;
	}

	public String getEffectiveDateStr() {
		return effectiveDateStr;
	}

	public void setEffectiveDateStr(String effectiveDateStr) {
		this.effectiveDateStr = effectiveDateStr;
	}

	public Timestamp getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Timestamp submitDate) {
		this.submitDate = submitDate;
	}
}

