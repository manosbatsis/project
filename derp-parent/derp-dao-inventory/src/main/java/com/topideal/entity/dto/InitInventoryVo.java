package com.topideal.entity.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 期初库存
 * @author baols
 *
 */
public class InitInventoryVo  implements Serializable{

     //失效日期
	 @ApiModelProperty(value = "失效日期")
     private String overdueDate;
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
     //库存类型 1 好品  2 坏品
	 @ApiModelProperty(value = "库存类型 1 好品  2 坏品")
     private String type;
     //商家名称
	 @ApiModelProperty(value = "商家名称")
     private String merchantName;
     //生产日期
	 @ApiModelProperty(value = "生产日期")
     private String productionDate;
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
     private String createDate;
     //创建人
	 @ApiModelProperty(value = "创建")
     private Long creater;
     //库存状态  E0: 可用  E1:冻结
	 @ApiModelProperty(value = "库存状态  E0: 可用  E1:冻结")
     private String inventoryStatus;
     //商品货号
	 @ApiModelProperty(value = "商品货号")
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
	 @ApiModelProperty(value = "仓库类型(")
     private String depotType;
     //是否代销仓(1-是,0-否)
	 @ApiModelProperty(value = "是否代销仓(1-是,0-否)")
     private String isTopBooks;
     //商家卓志编码
	 @ApiModelProperty(value = "商家卓志编码")
     private String topidealCode;
     
     //仓库编码 （新增字段用于期初导入做展示） 2018-6-22
	 @ApiModelProperty(value = "仓库编码")
     private String depotCode;
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
    /*type set 方法 */
    public void setType(String  type){
        this.type=type;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName=merchantName;
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
    /*state set 方法 */
    public void setState(String  state){
        this.state=state;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
        return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
        this.goodsName=goodsName;
    }
    
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getDepotCode() {
		return depotCode;
	}
	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}
	public String getOverdueDate() {
		return overdueDate;
	}
	public void setOverdueDate(String overdueDate) {
		this.overdueDate = overdueDate;
	}
	public String getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}
	public String getLpn() {
		return lpn;
	}
	public void setLpn(String lpn) {
		this.lpn = lpn;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getDepotType() {
		return depotType;
	}
	public void setDepotType(String depotType) {
		this.depotType = depotType;
	}
	public String getIsTopBooks() {
		return isTopBooks;
	}
	public void setIsTopBooks(String isTopBooks) {
		this.isTopBooks = isTopBooks;
	}
	public String getTopidealCode() {
		return topidealCode;
	}
	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}
	public String getCommbarcode() {
		return commbarcode;
	}
	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}

	

   



}

