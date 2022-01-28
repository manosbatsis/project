package com.topideal.order.webapi.common.form;

import com.topideal.common.system.webapi.PageForm;
import com.topideal.entity.vo.common.SdSaleConfigItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
@ApiModel
public class SdSaleConfigForm extends PageForm{
	
	@ApiModelProperty(value = "令牌", required = true)
    private String token;
	
	@ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "公司ID")
    private Long merchantId;

	@ApiModelProperty(value = "公司名称")
    private String merchantName;

	@ApiModelProperty(value = "事业部ID")
    private Long buId;

	@ApiModelProperty(value = "事业部名称")
    private String buName;

	@ApiModelProperty(value = "客户ID")
    private Long customerId;

	@ApiModelProperty(value = "i客户名称d")
    private String customerName;

	@ApiModelProperty(value = "生效日期")
    private String effectiveDateStr;
  
	@ApiModelProperty(value = "失效日期")
    private String expirationDateStr;

	@ApiModelProperty(value = "平台备注")
    private String remark;
	
	@ApiModelProperty(value = "销售sd配置明细")
	private List<SdSaleConfigItemModel> itemList;
	
	@ApiModelProperty(value = "操作 1-保存，2-审核")
	private String operate;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
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

	public String getEffectiveDateStr() {
		return effectiveDateStr;
	}

	public void setEffectiveDateStr(String effectiveDateStr) {
		this.effectiveDateStr = effectiveDateStr;
	}

	public String getExpirationDateStr() {
		return expirationDateStr;
	}

	public void setExpirationDateStr(String expirationDateStr) {
		this.expirationDateStr = expirationDateStr;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<SdSaleConfigItemModel> getItemList() {
		return itemList;
	}

	public void setItemList(List<SdSaleConfigItemModel> itemList) {
		this.itemList = itemList;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}
	
}
	
