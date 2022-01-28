package com.topideal.entity.dto.main;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 供应商询价池
 * @author lianchenxing
 *
 */
public class SupplierInquiryPoolDTO extends PageModel implements Serializable{

     //商品分类名称
     private String merchandiseCatName;
     //品牌名称
     private String brandName;
     //供货价
     private BigDecimal supplyPrice;
     //修改日期
     private Timestamp modifyDate;
     //商品ID
     private Long goodsId;
     //修改人
     private Long modifier;
     //供应商编码
     private String customerCode;
     //供应商名称
     private String customerName;
     //规格型号
     private String spec;
     //计量单位
     private String unit;
     //原产国ID
     private Long countryId;
     //商品分类ID
     private Long merchandiseCatId;
     //品牌ID
     private Long brandId;
     //供应商ID
     private Long customerId;
     //创建人
     private Long creater;
     //最大起订数
     private Integer maximum;
     //id
     private Long id;
     //商品名称
     private String goodsName;
     //最小起订数
     private Integer minimum;
     //原产国名称
     private String countryName;
     //创建时间
     private Timestamp createDate;
     //单位ID
     private Long unitId;
     //商家ID
     private Long merchantId;
     

     //拓展字段
     //结算方式
     private String settlementMode;
     //
     private Long productTypeId;
     
     /*merchantId get 方法 */
     public Long getMerchantId(){
         return merchantId;
     }
     /*merchantId set 方法 */
     public void setMerchantId(Long  merchantId){
         this.merchantId=merchantId;
     }
     public Long getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}
	/*unitId get 方法 */
     public Long getUnitId(){
         return unitId;
     }
     /*unitId set 方法 */
     public void setUnitId(Long  unitId){
         this.unitId=unitId;
     }
    public String getSettlementMode() {
		return settlementMode;
	}
	public void setSettlementMode(String settlementMode) {
		this.settlementMode = settlementMode;
	}
	/*merchandiseCatName get 方法 */
    public String getMerchandiseCatName(){
        return merchandiseCatName;
    }
    /*merchandiseCatName set 方法 */
    public void setMerchandiseCatName(String  merchandiseCatName){
        this.merchandiseCatName=merchandiseCatName;
    }
    /*brandName get 方法 */
    public String getBrandName(){
        return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
        this.brandName=brandName;
    }
    /*supplyPrice get 方法 */
    public BigDecimal getSupplyPrice(){
        return supplyPrice;
    }
    /*supplyPrice set 方法 */
    public void setSupplyPrice(BigDecimal  supplyPrice){
        this.supplyPrice=supplyPrice;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
        this.modifyDate=modifyDate;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
        return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
        this.goodsId=goodsId;
    }
    /*modifier get 方法 */
    public Long getModifier(){
        return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
        this.modifier=modifier;
    }
    /*customerCode get 方法 */
    public String getCustomerCode(){
        return customerCode;
    }
    /*customerCode set 方法 */
    public void setCustomerCode(String  customerCode){
        this.customerCode=customerCode;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
        return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
        this.customerName=customerName;
    }
    /*spec get 方法 */
    public String getSpec(){
        return spec;
    }
    /*spec set 方法 */
    public void setSpec(String  spec){
        this.spec=spec;
    }
    /*unit get 方法 */
    public String getUnit(){
        return unit;
    }
    /*unit set 方法 */
    public void setUnit(String  unit){
        this.unit=unit;
    }
    /*countyId get 方法 */
    public Long getCountryId(){
        return countryId;
    }
    /*countyId set 方法 */
    public void setCountryId(Long  countryId){
        this.countryId=countryId;
    }
    /*merchandiseCatId get 方法 */
    public Long getMerchandiseCatId(){
        return merchandiseCatId;
    }
    /*merchandiseCatId set 方法 */
    public void setMerchandiseCatId(Long  merchandiseCatId){
        this.merchandiseCatId=merchandiseCatId;
    }
    /*brandId get 方法 */
    public Long getBrandId(){
        return brandId;
    }
    /*brandId set 方法 */
    public void setBrandId(Long  brandId){
        this.brandId=brandId;
    }
    /*customerId get 方法 */
    public Long getCustomerId(){
        return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
        this.customerId=customerId;
    }
    /*creater get 方法 */
    public Long getCreater(){
        return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
        this.creater=creater;
    }
    /*maximum get 方法 */
    public Integer getMaximum(){
        return maximum;
    }
    /*maximum set 方法 */
    public void setMaximum(Integer  maximum){
        this.maximum=maximum;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
        return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
        this.goodsName=goodsName;
    }
    /*minimum get 方法 */
    public Integer getMinimum(){
        return minimum;
    }
    /*minimum set 方法 */
    public void setMinimum(Integer  minimum){
        this.minimum=minimum;
    }
    /*countyName get 方法 */
    public String getCountryName(){
        return countryName;
    }
    /*countyName set 方法 */
    public void setCountryName(String  countryName){
        this.countryName=countryName;
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

