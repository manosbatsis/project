package com.topideal.order.webapi.sale.form;

import java.math.BigDecimal;
import java.util.List;

import com.topideal.entity.dto.sale.SaleFinancingOrderItemDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SaleFinancingOrderForm {

    @ApiModelProperty(value = "销售单id")
    private Long orderId;
   
	@ApiModelProperty(value = "订单金额")
    private BigDecimal saleAmount;
   
	@ApiModelProperty(value = "实收保证金")
    private BigDecimal actualMarginAmount;
   
	@ApiModelProperty(value = "起算时间")
    private String applyDateStr;
  
	@ApiModelProperty(value = "融资商品表体集合")
	private List<SaleFinancingOrderItemDTO> itemList;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public BigDecimal getActualMarginAmount() {
		return actualMarginAmount;
	}

	public void setActualMarginAmount(BigDecimal actualMarginAmount) {
		this.actualMarginAmount = actualMarginAmount;
	}

	public String getApplyDateStr() {
		return applyDateStr;
	}

	public void setApplyDateStr(String applyDateStr) {
		this.applyDateStr = applyDateStr;
	}

	public List<SaleFinancingOrderItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<SaleFinancingOrderItemDTO> itemList) {
		this.itemList = itemList;
	}
	
}
