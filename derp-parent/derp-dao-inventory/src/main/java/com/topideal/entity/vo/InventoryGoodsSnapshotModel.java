package com.topideal.entity.vo;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

/**
 * 库存商品快照表
 * @author lian_
 *
 */
public class InventoryGoodsSnapshotModel extends PageModel implements Serializable{

     //商品货号
     private String goodsNo;
     //仓库名称
     private String depotName;
     //商品id
     private Long goodsId;
     //仓库id
     private Long depotId;
     //库存余量
     private Integer surplusNum;
     //可用量
     private Integer availableNum;
     //托盘号
     private String lpn;

     //商家名称
     private String merchantName;
     //理货单位 0 托盘 1箱  2 件
     private String unit;
     //商家ID
     private Long merchantId;
     //创建人
     private Long creater;
     //id
     private Long id;

     //商品名称
     private String goodsName;
     //创建时间
     private Timestamp createDate;
     //快照日期
     private String snapshotDate;
     /**
      * 品牌id
      */
      private Long brandId;
  	    /**
      * 品牌名称
      */
      private String brandName;

     //仓库类型
     private String depotType;
     //商家卓志编码
     private String topidealCode;
     //结转月份
     private String ownMonth;
     //是否代销仓(1-是,0-否)
     private String isTopBooks;
     //条形码
     private String barcode;
     //冻结库存量(默认0)
     private Integer freezeNum;
     //仓库编码
     private String depotCode;

     //好品数量
     private Integer expireNum;

     //坏品数量
     private Integer badNum;

    public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getDepotType() {
		return depotType;
	}
	public void setDepotType(String depotType) {
		this.depotType = depotType;
	}
	public String getTopidealCode() {
		return topidealCode;
	}
	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}
	public String getOwnMonth() {
		return ownMonth;
	}
	public void setOwnMonth(String ownMonth) {
		this.ownMonth = ownMonth;
	}
	public String getIsTopBooks() {
		return isTopBooks;
	}
	public void setIsTopBooks(String isTopBooks) {
		this.isTopBooks = isTopBooks;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Integer getFreezeNum() {
		return freezeNum;
	}
	public void setFreezeNum(Integer freezeNum) {
		this.freezeNum = freezeNum;
	}
	public String getDepotCode() {
		return depotCode;
	}
	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}
    /*goodsNo get 方法 */
    public String getGoodsNo(){
        return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
        this.goodsNo=goodsNo;
    }
    /*depotName get 方法 */
    public String getDepotName(){
        return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
        this.depotName=depotName;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
        return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
        this.goodsId=goodsId;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
        return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
        this.depotId=depotId;
    }
    /*surplusNum get 方法 */
    public Integer getSurplusNum(){
        return surplusNum;
    }
    /*surplusNum set 方法 */
    public void setSurplusNum(Integer  surplusNum){
        this.surplusNum=surplusNum;
    }
    /*availableNum get 方法 */
    public Integer getAvailableNum(){
        return availableNum;
    }
    /*availableNum set 方法 */
    public void setAvailableNum(Integer  availableNum){
        this.availableNum=availableNum;
    }
    /*lpn get 方法 */
    public String getLpn(){
        return lpn;
    }
    /*lpn set 方法 */
    public void setLpn(String  lpn){
        this.lpn=lpn;
    }

    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName=merchantName;
    }
    /*unit get 方法 */
    public String getUnit(){
        return unit;
    }
    /*unit set 方法 */
    public void setUnit(String  unit){
        this.unit=unit;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*creater get 方法 */
    public Long getCreater(){
        return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
        this.creater=creater;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
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
	public String getSnapshotDate() {
		return snapshotDate;
	}
	public void setSnapshotDate(String snapshotDate) {
		this.snapshotDate = snapshotDate;
	}

    public Integer getExpireNum() {
        return expireNum;
    }

    public void setExpireNum(Integer expireNum) {
        this.expireNum = expireNum;
    }

    public Integer getBadNum() {
        return badNum;
    }

    public void setBadNum(Integer badNum) {
        this.badNum = badNum;
    }
}

