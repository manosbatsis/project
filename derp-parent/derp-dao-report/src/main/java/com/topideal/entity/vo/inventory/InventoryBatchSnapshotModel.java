package com.topideal.entity.vo.inventory;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;

public class InventoryBatchSnapshotModel extends PageModel implements Serializable{

    /**
    * id
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
    * 仓库id
    */
    private Long depotId;
    /**
    * 仓库名称
    */
    private String depotName;
    /**
    * 商品id
    */
    private Long goodsId;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 批次号
    */
    private String batchNo;
    /**
    * 生产日期
    */
    private Date productionDate;
    /**
    * 失效日期
    */
    private Date overdueDate;
    /**
    * 库存余量
    */
    private Integer surplusNum;
    /**
    * 仓库库存量
    */
    private Integer availableNum;
    /**
    * 库存类型  0 正常品  1 残次品
    */
    private String type;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * 理货单位 0 托盘 1箱  2 件
    */
    private String unit;
    /**
    * 托盘号
    */
    private String lpn;
    /**
    * 是否过期（0 过期 1未过期）
    */
    private String isExpire;
    /**
    * 商家卓志编码
    */
    private String topidealCode;
    /**
    * 仓库编码
    */
    private String depotCode;
    /**
    * 结转月份
    */
    private String ownMonth;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 冻结库存量(默认0)
    */
    private Integer freezeNum;
    /**
    * 仓库类型
    */
    private String depotType;
    /**
    * 是否代销仓(1-是,0-否)
    */
    private String isTopBooks;
    /**
    * 品牌id
    */
    private Long brandId;
    /**
    * 品牌名称
    */
    private String brandName;
    /**
    * 标准条码
    */
    private String commbarcode;

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
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*batchNo get 方法 */
    public String getBatchNo(){
    return batchNo;
    }
    /*batchNo set 方法 */
    public void setBatchNo(String  batchNo){
    this.batchNo=batchNo;
    }
    /*productionDate get 方法 */
    public Date getProductionDate(){
    return productionDate;
    }
    /*productionDate set 方法 */
    public void setProductionDate(Date  productionDate){
    this.productionDate=productionDate;
    }
    /*overdueDate get 方法 */
    public Date getOverdueDate(){
    return overdueDate;
    }
    /*overdueDate set 方法 */
    public void setOverdueDate(Date  overdueDate){
    this.overdueDate=overdueDate;
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
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
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
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*unit get 方法 */
    public String getUnit(){
    return unit;
    }
    /*unit set 方法 */
    public void setUnit(String  unit){
    this.unit=unit;
    }
    /*lpn get 方法 */
    public String getLpn(){
    return lpn;
    }
    /*lpn set 方法 */
    public void setLpn(String  lpn){
    this.lpn=lpn;
    }
    /*isExpire get 方法 */
    public String getIsExpire(){
    return isExpire;
    }
    /*isExpire set 方法 */
    public void setIsExpire(String  isExpire){
    this.isExpire=isExpire;
    }
    /*topidealCode get 方法 */
    public String getTopidealCode(){
    return topidealCode;
    }
    /*topidealCode set 方法 */
    public void setTopidealCode(String  topidealCode){
    this.topidealCode=topidealCode;
    }
    /*depotCode get 方法 */
    public String getDepotCode(){
    return depotCode;
    }
    /*depotCode set 方法 */
    public void setDepotCode(String  depotCode){
    this.depotCode=depotCode;
    }
    /*ownMonth get 方法 */
    public String getOwnMonth(){
    return ownMonth;
    }
    /*ownMonth set 方法 */
    public void setOwnMonth(String  ownMonth){
    this.ownMonth=ownMonth;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*freezeNum get 方法 */
    public Integer getFreezeNum(){
    return freezeNum;
    }
    /*freezeNum set 方法 */
    public void setFreezeNum(Integer  freezeNum){
    this.freezeNum=freezeNum;
    }
    /*depotType get 方法 */
    public String getDepotType(){
    return depotType;
    }
    /*depotType set 方法 */
    public void setDepotType(String  depotType){
    this.depotType=depotType;
    }
    /*isTopBooks get 方法 */
    public String getIsTopBooks(){
    return isTopBooks;
    }
    /*isTopBooks set 方法 */
    public void setIsTopBooks(String  isTopBooks){
    this.isTopBooks=isTopBooks;
    }
    /*brandId get 方法 */
    public Long getBrandId(){
    return brandId;
    }
    /*brandId set 方法 */
    public void setBrandId(Long  brandId){
    this.brandId=brandId;
    }
    /*brandName get 方法 */
    public String getBrandName(){
    return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
    this.brandName=brandName;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }






}
