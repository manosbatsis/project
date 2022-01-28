package com.topideal.entity.vo.inventory;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 期初库存
 * @author lian_
 *
 */
public class InitInventoryModel extends PageModel implements Serializable{

     //失效日期
     private Date overdueDate;
     //仓库名称
     private String depotName;
     //批次号
     private String batchNo;
     //商品id
     private Long goodsId;
     //仓库id
     private Long depotId;
     //冻结库存
     private Integer frozenNum;
     //剩余库存
     private Integer surplusNum;
     //可用库存量
     private Integer availableNum;
     //库存类型 1 好品  2 坏品
     private String type;
     //商家名称
     private String merchantName;
     //生产日期
     private Date productionDate;
     //锁定库存
     private Integer lockNum;
     //商家ID
     private Long merchantId;
     //库存数量
     private Integer initNum;
     //单位id
     private Long unitId;
     //id
     private Long id;
     //1、未确认  2、已确认
     private String state;
     //商品名称
     private String goodsName;
     //创建日期（录入日期）
     private Timestamp createDate;
     //创建人
     private Long creater;
     //库存状态  E0: 可用  E1:冻结
     private String inventoryStatus;
     //商品货号
     private String goodsNo;
     //数据来源：1 录入/导入  2 op
     private String source;
     //商品条码
     private String barcode;
     //商品编码
     private String goodsCode;
     //托盘号
     private String lpn;
     //理货单位
     private String unit;
     //仓库类型(a-卓志保税仓，b-非卓志保税仓，c-卓志海外仓，d-在途仓,e非卓志国内仓）
     private String depotType;
     //是否代销仓(1-是,0-否)
     private String isTopBooks;
     //商家卓志编码
     private String topidealCode;
     //仓库编码 （新增字段用于期初导入做展示） 2018-6-22
     private String depotCode;
     //修改时间
     private Timestamp modifyDate;
     
     //结算时间 (新增字段 2018-09-19)
     private String settlementTime;

     //标准条码
     private String commbarcode;
     //事业部ID
     private Long buId;
     //事业部名称
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
}

