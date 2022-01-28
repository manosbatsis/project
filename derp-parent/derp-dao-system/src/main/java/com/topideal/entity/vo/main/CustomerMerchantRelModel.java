package com.topideal.entity.vo.main;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class CustomerMerchantRelModel extends PageModel implements Serializable{

    /**
    * 自增长ID
    */
    private Long id;
    /**
    * 经分销客户表id
    */
    private Long customerId;
    /**
    * 客户编码
    */
    private String code;
    /**
    * 客户名称
    */
    private String name;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    *  状态(1使用中,0已禁用)
    */
    private String status;
    
    /**
     * 结算类型 1:应收,2:预收
     */
    private String settlementType ;
    
    /**
     * 账期  0:预售货款,1:信用账期-7天,2:信用账期-15天,3:信用账期-30天,4:信用账期-40天,5:信用账期-45天,6:信用账期-50天,7:信用账期-60天,8:信用账期-90天,信用账期-90天以上
     */
    private String accountPeriod ;
    //是否启用采购价格管理 1-启用 0-不启用
    private String purchasePriceManage;
  //是否启用销售价格管理 1-启用 0-不启用
    private String salePriceManage;
    /**
     * 销售模式  1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结
     */
    private String businessModel;
    /**
     * 税率
     */
    private BigDecimal rate;
    /**
     * 税率id
     */
    private Long rateId;

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
    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
    }
    /*name get 方法 */
    public String getName(){
    return name;
    }
    /*name set 方法 */
    public void setName(String  name){
    this.name=name;
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
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    }
	public String getSettlementType() {
		return settlementType;
	}
	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}
	public String getAccountPeriod() {
		return accountPeriod;
	}
	public void setAccountPeriod(String accountPeriod) {
		this.accountPeriod = accountPeriod;
	}
	public String getPurchasePriceManage() {
		return purchasePriceManage;
	}
	public void setPurchasePriceManage(String purchasePriceManage) {
		this.purchasePriceManage = purchasePriceManage;
	}
	public String getSalePriceManage() {
		return salePriceManage;
	}
	public void setSalePriceManage(String salePriceManage) {
		this.salePriceManage = salePriceManage;
	}

    public String getBusinessModel() {
        return businessModel;
    }

    public void setBusinessModel(String businessModel) {
        this.businessModel = businessModel;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Long getRateId() {
        return rateId;
    }

    public void setRateId(Long rateId) {
        this.rateId = rateId;
    }
}
