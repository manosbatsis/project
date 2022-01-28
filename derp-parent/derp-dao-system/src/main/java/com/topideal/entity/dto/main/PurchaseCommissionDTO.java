package com.topideal.entity.dto.main;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class PurchaseCommissionDTO extends PageModel implements Serializable{

    /**
    * 自增长ID
    */
    private Long id;
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
    private BigDecimal saleRebate;
    /**
    * 
    */
    private BigDecimal purchaseCommission;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改人
    */
    private Long modifier;
    /**
    * 修改日期
    */
    private Timestamp modifyDate;

    /**
     * 商家ID
     */
    private Long merchantId;

    //配置类型  1-采购执行比例 2-公司加价比例
    private String configType;
    private String configTypeLabel;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
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
    public BigDecimal getSaleRebate(){
    return saleRebate;
    }
    /*saleRebate set 方法 */
    public void setSaleRebate(BigDecimal  saleRebate){
    this.saleRebate=saleRebate;
    }
    /*purchaseCommission get 方法 */
    public BigDecimal getPurchaseCommission(){
    return purchaseCommission;
    }
    /*purchaseCommission set 方法 */
    public void setPurchaseCommission(BigDecimal  purchaseCommission){
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
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*modifier get 方法 */
    public Long getModifier(){
    return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
    this.modifier=modifier;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
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
        if(StringUtils.isNotBlank(configType)){
            this.configTypeLabel = DERP_ORDER.getLabelByKey(DERP_SYS.purchaseCommission_configTypeList, configType);
        }
    }

    public String getConfigTypeLabel() {
        return configTypeLabel;
    }

    public void setConfigTypeLabel(String configTypeLabel) {
        this.configTypeLabel = configTypeLabel;
    }
}
