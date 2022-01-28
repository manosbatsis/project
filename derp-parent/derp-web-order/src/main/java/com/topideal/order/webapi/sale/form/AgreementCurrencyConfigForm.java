package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 协议单价Form
 * @date 2021-02-05
 */
@ApiModel
public class AgreementCurrencyConfigForm extends PageForm{
	
	@ApiModelProperty(value = "令牌",required = true)
	private String token;
    @ApiModelProperty(value = "移入事业部id")
    private String inBuId;
    @ApiModelProperty(value = "移出事业部id")
    private String outBuId;
    @ApiModelProperty(value = "商品货号")
    private String goodsNo;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "主键集合ids")
    private String ids;
    
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
}
