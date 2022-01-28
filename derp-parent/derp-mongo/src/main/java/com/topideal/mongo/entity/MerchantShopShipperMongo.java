package com.topideal.mongo.entity;

public class MerchantShopShipperMongo {

    /**
     * 店铺货主id
     */
    private Long shipperId;
    /**
    * 店铺id
    */
    private Long shopId;
    /**
    * 公司id
    */
    private Long merchantId;
    /**
    * 公司名称
    */
    private String merchantName;
    /**
    * 事业部id
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 销售类型 01-自营 02-代发
    */
    private String saleType;
    /**
    * 修改时间
    */
    private String createDate;
    /**
    * 修改时间
    */
    private String modifyDate;
    /**
     * 事业部库位类型ID
     */
    private Long stockLocationTypeId;

    /**
     * 事业部库位类型名称
     */
    private String stockLocationTypeName;

    /*shopId get 方法 */
    public Long getShopId(){
    return shopId;
    }
    /*shopId set 方法 */
    public void setShopId(Long  shopId){
    this.shopId=shopId;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
    return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
    this.merchantId=merchantId;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
    return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
    this.merchantName=merchantName;
    }
    /*buId get 方法 */
    public Long getBuId(){
    return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    this.buId=buId;
    }
    /*buName get 方法 */
    public String getBuName(){
    return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
    this.buName=buName;
    }
    /*saleType get 方法 */
    public String getSaleType(){
    return saleType;
    }
    /*saleType set 方法 */
    public void setSaleType(String  saleType){
    this.saleType=saleType;
    }

	public Long getShipperId() {
		return shipperId;
	}
	public void setShipperId(Long shipperId) {
		this.shipperId = shipperId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

    public Long getStockLocationTypeId() {
        return stockLocationTypeId;
    }

    public void setStockLocationTypeId(Long stockLocationTypeId) {
        this.stockLocationTypeId = stockLocationTypeId;
    }

    public String getStockLocationTypeName() {
        return stockLocationTypeName;
    }

    public void setStockLocationTypeName(String stockLocationTypeName) {
        this.stockLocationTypeName = stockLocationTypeName;
    }
}
