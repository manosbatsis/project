package com.topideal.json.pushapi.ywms.sale.pushback;

public class Request {
	private DeliveryOrder deliveryOrder;

    private Packages packages;

    private OrderLines orderLines;

	public DeliveryOrder getDeliveryOrder() {
		return deliveryOrder;
	}

	public void setDeliveryOrder(DeliveryOrder deliveryOrder) {
		this.deliveryOrder = deliveryOrder;
	}

	public Packages getPackages() {
		return packages;
	}

	public void setPackages(Packages packages) {
		this.packages = packages;
	}

	public OrderLines getOrderLines() {
		return orderLines;
	}

	public void setOrderLines(OrderLines orderLines) {
		this.orderLines = orderLines;
	}
    
    
}
