package com.topideal.entity.vo.reporting;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class SalePurchasedailyDayModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 商家id
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 客户id
    */
    private Long customerId;
    /**
    * 客户名称
    */
    private String customerName;
    /**
    * 品牌id
    */
    private Long brandId;
    /**
    * 品牌名称
    */
    private String brandName;
    /**
    * 分类id
    */
    private Long productTypeId;
    /**
    * 分类名称
    */
    private String productTypeName;
    /**
    * 当日销售额
    */
    private BigDecimal daySaleAmount;
    /**
    * 报表归属日期 “yyyy-MM-dd”
    */
    private String reportDate;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 当日销售量
    */
    private Long daySaleCount;
    
    //===========扩展字段==========
    private Long monSaleCount;//月销售量（用于分页查询）
    private BigDecimal monSaleAmount;//月销售额（用于列表的分页查询）
    private Long yearSaleCount;//年度销售量（用于分页查询）
    private BigDecimal yearSaleAmount;//年度销售额（用于分页查询）
    private Integer monAvgCount;//月日均销量（用于分页查询）
    
    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
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
    /*customerId get 方法 */
    public Long getCustomerId(){
    return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
    this.customerId=customerId;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
    return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
    this.customerName=customerName;
    }
    /*brandId get 方法 */
    public Long getBrandId(){
    return brandId;
    }
    /*brandId set 方法 */
    public void setBrandId(Long  brandId){
    this.brandId=brandId;
    }
    /*brandName get 方法 */
    public String getBrandName(){
    return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
    this.brandName=brandName;
    }
    /*productTypeId get 方法 */
    public Long getProductTypeId(){
    return productTypeId;
    }
    /*productTypeId set 方法 */
    public void setProductTypeId(Long  productTypeId){
    this.productTypeId=productTypeId;
    }
    /*productTypeName get 方法 */
    public String getProductTypeName(){
    return productTypeName;
    }
    /*productTypeName set 方法 */
    public void setProductTypeName(String  productTypeName){
    this.productTypeName=productTypeName;
    }
    /*daySaleAmount get 方法 */
    public BigDecimal getDaySaleAmount(){
    return daySaleAmount;
    }
    /*daySaleAmount set 方法 */
    public void setDaySaleAmount(BigDecimal  daySaleAmount){
    this.daySaleAmount=daySaleAmount;
    }
    /*reportDate get 方法 */
    public String getReportDate(){
    return reportDate;
    }
    /*reportDate set 方法 */
    public void setReportDate(String  reportDate){
    this.reportDate=reportDate;
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
    /*daySaleCount get 方法 */
    public Long getDaySaleCount(){
    return daySaleCount;
    }
    /*daySaleCount set 方法 */
    public void setDaySaleCount(Long  daySaleCount){
    this.daySaleCount=daySaleCount;
    }
    
	public Long getMonSaleCount() {
		return monSaleCount;
	}
	public void setMonSaleCount(Long monSaleCount) {
		this.monSaleCount = monSaleCount;
	}
	public BigDecimal getMonSaleAmount() {
		return monSaleAmount;
	}
	public void setMonSaleAmount(BigDecimal monSaleAmount) {
		this.monSaleAmount = monSaleAmount;
	}
	public Long getYearSaleCount() {
		return yearSaleCount;
	}
	public void setYearSaleCount(Long yearSaleCount) {
		this.yearSaleCount = yearSaleCount;
	}
	public BigDecimal getYearSaleAmount() {
		return yearSaleAmount;
	}
	public void setYearSaleAmount(BigDecimal yearSaleAmount) {
		this.yearSaleAmount = yearSaleAmount;
	}
	public Integer getMonAvgCount() {
		return monAvgCount;
	}
	public void setMonAvgCount(Integer monAvgCount) {
		this.monAvgCount = monAvgCount;
	}



}
