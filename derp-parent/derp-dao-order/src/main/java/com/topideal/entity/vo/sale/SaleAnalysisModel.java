package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 销售出库分析表
 * @author lian_
 *
 */
public class SaleAnalysisModel extends PageModel implements Serializable{

     //商品货号
     private String goodsNo;
     //销售出库单id
     private Long saleOutDepotId;
     //结余
     private Integer surplus;
     //销售订单ID
     private Long orderId;
     //完结出库时间
     private Timestamp endDate;
     //商品id
     private Long goodsId;
     //销售出库编码
     private String saleOutDepotCode;
     //销售数量
     private Integer saleNum;
     //是否完结入库(1-是,0-否)
     private String isEnd;
     //客户名称
     private String customerName;
     //出库数量
     private Integer outDepotNum;
     //客户ID
     private Long customerId;
     //创建人
     private Long creater;
     //销售订单编号
     private String orderCode;
     //id
     private Long id;
     //商品名称
     private String goodsName;
     //创建时间
     private Timestamp createDate;
     //商家ID
     private Long merchantId;
     //商家名称
     private String merchantName;
     //00-托盘 01-箱 02-件
     private String tallyingUnit;
     //销售类型 1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结
     private String saleType;

    //事业部名称
    private String buName;

    /**
     *  事业部id
     */
    private Long buId;
    /**
     *  销售明细id
     */
    private Long saleItemId;

     public String getTallyingUnit() {
		return tallyingUnit;
	}
	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}
	public String getSaleType() {
		return saleType;
	}
	public void setSaleType(String saleType) {
		this.saleType = saleType;
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
    /*goodsNo get 方法 */
    public String getGoodsNo(){
        return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
        this.goodsNo=goodsNo;
    }
    /*saleOutDepotId get 方法 */
    public Long getSaleOutDepotId(){
        return saleOutDepotId;
    }
    /*saleOutDepotId set 方法 */
    public void setSaleOutDepotId(Long  saleOutDepotId){
        this.saleOutDepotId=saleOutDepotId;
    }
    /*surplus get 方法 */
    public Integer getSurplus(){
        return surplus;
    }
    /*surplus set 方法 */
    public void setSurplus(Integer  surplus){
        this.surplus=surplus;
    }
    /*orderId get 方法 */
    public Long getOrderId(){
        return orderId;
    }
    /*orderId set 方法 */
    public void setOrderId(Long  orderId){
        this.orderId=orderId;
    }
    /*endDate get 方法 */
    public Timestamp getEndDate(){
        return endDate;
    }
    /*endDate set 方法 */
    public void setEndDate(Timestamp  endDate){
        this.endDate=endDate;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
        return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
        this.goodsId=goodsId;
    }
    /*saleOutDepotCode get 方法 */
    public String getSaleOutDepotCode(){
        return saleOutDepotCode;
    }
    /*saleOutDepotCode set 方法 */
    public void setSaleOutDepotCode(String  saleOutDepotCode){
        this.saleOutDepotCode=saleOutDepotCode;
    }
    /*saleNum get 方法 */
    public Integer getSaleNum(){
        return saleNum;
    }
    /*saleNum set 方法 */
    public void setSaleNum(Integer  saleNum){
        this.saleNum=saleNum;
    }
    /*isEnd get 方法 */
    public String getIsEnd(){
        return isEnd;
    }
    /*isEnd set 方法 */
    public void setIsEnd(String  isEnd){
        this.isEnd=isEnd;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
        return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
        this.customerName=customerName;
    }
    /*outDepotNum get 方法 */
    public Integer getOutDepotNum(){
        return outDepotNum;
    }
    /*outDepotNum set 方法 */
    public void setOutDepotNum(Integer  outDepotNum){
        this.outDepotNum=outDepotNum;
    }
    /*customerId get 方法 */
    public Long getCustomerId(){
        return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
        this.customerId=customerId;
    }
    /*creater get 方法 */
    public Long getCreater(){
        return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
        this.creater=creater;
    }
    /*orderCode get 方法 */
    public String getOrderCode(){
        return orderCode;
    }
    /*orderCode set 方法 */
    public void setOrderCode(String  orderCode){
        this.orderCode=orderCode;
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

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public Long getSaleItemId() {
        return saleItemId;
    }

    public void setSaleItemId(Long saleItemId) {
        this.saleItemId = saleItemId;
    }
}

