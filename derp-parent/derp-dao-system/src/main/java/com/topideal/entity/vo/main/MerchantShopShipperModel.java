package com.topideal.entity.vo.main;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

public class MerchantShopShipperModel extends PageModel implements Serializable{

    /**
    * 主键id
    */
    private Long id;
    
    /**
    * 店铺id
    */
    private Long shopId;
    /**
    * 公司id
    */
    private Long merchantId;
    /**
    * 公司名称
    */
    private String merchantName;
    /**
    * 事业部id
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 修改时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    /**
     * 事业部库位类型ID
     */
    private Long stockLocationTypeId;

    private String stockLocationTypeName;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
	/*shopId get 方法 */
    public Long getShopId(){
    return shopId;
    }
    /*shopId set 方法 */
    public void setShopId(Long  shopId){
    this.shopId=shopId;
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

    public Long getStockLocationTypeId() {
        return stockLocationTypeId;
    }

    public void setStockLocationTypeId(Long stockLocationTypeId) {
        this.stockLocationTypeId = stockLocationTypeId;
    }

    public String getStockLocationTypeName() {
        return stockLocationTypeName;
    }

    public void setStockLocationTypeName(String stockLocationTypeName) {
        this.stockLocationTypeName = stockLocationTypeName;
    }
}
