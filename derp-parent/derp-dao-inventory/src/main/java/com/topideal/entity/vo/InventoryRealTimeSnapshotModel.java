package com.topideal.entity.vo;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

/**
 * 实时库存快照表
 * @author lian_
 */
public class InventoryRealTimeSnapshotModel extends PageModel implements Serializable{

     //商品货号
     private String goodsNo;
     //失效日期
     private Date overdueDate;
     //仓库名称
     private String depotName;
     //批次号
     private String batchNo;
     //库存可用数量
     private Integer realStockNum;
     //库存锁定数量
     private Integer lockStockNum;
     //库存类型  0 正常品  1 残次品
     private String stockType;
     //商品id
     private Long goodsId;
     //仓库id
     private Long depotId;
     //托盘号
     private String lpn;
     //商家名称
     private String merchantName;
     //库存状态  0 可用，1 冻结
     private String storesType;
     //库存单位 字符串 00：托盘，01：箱 , 02：件
     private String unit;
     //生产日期
     private Date productionDate;
     //商家ID
     private Long merchantId;
     //库存数量
     private Integer qty;
     //创建人
     private Long creater;
     //库存冻结数量
     private Integer realFrozenStockNum;
     //id
     private Long id;
     //商品名称
     private String goodsName;
     //创建时间
     private Timestamp createDate;
     //条形码
     private String barcode;
     //修改时间
     private Timestamp modifyDate;
     // 获取快照时间 YYYY-MM-dd HH:mm:ss
     private Timestamp requestTime;
     // opg号
     private String opgCode;
     //OP-OP接口 OFC-OFC接口 GSS-GSS接口
     private String snapshotSource;
     
     
     public String getSnapshotSource() {
		return snapshotSource;
	}
	public void setSnapshotSource(String snapshotSource) {
		this.snapshotSource = snapshotSource;
	}
	public String getOpgCode() {
		return opgCode;
	}
	public void setOpgCode(String opgCode) {
		this.opgCode = opgCode;
	}
	public Timestamp getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

    public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
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
    /*realStockNum get 方法 */
    public Integer getRealStockNum(){
        return realStockNum;
    }
    /*realStockNum set 方法 */
    public void setRealStockNum(Integer  realStockNum){
        this.realStockNum=realStockNum;
    }
    /*lockStockNum get 方法 */
    public Integer getLockStockNum(){
        return lockStockNum;
    }
    /*lockStockNum set 方法 */
    public void setLockStockNum(Integer  lockStockNum){
        this.lockStockNum=lockStockNum;
    }
    /*stockType get 方法 */
    public String getStockType(){
        return stockType;
    }
    /*stockType set 方法 */
    public void setStockType(String  stockType){
        this.stockType=stockType;
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
    /*storesType get 方法 */
    public String getStoresType(){
        return storesType;
    }
    /*storesType set 方法 */
    public void setStoresType(String  storesType){
        this.storesType=storesType;
    }
    /*unit get 方法 */
    public String getUnit(){
        return unit;
    }
    /*unit set 方法 */
    public void setUnit(String  unit){
        this.unit=unit;
    }
    /*productionDate get 方法 */
    public Date getProductionDate(){
        return productionDate;
    }
    /*productionDate set 方法 */
    public void setProductionDate(Date  productionDate){
        this.productionDate=productionDate;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*qty get 方法 */
    public Integer getQty(){
        return qty;
    }
    /*qty set 方法 */
    public void setQty(Integer  qty){
        this.qty=qty;
    }
    /*creater get 方法 */
    public Long getCreater(){
        return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
        this.creater=creater;
    }
    /*realFrozenStockNum get 方法 */
    public Integer getRealFrozenStockNum(){
        return realFrozenStockNum;
    }
    /*realFrozenStockNum set 方法 */
    public void setRealFrozenStockNum(Integer  realFrozenStockNum){
        this.realFrozenStockNum=realFrozenStockNum;
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






}

