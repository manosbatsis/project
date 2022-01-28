package com.topideal.entity.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * 期初库存DTO
 * @author lian_
 *
 */
public class InitInventoryDTO extends PageModel implements Serializable{

     //失效日期
	 @ApiModelProperty(value = "失效日期")
     private Date overdueDate;
     //仓库名称
	 @ApiModelProperty(value = "仓库名称")
     private String depotName;
     //批次号
	 @ApiModelProperty(value = "批次号")
     private String batchNo;
     //商品id
	 @ApiModelProperty(value = "商品id")
     private Long goodsId;
     //仓库id
	 @ApiModelProperty(value = "仓库id")
     private Long depotId;
     //冻结库存
	 @ApiModelProperty(value = "冻结库存")
     private Integer frozenNum;
     //剩余库存
	 @ApiModelProperty(value = "剩余库存")
     private Integer surplusNum;
     //可用库存量
	 @ApiModelProperty(value = "可用库存量")
     private Integer availableNum;
     //库存类型 0 好品  1 坏品
	 @ApiModelProperty(value = "库存类型 0 好品  1 坏品")
     private String type;
     //商家名称
	 @ApiModelProperty(value = "商家名称")
     private String merchantName;
     //生产日期
	 @ApiModelProperty(value = "生产日期")
     private Date productionDate;
     //锁定库存
	 @ApiModelProperty(value = "锁定库存")
     private Integer lockNum;
     //商家ID
	 @ApiModelProperty(value = "商家ID")
     private Long merchantId;
     //库存数量
	 @ApiModelProperty(value = "库存数量")
     private Integer initNum;
     //单位id
	 @ApiModelProperty(value = "单位id")
     private Long unitId;
     //id
	 @ApiModelProperty(value = "id")
     private Long id;
     //1、未确认  2、已确认
	 @ApiModelProperty(value = "1、未确认  2、已确认")
     private String state;
     //商品名称
	 @ApiModelProperty(value = "商品名称")
     private String goodsName;
     //创建日期（录入日期）
	 @ApiModelProperty(value = "创建日期（录入日期）")
     private Timestamp createDate;
     //创建人
	 @ApiModelProperty(value = "创建人")
     private Long creater;
     //库存状态  E0: 可用  E1:冻结
	 @ApiModelProperty(value = "库存状态  E0: 可用  E1:冻结")
     private String inventoryStatus;
     //商品货号
	 @ApiModelProperty(value = "每页数量")
     private String goodsNo;
     //数据来源：1 录入/导入  2 op
	 @ApiModelProperty(value = "数据来源：1 录入/导入  2 op")
     private String source;
     //商品条码
	 @ApiModelProperty(value = "商品条码")
     private String barcode;
     //商品编码
	 @ApiModelProperty(value = "商品编码")
     private String goodsCode;
     //托盘号
	 @ApiModelProperty(value = "托盘号")
     private String lpn;
     //理货单位
	 @ApiModelProperty(value = "理货单位")
     private String unit;
     //仓库类型(a-卓志保税仓，b-非卓志保税仓，c-卓志海外仓，d-在途仓,e非卓志国内仓）
	 @ApiModelProperty(value = "仓库类型")
     private String depotType;
     //是否代销仓(1-是,0-否)
	 @ApiModelProperty(value = "是否代销仓(1-是,0-否)")
     private String isTopBooks;
     //商家卓志编码
	 @ApiModelProperty(value = "商家卓志编码")
     private String topidealCode;
     //仓库编码 （新增字段用于期初导入做展示） 2018-6-22
	 @ApiModelProperty(value = "仓库编码 ")
     private String depotCode;
     
     //结算时间 (新增字段 2018-09-19)
	 @ApiModelProperty(value = "结算时间")
     private String settlementTime;
     //修改时间
	 @ApiModelProperty(value = "修改时间")
     private Timestamp modifyDate;

    //标准条码
	 @ApiModelProperty(value = "标准条码")
    private String commbarcode;
    //事业部ID
	 @ApiModelProperty(value = "事业部ID")
    private Long buId;
    //事业部名称
	 @ApiModelProperty(value = "事业部名称")
    private String buName;
    
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
	/*数据库存响应转化字段*/
    //库存类型 0 好品  1 坏品   Label
    private String typeLabel;
    //理货单位  Label
    private String unitLabel;
    //1、未确认  2、已确认 Label
    private String stateLabel;
    

     
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
     
