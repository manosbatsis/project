package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class JdStocksaleInfoModel extends PageModel implements Serializable{

    /**
    * 主键id
    */
    private Long id;
    /**
    * 商品编码
    */
    private String wareId;
    /**
    * 商品名称
    */
    private String name;
    /**
    * 仓库
    */
    private String warehouse;
    /**
    * 销量
    */
    private Integer volume;
    /**
    * 库存
    */
    private Integer stock;
    /**
    * 可订购
    */
    private Integer orderNum;
    /**
    * 归属日期
    */
    private Date businessDate;
    /**
    * 平台
    */
    private String platform;
    /**
    * 账号
    */
    private String userCode;
    /**
    * 商家id
    */
    private Integer oldId;
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
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*wareId get 方法 */
    public String getWareId(){
    return wareId;
    }
    /*wareId set 方法 */
    public void setWareId(String  wareId){
    this.wareId=wareId;
    }
    /*name get 方法 */
    public String getName(){
    return name;
    }
    /*name set 方法 */
    public void setName(String  name){
    this.name=name;
    }
    /*warehouse get 方法 */
    public String getWarehouse(){
    return warehouse;
    }
    /*warehouse set 方法 */
    public void setWarehouse(String  warehouse){
    this.warehouse=warehouse;
    }
    /*volume get 方法 */
    public Integer getVolume(){
    return volume;
    }
    /*volume set 方法 */
    public void setVolume(Integer  volume){
    this.volume=volume;
    }
    /*stock get 方法 */
    public Integer getStock(){
    return stock;
    }
    /*stock set 方法 */
    public void setStock(Integer  stock){
    this.stock=stock;
    }
    /*orderNum get 方法 */
    public Integer getOrderNum(){
    return orderNum;
    }
    /*orderNum set 方法 */
    public void setOrderNum(Integer  orderNum){
    this.orderNum=orderNum;
    }
    /*businessDate get 方法 */
    public Date getBusinessDate(){
    return businessDate;
    }
    /*businessDate set 方法 */
    public void setBusinessDate(Date  businessDate){
    this.businessDate=businessDate;
    }
    /*platform get 方法 */
    public String getPlatform(){
    return platform;
    }
    /*platform set 方法 */
    public void setPlatform(String  platform){
    this.platform=platform;
    }
    /*userCode get 方法 */
    public String getUserCode(){
    return userCode;
    }
    /*userCode set 方法 */
    public void setUserCode(String  userCode){
    this.userCode=userCode;
    }
    /*oldId get 方法 */
    public Integer getOldId(){
    return oldId;
    }
    /*oldId set 方法 */
    public void setOldId(Integer  oldId){
    this.oldId=oldId;
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






}
