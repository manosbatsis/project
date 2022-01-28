package com.topideal.order.webapi.common.form;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class SdSaleVerifyConfigForm extends PageForm {

    @ApiModelProperty(value = "令牌", required = true)
    private String token;

    @ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "公司ID")
    private Long merchantId;

	@ApiModelProperty(value = "事业部ID")
    private Long buId;

	@ApiModelProperty(value = "客户ID")
    private Long customerId;

	@ApiModelProperty(value = "核销类型 0-按PO核销 1-按上架核销")
    private String verifyType;

	@ApiModelProperty(value = "是否按商品核销 0-否 1-是")
    private String isMerchandiseVerify;

	@ApiModelProperty(value = "备注")
    private String remark;

	@ApiModelProperty(value = "状态 0-未审核 1-已启用 2-已停用")
    private String status;
	
	@ApiModelProperty(value = "操作编码 ，1-保存 2-审核")
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

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getVerifyType() {
		return verifyType;
	}

	public void setVerifyType(String verifyType) {
		this.verifyType = verifyType;
	}

	public String getIsMerchandiseVerify() {
		return isMerchandiseVerify;
	}

	public void setIsMerchandiseVerify(String isMerchandiseVerify) {
		this.isMerchandiseVerify = isMerchandiseVerify;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}
}
