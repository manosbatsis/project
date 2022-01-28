package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class SaleShelfIdepotModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 上架入库单号
    */
    private String code;
    /**
    * 销售订单编号
    */
    private String saleOrderCode;
    /**
    * 销售出库清单编号
    */
    private String saleOutCode;
    /**
    * 销售上架单id
    */
    private Long saleShelfId;
    /**
    * 入仓仓库id
    */
    private Long inDepotId;
    /**
    * 入仓仓库
    */
    private String inDepotName;
    /**
    * 入仓仓库编码
    */
    private String inDepotCode;
    /**
    * 出仓仓库ID
    */
    private Long outDepotId;
    /**
    * 出仓仓库名称
    */
    private String outDepotName;
    /**
    * 出仓仓库编码
    */
    private String outDepotCode;
    /**
    * po单号
    */
    private String poNo;
    /**
    * 单据状态:  011-待入仓,012-已入仓,028-入库中
    */
    private String state;
    /**
    * 上架时间
    */
    private Timestamp shelfDate;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 客户id
    */
    private Long customerId;
    /**
    * 客户名称
    */
    private String customerName;
    /**
    * 操作人
    */
    private String operator;
    /**
    * 操作人ID
    */
    private Long operatorId;
    /**
    * 操作时间
    */
    private Timestamp operatorDate;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    //销售订单ID
    private Long saleOrderId;
    //销售出库ID
    private Long saleOutDepotId;
    //核销表获取状态（0/空-未获取、1-已获取）
    private String verifyRelState;

    //事业部名称
    private String buName;

    /**
     *  事业部id
     */
    private Long buId;
    /**
     * 是否红冲单：0-否，1-是
     */
    private String isWriteOff;
    /**
     * 原上架入库单id
     */
    private Long originalShelfIdepotId;
    /**
     * 原上架入库单号
     */
    private String originalShelfIdepotCode;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
    }
    /*saleOrderCode get 方法 */
    public String getSaleOrderCode(){
    return saleOrderCode;
    }
    /*saleOrderCode set 方法 */
    public void setSaleOrderCode(String  saleOrderCode){
    this.saleOrderCode=saleOrderCode;
    }
    /*saleOutCode get 方法 */
    public String getSaleOutCode(){
    return saleOutCode;
    }
    /*saleOutCode set 方法 */
    public void setSaleOutCode(String  saleOutCode){
    this.saleOutCode=saleOutCode;
    }
    /*saleShelfId get 方法 */
    public Long getSaleShelfId(){
    return saleShelfId;
    }
    /*saleShelfId set 方法 */
    public void setSaleShelfId(Long  saleShelfId){
    this.saleShelfId=saleShelfId;
    }
    /*inDepotId get 方法 */
    public Long getInDepotId(){
    return inDepotId;
    }
    /*inDepotId set 方法 */
    public void setInDepotId(Long  inDepotId){
    this.inDepotId=inDepotId;
    }
    /*inDepotName get 方法 */
    public String getInDepotName(){
    return inDepotName;
    }
    /*inDepotName set 方法 */
    public void setInDepotName(String  inDepotName){
    this.inDepotName=inDepotName;
    }
    /*inDepotCode get 方法 */
    public String getInDepotCode(){
    return inDepotCode;
    }
    /*inDepotCode set 方法 */
    public void setInDepotCode(String  inDepotCode){
    this.inDepotCode=inDepotCode;
    }
    /*outDepotId get 方法 */
    public Long getOutDepotId(){
    return outDepotId;
    }
    /*outDepotId set 方法 */
    public void setOutDepotId(Long  outDepotId){
    this.outDepotId=outDepotId;
    }
    /*outDepotName get 方法 */
    public String getOutDepotName(){
    return outDepotName;
    }
    /*outDepotName set 方法 */
    public void setOutDepotName(String  outDepotName){
    this.outDepotName=outDepotName;
    }
    /*outDepotCode get 方法 */
    public String getOutDepotCode(){
    return outDepotCode;
    }
    /*outDepotCode set 方法 */
    public void setOutDepotCode(String  outDepotCode){
    this.outDepotCode=outDepotCode;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*state get 方法 */
    public String getState(){
    return state;
    }
    /*state set 方法 */
    public void setState(String  state){
    this.state=state;
    }
    /*shelfDate get 方法 */
    public Timestamp getShelfDate(){
    return shelfDate;
    }
    /*shelfDate set 方法 */
    public void setShelfDate(Timestamp  shelfDate){
    this.shelfDate=shelfDate;
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
    /*customerId get 方法 */
    public Long getCustomerId(){
    return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
    this.customerId=customerId;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
    return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
    this.customerName=customerName;
    }
    /*operator get 方法 */
    public String getOperator(){
    return operator;
    }
    /*operator set 方法 */
    public void setOperator(String  operator){
    this.operator=operator;
    }
    /*operatorId get 方法 */
    public Long getOperatorId(){
    return operatorId;
    }
    /*operatorId set 方法 */
    public void setOperatorId(Long  operatorId){
    this.operatorId=operatorId;
    }
    /*operatorDate get 方法 */
    public Timestamp getOperatorDate(){
    return operatorDate;
    }
    /*operatorDate set 方法 */
    public void setOperatorDate(Timestamp  operatorDate){
    this.operatorDate=operatorDate;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
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

    public Long getSaleOrderId() {
        return saleOrderId;
    }

    public void setSaleOrderId(Long saleOrderId) {
        this.saleOrderId = saleOrderId;
    }

    public Long getSaleOutDepotId() {
        return saleOutDepotId;
    }

    public void setSaleOutDepotId(Long saleOutDepotId) {
        this.saleOutDepotId = saleOutDepotId;
    }

    public String getVerifyRelState() {
        return verifyRelState;
    }

    public void setVerifyRelState(String verifyRelState) {
        this.verifyRelState = verifyRelState;
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

    public String getIsWriteOff() {
        return isWriteOff;
    }

    public void setIsWriteOff(String isWriteOff) {
        this.isWriteOff = isWriteOff;
    }

    public Long getOriginalShelfIdepotId() {
        return originalShelfIdepotId;
    }

    public void setOriginalShelfIdepotId(Long originalShelfIdepotId) {
        this.originalShelfIdepotId = originalShelfIdepotId;
    }

    public String getOriginalShelfIdepotCode() {
        return originalShelfIdepotCode;
    }

    public void setOriginalShelfIdepotCode(String originalShelfIdepotCode) {
        this.originalShelfIdepotCode = originalShelfIdepotCode;
    }
}
