package com.topideal.json.pushapi.ywms.inventory.pushback;

public class Item {
	private String warehouseCode;

    private String itemCode;

    private String itemId;

    private String inventoryType;

    private String quantity;

    private String lockQuantity;

    private String batchCode;

    private String productDate;

    private String expireDate;

    private String produceCode;
    
    private LocationInfos locationInfos;

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getInventoryType() {
		return inventoryType;
	}

	public void setInventoryType(String inventoryType) {
		this.inventoryType = inventoryType;
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

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getProductDate() {
		return productDate;
	}

	public void setProductDate(String productDate) {
		this.productDate = productDate;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getProduceCode() {
		return produceCode;
	}

	public void setProduceCode(String produceCode) {
		this.produceCode = produceCode;
	}

	public LocationInfos getLocationInfos() {
		return locationInfos;
	}

	public void setLocationInfos(LocationInfos locationInfos) {
		this.locationInfos = locationInfos;
	}

    
}
