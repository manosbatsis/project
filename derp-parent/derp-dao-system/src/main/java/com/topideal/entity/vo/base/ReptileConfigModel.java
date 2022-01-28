package com.topideal.entity.vo.base;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 爬虫配置表
 * @author lian_
 */
public class ReptileConfigModel extends PageModel implements Serializable{

     //商家ID
     private Long merchantId;
     //登陆用户名
     private String loginName;
     //爬虫账单时点
     private Integer timePoint;
     //id
     private Long id;
     //使用平台
     private String platformName;
     //创建时间
     private Timestamp createDate;
     //客户ID
     private Long customerId;
     //是否代理 0商家 1 代理商家
     private String isProxy;
     //代理商家Id
     private Long proxyId;
     
     //拓展字段
     //商家名称
     private String merchantName;
     //客户名称
     private String customerName;
     //平台类型 ：1-云集  2-唯品账单
     private String platformType;

    /**
     * 出仓库id
     */
    private Long outDepotId;
    /**
     * 出仓库名称
     */
    private String outDepotName;
    /**
     * 入仓库id
     */
    private Long inDepotId;
    /**
     * 入仓库名称
     */
    private String inDepotName;
    /**
     * 店铺ID
     */
    private Long shopId;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 店铺编码
     */
    private String shopCode;

     /*proxyId get 方法 */
     public Long getProxyId(){
         return proxyId;
     }
     /*proxyId set 方法 */
     public void setProxyId(Long  proxyId){
         this.proxyId=proxyId;
     }
     /*isProxy get 方法 */
     public String getIsProxy(){
         return isProxy;
     }
     /*isProxy set 方法 */
     public void setIsProxy(String  isProxy){
         this.isProxy=isProxy;
     }
    public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*loginName get 方法 */
    public String getLoginName(){
        return loginName;
    }
    /*loginName set 方法 */
    public void setLoginName(String  loginName){
        this.loginName=loginName;
    }
    /*timePoint get 方法 */
    public Integer getTimePoint(){
        return timePoint;
    }
    /*timePoint set 方法 */
    public void setTimePoint(Integer  timePoint){
        this.timePoint=timePoint;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*platformName get 方法 */
    public String getPlatformName(){
        return platformName;
    }
    /*platformName set 方法 */
    public void setPlatformName(String  platformName){
        this.platformName=platformName;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public Long getOutDepotId() {
        return outDepotId;
    }

    public void setOutDepotId(Long outDepotId) {
        this.outDepotId = outDepotId;
    }

    public String getOutDepotName() {
        return outDepotName;
    }

    public void setOutDepotName(String outDepotName) {
        this.outDepotName = outDepotName;
    }

    public Long getInDepotId() {
        return inDepotId;
    }

    public void setInDepotId(Long inDepotId) {
        this.inDepotId = inDepotId;
    }

    public String getInDepotName() {
        return inDepotName;
    }

    public void setInDepotName(String inDepotName) {
        this.inDepotName = inDepotName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }
}

