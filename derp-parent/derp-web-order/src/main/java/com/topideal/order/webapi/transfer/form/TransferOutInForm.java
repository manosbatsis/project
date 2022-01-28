package com.topideal.order.webapi.transfer.form;


import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 调出调入流水bean
 * */
@ApiModel
public class TransferOutInForm extends PageForm {

	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	@ApiModelProperty(value = "商家id")
	private Long merchantId;
	@ApiModelProperty(value = "调出仓库id")
	private Long outDepotId;
	@ApiModelProperty(value = "调入仓库id")
	private Long inDepotId;
	@ApiModelProperty(value = "调拨单号")
	private String code;
	@ApiModelProperty(value = "调出商品货号")
	private String outGoodsNo;
	@ApiModelProperty(value = "调拨起始时间")
	private String createDateStart;
	@ApiModelProperty(value = "调拨结束时间")
	private String createDateEnd;
	@ApiModelProperty(value = "事业部id")
	private Long buId;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Long getOutDepotId() {
		return outDepotId;
	}

	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}

	public Long getInDepotId() {
		return inDepotId;
	}

	public void setInDepotId(Long inDepotId) {
		this.inDepotId = inDepotId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOutGoodsNo() {
		return outGoodsNo;
	}

	public void setOutGoodsNo(String outGoodsNo) {
		this.outGoodsNo = outGoodsNo;
	}

	public String getCreateDateStart() {
		return createDateStart;
	}

	public void setCreateDateStart(String createDateStart) {
		this.createDateStart = createDateStart;
	}

	public String getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}
}
