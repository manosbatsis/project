package com.topideal.inventory.webapi.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 库存信息表
 * 
 * @author lianchenxing
 *
 */
@ApiModel
public class InventoryRealTimeForm extends PageForm implements Serializable {

	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	// 商家名称
	@ApiModelProperty(value = "商家名称")
	private String merchantName;

	// 商品货号
	@ApiModelProperty(value = "商品货号")
	private String goodsNo;

	@ApiModelProperty(value = "仓库id")
	private Long depotId;

	@ApiModelProperty(value = "条形码")
     private String barcode;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public Long getDepotId() {
		return depotId;
	}

	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
}
