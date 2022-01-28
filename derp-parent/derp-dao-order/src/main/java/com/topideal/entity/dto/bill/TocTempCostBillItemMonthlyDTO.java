package com.topideal.entity.dto.bill;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/17 11:03
 * @Description: Toc暂估收入月结账单
 */
public class TocTempCostBillItemMonthlyDTO extends PageModel implements Serializable {

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
    private String shopTypeName;
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
    private String storePlatformName;
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
    private String settlementMarkLabel;
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
    private String orderTypeLabel;

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

    //剩余结算金额
    @ApiModelProperty(value = "剩余结算金额")
    private BigDecimal lastReceiveAmount;

    //入账日期
    private Date billInDate;

    /**
     * 暂估月份
     */
    private String tempMonth;

    @ApiModelProperty(value = "月结月份最后一天日期", hidden = true)
    private String lastDay;

    @ApiModelProperty(value = "日期区间", hidden = true)
    private String type;

    //起始账单月份
    @ApiModelProperty(value = "起始账单月份", required = false)
    private String monthStart;
    //结束账单月份
    @ApiModelProperty(value = "结束账单月份", required = false)
    private String monthEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getShopTypeCode() {
        return shopTypeCode;
    }

    public void setShopTypeCode(String shopTypeCode) {
        this.shopTypeCode = shopTypeCode;
        this.shopTypeName = DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, shopTypeCode);
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStorePlatformCode() {
        return storePlatformCode;
    }

    public void setStorePlatformCode(String storePlatformCode) {
        this.storePlatformCode = storePlatformCode;
        this.storePlatformName = DERP.getLabelByKey(DERP.storePlatformList, storePlatformCode);
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public String getTemporaryCurrency() {
        return temporaryCurrency;
    }

    public void setTemporaryCurrency(String temporaryCurrency) {
        this.temporaryCurrency = temporaryCurrency;
    }

    public BigDecimal getTemporaryRmbCost() {
        return temporaryRmbCost;
    }

    public void setTemporaryRmbCost(BigDecimal temporaryRmbCost) {
        this.temporaryRmbCost = temporaryRmbCost;
    }

    public BigDecimal getSettlementOriCost() {
        return settlementOriCost;
    }

    public void setSettlementOriCost(BigDecimal settlementOriCost) {
        this.settlementOriCost = settlementOriCost;
    }

    public BigDecimal getSettlementRmbCost() {
        return settlementRmbCost;
    }

    public void setSettlementRmbCost(BigDecimal settlementRmbCost) {
        this.settlementRmbCost = settlementRmbCost;
    }

    public String getOriginalCurrency() {
        return originalCurrency;
    }

    public void setOriginalCurrency(String originalCurrency) {
        this.originalCurrency = originalCurrency;
    }

    public Timestamp getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Timestamp settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getSettlementCode() {
        return settlementCode;
    }

    public void setSettlementCode(String settlementCode) {
        this.settlementCode = settlementCode;
    }

    public String getSettlementMark() {
        return settlementMark;
    }

    public void setSettlementMark(String settlementMark) {
        this.settlementMark = settlementMark;
        this.settlementMarkLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.tocTempItemCostBill_settlementMarkList, settlementMark);
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public BigDecimal getWriteOffAmount() {
        return writeOffAmount;
    }

    public void setWriteOffAmount(BigDecimal writeOffAmount) {
        this.writeOffAmount = writeOffAmount;
    }

    public BigDecimal getNonVerifyAmount() {
        return nonVerifyAmount;
    }

    public void setNonVerifyAmount(BigDecimal nonVerifyAmount) {
        this.nonVerifyAmount = nonVerifyAmount;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
        this.orderTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.tocTempItemCostBill_orderTypeList, orderType);
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

    public String getLastDay() {
        return lastDay;
    }

    public void setLastDay(String lastDay) {
        this.lastDay = lastDay;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getShopTypeName() {
        return shopTypeName;
    }

    public void setShopTypeName(String shopTypeName) {
        this.shopTypeName = shopTypeName;
    }
}
