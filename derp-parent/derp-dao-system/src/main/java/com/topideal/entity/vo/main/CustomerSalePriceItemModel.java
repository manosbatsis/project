package com.topideal.entity.vo.main;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 客户销售价格表体
 * @author lian_
 *
 */
public class CustomerSalePriceItemModel extends PageModel implements Serializable{

     //商品货号
     private String goodsNo;
     //品牌名称
     private String brandName;
     //单位名称
     private String unitName;
     //修改时间
     private Timestamp modifyDate;
     //销售价格
     private BigDecimal salePrice;
     //商品id
     private Long goodsId;
     //商品规格
     private String spec;
     //产品类别id
     private Long productTypeId;
     //商家名称
     private String merchantName;
     //商家id
     private Long merchantId;
     //品牌id
     private Long brandId;
     //单位id
     private Long unitId;
     //产品类别名称
     private String goodsClassifyName;
     //id
     private Long id;
     //条形码
     private String barcode;
     //商品名称
     private String goodsName;
     //创建时间
     private Timestamp createDate;
     //客户销售价格表头id
     private Long salePriceId;
     //是否组合
     private String isGroup;
     
     //拓展字段
     //客户名称
     private String customerName;
     //价格失效时间
     private Timestamp expiryDate;
     //客户ID
     private Long customerId;
     //价格生效时间
     private Timestamp effectiveDate;
     //1立即生效 0不立即生效
     private String immediateEffect;

    public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Timestamp getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Timestamp expiryDate) {
		this.expiryDate = expiryDate;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Timestamp getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Timestamp effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getImmediateEffect() {
		return immediateEffect;
	}
	public void setImmediateEffect(String immediateEffect) {
		this.immediateEffect = immediateEffect;
	}
	public String getIsGroup() {
		return isGroup;
	}
	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
	}
	public Long getSalePriceId() {
		return salePriceId;
	}
	public void setSalePriceId(Long salePriceId) {
		this.salePriceId = salePriceId;
	}
	/*goodsNo get 方法 */
    public String getGoodsNo(){
        return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
        this.goodsNo=goodsNo;
    }
    /*brandName get 方法 */
    public String getBrandName(){
        return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
        this.brandName=brandName;
    }
    /*unitName get 方法 */
    public String getUnitName(){
        return unitName;
    }
    /*unitName set 方法 */
    public void setUnitName(String  unitName){
        this.unitName=unitName;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
        this.modifyDate=modifyDate;
    }
    /*salePrice get 方法 */
    public BigDecimal getSalePrice(){
        return salePrice;
    }
    /*salePrice set 方法 */
    public void setSalePrice(BigDecimal  salePrice){
        this.salePrice=salePrice;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
        return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
        this.goodsId=goodsId;
    }
    /*spec get 方法 */
    public String getSpec(){
        return spec;
    }
    /*spec set 方法 */
    public void setSpec(String  spec){
        this.spec=spec;
    }
    /*productTypeId get 方法 */
    public Long getProductTypeId(){
        return productTypeId;
    }
    /*productTypeId set 方法 */
    public void setProductTypeId(Long  productTypeId){
        this.productTypeId=productTypeId;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName=merchantName;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*brandId get 方法 */
    public Long getBrandId(){
        return brandId;
    }
    /*brandId set 方法 */
    public void setBrandId(Long  brandId){
        this.brandId=brandId;
    }
    /*unitId get 方法 */
    public Long getUnitId(){
        return unitId;
    }
    /*unitId set 方法 */
    public void setUnitId(Long  unitId){
        this.unitId=unitId;
    }
    /*productTypeName get 方法 */
    public String getGoodsClassifyName(){
        return goodsClassifyName;
    }
    /*productTypeName set 方法 */
    public void setGoodsClassifyName(String  goodsClassifyName){
        this.goodsClassifyName=goodsClassifyName;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*barcode get 方法 */
    public String getBarcode(){
        return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
        this.barcode=barcode;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
        return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
        this.goodsName=goodsName;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }






}

