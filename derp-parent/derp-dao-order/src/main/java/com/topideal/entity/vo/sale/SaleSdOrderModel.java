package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class SaleSdOrderModel extends PageModel implements Serializable{


    /**
     * ID
     */
    private Long id;
    /**
     * 销售SD编码
     */
    private String code;
    /**
     * 事业部ID
     */
    private Long buId;
    /**
     * 事业部名称
     */
    private String buName;
    /**
     * 公司ID
     */
    private Long merchantId;
    /**
     * 公司名称
     */
    private String merchantName;
    /**
     * 客户ID
     */
    private Long customerId;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * po号
     */
    private String poNo;
    /**
     * 销售SD类型id
     */
    private Long sdTypeId;
    /**
     * 销售SD类型
     */
    private String sdType;
    /**
     * 销售SD类型名称
     */
    private String sdTypeName;
    /**
     * 币种
     */
    private String currency;
    /**
     * SD总数量
     */
    private Integer totalSdNum;
    /**
     * SD总金额
     */
    private BigDecimal totalSdAmount;
    /**
     * 订单状态:006-已删除
     */
    private String state;
    /**
     * 创建人id
     */
    private Long creater;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 创建时间
     */
    private Timestamp createDate;
    /**
     * 修改人id
     */
    private Long modifier;
    /**
     * 修改人名称
     */
    private String modifiyName;
    /**
     * 修改时间
     */
    private Timestamp modifyDate;
    /**
     * 数据来源 1-上架生成 2-手工导入
     */
    private String orderSource;
    /**
     * 来源id
     */
    private Long orderId;
    /**
     * 来源单号
     */
    private String orderCode;
    /**
     * 业务Id
     */
    private Long businessId;
    /**
     * 业务单号
     */
    private String businessCode;
    /**
     * 单据类型 1-上架单 2-销售退入库单
     */
    private String orderType;

    /**
     * 是否红冲单：0-否，1-是
     */
    private String isWriteOff;
    /**
     * 归属日期
     */
    private Timestamp ownDate;

    /**
     * 原销售SD单id
     */
    private Long originalSaleSdOrderId;
    /**
     * 原销售SD单号
     */
    private String originalSaleSdOrderCode;

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
    /*buId get 方法 */
    public Long getBuId(){
        return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
        this.buId=buId;
    }
    /*buName get 方法 */
    public String getBuName(){
        return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
        this.buName=buName;
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
    /*poNo get 方法 */
    public String getPoNo(){
        return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
        this.poNo=poNo;
    }
    /*sdTypeId get 方法 */
    public Long getSdTypeId(){
        return sdTypeId;
    }
    /*sdTypeId set 方法 */
    public void setSdTypeId(Long  sdTypeId){
        this.sdTypeId=sdTypeId;
    }
    /*sdType get 方法 */
    public String getSdType(){
        return sdType;
    }
    /*sdType set 方法 */
    public void setSdType(String  sdType){
        this.sdType=sdType;
    }
    /*sdTypeName get 方法 */
    public String getSdTypeName(){
        return sdTypeName;
    }
    /*sdTypeName set 方法 */
    public void setSdTypeName(String  sdTypeName){
        this.sdTypeName=sdTypeName;
    }
    /*currency get 方法 */
    public String getCurrency(){
        return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
        this.currency=currency;
    }
    /*totalSdNum get 方法 */
    public Integer getTotalSdNum(){
        return totalSdNum;
    }
    /*totalSdNum set 方法 */
    public void setTotalSdNum(Integer  totalSdNum){
        this.totalSdNum=totalSdNum;
    }
    /*totalSdAmount get 方法 */
    public BigDecimal getTotalSdAmount(){
        return totalSdAmount;
    }
    /*totalSdAmount set 方法 */
    public void setTotalSdAmount(BigDecimal  totalSdAmount){
        this.totalSdAmount=totalSdAmount;
    }
    /*state get 方法 */
    public String getState(){
        return state;
    }
    /*state set 方法 */
    public void setState(String  state){
        this.state=state;
    }
    /*creater get 方法 */
    public Long getCreater(){
        return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
        this.creater=creater;
    }
    /*createName get 方法 */
    public String getCreateName(){
        return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
        this.createName=createName;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
    /*modifier get 方法 */
    public Long getModifier(){
        return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
        this.modifier=modifier;
    }
    /*modifiyName get 方法 */
    public String getModifiyName(){
        return modifiyName;
    }
    /*modifiyName set 方法 */
    public void setModifiyName(String  modifiyName){
        this.modifiyName=modifiyName;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
        this.modifyDate=modifyDate;
    }
    /*orderSource get 方法 */
    public String getOrderSource(){
        return orderSource;
    }
    /*orderSource set 方法 */
    public void setOrderSource(String  orderSource){
        this.orderSource=orderSource;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Timestamp getOwnDate() {
        return ownDate;
    }

    public void setOwnDate(Timestamp ownDate) {
        this.ownDate = ownDate;
    }

    public String getIsWriteOff() {
        return isWriteOff;
    }

    public void setIsWriteOff(String isWriteOff) {
        this.isWriteOff = isWriteOff;
    }

    public Long getOriginalSaleSdOrderId() {
        return originalSaleSdOrderId;
    }

    public void setOriginalSaleSdOrderId(Long originalSaleSdOrderId) {
        this.originalSaleSdOrderId = originalSaleSdOrderId;
    }

    public String getOriginalSaleSdOrderCode() {
        return originalSaleSdOrderCode;
    }

    public void setOriginalSaleSdOrderCode(String originalSaleSdOrderCode) {
        this.originalSaleSdOrderCode = originalSaleSdOrderCode;
    }
}
