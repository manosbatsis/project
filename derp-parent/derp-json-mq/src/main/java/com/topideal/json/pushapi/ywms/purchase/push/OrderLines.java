package com.topideal.json.pushapi.ywms.purchase.push;

import java.util.List;

/**
 * 采购指令下推众邦云仓json--商品信息
 * @author Guobs
 *
 */
public class OrderLines {
	List<OrderLine> orderLine ;

	public List<OrderLine> getOrderLine() {
		return orderLine;
	}

	public void setOrderLine(List<OrderLine> orderLine) {
		this.orderLine = orderLine;
	}
	
	
}
