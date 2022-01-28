package com.topideal.entity.dto;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 库存收发明细
 * @author lian_
 *
 */
@ApiModel
public class InventoryDetailsDTO extends PageModel implements Serializable{
	
     //批次号
     @ApiModelProperty(value = "批次号",required = false)
     private String batchNo;
     //数量
     @ApiModelProperty(value = "数量",required = false)
     private Integer num;
     //商品货号
     @ApiModelProperty(value = "商品货号",required = false)
     private String goodsNo;
     //仓库名称
     @ApiModelProperty(value = "仓库名称",required = false)
     private String depotName;
     //商品ID
     @ApiModelProperty(value = "库位货号id",required = false)
     private Long goodsId;
     //仓库ID
     @ApiModelProperty(value = "仓库ID",required = false)
     private Long depotId;
     //来源类型  1采购入库  2 销售入库
     @ApiModelProperty(value = "来源类型",required = false)
     private String sourceType;
     //商家名称
     @ApiModelProperty(value = "商家名称",required = false)
     private String merchantName;
     //来源单据号
     @ApiModelProperty(value = "来源单据号",required = false)
     private String source;
     //商家ID
     @ApiModelProperty(value = "商家ID",required = false)
     private Long merchantId;
     //商品名称
     @ApiModelProperty(value = "商品名称",required = false)
     private String goodsName;
     //id
     @ApiModelProperty(value = "id",required = false)
     private Long id;
     //创建时间
     @ApiModelProperty(value = "创建时间",required = false)
     private Timestamp createDate;
     //属性说明
     @ApiModelProperty(value = "属性说明",required = false)
     private String orderNo;
     //出入时间
     @ApiModelProperty(value = "出入时间",required = false)
     private Timestamp divergenceDate;
     //单据状态 名称
     @ApiModelProperty(value = "单据状态",required = false)
     private String thingStatus;
     //库存类型  1 正常品  2 残次品
     @ApiModelProperty(value = "库存类型",required = false)
     private String type;
     //单据日期
     @ApiModelProperty(value = "单据日期",required = false)
     private Timestamp sourceDate;
     //单位（ 0 托盘 1箱  2件）
     @ApiModelProperty(value = "单位",required = false)
     private String unit;
     //是否代销仓(1-是,0-否)
     @ApiModelProperty(value = "是否代销仓",required = false)
     private String isTopBooks;
     //失效日期
     @ApiModelProperty(value = "失效日期",required = false)
     private Date overdueDate;
     //生产日期
     @ApiModelProperty(value = "生产日期",required = false)
     private Date productionDate;
     //仓库类型(a-卓志保税仓，b-非卓志保税仓，c-卓志海外仓，d-在途仓,e非卓志国内仓）
     @ApiModelProperty(value = "仓库类型",required = false)
     private String depotType;
     //操作类型  0 调减 1调增
     @ApiModelProperty(value = "操作类型",required = false)
     private String operateType;
     //商家卓志编码
     @ApiModelProperty(value = "商家卓志编码",required = false)
     private String topidealCode;
     //仓库编码
     @ApiModelProperty(value = "仓库编码",required = false)
     private String depotCode;
     //开始时间
     @ApiModelProperty(value = "开始时间",required = false)
     private String startTime;
     //结算时间
     @ApiModelProperty(value = "结算时间",required = false)
     private String endTime;
     //结转月份
     @ApiModelProperty(value = "结转月份",required = false)
     private String ownMonth;
     //是否过期（0 未过期 1已过期）
     @ApiModelProperty(value = "是否过期",required = false)
     private String isExpire;
     //事物类型名称
     @ApiModelProperty(value = "事物类型名称",required = false)
     private String thingName;
     //业务单号
     @ApiModelProperty(value = "业务单号",required = false)
     private String businessNo;
     //条形码
     @ApiModelProperty(value = "条形码",required = false)
     private String barcode;
     //修改时间
     @ApiModelProperty(value = "修改时间",required = false)
     private Timestamp modifyDate;
     // 批次库存id
     @ApiModelProperty(value = "批次库存id",required = false)
     private Long inventoryBatchId;

    //标准条码
    @ApiModelProperty(value = "标准条码",required = false)
    private String commbarcode;

    //电商平台名称
    @ApiModelProperty(value = "电商平台名称",required = false)
    private String storePlatformName;
    /**
     * 店铺编码
     */
    @ApiModelProperty(value = "店铺编码",required = false)
    private String shopCode;
    
