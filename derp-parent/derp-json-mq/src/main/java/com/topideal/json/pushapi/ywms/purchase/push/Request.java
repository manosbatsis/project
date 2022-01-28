package com.topideal.json.pushapi.ywms.purchase.push;

/**
 * 	采购指令下推众邦云仓json--请求信息封装
 * 	XML 根节点 request
 * @author Guobs
 *
 */

public class Request {
	private EntryOrder entryOrder;

    private OrderLines orderLines;

	public EntryOrder getEntryOrder() {
		return entryOrder;
	}

	public void setEntryOrder(EntryOrder entryOrder) {
		this.entryOrder = entryOrder;
	}

	public OrderLines getOrderLines() {
		return orderLines;
	}

	public void setOrderLines(OrderLines orderLines) {
		this.orderLines = orderLines;
	}

    
    
}
