package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

public class MerchandiseContrastModel extends PageModel implements Serializable{

    /**
    * 主键ID
    */
    private Long id;
    /**
    * 平台
    */
    private String platformName;
    /**
    * 平台用户名
    */
    private String platformUsername;
    /**
    * 爬虫商品货号
    */
    private String crawlerGoodsNo;
    /**
    * 爬虫商品名称
    */
    private String crawlerGoodsName;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 0-启用 1-禁用
    */
    private String status;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 创建人用户名
    */
    private String createUsername;
    /**
    * 修改人用户名
    */
    private String updateUsername;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 事业部id
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 类型 1-云集 2-唯品
    */
    private String type;

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
    /*platformUsername get 方法 */
    public String getPlatformUsername(){
    return platformUsername;
    }
    /*platformUsername set 方法 */
    public void setPlatformUsername(String  platformUsername){
    this.platformUsername=platformUsername;
    }
    /*crawlerGoodsNo get 方法 */
    public String getCrawlerGoodsNo(){
    return crawlerGoodsNo;
    }
    /*crawlerGoodsNo set 方法 */
    public void setCrawlerGoodsNo(String  crawlerGoodsNo){
    this.crawlerGoodsNo=crawlerGoodsNo;
    }
    /*crawlerGoodsName get 方法 */
    public String getCrawlerGoodsName(){
    return crawlerGoodsName;
    }
    /*crawlerGoodsName set 方法 */
    public void setCrawlerGoodsName(String  crawlerGoodsName){
    this.crawlerGoodsName=crawlerGoodsName;
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
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*createUsername get 方法 */
    public String getCreateUsername(){
    return createUsername;
    }
    /*createUsername set 方法 */
    public void setCreateUsername(String  createUsername){
    this.createUsername=createUsername;
    }
    /*updateUsername get 方法 */
    public String getUpdateUsername(){
    return updateUsername;
    }
    /*updateUsername set 方法 */
    public void setUpdateUsername(String  updateUsername){
    this.updateUsername=updateUsername;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
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
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
    }




}
