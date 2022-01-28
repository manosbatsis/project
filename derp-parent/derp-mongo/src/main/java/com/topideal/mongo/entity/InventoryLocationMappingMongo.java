package com.topideal.mongo.entity;

public class InventoryLocationMappingMongo {

    /**
    * 自增ID
    */
    private Long inventoryLocationMappingId;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名
    */
    private String merchantName;
    /**
    * 商品ID
    */
    private Long goodsId;
    /**
    * 库位货号
    */
    private String goodsNo;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 原货号
    */
    private String originalGoodsNo;
    
    private Long originalGoodsId;
    /**
    * 库位类型：1、常规品 2、赠送品 3、sample
    */
    private String type;
    /**
    * 创建人ID
    */
    private Long creator;
    /**
    * 创建人
    */
    private String createName;
    /**
    * 修改人ID
    */
    private Long modifier;
    /**
    * 修改人
    */
    private String modifyName;

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
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*originalGoodsNo get 方法 */
    public String getOriginalGoodsNo(){
    return originalGoodsNo;
    }
    /*originalGoodsNo set 方法 */
    public void setOriginalGoodsNo(String  originalGoodsNo){
    this.originalGoodsNo=originalGoodsNo;
    }
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
    }
    /*creator get 方法 */
    public Long getCreator(){
    return creator;
    }
    /*creator set 方法 */
    public void setCreator(Long  creator){
    this.creator=creator;
    }
    /*createName get 方法 */
    public String getCreateName(){
    return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
    this.createName=createName;
    }
    /*modifier get 方法 */
    public Long getModifier(){
    return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
    this.modifier=modifier;
    }
    /*modifyName get 方法 */
    public String getModifyName(){
    return modifyName;
    }
    /*modifyName set 方法 */
    public void setModifyName(String  modifyName){
    this.modifyName=modifyName;
    }
	public Long getInventoryLocationMappingId() {
		return inventoryLocationMappingId;
	}
	public void setInventoryLocationMappingId(Long inventoryLocationMappingId) {
		this.inventoryLocationMappingId = inventoryLocationMappingId;
	}
	public Long getOriginalGoodsId() {
		return originalGoodsId;
	}
	public void setOriginalGoodsId(Long originalGoodsId) {
		this.originalGoodsId = originalGoodsId;
	}






}
