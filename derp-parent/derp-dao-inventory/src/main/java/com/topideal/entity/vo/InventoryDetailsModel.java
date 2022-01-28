package com.topideal.entity.vo;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 库存收发明细
 * @author lian_
 *
 */
public class InventoryDetailsModel extends PageModel implements Serializable{
	
     //批次号
     private String batchNo;
     //数量
     private Integer num;
     //商品货号
     private String goodsNo;
     //仓库名称
     private String depotName;
     //商品ID
     private Long goodsId;
     //仓库ID
     private Long depotId;
     //来源类型  1采购入库  2 销售入库
     private String sourceType;
     //商家名称
     private String merchantName;
     //来源单据号
     private String source;
     //商家ID
     private Long merchantId;
     //商品名称
     private String goodsName;
     //id
     private Long id;
     //创建时间
     private Timestamp createDate;
     //属性说明
     private String orderNo;
     //出入时间
     private Timestamp divergenceDate;
     //单据状态 名称
     private String thingStatus;
     //库存类型  1 正常品  2 残次品
     private String type;
     //单据日期
     private Timestamp sourceDate;
     //单位（ 0 托盘 1箱  2件）
     private String unit;
     //是否代销仓(1-是,0-否)
     private String isTopBooks;
     //失效日期  
     private Date overdueDate;
     //生产日期
     private Date productionDate;
     //仓库类型(a-卓志保税仓，b-非卓志保税仓，c-卓志海外仓，d-在途仓,e非卓志国内仓）
     private String depotType;
     //操作类型  0 调减 1调增
     private String operateType;
     //商家卓志编码
     private String topidealCode;
     //仓库编码
     private String depotCode;
     //开始时间
     private String startTime;
     //结算时间
     private String endTime;
     //结转月份
     private String ownMonth;
     //是否过期（0 未过期 1已过期）
     private String isExpire;
     //事物类型名称
     private String thingName;
     //业务单号
     private String businessNo;
     //条形码
     private String barcode;
     //修改时间
     private Timestamp modifyDate;
     // 批次库存id
     private Long inventoryBatchId;

    //标准条码
    private String commbarcode;

    //电商平台名称
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

     public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	/*barcode get 方法 */
     public String getBarcode(){
         return barcode;
     }
     /*barcode set 方法 */
     public void setBarcode(String  barcode){
         this.barcode=barcode;
     }
     public String getBusinessNo() {
		return businessNo;
	}
	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}
	/*thingName get 方法 */
     public String getThingName(){
         return thingName;
     }
     /*thingName set 方法 */
     public void setThingName(String  thingName){
         this.thingName=thingName;
     }
     /*isExpire get 方法 */
     public String getIsExpire(){
         return isExpire;
     }
     /*isExpire set 方法 */
     public void setIsExpire(String  isExpire){
         this.isExpire=isExpire;
     }
     /*ownMonth get 方法 */
     public String getOwnMonth(){
         return ownMonth;
     }
     /*ownMonth set 方法 */
     public void setOwnMonth(String  ownMonth){
         this.ownMonth=ownMonth;
     }
     /*depotCode get 方法 */
     public String getDepotCode(){
         return depotCode;
     }
     /*depotCode set 方法 */
     public void setDepotCode(String  depotCode){
         this.depotCode=depotCode;
     }
     /*topidealCode get 方法 */
     public String getTopidealCode(){
         return topidealCode;
     }
     /*topidealCode set 方法 */
     public void setTopidealCode(String  topidealCode){
         this.topidealCode=topidealCode;
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
     
     /*isTopBooks get 方法 */
     public String getIsTopBooks(){
         return isTopBooks;
     }
     /*isTopBooks set 方法 */
     public void setIsTopBooks(String  isTopBooks){
         this.isTopBooks=isTopBooks;
     }
     /*sourceDate get 方法 */
     public Timestamp getSourceDate(){
         return sourceDate;
     }
     /*sourceDate set 方法 */
     public void setSourceDate(Timestamp  sourceDate){
         this.sourceDate=sourceDate;
     }
     /*unit get 方法 */
     public String getUnit(){
         return unit;
     }
     /*unit set 方法 */
     public void setUnit(String  unit){
         this.unit=unit;
     }
     
     /*type get 方法 */
     public String getType(){
         return type;
     }
     /*type set 方法 */
     public void setType(String  type){
         this.type=type;
     }
     /*thingStatus get 方法 */
     public String getThingStatus(){
         return thingStatus;
     }
     /*thingStatus set 方法 */
     public void setThingStatus(String  thingStatus){
         this.thingStatus=thingStatus;
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

    public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	/*depotName get 方法 */
    public String getDepotName(){
        return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
        this.depotName=depotName;
    }
    /*batchNo get 方法 */
    public String getBatchNo(){
        return batchNo;
    }
    /*batchNo set 方法 */
    public void setBatchNo(String  batchNo){
        this.batchNo=batchNo;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
        return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
        this.goodsId=goodsId;
    }
    /*num get 方法 */
    public Integer getNum(){
        return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
        this.num=num;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
        return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
        this.depotId=depotId;
    }
    /*sourceType get 方法 */
    public String getSourceType(){
        return sourceType;
    }
    /*sourceType set 方法 */
    public void setSourceType(String  sourceType){
        this.sourceType=sourceType;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName=merchantName;
    }
    /*sourceCode get 方法 */
    public String getSource(){
        return source;
    }
    /*sourceCode set 方法 */
    public void setSource(String  source){
        this.source=source;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
        return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
        this.goodsName=goodsName;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Long getInventoryBatchId() {
		return inventoryBatchId;
	}
	public void setInventoryBatchId(Long inventoryBatchId) {
		this.inventoryBatchId = inventoryBatchId;
	}


    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    public String getStorePlatformName() {
        return storePlatformName;
    }

    public void setStorePlatformName(String storePlatformName) {
        this.storePlatformName = storePlatformName;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
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

