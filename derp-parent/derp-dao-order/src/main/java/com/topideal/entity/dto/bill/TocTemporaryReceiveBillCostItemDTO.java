package com.topideal.entity.dto.bill;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class TocTemporaryReceiveBillCostItemDTO extends PageModel implements Serializable{

    /**
    * id
    */
    @ApiModelProperty(value = "id", required = false)
    private Long id;
    /**
    * 账单id
    */
    @ApiModelProperty(value = "账单id", required = false)
    private Long billId;
    /**
    * 系统订单号
    */
    @ApiModelProperty(value = "系统订单号", required = false)
    private String orderCode;
    /**
    * 外部订单号
    */
    @ApiModelProperty(value = "外部订单号", required = false)
    private String externalCode;
    /**
    * 归属月份 YYYY-MM
    */
    @ApiModelProperty(value = "归属月份 YYYY-MM", required = false)
    private String month;
    /**
    * 店铺类型值编码 001:POP; 002:一件代发
    */
    @ApiModelProperty(value = "店铺类型值编码 001:POP; 002:一件代发", required = false)
    private String shopTypeCode;
    /**
    * 公司ID
    */
    @ApiModelProperty(value = "公司id", required = false)
    private Long merchantId;
    /**
    * 公司名称
    */
    @ApiModelProperty(value = "公司名称", required = false)
    private String merchantName;
    /**
    * 事业部id
    */
    @ApiModelProperty(value = "事业部id", required = false)
    private Long buId;
    /**
    * 事业部名称
    */
    @ApiModelProperty(value = "事业部名称", required = false)
    private String buName;
    /**
    * 客户id
    */
    @ApiModelProperty(value = "客户id", required = false)
    private Long customerId;
    /**
    * 客户名称
    */
    @ApiModelProperty(value = "客户名称", required = false)
    private String customerName;
    /**
    * 店铺编码
    */
    @ApiModelProperty(value = "店铺编码", required = false)
    private String shopCode;
    /**
    * 店铺名称
    */
    @ApiModelProperty(value = "店铺名称", required = false)
    private String shopName;
    /**
    * 电商平台编码
    */
    @ApiModelProperty(value = "电商平台编码", required = false)
    private String storePlatformCode;
    @ApiModelProperty(value = "电商平台名称", required = false)
    private String storePlatformName;
    /**
    * 费项id
    */
    @ApiModelProperty(value = "费项id", required = false)
    private Long projectId;
    /**
    * 费项名称
    */
    @ApiModelProperty(value = "费项名称", required = false)
    private String projectName;
    /**
    * 平台费项id
    */
    @ApiModelProperty(value = "平台费项id", required = false)
    private Long platformProjectId;
    /**
    * 平台费项名称
    */
    @ApiModelProperty(value = "平台费项名称", required = false)
    private String platformProjectName;
    /**
    * 订单原币种
    */
    @ApiModelProperty(value = "订单原币种", required = false)
    private String temporaryCurrency;
    /**
    * 暂估费用金额（RMB）
    */
    @ApiModelProperty(value = "暂估费用金额（RMB）", required = false)
    private BigDecimal temporaryRmbCost;
    /**
    * 平台结算费用（原币）
    */
    @ApiModelProperty(value = "平台结算费用（原币）", required = false)
    private BigDecimal settlementOriCost;
    /**
    * 平台结算费用（RMB）
    */
    @ApiModelProperty(value = "平台结算费用（RMB）", required = false)
    private BigDecimal settlementRmbCost;
    /**
    * 订单原币种
    */
    @ApiModelProperty(value = "订单原币种", required = false)
    private String originalCurrency;
    /**
    * 结算日期
    */
    @ApiModelProperty(value = "结算日期", required = false)
    private Timestamp settlementDate;
    /**
    * 结算单号
    */
    @ApiModelProperty(value = "结算单号", required = false)
    private String settlementCode;
    /**
    * 结算标识：1-已核销 0-未核销
    */
    @ApiModelProperty(value = "结算标识：1-已核销 0-未核销", required = false)
    private String settlementMark;
    private String settlementMarkLabel;
    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间", required = false)
    private Timestamp createDate;
    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间", required = false)
    private Timestamp modifyDate;

    // 当前登录用户绑定的事业部集合
    @ApiModelProperty(value = "当前登录用户绑定的事业部集合", required = false)
    private List<Long> buList;

    //起始账单月份
    @ApiModelProperty(value = "起始账单月份", required = false)
    private String monthStart;
    //结束账单月份
    @ApiModelProperty(value = "结束账单月份", required = false)
    private String monthEnd;

    @ApiModelProperty(hidden = true)
    private String billIds;

    @ApiModelProperty(value = "冲销金额", required = false)
    private BigDecimal writeOffAmount;
    @ApiModelProperty(value = "未核销金额", required = false)
    private BigDecimal nonVerifyAmount;


    @ApiModelProperty(value = "单据类型：0-发货订单 1-退款订单 2-差异调整单", required = false)
    private String orderType;
    private String orderTypeLabel;

    /**
     * 母品牌
     */
     @ApiModelProperty(value = "母品牌", required = false)
     private String parentBrandName;
     /**
     * 母品牌id
     */
     @ApiModelProperty(value = "母品牌id", required = false)
     private Long parentBrandId;
     /**
     * 母品牌编码
     */
     @ApiModelProperty(value = "母品牌编码", required = false)
     private String parentBrandCode;

    /**
     * ids
     */
    private String ids;
    /**
     * 结算明细ID
     */
    private String settlementItemId;

    /**
     * 差异调整金额(rmb)
     */
    @ApiModelProperty(value = "差异调整金额(rmb)", required = false)
    private BigDecimal adjustmentRmbAmount;

    //入账日期
    @ApiModelProperty(value = "入账日期", required = false)
    private Date billInDate;

    /**
     * 用于sql中查询入账时间比结算时间大的数据
     */
    private Boolean compareBillInDate;
    /**
     * 用于sql中查询已核销及红冲的数据
     */
    private Boolean searchVerifyStatus;

    /**
     * 用于sql查询暂估订单, OrderType = 0,1
     */
    private Boolean searchTempOrderType;
    /**
     * 用于sql入账月份小于等于结算时间
     */
    private Boolean smallEqBillInDate;

    //月结月份最后一天 yyyy-mm-dd
    private String monthBillLastDate;

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
        this.orderTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.tocTempItemCostBill_orderTypeList, orderType);
    }

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*billId get 方法 */
    public Long getBillId(){
    return billId;
    }
    /*billId set 方法 */
    public void setBillId(Long  billId){
    this.billId=billId;
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
        this.storePlatformName = DERP.getLabelByKey(DERP.storePlatformList, storePlatformCode);
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
        this.settlementMarkLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.tocTempItemCostBill_settlementMarkList, settlementMark);
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

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }

    public String getMonthStart() {
        return monthStart;
    }

    public void setMonthStart(String monthStart) {
        this.monthStart = monthStart;
    }

    public String getMonthEnd() {
        return monthEnd;
    }

    public void setMonthEnd(String monthEnd) {
        this.monthEnd = monthEnd;
    }

    public String getBillIds() {
        return billIds;
    }

    public void setBillIds(String billIds) {
        this.billIds = billIds;
    }

    public String getStorePlatformName() {
        return storePlatformName;
    }

    public void setStorePlatformName(String storePlatformName) {
        this.storePlatformName = storePlatformName;
    }

    public String getSettlementMarkLabel() {
        return settlementMarkLabel;
    }

    public void setSettlementMarkLabel(String settlementMarkLabel) {
        this.settlementMarkLabel = settlementMarkLabel;
    }

    public String getOrderTypeLabel() {
        return orderTypeLabel;
    }

    public void setOrderTypeLabel(String orderTypeLabel) {
        this.orderTypeLabel = orderTypeLabel;
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

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
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

    public Boolean getCompareBillInDate() {
        return compareBillInDate;
    }

    public void setCompareBillInDate(Boolean compareBillInDate) {
        this.compareBillInDate = compareBillInDate;
    }

    public Boolean getSearchVerifyStatus() {
        return searchVerifyStatus;
    }

    public void setSearchVerifyStatus(Boolean searchVerifyStatus) {
        this.searchVerifyStatus = searchVerifyStatus;
    }

    public Boolean getSearchTempOrderType() {
        return searchTempOrderType;
    }

    public void setSearchTempOrderType(Boolean searchTempOrderType) {
        this.searchTempOrderType = searchTempOrderType;
    }

    public Boolean getSmallEqBillInDate() {
        return smallEqBillInDate;
    }

    public void setSmallEqBillInDate(Boolean smallEqBillInDate) {
        this.smallEqBillInDate = smallEqBillInDate;
    }

    public String getMonthBillLastDate() {
        return monthBillLastDate;
    }

    public void setMonthBillLastDate(String monthBillLastDate) {
        this.monthBillLastDate = monthBillLastDate;
    }
}
