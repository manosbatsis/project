package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 
 * @author lian_
 */
public class CrawlerInventoryModel extends PageModel implements Serializable{

     //商家编码
     private String merchantCode;
     //仓库名称
     private String depotName;
     //库存日期
     private Timestamp inventoryDate;
     //仓库id
     private Long depotId;
     //用户编码
     private String userCode;
     //商家名称
     private String merchantName;
     //库存数量
     private Integer inventoryNum;
     //商家id
     private Long merchantId;
     //仓库编码
     private String depotCode;
     //id
     private Long id;
     //商品sku
     private String sku;
     //商品名称
     private String goodsName;
     //创建日期
     private Timestamp createDate;
     // 修改日期
     private Timestamp modifyDate;
     // 云集爬虫数据表对应id
     private Long oldId;
     /**
      * 云集仓库名称
      */
      private String yunjiDepotName;
     //代理商家id
      private Long proxyId;
      /**
       * 客户id
       */
       private Long customerId;
       /**
       * 客户名称
       */
       private String customerName;
     
     

    public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Long getProxyId() {
		return proxyId;
	}
	public void setProxyId(Long proxyId) {
		this.proxyId = proxyId;
	}
	public String getYunjiDepotName() {
		return yunjiDepotName;
	}
	public void setYunjiDepotName(String yunjiDepotName) {
		this.yunjiDepotName = yunjiDepotName;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Long getOldId() {
		return oldId;
	}
	public void setOldId(Long oldId) {
		this.oldId = oldId;
	}
	/*merchantCode get 方法 */
    public String getMerchantCode(){
        return merchantCode;
    }
    /*merchantCode set 方法 */
    public void setMerchantCode(String  merchantCode){
        this.merchantCode=merchantCode;
    }
    /*depotName get 方法 */
    public String getDepotName(){
        return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
        this.depotName=depotName;
    }
    /*inventoryDate get 方法 */
    public Timestamp getInventoryDate(){
        return inventoryDate;
    }
    /*inventoryDate set 方法 */
    public void setInventoryDate(Timestamp  inventoryDate){
        this.inventoryDate=inventoryDate;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
        return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
        this.depotId=depotId;
    }
    /*userCode get 方法 */
    public String getUserCode(){
        return userCode;
    }
    /*userCode set 方法 */
    public void setUserCode(String  userCode){
        this.userCode=userCode;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName=merchantName;
    }
    /*inventoryNum get 方法 */
    public Integer getInventoryNum(){
        return inventoryNum;
    }
    /*inventoryNum set 方法 */
    public void setInventoryNum(Integer  inventoryNum){
        this.inventoryNum=inventoryNum;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*depotCode get 方法 */
    public String getDepotCode(){
        return depotCode;
    }
    /*depotCode set 方法 */
    public void setDepotCode(String  depotCode){
        this.depotCode=depotCode;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*sku get 方法 */
    public String getSku(){
        return sku;
    }
    /*sku set 方法 */
    public void setSku(String  sku){
        this.sku=sku;
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

