package com.topideal.json.pushapi.ywms.inventory.pushback;

public class LocationInfo {
	private String locationName;

    private String quantity;

    private String lockQuantity;

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getLockQuantity() {
		return lockQuantity;
	}

	public void setLockQuantity(String lockQuantity) {
		this.lockQuantity = lockQuantity;
	}
    
}
