package com.topideal.entity.vo.order;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class PojzTempModel extends PageModel implements Serializable{

    /**
    * id主键
    */
    private Long id;
    /**
    * 数据类型 1-po结转余量
    */
    private String datatype;
    /**
    * 商家id
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 仓库ID
    */
    private Long depotId;
    /**
    * 仓库名称
    */
    private String depotName;
    /**
    * 销售单号
    */
    private String saleCode;
    /**
    * po号
    */
    private String poNo;
    /**
    * 爬虫sku
    */
    private String sku;
    /**
    * 货号
    */
    private String goodsNo;
    /**
    * 结转余量
    */
    private Integer surplusNum;
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
    /*datatype get 方法 */
    public String getDatatype(){
    return datatype;
    }
    /*datatype set 方法 */
    public void setDatatype(String  datatype){
    this.datatype=datatype;
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
    /*depotId get 方法 */
    public Long getDepotId(){
    return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
    this.depotId=depotId;
    }
    /*depotName get 方法 */
    public String getDepotName(){
    return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
    this.depotName=depotName;
    }
    /*saleCode get 方法 */
    public String getSaleCode(){
    return saleCode;
    }
    /*saleCode set 方法 */
    public void setSaleCode(String  saleCode){
    this.saleCode=saleCode;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*sku get 方法 */
    public String getSku(){
    return sku;
    }
    /*sku set 方法 */
    public void setSku(String  sku){
    this.sku=sku;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*surplusNum get 方法 */
    public Integer getSurplusNum(){
    return surplusNum;
    }
    /*surplusNum set 方法 */
    public void setSurplusNum(Integer  surplusNum){
    this.surplusNum=surplusNum;
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
