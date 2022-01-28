package com.topideal.entity.vo.order;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class YunjiReturnDetailModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 结算单号
    */
    private String settleId;
    /**
    * 退款单号
    */
    private String returnOrderId;
    /**
    * 订单号
    */
    private String orderId;
    /**
    * 主单
    */
    private String srcOrderId;
    /**
    * 支付时间
    */
    private Timestamp payTime;
    /**
    * 退款完成时间
    */
    private Timestamp returnFinishTime;
    /**
    * 商品编码
    */
    private String skuNo;
    /**
    * 商品名称
    */
    private String itemName;
    /**
    * 商品规格
    */
    private String normName;
    /**
    * 退货数量
    */
    private Integer qty;
    /**
    * 结算单价
    */
    private BigDecimal settlementPrice;
    /**
    * 结算总价
    */
    private BigDecimal settlementTotalPrice;
    /**
    * 结算税额
    */
    private BigDecimal settlementTaxPrice;
    /**
    * 2 ：退款不退货 ，3 ：退货退款，6 ：换货，默认：其他
    */
    private String returnType;
    /**
    * 
    */
    private String userCode;
    /**
    * 0 ：未使用 ，1 ：已使用
    */
    private String isUsed;
    /**
    * 厦门库创建时间
    */
    private Timestamp createTime;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 爬虫云集原ID
    */
    private Long oldId;
    /**
    * 是否已校验（云集自动校验1：已校验，0：未校验）
    */
    private String isVerify;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     *成功或失败原因
     */
    private String reason;
    /**
     * 账单出入库单号
     */
     private String billOutinCode;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*settleId get 方法 */
    public String getSettleId(){
    return settleId;
    }
    /*settleId set 方法 */
    public void setSettleId(String  settleId){
    this.settleId=settleId;
    }
    /*returnOrderId get 方法 */
    public String getReturnOrderId(){
    return returnOrderId;
    }
    /*returnOrderId set 方法 */
    public void setReturnOrderId(String  returnOrderId){
    this.returnOrderId=returnOrderId;
    }
    /*orderId get 方法 */
    public String getOrderId(){
    return orderId;
    }
    /*orderId set 方法 */
    public void setOrderId(String  orderId){
    this.orderId=orderId;
    }
    /*srcOrderId get 方法 */
    public String getSrcOrderId(){
    return srcOrderId;
    }
    /*srcOrderId set 方法 */
    public void setSrcOrderId(String  srcOrderId){
    this.srcOrderId=srcOrderId;
    }
    /*payTime get 方法 */
    public Timestamp getPayTime(){
    return payTime;
    }
    /*payTime set 方法 */
    public void setPayTime(Timestamp  payTime){
    this.payTime=payTime;
    }
    /*returnFinishTime get 方法 */
    public Timestamp getReturnFinishTime(){
    return returnFinishTime;
    }
    /*returnFinishTime set 方法 */
    public void setReturnFinishTime(Timestamp  returnFinishTime){
    this.returnFinishTime=returnFinishTime;
    }
    /*skuNo get 方法 */
    public String getSkuNo(){
    return skuNo;
    }
    /*skuNo set 方法 */
    public void setSkuNo(String  skuNo){
    this.skuNo=skuNo;
    }
    /*itemName get 方法 */
    public String getItemName(){
    return itemName;
    }
    /*itemName set 方法 */
    public void setItemName(String  itemName){
    this.itemName=itemName;
    }
    /*normName get 方法 */
    public String getNormName(){
    return normName;
    }
    /*normName set 方法 */
    public void setNormName(String  normName){
    this.normName=normName;
    }
    /*qty get 方法 */
    public Integer getQty(){
    return qty;
    }
    /*qty set 方法 */
    public void setQty(Integer  qty){
    this.qty=qty;
    }
    /*settlementPrice get 方法 */
    public BigDecimal getSettlementPrice(){
    return settlementPrice;
    }
    /*settlementPrice set 方法 */
    public void setSettlementPrice(BigDecimal  settlementPrice){
    this.settlementPrice=settlementPrice;
    }
    /*settlementTotalPrice get 方法 */
    public BigDecimal getSettlementTotalPrice(){
    return settlementTotalPrice;
    }
    /*settlementTotalPrice set 方法 */
    public void setSettlementTotalPrice(BigDecimal  settlementTotalPrice){
    this.settlementTotalPrice=settlementTotalPrice;
    }
    /*settlementTaxPrice get 方法 */
    public BigDecimal getSettlementTaxPrice(){
    return settlementTaxPrice;
    }
    /*settlementTaxPrice set 方法 */
    public void setSettlementTaxPrice(BigDecimal  settlementTaxPrice){
    this.settlementTaxPrice=settlementTaxPrice;
    }
    /*returnType get 方法 */
    public String getReturnType(){
    return returnType;
    }
    /*returnType set 方法 */
    public void setReturnType(String  returnType){
    this.returnType=returnType;
    }
    /*userCode get 方法 */
    public String getUserCode(){
    return userCode;
    }
    /*userCode set 方法 */
    public void setUserCode(String  userCode){
    this.userCode=userCode;
    }
    /*isUsed get 方法 */
    public String getIsUsed(){
    return isUsed;
    }
    /*isUsed set 方法 */
    public void setIsUsed(String  isUsed){
    this.isUsed=isUsed;
    }
    /*createTime get 方法 */
    public Timestamp getCreateTime(){
    return createTime;
    }
    /*createTime set 方法 */
    public void setCreateTime(Timestamp  createTime){
    this.createTime=createTime;
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
    /*oldId get 方法 */
    public Long getOldId(){
    return oldId;
    }
    /*oldId set 方法 */
    public void setOldId(Long  oldId){
    this.oldId=oldId;
    }
    /*isVerify get 方法 */
    public String getIsVerify(){
    return isVerify;
    }
    /*isVerify set 方法 */
    public void setIsVerify(String  isVerify){
    this.isVerify=isVerify;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
	public String getBillOutinCode() {
		return billOutinCode;
	}
	public void setBillOutinCode(String billOutinCode) {
		this.billOutinCode = billOutinCode;
	}
    
    
}
