package com.topideal.entity.dto.bill;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class TocTemporaryCostBillDTO extends PageModel implements Serializable{

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "店铺类型值编码 001:POP; 002:一件代发")
    private String shopTypeCode;
    private String shopTypeName;

    @ApiModelProperty(value = "归属月份")
    private String month;

    @ApiModelProperty(value = "公司ID")
    private Long merchantId;

    @ApiModelProperty(value = "公司名称")
    private String merchantName;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "店铺编码")
    private String shopCode;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "电商平台编码")
    private String storePlatformCode;
    @ApiModelProperty(value = "电商平台名称")
    private String storePlatformName;

    @ApiModelProperty(value = "结算完成月份")
    private String settlementEndMonth;

    @ApiModelProperty(value = "结算状态")
    private String settlementStatus;
    @ApiModelProperty(value = "结算状态中文")
    private String settlementStatusLabel;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    @ApiModelProperty(value = "总暂估应收")
    private BigDecimal totalReceiveAmount;

    @ApiModelProperty(value = "总订单数")
    private Long totalReceiveNum;
    //已结算订单金额
    @ApiModelProperty(value = "已结算订单金额")
    private BigDecimal alreadyReceiveAmount;
    //已结算订单数
    @ApiModelProperty(value = "已结算订单数")
    private Long alreadyReceiveNum;
    //币种
    @ApiModelProperty(value = "币种")
    private String currency;
    //剩余结算金额
    @ApiModelProperty(value = "剩余结算金额")
    private BigDecimal lastReceiveAmount;

    //剩余结算单数
    @ApiModelProperty(value = "剩余结算金额")
    private Long lastReceiveNum;

    //起始账单月份
    @ApiModelProperty(value = "起始账单月份")
    private String monthStart;
    //结束账单月份
    @ApiModelProperty(value = "结束账单月份")
    private String monthEnd;
    //导出ids
    @ApiModelProperty(value = "导出ids")
    private String ids;

    @ApiModelProperty(value = "事业部id")
    private Long buId;

    @ApiModelProperty(value = "事业部名称")
    private String buName;

    @ApiModelProperty(value = "母品牌")
    private String parentBrandName;

    private BigDecimal amount;//应收账龄

    // 当前登录用户绑定的事业部集合
    @ApiModelProperty(value = "当前登录用户绑定的事业部集合", hidden = true)
    private List<Long> buList;
    /**
     * 差异调整金额(rmb)
     */
    @ApiModelProperty(value = "差异调整金额(rmb)")
    private BigDecimal adjustmentRmbAmount;

    /**
     * 平台费项ID
     */
    private Long platformProjectId;

    /**
     * 平台费项名称
     * @return
     */
    private String platformProjectName;
    
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*shopTypeCode get 方法 */
    public String getShopTypeCode(){
    return shopTypeCode;
    }
    /*shopTypeCode set 方法 */
    public void setShopTypeCode(String  shopTypeCode){
    this.shopTypeCode=shopTypeCode;
        this.shopTypeName = DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, shopTypeCode);
    }
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
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
    /*settlementEndMonth get 方法 */
    public String getSettlementEndMonth(){
    return settlementEndMonth;
    }
    /*settlementEndMonth set 方法 */
    public void setSettlementEndMonth(String  settlementEndMonth){
    this.settlementEndMonth=settlementEndMonth;
    }
    /*settlementStatus get 方法 */
    public String getSettlementStatus(){
    return settlementStatus;
    }
    /*settlementStatus set 方法 */
    public void setSettlementStatus(String  settlementStatus){
    this.settlementStatus=settlementStatus;
        this.settlementStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.toctempbill_settlementStatusList, settlementStatus);
    }
    /*totalReceiveAmount get 方法 */
    public BigDecimal getTotalReceiveAmount(){
    return totalReceiveAmount;
    }
    /*totalReceiveAmount set 方法 */
    public void setTotalReceiveAmount(BigDecimal  totalReceiveAmount){
    this.totalReceiveAmount=totalReceiveAmount;
    }
    /*totalReceiveNum get 方法 */
    public Long getTotalReceiveNum(){
    return totalReceiveNum;
    }
    /*totalReceiveNum set 方法 */
    public void setTotalReceiveNum(Long  totalReceiveNum){
    this.totalReceiveNum=totalReceiveNum;
    }
    /*alreadyReceiveAmount get 方法 */
    public BigDecimal getAlreadyReceiveAmount(){
    return alreadyReceiveAmount;
    }
    /*alreadyReceiveAmount set 方法 */
    public void setAlreadyReceiveAmount(BigDecimal  alreadyReceiveAmount){
    this.alreadyReceiveAmount=alreadyReceiveAmount;
    }
    /*alreadyReceiveNum get 方法 */
    public Long getAlreadyReceiveNum(){
    return alreadyReceiveNum;
    }
    /*alreadyReceiveNum set 方法 */
    public void setAlreadyReceiveNum(Long  alreadyReceiveNum){
    this.alreadyReceiveNum=alreadyReceiveNum;
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

    public String getShopTypeName() {
        return shopTypeName;
    }

    public void setShopTypeName(String shopTypeName) {
        this.shopTypeName = shopTypeName;
    }

    public String getStorePlatformName() {
        return storePlatformName;
    }

    public void setStorePlatformName(String storePlatformName) {
        this.storePlatformName = storePlatformName;
    }

    public String getSettlementStatusLabel() {
        return settlementStatusLabel;
    }

    public void setSettlementStatusLabel(String settlementStatusLabel) {
        this.settlementStatusLabel = settlementStatusLabel;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getLastReceiveAmount() {
        return lastReceiveAmount;
    }

    public void setLastReceiveAmount(BigDecimal lastReceiveAmount) {
        this.lastReceiveAmount = lastReceiveAmount;
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

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getParentBrandName() {
        return parentBrandName;
    }

    public void setParentBrandName(String parentBrandName) {
        this.parentBrandName = parentBrandName;
    }

    public Long getLastReceiveNum() {
        return lastReceiveNum;
    }

    public void setLastReceiveNum(Long lastReceiveNum) {
        this.lastReceiveNum = lastReceiveNum;
    }

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }

	public BigDecimal getAdjustmentRmbAmount() {
		return adjustmentRmbAmount;
	}

	public void setAdjustmentRmbAmount(BigDecimal adjustmentRmbAmount) {
		this.adjustmentRmbAmount = adjustmentRmbAmount;
	}

    public Long getPlatformProjectId() {
        return platformProjectId;
    }

    public void setPlatformProjectId(Long platformProjectId) {
        this.platformProjectId = platformProjectId;
    }

    public String getPlatformProjectName() {
        return platformProjectName;
    }

    public void setPlatformProjectName(String platformProjectName) {
        this.platformProjectName = platformProjectName;
    }
}
