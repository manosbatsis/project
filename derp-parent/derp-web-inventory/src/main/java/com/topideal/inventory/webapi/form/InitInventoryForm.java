package com.topideal.inventory.webapi.form;

import com.topideal.common.system.ibatis.PageModel;
import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 期初库存列表请求form
 *
 * @author lian_
 */
@ApiModel
public class InitInventoryForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "令牌", required = true)
    private String token;
    @ApiModelProperty(value = "仓库id")
    private Long depotId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品货号")
    private String goodsNo;
    @ApiModelProperty(value = "商品条码")
    private String barcode;
    @ApiModelProperty(value = "库存类型 0 好品  1 坏品")
    private String type;
    @ApiModelProperty(value = "1 录入/导入 ,2 OP, 3 OFC")
    private String source;
    @ApiModelProperty(value = "1、未确认  2、已确认")
    private String state;
    @ApiModelProperty(value = "事业部id")
    private Long buId;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getDepotId() {
		return depotId;
	}
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
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
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Long getBuId() {
		return buId;
	}
	public void setBuId(Long buId) {
		this.buId = buId;
	}


    

  
}

