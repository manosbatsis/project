package com.topideal.json.pushapi.ywms.sale.push;

public class Request {
	private DeliveryOrder deliveryOrder;

    private OrderLines orderLines;

	public OrderLines getOrderLines() {
		return orderLines;
	}

	public void setOrderLines(OrderLines orderLines) {
		this.orderLines = orderLines;
	}

	public DeliveryOrder getDeliveryOrder() {
		return deliveryOrder;
	}

	public void setDeliveryOrder(DeliveryOrder deliveryOrder) {
		this.deliveryOrder = deliveryOrder;
	}

    
    
}
