package com.topideal.entity.vo;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class IndexOrderStatisticsModel extends PageModel implements Serializable{

    /**
    * 主键
    */
    private Long id;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 名称
    */
    private String name;
    /**
    * 销售总量
    */
    private Integer saleNum;
    /**
    * 店铺类型值编码
    */
    private String shopTypeCode;
    /**
    * 店铺类型名称
    */
    private String shopTypeName;
    /**
    * 归属月份
    */
    private String month;
    /**
    * 统计维度 1-店铺销售总量 2-品牌销售总量
    */
    private String statisticalDimension;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    /**
     * 编码
     */
    private String code;

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
    /*name get 方法 */
    public String getName(){
    return name;
    }
    /*name set 方法 */
    public void setName(String  name){
    this.name=name;
    }
    /*saleNum get 方法 */
    public Integer getSaleNum(){
    return saleNum;
    }
    /*saleNum set 方法 */
    public void setSaleNum(Integer  saleNum){
    this.saleNum=saleNum;
    }
    /*shopTypeCode get 方法 */
    public String getShopTypeCode(){
    return shopTypeCode;
    }
    /*shopTypeCode set 方法 */
    public void setShopTypeCode(String  shopTypeCode){
    this.shopTypeCode=shopTypeCode;
    }
    /*shopTypeName get 方法 */
    public String getShopTypeName(){
    return shopTypeName;
    }
    /*shopTypeName set 方法 */
    public void setShopTypeName(String  shopTypeName){
    this.shopTypeName=shopTypeName;
    }
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*statisticalDimension get 方法 */
    public String getStatisticalDimension(){
    return statisticalDimension;
    }
    /*statisticalDimension set 方法 */
    public void setStatisticalDimension(String  statisticalDimension){
    this.statisticalDimension=statisticalDimension;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
