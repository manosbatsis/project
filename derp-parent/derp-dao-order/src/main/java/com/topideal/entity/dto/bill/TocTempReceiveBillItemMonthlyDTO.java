package com.topideal.entity.dto.bill;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/17 11:03
 * @Description: Toc暂估收入月结账单
 */
public class TocTempReceiveBillItemMonthlyDTO extends PageModel implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * 归属月份 YYYY-MM
     */
    private String month;
    /**
     * 系统订单号
     */
    private String orderCode;
    /**
     * 外部订单号
     */
    private String externalCode;
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
     * 电商平台编码：第e仓：100011111；京东自营：100044998；京东POP-拜耳：10002075；菜鸟：1000000310；拼多多：1000004790
     */
    private String storePlatformCode;
    private String storePlatformName;
    /**
     * 销售数量
     */
    private Integer saleNum;
    /**
     * 订单原币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
     */
    private String temporaryCurrency;
    private String temporaryCurrencyLabel;
    /**
     * 暂估应收金额（RMB）
     */
    private BigDecimal temporaryRmbAmount;
    /**
     * 冲销金额
     */
    private BigDecimal writeOffAmount;
    /**
     * 平台结算货款（原币）
     */
    private BigDecimal settlementOriAmount;
    /**
     * 平台结算金额（RMB）
     */
    private BigDecimal settlementRmbAmount;
    /**
     * 订单原币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
     */
    private String originalCurrency;
    private String originalCurrencyLabel;
    /**
     * 结算标识：0-未核销 1-已红冲 2-已核销
     */
    private String settlementMark;
    private String settlementMarkLabel;
    /**
     * 结算日期
     */
    private Timestamp settlementDate;
    /**
     * 结算单号
     */
    private String settlementCode;
    /**
     * 创建时间
     */
    private Timestamp createDate;
    /**
     * 修改时间
     */
    private Timestamp modifyDate;
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

    private String tempMonth;

    //剩余结算金额
    private BigDecimal lastReceiveAmount;

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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public String getTemporaryCurrency() {
        return temporaryCurrency;
    }

    public void setTemporaryCurrency(String temporaryCurrency) {
        this.temporaryCurrency = temporaryCurrency;
        this.temporaryCurrencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, temporaryCurrency);
    }

    public BigDecimal getTemporaryRmbAmount() {
        return temporaryRmbAmount;
    }

    public void setTemporaryRmbAmount(BigDecimal temporaryRmbAmount) {
        this.temporaryRmbAmount = temporaryRmbAmount;
    }

    public BigDecimal getWriteOffAmount() {
        return writeOffAmount;
    }

    public void setWriteOffAmount(BigDecimal writeOffAmount) {
        this.writeOffAmount = writeOffAmount;
    }

    public BigDecimal getSettlementOriAmount() {
        return settlementOriAmount;
    }

    public void setSettlementOriAmount(BigDecimal settlementOriAmount) {
        this.settlementOriAmount = settlementOriAmount;
    }

    public BigDecimal getSettlementRmbAmount() {
        return settlementRmbAmount;
    }

    public void setSettlementRmbAmount(BigDecimal settlementRmbAmount) {
        this.settlementRmbAmount = settlementRmbAmount;
    }

    public String getOriginalCurrency() {
        return originalCurrency;
    }

    public void setOriginalCurrency(String originalCurrency) {
        this.originalCurrency = originalCurrency;
        this.originalCurrencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, originalCurrency);
    }

    public String getSettlementMark() {
        return settlementMark;
    }

    public void setSettlementMark(String settlementMark) {
        this.settlementMark = settlementMark;
        this.settlementMarkLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.tocTempItemBill_settlementMarkList, settlementMark);
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
        this.orderTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.tocTempItemBill_orderTypeList, orderType);
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

    public String getOrderTypeLabel() {
        return orderTypeLabel;
    }

    public void setOrderTypeLabel(String orderTypeLabel) {
        this.orderTypeLabel = orderTypeLabel;
    }

    public String getTemporaryCurrencyLabel() {
        return temporaryCurrencyLabel;
    }

    public void setTemporaryCurrencyLabel(String temporaryCurrencyLabel) {
        this.temporaryCurrencyLabel = temporaryCurrencyLabel;
    }

    public String getOriginalCurrencyLabel() {
        return originalCurrencyLabel;
    }

    public void setOriginalCurrencyLabel(String originalCurrencyLabel) {
        this.originalCurrencyLabel = originalCurrencyLabel;
    }

}
