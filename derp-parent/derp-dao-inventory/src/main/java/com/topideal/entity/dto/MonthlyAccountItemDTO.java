package com.topideal.entity.dto;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.system.ibatis.PageModel;
/**
 * 月结账单商品明细
 * @author lian_
 *
 */
public class MonthlyAccountItemDTO extends PageModel implements Serializable{

     //商品货号
     private String goodsNo;
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
     //期末结转库存
     private Integer settlementNum;
     //库存余量
     private Integer surplusNum;
     //现库存
     private Integer availableNum;
     //库存类型  1 正常品  2 残次品
     private String type;
     //生产日期
     private Date productionDate;
     //商品名称
     private String goodsName;
     //创建人
     private Long creater;
     //id
     private Long id;
     //创建时间
     private Timestamp createDate;
     //月结账单id
     private Long monthlyAccountId;
     //商家id
     private Long merchantId;
     //商品编号
     private String goodsCode;
     //商品条形码
     private String barcode;
     //修改时间
     private Timestamp modifyDate;
     //库存单位
     private String unit;
     
     //============新增封装字段======
      //结转状态
     private String state;
      //结转月份
     private String settlementMonth;
     //商家名称
     private String merchantName;
     //月结当前时间 yyyy-MM-dd
     private String  snapshotDate;
     //是否过期（0 未过期 1已过期）
     private String isExpire;

     //标准条码
     private String commbarcode;
     
     
     private String typeLabel;
     private String unitLabel;
     private String stateLabel;
     private String isExpireLabel;
     
     /**
      * 首次上架日期
      */
      private Date firstShelfDate;
     
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

	public String getIsExpireLabel() {
		return isExpireLabel;
	}

	public void setIsExpireLabel(String isExpireLabel) {
		this.isExpireLabel = isExpireLabel;
	}

	public String getUnit() {
		return unit;
	}

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
     /*goodsCode get 方法 */
     public String getGoodsCode(){
         return goodsCode;
     }
     /*goodsCode set 方法 */
     public void setGoodsCode(String  goodsCode){
         this.goodsCode=goodsCode;
     }
     /*monthlyAccountId get 方法 */
     public Long getMonthlyAccountId(){
         return monthlyAccountId;
     }
     /*monthlyAccountId set 方法 */
     public void setMonthlyAccountId(Long  monthlyAccountId){
         this.monthlyAccountId=monthlyAccountId;
     }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
        return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
        this.goodsNo=goodsNo;
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
    /*settlementNum get 方法 */
    public Integer getSettlementNum(){
        return settlementNum;
    }
    /*settlementNum set 方法 */
    public void setSettlementNum(Integer  settlementNum){
        this.settlementNum=settlementNum;
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

    /*productionDate get 方法 */
    public Date getProductionDate(){
        return productionDate;
    }
    /*productionDate set 方法 */
    public void setProductionDate(Date  productionDate){
        this.productionDate=productionDate;
    }
    /*name get 方法 */
    public String getGoodsName(){
        return goodsName;
    }
    /*name set 方法 */
    public void setGoodsName(String  goodsName){
        this.goodsName=goodsName;
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
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getState() {
		return state;
	}

	public String getSettlementMonth() {
		return settlementMonth;
	}
	public void setSettlementMonth(String settlementMonth) {
		this.settlementMonth = settlementMonth;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getSnapshotDate() {
		return snapshotDate;
	}
	public void setSnapshotDate(String snapshotDate) {
		this.snapshotDate = snapshotDate;
	}
	public String getIsExpire() {
		return isExpire;
	}



    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }
    
    /*type set 方法 */
    public void setType(String  type){
        this.type=type;
        this.typeLabel=DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.initInventory_typeList, type);
    }
	public void setUnit(String unit) {
		this.unit = unit;
		this.unitLabel = DERP.getLabelByKey(DERP.inventory_unitList, unit);
	}
	public void setState(String state) {
		this.state = state;
		this.stateLabel=DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.monthlyAccount_stateList, state);
	}
	public void setIsExpire(String isExpire) {
		this.isExpire = isExpire;
		this.isExpireLabel = DERP.getLabelByKey(DERP.isExpireList, isExpire);
	}

	public Date getFirstShelfDate() {
		return firstShelfDate;
	}

	public void setFirstShelfDate(Date firstShelfDate) {
		this.firstShelfDate = firstShelfDate;
	}
	
	
}

