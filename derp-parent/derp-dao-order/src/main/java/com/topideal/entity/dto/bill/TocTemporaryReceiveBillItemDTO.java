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
import java.util.List;

public class TocTemporaryReceiveBillItemDTO extends PageModel implements Serializable{

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
    @ApiModelProperty(value = "归属月份YYYY-MM", required = false)
    private String month;
    /**
     * 店铺类型值编码 001:POP; 002:一件代发
     */
    @ApiModelProperty(value = "店铺类型值编码", required = false)
    private String shopTypeCode;
    @ApiModelProperty(value = "店铺类型值名称", required = false)
    private String shopTypeName;

    @ApiModelProperty(value = "公司ID", required = false)
    private Long merchantId;

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
     * 电商平台编码：第e仓：100011111；京东自营：100044998；京东POP-拜耳：10002075；菜鸟：1000000310；拼多多：1000004790
     */
    @ApiModelProperty(value = "电商平台编码：第e仓：100011111；京东自营：100044998；京东POP-拜耳：10002075；菜鸟：1000000310；拼多多：1000004790", required = false)
    private String storePlatformCode;
    @ApiModelProperty(value = "电商平台名称", required = false)
    private String storePlatformName;
    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID", required = false)
    private Long goodsId;
    /**
     * 商品货号
     */
    @ApiModelProperty(value = "商品货号", required = false)
    private String goodsNo;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", required = false)
    private String goodsName;
    /**
     * 销售数量
     */
    @ApiModelProperty(value = "销售数量", required = false)
    private Integer saleNum;
    /**
     * 订单原币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
     */
    @ApiModelProperty(value = "订单原币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑", required = false)
    private String temporaryCurrency;
    private String temporaryCurrencyLabel;
    /**
     * 暂估应收金额（RMB）
     */
    @ApiModelProperty(value = "暂估应收金额（RMB）", required = false)
    private BigDecimal temporaryRmbAmount;
    /**
     * 平台结算货款（原币）
     */
    @ApiModelProperty(value = "平台结算货款（原币）", required = false)
    private BigDecimal settlementOriAmount;
    /**
     * 平台结算金额（RMB）
     */
    @ApiModelProperty(value = "平台结算金额（RMB）", required = false)
    private BigDecimal settlementRmbAmount;
    /**
     * 订单原币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
     */
    @ApiModelProperty(value = "订单原币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑", required = false)
    private String originalCurrency;
    private String originalCurrencyLabel;
    /**
     * 结算标识：1-已结算 0-未结算
     */
    @ApiModelProperty(value = "结算标识：0-未核销 1-已红冲 2-已核销", required = false)
    private String settlementMark;
    private String settlementMarkLabel;
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

    /**
     * 冲销金额
     */
    @ApiModelProperty(value = "冲销金额")
    private BigDecimal writeOffAmount;
    @ApiModelProperty(value = "未核销金额")
    private BigDecimal nonVerifyAmount;
    /**
     * 单据类型：0-发货订单 1-退款订单
     */
    @ApiModelProperty(value = "单据类型：0-发货订单 1-退款订单 2-差异调整单 3-差异调整单")
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
     * 用于sql查询入账时间小于等于结算时间
     */
    private Boolean smallEqBillInDate;

    //月结月份最后一天 yyyy-mm-dd
    private String monthBillLastDate;

    @ApiModelProperty(value = "结算明细ID")
    private String settlementItemId;

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
        this.orderTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.tocTempItemBill_orderTypeList, orderType);
    }

    public BigDecimal getNonVerifyAmount() {
        return nonVerifyAmount;
    }

    public void setNonVerifyAmount(BigDecimal nonVerifyAmount) {
        this.nonVerifyAmount = nonVerifyAmount;
    }

    public String getOrderTypeLabel() {
        return orderTypeLabel;
    }

    public void setOrderTypeLabel(String orderTypeLabel) {
        this.orderTypeLabel = orderTypeLabel;
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
        this.shopTypeName = DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, shopTypeCode);
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
    /*goodsId get 方法 */
    public Long getGoodsId(){
        return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
        this.goodsId=goodsId;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
        return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
        this.goodsNo=goodsNo;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
        return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
        this.goodsName=goodsName;
    }
    /*saleNum get 方法 */
    public Integer getSaleNum(){
        return saleNum;
    }
    /*saleNum set 方法 */
    public void setSaleNum(Integer  saleNum){
        this.saleNum=saleNum;
    }
    /*temporaryCurrency get 方法 */
    public String getTemporaryCurrency(){
        return temporaryCurrency;
    }
    /*temporaryCurrency set 方法 */
    public void setTemporaryCurrency(String  temporaryCurrency){
        this.temporaryCurrency=temporaryCurrency;
        this.temporaryCurrencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, temporaryCurrency);
    }
    /*temporaryRmbAmount get 方法 */
    public BigDecimal getTemporaryRmbAmount(){
        return temporaryRmbAmount;
    }
    /*temporaryRmbAmount set 方法 */
    public void setTemporaryRmbAmount(BigDecimal  temporaryRmbAmount){
        this.temporaryRmbAmount=temporaryRmbAmount;
    }
    /*settlementOriAmount get 方法 */
    public BigDecimal getSettlementOriAmount(){
        return settlementOriAmount;
    }
    /*settlementOriAmount set 方法 */
    public void setSettlementOriAmount(BigDecimal  settlementOriAmount){
        this.settlementOriAmount=settlementOriAmount;
    }
    /*settlementRmbAmount get 方法 */
    public BigDecimal getSettlementRmbAmount(){
        return settlementRmbAmount;
    }
    /*settlementRmbAmount set 方法 */
    public void setSettlementRmbAmount(BigDecimal  settlementRmbAmount){
        this.settlementRmbAmount=settlementRmbAmount;
    }
    /*originalCurrency get 方法 */
    public String getOriginalCurrency(){
        return originalCurrency;
    }
    /*originalCurrency set 方法 */
    public void setOriginalCurrency(String  originalCurrency){
        this.originalCurrency=originalCurrency;
        this.originalCurrencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, originalCurrency);
    }
    /*settlementMark get 方法 */
    public String getSettlementMark(){
        return settlementMark;
    }
    /*settlementMark set 方法 */
    public void setSettlementMark(String  settlementMark){
        this.settlementMark=settlementMark;
        this.settlementMarkLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.tocTempItemBill_settlementMarkList, settlementMark);
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

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getSettlementMarkLabel() {
        return settlementMarkLabel;
    }

    public void setSettlementMarkLabel(String settlementMarkLabel) {
        this.settlementMarkLabel = settlementMarkLabel;
    }

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
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

    public String getSettlementItemId() {
        return settlementItemId;
    }

    public void setSettlementItemId(String settlementItemId) {
        this.settlementItemId = settlementItemId;
    }
}
