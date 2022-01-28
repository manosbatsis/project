package com.topideal.report.webapi.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class WeightedPriceForm extends PageForm{

	@ApiModelProperty(value = "令牌")
    private String token;

	@ApiModelProperty(value = "标准条码")
    private String commbarcode;

	@ApiModelProperty(value = "品牌id")
    private Long brandId;

	@ApiModelProperty(value = "生效月份")
    private String effectiveMonth;

	@ApiModelProperty(value = "事业部id")
    private Long buId;
	
	@ApiModelProperty(value = "条形码")
    private String barcode;

	@ApiModelProperty(value = "更新开始时间")
    private String createDateStart;

	@ApiModelProperty(value = "更新结束时间")
    private String createDateEnd;
	
	@ApiModelProperty(value = "单据id，多个用逗号分隔")
    private String ids;
	
	@ApiModelProperty(value = "商品名称")
    private String goodsName;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCommbarcode() {
		return commbarcode;
	}

	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getEffectiveMonth() {
		return effectiveMonth;
	}

	public void setEffectiveMonth(String effectiveMonth) {
		this.effectiveMonth = effectiveMonth;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
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

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	
}
