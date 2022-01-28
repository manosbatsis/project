package com.topideal.entity.vo;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;

public class InventoryDetailsHistoryModel extends PageModel implements Serializable{

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
    * 仓库ID
    */
    private Long depotId;
    /**
    * 仓库名称
    */
    private String depotName;
    /**
    * 商品ID
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
    * 数量
    */
    private Integer num;
    /**
    * 来源单据号
    */
    private String source;
    /**
    * 单据类型
    */
    private String sourceType;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 出入时间
    */
    private Timestamp divergenceDate;
    /**
    * 订单编号
    */
    private String orderNo;
    /**
    * 单据类型名称
    */
    private String thingStatus;
    /**
    * 库存类型  0 正常品  1 残次品
    */
    private String type;
    /**
    * 是否代销仓(1-是,0-否)
    */
    private String isTopBooks;
    /**
    * 单位（ 0 托盘 1箱  2件）
    */
    private String unit;
    /**
    * 单据日期
    */
    private Timestamp sourceDate;
    /**
    * 生产日期
    */
    private Date productionDate;
    /**
    * 失效日期
    */
    private Date overdueDate;
    /**
    * 操作类型  0 调减 1调增
    */
    private String operateType;
    /**
    * 仓库类型(a-卓志保税仓，b-非卓志保税仓，c-卓志海外仓，d-在途仓,e非卓志国内仓）
    */
    private String depotType;
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
    * 是否过期（0 过期 1 未过期）
    */
    private String isExpire;
    /**
    * 事物类型名称
    */
    private String thingName;
    /**
    * 业务单号
    */
    private String businessNo;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 批次库存id
    */
    private Long inventoryBatchId;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 电商平台名称
    */
    private String storePlatformName;
    /**
    * 店铺编码
    */
    private String shopCode;
    /**
    * 事业部ID
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;

    /**
     * 事业部库位类型ID
     */
    private Long stockLocationTypeId;
    /**
     * 事业部库位类型名称
     */
    private String stockLocationTypeName;


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
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*source get 方法 */
    public String getSource(){
    return source;
    }
    /*source set 方法 */
    public void setSource(String  source){
    this.source=source;
    }
    /*sourceType get 方法 */
    public String getSourceType(){
    return sourceType;
    }
    /*sourceType set 方法 */
    public void setSourceType(String  sourceType){
    this.sourceType=sourceType;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*divergenceDate get 方法 */
    public Timestamp getDivergenceDate(){
    return divergenceDate;
    }
    /*divergenceDate set 方法 */
    public void setDivergenceDate(Timestamp  divergenceDate){
    this.divergenceDate=divergenceDate;
    }
    /*orderNo get 方法 */
    public String getOrderNo(){
    return orderNo;
    }
    /*orderNo set 方法 */
    public void setOrderNo(String  orderNo){
    this.orderNo=orderNo;
    }
    /*thingStatus get 方法 */
    public String getThingStatus(){
    return thingStatus;
    }
    /*thingStatus set 方法 */
    public void setThingStatus(String  thingStatus){
    this.thingStatus=thingStatus;
    }
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
    }
    /*isTopBooks get 方法 */
    public String getIsTopBooks(){
    return isTopBooks;
    }
    /*isTopBooks set 方法 */
    public void setIsTopBooks(String  isTopBooks){
    this.isTopBooks=isTopBooks;
    }
    /*unit get 方法 */
    public String getUnit(){
    return unit;
    }
    /*unit set 方法 */
    public void setUnit(String  unit){
    this.unit=unit;
    }
    /*sourceDate get 方法 */
    public Timestamp getSourceDate(){
    return sourceDate;
    }
    /*sourceDate set 方法 */
    public void setSourceDate(Timestamp  sourceDate){
    this.sourceDate=sourceDate;
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
    /*operateType get 方法 */
    public String getOperateType(){
    return operateType;
    }
    /*operateType set 方法 */
    public void setOperateType(String  operateType){
    this.operateType=operateType;
    }
    /*depotType get 方法 */
    public String getDepotType(){
    return depotType;
    }
    /*depotType set 方法 */
    public void setDepotType(String  depotType){
    this.depotType=depotType;
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
    /*isExpire get 方法 */
    public String getIsExpire(){
    return isExpire;
    }
    /*isExpire set 方法 */
    public void setIsExpire(String  isExpire){
    this.isExpire=isExpire;
    }
    /*thingName get 方法 */
    public String getThingName(){
    return thingName;
    }
    /*thingName set 方法 */
    public void setThingName(String  thingName){
    this.thingName=thingName;
    }
    /*businessNo get 方法 */
    public String getBusinessNo(){
    return businessNo;
    }
    /*businessNo set 方法 */
    public void setBusinessNo(String  businessNo){
    this.businessNo=businessNo;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
    /*inventoryBatchId get 方法 */
    public Long getInventoryBatchId(){
    return inventoryBatchId;
    }
    /*inventoryBatchId set 方法 */
    public void setInventoryBatchId(Long  inventoryBatchId){
    this.inventoryBatchId=inventoryBatchId;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*storePlatformName get 方法 */
    public String getStorePlatformName(){
    return storePlatformName;
    }
    /*storePlatformName set 方法 */
    public void setStorePlatformName(String  storePlatformName){
    this.storePlatformName=storePlatformName;
    }
    /*shopCode get 方法 */
    public String getShopCode(){
    return shopCode;
    }
    /*shopCode set 方法 */
    public void setShopCode(String  shopCode){
    this.shopCode=shopCode;
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
