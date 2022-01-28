package com.topideal.mongo.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 采购执行回购mongo 实体
 * @author gy
 *
 */
public class PurchaseCommissionMongo{

    /**
    * 自增长ID
    */
    private Long purchaseCommissionId;
    /**
    * 客户ID
    */
    private Long customerId;
    /**
    * 客户编码
    */
    private String customerCode;
    /**
    * 客户名
    */
    private String customerName;
    /**
    * 供应商ID
    */
    private Long supplierId;
    /**
    * 供应商编码
    */
    private String supplierCode;
    /**
    * 供应商名称
    */
    private String supplierName;
    /**
    * 销售回扣
    */
    private Double saleRebate;
    /**
    * 
    */
    private Double purchaseCommission;
    /**
    * 创建人
    */
    private Long creater;

    /**
    * 修改人
    */
    private Long modifier;


    /**
     * 商家ID
     */
    private Long merchantId;
    //配置类型  1-采购执行比例 2-公司加价比例
    private String configType;

    public Long getPurchaseCommissionId() {
		return purchaseCommissionId;
	}
	public void setPurchaseCommissionId(Long purchaseCommissionId) {
		this.purchaseCommissionId = purchaseCommissionId;
	}
	/*customerId get 方法 */
    public Long getCustomerId(){
    return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
    this.customerId=customerId;
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
    /*supplierId get 方法 */
    public Long getSupplierId(){
    return supplierId;
    }
    /*supplierId set 方法 */
    public void setSupplierId(Long  supplierId){
    this.supplierId=supplierId;
    }
    /*supplierCode get 方法 */
    public String getSupplierCode(){
    return supplierCode;
    }
    /*supplierCode set 方法 */
    public void setSupplierCode(String  supplierCode){
    this.supplierCode=supplierCode;
    }
    /*supplierName get 方法 */
    public String getSupplierName(){
    return supplierName;
    }
    /*supplierName set 方法 */
    public void setSupplierName(String  supplierName){
    this.supplierName=supplierName;
    }
    /*saleRebate get 方法 */
    public Double getSaleRebate(){
    return saleRebate;
    }
    /*saleRebate set 方法 */
    public void setSaleRebate(Double  saleRebate){
    this.saleRebate=saleRebate;
    }
    /*purchaseCommission get 方法 */
    public Double getPurchaseCommission(){
    return purchaseCommission;
    }
    /*purchaseCommission set 方法 */
    public void setPurchaseCommission(Double  purchaseCommission){
    this.purchaseCommission=purchaseCommission;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }

    /*modifier get 方法 */
    public Long getModifier(){
    return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
    this.modifier=modifier;
    }


    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }


    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }
}
