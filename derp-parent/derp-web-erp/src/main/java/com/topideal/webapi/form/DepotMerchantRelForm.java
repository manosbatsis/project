package com.topideal.webapi.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 仓库商家关联表 model
 */
@ApiModel
public class DepotMerchantRelForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;

	@ApiModelProperty(value = "公司id")
	private String merchantId;

	@ApiModelProperty(value = "进出接口指令 1-是 0 - 否")
	private String isInOutInstruction;

	@ApiModelProperty(value = "统计存货跌价 1-是 0-否")
	private String isInvertoryFallingPrice;

	@ApiModelProperty(value = "选品限制 1-仅备案商品 2-仅外仓统一码 3-无限制")
	private String productRestriction;

	@ApiModelProperty(value = "已入定出 1-是 0-否")
	private String isInDependOut;

	@ApiModelProperty(value = "已出定入 0-否 1-是")
	private String isOutDependIn;

	@ApiModelProperty(value = "是否代销仓 0-否,1-是")
	private String isTopBooks;

	@ApiModelProperty(value = "仓库类别 a-保税仓，b-备查库，c-海外仓，d-中转仓,e-暂存区，f-销毁区 g-内贸仓")
	private String type ;
	@ApiModelProperty(value = "是否代客管理仓库")
	private String isValetManage;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getIsInOutInstruction() {
		return isInOutInstruction;
	}

	public void setIsInOutInstruction(String isInOutInstruction) {
		this.isInOutInstruction = isInOutInstruction;
	}

	public String getIsInvertoryFallingPrice() {
		return isInvertoryFallingPrice;
	}

	public void setIsInvertoryFallingPrice(String isInvertoryFallingPrice) {
		this.isInvertoryFallingPrice = isInvertoryFallingPrice;
	}

	public String getProductRestriction() {
		return productRestriction;
	}

	public void setProductRestriction(String productRestriction) {
		this.productRestriction = productRestriction;
	}

	public String getIsInDependOut() {
		return isInDependOut;
	}

	public void setIsInDependOut(String isInDependOut) {
		this.isInDependOut = isInDependOut;
	}

	public String getIsOutDependIn() {
		return isOutDependIn;
	}

	public void setIsOutDependIn(String isOutDependIn) {
		this.isOutDependIn = isOutDependIn;
	}

	public String getIsTopBooks() {
		return isTopBooks;
	}

	public void setIsTopBooks(String isTopBooks) {
		this.isTopBooks = isTopBooks;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsValetManage() {
		return isValetManage;
	}

	public void setIsValetManage(String isValetManage) {
		this.isValetManage = isValetManage;
	}
}
