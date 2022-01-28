package com.topideal.entity.vo.bill;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class TocTemporaryReceiveBillCostItemMonthlyModel extends PageModel implements Serializable{

    /**
     * id
     */
    private Long id;
    /**
     * 系统订单号
     */
    private String orderCode;
    /**
     * 外部订单号
     */
    private String externalCode;
    /**
     * 归属月份 YYYY-MM
     */
    private String month;
    /**
     * 店铺类型值编码 001:POP; 002:一件代发
     */
    private String shopTypeCode;
    /**
     * 商家ID
     */
    private Long merchantId;
    /**
     * 商家名称
     */
    private String merchantName;
    /**
     * 事业部id
     */
    private Long buId;
    /**
     * 事业部名称
     */
    private String buName;
    /**
     * 客户id
     */
    private Long customerId;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 店铺编码
     */
    private String shopCode;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 电商平台编码
     */
    private String storePlatformCode;
    /**
     * 费项id
     */
    private Long projectId;
    /**
     * 费项名称
     */
    private String projectName;
    /**
     * 平台费项id
     */
    private Long platformProjectId;
    /**
     * 平台费项名称
     */
    private String platformProjectName;
    /**
     * 订单原币种
     */
    private String temporaryCurrency;
    /**
     * 暂估费用金额（RMB）
     */
    private BigDecimal temporaryRmbCost;
    /**
     * 平台结算费用（原币）
     */
    private BigDecimal settlementOriCost;
    /**
     * 平台结算费用（RMB）
     */
    private BigDecimal settlementRmbCost;
    /**
     * 订单原币种
     */
    private String originalCurrency;
    /**
     * 结算日期
     */
    private Timestamp settlementDate;
    /**
     * 结算单号
     */
    private String settlementCode;
    /**
     * 结算标识：1-已核销 0-未核销
     */
    private String settlementMark;
    /**
     * 创建时间
     */
    private Timestamp createDate;
    /**
     * 修改时间
     */
    private Timestamp modifyDate;


    /**
     *冲销金额
     */
    private BigDecimal writeOffAmount;

    /**
     * 未核销金额
     */
    private BigDecimal nonVerifyAmount;

    /**
     * 单据类型：0-发货订单 1-退款订单
     */
    private String orderType;

    /**
     * 母品牌
     */
    private String parentBrandName;
    /**
     * 母品牌id
     */
    private Long parentBrandId;
    /**
     * 母品牌编码
     */
    private String parentBrandCode;
    /**
     * 结算明细ID
     */
    private String settlementItemId;
    /**
     * 差异调整金额(rmb)
     */
    private BigDecimal adjustmentRmbAmount;

    //入账日期
    private Date billInDate;

    //暂估月份
    private String tempMonth;

    //剩余结算金额
    private BigDecimal lastReceiveAmount;

    public BigDecimal getNonVerifyAmount() {
        return nonVerifyAmount;
    }

    public void setNonVerifyAmount(BigDecimal nonVerifyAmount) {
        this.nonVerifyAmount = nonVerifyAmount;
    }

    public BigDecimal getWriteOffAmount() {
        return writeOffAmount;
    }

    public void setWriteOffAmount(BigDecimal writeOffAmount) {
        this.writeOffAmount = writeOffAmount;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*orderCode get 方法 */
    public String getOrderCode(){
        return orderCode;
    }
    /*orderCode set 方法 */
    public void setOrderCode(String  orderCode){
        this.orderCode=orderCode;
    }
    /*externalCode get 方法 */
    public String getExternalCode(){
        return externalCode;
    }
    /*externalCode set 方法 */
    public void setExternalCode(String  externalCode){
        this.externalCode=externalCode;
    }
    /*month get 方法 */
    public String getMonth(){
        return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
        this.month=month;
    }
    /*shopTypeCode get 方法 */
    public String getShopTypeCode(){
        return shopTypeCode;
    }
    /*shopTypeCode set 方法 */
    public void setShopTypeCode(String  shopTypeCode){
        this.shopTypeCode=shopTypeCode;
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
    /*shopCode get 方法 */
    public String getShopCode(){
        return shopCode;
    }
    /*shopCode set 方法 */
    public void setShopCode(String  shopCode){
        this.shopCode=shopCode;
    }
    /*shopName get 方法 */
    public String getShopName(){
        return shopName;
    }
    /*shopName set 方法 */
    public void setShopName(String  shopName){
        this.shopName=shopName;
    }
    /*storePlatformCode get 方法 */
    public String getStorePlatformCode(){
        return storePlatformCode;
    }
    /*storePlatformCode set 方法 */
    public void setStorePlatformCode(String  storePlatformCode){
        this.storePlatformCode=storePlatformCode;
    }
    /*projectId get 方法 */
    public Long getProjectId(){
        return projectId;
    }
    /*projectId set 方法 */
    public void setProjectId(Long  projectId){
        this.projectId=projectId;
    }
    /*projectName get 方法 */
    public String getProjectName(){
        return projectName;
    }
    /*projectName set 方法 */
    public void setProjectName(String  projectName){
        this.projectName=projectName;
    }
    /*platformProjectId get 方法 */
    public Long getPlatformProjectId(){
        return platformProjectId;
    }
    /*platformProjectId set 方法 */
    public void setPlatformProjectId(Long  platformProjectId){
        this.platformProjectId=platformProjectId;
    }
    /*platformProjectName get 方法 */
    public String getPlatformProjectName(){
        return platformProjectName;
    }
    /*platformProjectName set 方法 */
    public void setPlatformProjectName(String  platformProjectName){
        this.platformProjectName=platformProjectName;
    }
    /*temporaryCurrency get 方法 */
    public String getTemporaryCurrency(){
        return temporaryCurrency;
    }
    /*temporaryCurrency set 方法 */
    public void setTemporaryCurrency(String  temporaryCurrency){
        this.temporaryCurrency=temporaryCurrency;
    }
    /*temporaryRmbCost get 方法 */
    public BigDecimal getTemporaryRmbCost(){
        return temporaryRmbCost;
    }
    /*temporaryRmbCost set 方法 */
    public void setTemporaryRmbCost(BigDecimal  temporaryRmbCost){
        this.temporaryRmbCost=temporaryRmbCost;
    }
    /*settlementOriCost get 方法 */
    public BigDecimal getSettlementOriCost(){
        return settlementOriCost;
    }
    /*settlementOriCost set 方法 */
    public void setSettlementOriCost(BigDecimal  settlementOriCost){
        this.settlementOriCost=settlementOriCost;
    }
    /*settlementRmbCost get 方法 */
    public BigDecimal getSettlementRmbCost(){
        return settlementRmbCost;
    }
    /*settlementRmbCost set 方法 */
    public void setSettlementRmbCost(BigDecimal  settlementRmbCost){
        this.settlementRmbCost=settlementRmbCost;
    }
    /*originalCurrency get 方法 */
    public String getOriginalCurrency(){
        return originalCurrency;
    }
    /*originalCurrency set 方法 */
    public void setOriginalCurrency(String  originalCurrency){
        this.originalCurrency=originalCurrency;
    }
    /*settlementDate get 方法 */
    public Timestamp getSettlementDate(){
        return settlementDate;
    }
    /*settlementDate set 方法 */
    public void setSettlementDate(Timestamp  settlementDate){
        this.settlementDate=settlementDate;
    }
    /*settlementCode get 方法 */
    public String getSettlementCode(){
        return settlementCode;
    }
    /*settlementCode set 方法 */
    public void setSettlementCode(String  settlementCode){
        this.settlementCode=settlementCode;
    }
    /*settlementMark get 方法 */
    public String getSettlementMark(){
        return settlementMark;
    }
    /*settlementMark set 方法 */
    public void setSettlementMark(String  settlementMark){
        this.settlementMark=settlementMark;
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

    public String getParentBrandName() {
        return parentBrandName;
    }

    public void setParentBrandName(String parentBrandName) {
        this.parentBrandName = parentBrandName;
    }

    public Long getParentBrandId() {
        return parentBrandId;
    }

    public void setParentBrandId(Long parentBrandId) {
        this.parentBrandId = parentBrandId;
    }

    public String getParentBrandCode() {
        return parentBrandCode;
    }

    public void setParentBrandCode(String parentBrandCode) {
        this.parentBrandCode = parentBrandCode;
    }

    public String getSettlementItemId() {
        return settlementItemId;
    }

    public void setSettlementItemId(String settlementItemId) {
        this.settlementItemId = settlementItemId;
    }


    public BigDecimal getAdjustmentRmbAmount() {
        return adjustmentRmbAmount;
    }

    public void setAdjustmentRmbAmount(BigDecimal adjustmentRmbAmount) {
        this.adjustmentRmbAmount = adjustmentRmbAmount;
    }

    public Date getBillInDate() {
        return billInDate;
    }

    public void setBillInDate(Date billInDate) {
        this.billInDate = billInDate;
    }

    public String getTempMonth() {
        return tempMonth;
    }

    public void setTempMonth(String tempMonth) {
        this.tempMonth = tempMonth;
    }

    public BigDecimal getLastReceiveAmount() {
        return lastReceiveAmount;
    }

    public void setLastReceiveAmount(BigDecimal lastReceiveAmount) {
        this.lastReceiveAmount = lastReceiveAmount;
    }
}
