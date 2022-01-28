package com.topideal.order.webapi.purchase.form;

import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 采购链路step 3 返回form
 * @author Guobs
 *
 */
@ApiModel
public class PurchaseLinkInfoStepThreeForm {

	@ApiModelProperty(value="采购链路ID")
	private Long id ;
	
	@ApiModelProperty(value="预览订单集合")
	private Map<String, Object> map ;
	
	@ApiModelProperty(value="校验库存商品ID，多个以','隔开")
	private String goodsIds ;
	
	@ApiModelProperty(value="校验库存商品货号，多个以','隔开")
	private String goodsNos ;
	
	@ApiModelProperty(value="校验库存商品数量，多个以','隔开")
	private String goodsNums ;
	
	@ApiModelProperty(value="出仓仓库ID")
	private Long outDepotId ;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public String getGoodsIds() {
		return goodsIds;
	}

	public void setGoodsIds(String goodsIds) {
		this.goodsIds = goodsIds;
	}

	public String getGoodsNos() {
		return goodsNos;
	}

	public void setGoodsNos(String goodsNos) {
		this.goodsNos = goodsNos;
	}

	public String getGoodsNums() {
		return goodsNums;
	}

	public void setGoodsNums(String goodsNums) {
		this.goodsNums = goodsNums;
	}

	public Long getOutDepotId() {
		return outDepotId;
	}

	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}
	
	
}