    /*数据库存响应转化字段*/
    private String sourceTypeLabel;
    private String sourceLabel;
    private String thingStatusLabel;
    private String typeLabel;
    private String unitLabel;
    private String depotTypeLabel;
    private String isExpireLabel;
    private String operateTypeLabel;

    /**
     * 事业部ID
     */
    @ApiModelProperty(value = "事业部ID",required = false)
    private Long buId;
    /**
     * 事业部名称
     */
    @ApiModelProperty(value = "事业部名称",required = false)
    private String buName;
    /**
     * 时间段 1-近12个月数据 2-12个月以前数据
     */
    @ApiModelProperty(value = "时间段 1-近12个月数据 2-12个月以前数据",required = false)
    private String orderTimeRange;


    @ApiModelProperty(value = "id, 多个以英文逗号隔开",required = false)
    private String ids;

    /**
     * 事业部库位类型ID
     */
    @ApiModelProperty(value = "事业部库位类型ID")
    private Long stockLocationTypeId;
    /**
     * 事业部库位类型名称
     */
    @ApiModelProperty(value = "事业部库位类型名称")
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

     /*depotType get 方法 */
     public String getDepotType(){
         return depotType;
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

     
     /*type get 方法 */
     public String getType(){
         return type;
     }

     /*thingStatus get 方法 */
     public String getThingStatus(){
         return thingStatus;
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
    
    
    public String getSourceTypeLabel() {
		return sourceTypeLabel;
	}
	public void setSourceTypeLabel(String sourceTypeLabel) {
		this.sourceTypeLabel = sourceTypeLabel;
	}
	public String getSourceLabel() {
		return sourceLabel;
	}
	public void setSourceLabel(String sourceLabel) {
		this.sourceLabel = sourceLabel;
	}
	public String getThingStatusLabel() {
		return thingStatusLabel;
	}
	public void setThingStatusLabel(String thingStatusLabel) {
		this.thingStatusLabel = thingStatusLabel;
	}
	public String getTypeLabel() {
		return typeLabel;
	}
	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}
	public String getUnitLabel() {
		return unitLabel;
	}
	public void setUnitLabel(String unitLabel) {
		this.unitLabel = unitLabel;
	}
	public String getDepotTypeLabel() {
		return depotTypeLabel;
	}
	public void setDepotTypeLabel(String depotTypeLabel) {
		this.depotTypeLabel = depotTypeLabel;
	}
	public String getIsExpireLabel() {
		return isExpireLabel;
	}
	public void setIsExpireLabel(String isExpireLabel) {
		this.isExpireLabel = isExpireLabel;
	}
	public String getOperateTypeLabel() {
		return operateTypeLabel;
	}
	public void setOperateTypeLabel(String operateTypeLabel) {
		this.operateTypeLabel = operateTypeLabel;
	}
	/*sourceType set 方法 */
    public void setSourceType(String  sourceType){
        this.sourceType=sourceType;
        this.sourceTypeLabel=DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.inventory_sourceTypeList, sourceType);
    }
    /*sourceCode set 方法 */
    public void setSource(String  source){
        this.source=source;
        this.sourceLabel=DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.inventory_sourceList, source);
    }
    /*thingStatus set 方法 */
    public void setThingStatus(String  thingStatus){
        this.thingStatus=thingStatus;
        this.thingStatusLabel=DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.inventory_thingStatusList, thingStatus);
    }
    /*type set 方法 */
    public void setType(String  type){
        this.type=type;
        this.typeLabel=DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.initInventory_typeList, type);
    }
    /*unit set 方法 */
    public void setUnit(String  unit){
        this.unit=unit;
        this.unitLabel=DERP.getLabelByKey(DERP.inventory_unitList, unit);
    }
    /*depotType set 方法 */
    public void setDepotType(String  depotType){
        this.depotType=depotType;
        this.depotTypeLabel=DERP_SYS.getLabelByKey(DERP_SYS.depotInfo_typeList, depotType);
    }
    /*isExpire set 方法 */
    public void setIsExpire(String  isExpire){
        this.isExpire=isExpire;
        this.isExpireLabel=(DERP.getLabelByKey(DERP.isExpireList, isExpire));
    }
    /*operateType set 方法 */
    public void setOperateType(String  operateType){
        this.operateType=operateType;
        this.operateTypeLabel=DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.inventory_operateTypeList, operateType);
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
	public String getOrderTimeRange() {
		return orderTimeRange;
	}
	public void setOrderTimeRange(String orderTimeRange) {
		this.orderTimeRange = orderTimeRange;
	}

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
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

