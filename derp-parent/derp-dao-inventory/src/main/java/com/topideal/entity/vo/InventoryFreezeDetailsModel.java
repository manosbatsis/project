package com.topideal.entity.vo;
import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 库存冻结明细
 * @author lian_
 *
 */
public class InventoryFreezeDetailsModel extends PageModel implements Serializable{

     //商品货号
     private String goodsNo;
     //仓库名称
     private String depotName;
     //批次号
     private String batchNo;
     //来源单据号
     private String orderNo;
     //商品id
     private Long goodsId;
     //数量
     private Integer num;
     //仓库id
     private Long depotId;
     //单据来源  1采购  2 调拨 3 销售
     private String source;
     //商家名称
     private String merchantName;
     //商家ID
     private Long merchantId;
     //单据状态 名称
     private String statusName;
     //id
     private Long id;
     //商品名称
     private String goodsName;
     //单据状态
     private String status;
     //创建时间
     private Timestamp createDate;
     //出入时间
     private Timestamp divergenceDate;
     //库存类型  1 正常品  2 残次品
     private String type;
     //单据日期
     private Timestamp sourceDate;
     //单位（ 0 托盘 1箱  2件）
     private String unit;
     //是否代销仓(1-是,0-否)
     private String isTopBooks;
     //仓库类型(a-卓志保税仓，b-非卓志保税仓，c-卓志海外仓，d-在途仓,e非卓志国内仓）
     private String depotType;
     //操作类型  0 调减 1调增
     private String operateType;
     //业务单号
     private String businessNo;

    /**
     * 事业部ID
     */
    private Long buId;
    /**
     * 事业部名称
     */
    private String buName;
    /**
     * 事业部编码
     */
    private String buCode;
     
     
     public String getBusinessNo() {
		return businessNo;
	}
	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
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
    /*batchNo get 方法 */
    public String getBatchNo(){
        return batchNo;
    }
    /*batchNo set 方法 */
    public void setBatchNo(String  batchNo){
        this.batchNo=batchNo;
    }
    /*orderNo get 方法 */
    public String getOrderNo(){
        return orderNo;
    }
    /*orderNo set 方法 */
    public void setOrderNo(String  orderNo){
        this.orderNo=orderNo;
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
    /*source get 方法 */
    public String getSource(){
        return source;
    }
    /*source set 方法 */
    public void setSource(String  source){
        this.source=source;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName=merchantName;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*statusName get 方法 */
    public String getStatusName(){
        return statusName;
    }
    /*statusName set 方法 */
    public void setStatusName(String  statusName){
        this.statusName=statusName;
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
    /*status get 方法 */
    public String getStatus(){
        return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
        this.status=status;
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

    public String getBuCode() {
        return buCode;
    }

    public void setBuCode(String buCode) {
        this.buCode = buCode;
    }
}