     /*topidealCode get 方法 */
     public String getTopidealCode(){
         return topidealCode;
     }
     /*topidealCode set 方法 */
     public void setTopidealCode(String  topidealCode){
         this.topidealCode=topidealCode;
     }
     /*isTopBooks get 方法 */
     public String getIsTopBooks(){
         return isTopBooks;
     }
     /*isTopBooks set 方法 */
     public void setIsTopBooks(String  isTopBooks){
         this.isTopBooks=isTopBooks;
     }
     /*depotType get 方法 */
     public String getDepotType(){
         return depotType;
     }
     /*depotType set 方法 */
     public void setDepotType(String  depotType){
         this.depotType=depotType;
     }
     /*unit get 方法 */
     public String getUnit(){
         return unit;
     }
     
     /*lpn get 方法 */
     public String getLpn(){
         return lpn;
     }
     /*lpn set 方法 */
     public void setLpn(String  lpn){
         this.lpn=lpn;
     }
     /*inventoryStatus get 方法 */
     public String getInventoryStatus(){
         return inventoryStatus;
     }
     /*inventoryStatus set 方法 */
     public void setInventoryStatus(String  inventoryStatus){
         this.inventoryStatus=inventoryStatus;
     }
     /*goodsCode get 方法 */
     public String getGoodsCode(){
         return goodsCode;
     }
     /*goodsCode set 方法 */
     public void setGoodsCode(String  goodsCode){
         this.goodsCode=goodsCode;
     }
     /*barcode get 方法 */
     public String getBarcode(){
         return barcode;
     }
     /*barcode set 方法 */
     public void setBarcode(String  barcode){
         this.barcode=barcode;
     }
     /*source get 方法 */
     public String getSource(){
         return source;
     }
     /*source set 方法 */
     public void setSource(String  source){
         this.source=source;
     }
     /*goodsNo get 方法 */
     public String getGoodsNo(){
         return goodsNo;
     }
     /*goodsNo set 方法 */
     public void setGoodsNo(String  goodsNo){
         this.goodsNo=goodsNo;
     }
     /*creater get 方法 */
     public Long getCreater(){
         return creater;
     }
     /*creater set 方法 */
     public void setCreater(Long  creater){
         this.creater=creater;
     }
    /*overdueDate get 方法 */
    public Date getOverdueDate(){
        return overdueDate;
    }
    /*overdueDate set 方法 */
    public void setOverdueDate(Date  overdueDate){
        this.overdueDate=overdueDate;
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
    /*depotId get 方法 */
    public Long getDepotId(){
        return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
        this.depotId=depotId;
    }
    /*frozenNum get 方法 */
    public Integer getFrozenNum(){
        return frozenNum;
    }
    /*frozenNum set 方法 */
    public void setFrozenNum(Integer  frozenNum){
        this.frozenNum=frozenNum;
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

    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName=merchantName;
    }
    /*productionDate get 方法 */
    public Date getProductionDate(){
        return productionDate;
    }
    /*productionDate set 方法 */
    public void setProductionDate(Date  productionDate){
        this.productionDate=productionDate;
    }
    /*lockNum get 方法 */
    public Integer getLockNum(){
        return lockNum;
    }
    /*lockNum set 方法 */
    public void setLockNum(Integer  lockNum){
        this.lockNum=lockNum;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*initNum get 方法 */
    public Integer getInitNum(){
        return initNum;
    }
    /*initNum set 方法 */
    public void setInitNum(Integer  initNum){
        this.initNum=initNum;
    }
    /*unitId get 方法 */
    public Long getUnitId(){
        return unitId;
    }
    /*unitId set 方法 */
    public void setUnitId(Long  unitId){
        this.unitId=unitId;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*state get 方法 */
    public String getState(){
        return state;
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
	public String getDepotCode() {
		return depotCode;
	}
	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}
	public String getSettlementTime() {
		return settlementTime;
	}
	public void setSettlementTime(String settlementTime) {
		this.settlementTime = settlementTime;
	}


    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
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
	public String getStateLabel() {
		return stateLabel;
	}
	public void setStateLabel(String stateLabel) {
		this.stateLabel = stateLabel;
	}
	/*unit set 方法 */
    public void setUnit(String  unit){
        this.unit=unit;
        this.unitLabel=DERP.getLabelByKey(DERP.inventory_unitList, unit);
    }
    /*state set 方法 */
    public void setState(String  state){
        this.state=state;
        this.stateLabel=DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.initInventory_stateList, state);
    }
    /*type set 方法 */
    public void setType(String  type){
        this.type=type;
        this.typeLabel=DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.initInventory_typeList, type);
    }
}

