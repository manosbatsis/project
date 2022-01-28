package com.topideal.webapi.form;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModelProperty;

/**
 * 标准条码Form
 * @author gy
 *
 */
public class CommbarcodeForm extends PageForm implements Serializable{
	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	@ApiModelProperty(value = "标准条码")
    private String commbarcode;	
	@ApiModelProperty(value = "标准品牌Id")
    private Long commBrandParentId;
	@ApiModelProperty(value = "条形码")
    private String barcode;
	@ApiModelProperty(value = "商品名称")
    private String goodsName;
	@ApiModelProperty(value = "商品货号")
    private String goodsNo;   
    @ApiModelProperty(value = "维护状态 0-未维护 1-已维护")
    private String maintainStatus;
    
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
	public Long getCommBrandParentId() {
		return commBrandParentId;
	}
	public void setCommBrandParentId(Long commBrandParentId) {
		this.commBrandParentId = commBrandParentId;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getMaintainStatus() {
		return maintainStatus;
	}
	public void setMaintainStatus(String maintainStatus) {
		this.maintainStatus = maintainStatus;
	}
}
