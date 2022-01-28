package com.topideal.mongo.entity;

public class MerchandiseCustomsRelMongo {

    /**
    * 自增主键
    */
    private Long merchandiseCustomsRelId;
    /**
    * 商家id
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 商品id
    */
    private Long goodsId;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 关区ID
    */
    private Long customsAreaId;
    /**
    * 关区代码
    */
    private String customsAreaCode;
    /**
    * 关区名称
    */
    private String customsAreaName;

    
    public Long getMerchandiseCustomsRelId() {
		return merchandiseCustomsRelId;
	}
	public void setMerchandiseCustomsRelId(Long merchandiseCustomsRelId) {
		this.merchandiseCustomsRelId = merchandiseCustomsRelId;
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
    /*customsAreaId get 方法 */
    public Long getCustomsAreaId(){
    return customsAreaId;
    }
    /*customsAreaId set 方法 */
    public void setCustomsAreaId(Long  customsAreaId){
    this.customsAreaId=customsAreaId;
    }
    /*customsAreaCode get 方法 */
    public String getCustomsAreaCode(){
    return customsAreaCode;
    }
    /*customsAreaCode set 方法 */
    public void setCustomsAreaCode(String  customsAreaCode){
    this.customsAreaCode=customsAreaCode;
    }
    /*customsAreaName get 方法 */
    public String getCustomsAreaName(){
    return customsAreaName;
    }
    /*customsAreaName set 方法 */
    public void setCustomsAreaName(String  customsAreaName){
    this.customsAreaName=customsAreaName;
    }
}
