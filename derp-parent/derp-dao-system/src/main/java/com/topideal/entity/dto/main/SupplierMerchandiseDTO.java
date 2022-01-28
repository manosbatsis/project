package com.topideal.entity.dto.main;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;

public class SupplierMerchandiseDTO extends PageModel implements Serializable{

    /**
    * 自增长ID
    */
    private Long id;
    /**
    * 商品编码
    */
    private String goodsCode;
    /**
    * 商品名称
    */
    private String name;
    /**
    * 商品名称
    */
    private String brandName;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 产品类型
    */
    private String goodsType;
    /**
    * 建议零售价
    */
    private BigDecimal salePrice;
    /**
    * 经销商货号
    */
    private String supplierGoodsNo;
    /**
    * 事业部id
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 来源:1.欧莱雅接口
    */
    private String source;
    /**
     * 品牌编码
     */
    private String brandCode;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    
    /**
     * 来源:1.欧莱雅接口
     */
     private String sourceLabel;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*goodsCode get 方法 */
    public String getGoodsCode(){
    return goodsCode;
    }
    /*goodsCode set 方法 */
    public void setGoodsCode(String  goodsCode){
    this.goodsCode=goodsCode;
    }
    /*name get 方法 */
    public String getName(){
    return name;
    }
    /*name set 方法 */
    public void setName(String  name){
    this.name=name;
    }
    /*brandName get 方法 */
    public String getBrandName(){
    return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
    this.brandName=brandName;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*goodsType get 方法 */
    public String getGoodsType(){
    return goodsType;
    }
    /*goodsType set 方法 */
    public void setGoodsType(String  goodsType){
    this.goodsType=goodsType;
    }
    /*salePrice get 方法 */
    public BigDecimal getSalePrice(){
    return salePrice;
    }
    /*salePrice set 方法 */
    public void setSalePrice(BigDecimal  salePrice){
    this.salePrice=salePrice;
    }
    /*supplierGoodsNo get 方法 */
    public String getSupplierGoodsNo(){
    return supplierGoodsNo;
    }
    /*supplierGoodsNo set 方法 */
    public void setSupplierGoodsNo(String  supplierGoodsNo){
    this.supplierGoodsNo=supplierGoodsNo;
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
    /*source get 方法 */
    public String getSource(){
    return source;
    }
    /*source set 方法 */
    public void setSource(String  source){
    if(StringUtils.isNotBlank(source) ) {
    	this.sourceLabel = DERP_SYS.getLabelByKey(DERP_SYS.supplierMerchandise_sourceList, source);
    }	
    this.source=source;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
	public String getSourceLabel() {
		return sourceLabel;
	}
	public void setSourceLabel(String sourceLabel) {
		this.sourceLabel = sourceLabel;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}






}
