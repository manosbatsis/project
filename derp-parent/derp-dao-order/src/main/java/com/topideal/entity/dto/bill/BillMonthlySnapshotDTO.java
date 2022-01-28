package com.topideal.entity.dto.bill;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class BillMonthlySnapshotDTO extends PageModel implements Serializable{

    /**
    * id主键
    */
    @ApiModelProperty(value = "id主键")
    private Long id;
    /**
    * 公司id
    */
    @ApiModelProperty(value = "公司id")
    private Long merchantId;
    /**
    * 公司名称
    */
    @ApiModelProperty(value = "公司名称")
    private String merchantName;
    /**
    * 电商平台编码
    */
    @ApiModelProperty(value = "电商平台编码")
    private String storePlatformCode;
    @ApiModelProperty(value = "电商平台名称")
    private String storePlatformName;
    /**
     * 运营类型 001:POP; 002:一件代发
     */
    @ApiModelProperty(value = "运营类型 001:POP; 002:一件代发")
    private String shopTypeCode;
    /**
    * 客户id
    */
    @ApiModelProperty(value = "客户id")
    private Long customerId;
    /**
    * 客户名称
    */
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    /**
    * 事业部id
    */
    @ApiModelProperty(value = "事业部id")
    private Long buId;
    /**
    * 事业部名称
    */
    @ApiModelProperty(value = "事业部名称")
    private String buName;
    /**
    * 月结月份
    */
    @ApiModelProperty(value = "月结月份")
    private String month;
    /**
    * 入账月份
    */
    @ApiModelProperty(value = "入账月份")
    private String creditMonth;
    /**
    * 应收单号
    */
    @ApiModelProperty(value = "应收单号")
    private String receiveCode;
    /**
    * 账单类型 0-tob 1-toc
    */
    @ApiModelProperty(value = "账单类型 0-tob 1-toc")
    private String billType;
    private String billTypeLabel;
    /**
    * 结算币种
    */
    @ApiModelProperty(value = "结算币种")
    private String currency;
    private String currencyLabel;
    /**
    * 开单日期
    */
    @ApiModelProperty(value = "开单日期")
    private Timestamp invoiceDate;
    /**
    * 应收金额
    */
    @ApiModelProperty(value = "应收金额")
    private BigDecimal receivableAmount;
    /**
    * 未核销金额
    */
    @ApiModelProperty(value = "未核销金额")
    private BigDecimal nonverifyAmount;
    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

    /**
     * 账单日期
     */
    @ApiModelProperty(value = "账单日期")
    private Timestamp billDate;

    @ApiModelProperty(value = "月结月份最后一天日期", hidden = true)
    private String lastDay;

    @ApiModelProperty(value = "日期区间", hidden = true)
    private String type;

    @ApiModelProperty(value = "当前登录用户绑定的事业部集合")
    private List<Long> buList;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
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
    /*storePlatformCode get 方法 */
    public String getStorePlatformCode(){
    return storePlatformCode;
    }
    /*storePlatformCode set 方法 */
    public void setStorePlatformCode(String  storePlatformCode){
    this.storePlatformCode=storePlatformCode;
    this.storePlatformName = DERP.getLabelByKey(DERP.storePlatformList, storePlatformCode);
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
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*creditMonth get 方法 */
    public String getCreditMonth(){
    return creditMonth;
    }
    /*creditMonth set 方法 */
    public void setCreditMonth(String  creditMonth){
    this.creditMonth=creditMonth;
    }
    /*receiveCode get 方法 */
    public String getReceiveCode(){
    return receiveCode;
    }
    /*receiveCode set 方法 */
    public void setReceiveCode(String  receiveCode){
    this.receiveCode=receiveCode;
    }
    /*billType get 方法 */
    public String getBillType(){
    return billType;
    }
    /*billType set 方法 */
    public void setBillType(String  billType){
    this.billType=billType;
        this.billTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.billMonthlySnapshot_billTypeList, billType);
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    this.currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency);
    }
    /*invoiceDate get 方法 */
    public Timestamp getInvoiceDate(){
    return invoiceDate;
    }
    /*invoiceDate set 方法 */
    public void setInvoiceDate(Timestamp  invoiceDate){
    this.invoiceDate=invoiceDate;
    }
    /*receivableAmount get 方法 */
    public BigDecimal getReceivableAmount(){
    return receivableAmount;
    }
    /*receivableAmount set 方法 */
    public void setReceivableAmount(BigDecimal  receivableAmount){
    this.receivableAmount=receivableAmount;
    }
    /*nonverifyAmount get 方法 */
    public BigDecimal getNonverifyAmount(){
    return nonverifyAmount;
    }
    /*nonverifyAmount set 方法 */
    public void setNonverifyAmount(BigDecimal  nonverifyAmount){
    this.nonverifyAmount=nonverifyAmount;
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

    public String getBillTypeLabel() {
        return billTypeLabel;
    }

    public void setBillTypeLabel(String billTypeLabel) {
        this.billTypeLabel = billTypeLabel;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Timestamp getBillDate() {
        return billDate;
    }

    public void setBillDate(Timestamp billDate) {
        this.billDate = billDate;
    }

    public String getShopTypeCode() {
        return shopTypeCode;
    }

    public void setShopTypeCode(String shopTypeCode) {
        this.shopTypeCode = shopTypeCode;
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

    public String getCurrencyLabel() {
        return currencyLabel;
    }

    public void setCurrencyLabel(String currencyLabel) {
        this.currencyLabel = currencyLabel;
    }

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }
}
