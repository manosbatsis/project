package com.topideal.entity.vo.bill;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class TocSettlementReceiveBillModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 平台结算单号
    */
    private String code;
    /**
    * 运营类型 001:POP; 002:一件代发
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
    /**
    * 事业部id
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 结算日期
    */
    private Date settlementDate;
    /**
    * 结算币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    */
    private String settlementCurrency;
    /**
    * 账单状态 00-待提交 01-待审核 02-待核销 03-部分核销 04-已核销 05-作废待审 06-已作废 07-生成中 08-生成失败
    */
    private String billStatus;
    /**
    * 创建人ID
    */
    private Long createrId;
    /**
    * 创建人
    */
    private String creater;
    /**
    * NC状态 1-待审核 2-待入erp 3-待入账 4-已入账 5-已关账 6-待作废 7-待红冲 8-已作废 9-已红冲 10-未同步 11-已同步
    */
    private String ncStatus;
    /**
    * NC同步时间
    */
    private Timestamp ncSynDate;
    /**
    * NC单据号
    */
    private String ncCode;
    /**
    * 凭证编号
    */
    private String voucherCode;
    /**
    * 凭证名称
    */
    private String voucherName;
    /**
    * 凭证序号
    */
    private String voucherIndex;
    /**
    * 凭证状态：1-正常，0-作废
    */
    private String voucherStatus;
    /**
    * 会计期间
    */
    private String period;
    /**
    * 生成失败错误信息文件地址
    */
    private String errorMsgPath;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     * 应收金额
     */
    private BigDecimal receivableAmount;
    /**
     * 应收金额(原币)
     */
    private BigDecimal oriReceivableAmount;
    /**
     * 入账日期
     */
    private Date billInDate;
    //导入文件保存地址
    private String filePath;
    /**
     * 发票状态 00-待开票  01-待签章  02-已作废 03-已签章
     */
    private String invoiceStatus;
    /**
     * 发票关联id
     */
    private Long invoiceId;
    /**
     * 发票号
     */
    private String invoiceNo;
    /**
     * 外部结算单号
     */
    private String externalCode;
    /**
     * 平台结算原币 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
     */
    private String oriCurrency;

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
    /*settlementDate get 方法 */
    public Date getSettlementDate(){
    return settlementDate;
    }
    /*settlementDate set 方法 */
    public void setSettlementDate(Date  settlementDate){
    this.settlementDate=settlementDate;
    }
    /*settlementCurrency get 方法 */
    public String getSettlementCurrency(){
    return settlementCurrency;
    }
    /*settlementCurrency set 方法 */
    public void setSettlementCurrency(String  settlementCurrency){
    this.settlementCurrency=settlementCurrency;
    }
    /*billStatus get 方法 */
    public String getBillStatus(){
    return billStatus;
    }
    /*billStatus set 方法 */
    public void setBillStatus(String  billStatus){
    this.billStatus=billStatus;
    }
    /*createrId get 方法 */
    public Long getCreaterId(){
    return createrId;
    }
    /*createrId set 方法 */
    public void setCreaterId(Long  createrId){
    this.createrId=createrId;
    }
    /*creater get 方法 */
    public String getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(String  creater){
    this.creater=creater;
    }
    /*ncStatus get 方法 */
    public String getNcStatus(){
    return ncStatus;
    }
    /*ncStatus set 方法 */
    public void setNcStatus(String  ncStatus){
    this.ncStatus=ncStatus;
    }
    /*ncSynDate get 方法 */
    public Timestamp getNcSynDate(){
    return ncSynDate;
    }
    /*ncSynDate set 方法 */
    public void setNcSynDate(Timestamp  ncSynDate){
    this.ncSynDate=ncSynDate;
    }
    /*ncCode get 方法 */
    public String getNcCode(){
    return ncCode;
    }
    /*ncCode set 方法 */
    public void setNcCode(String  ncCode){
    this.ncCode=ncCode;
    }
    /*voucherCode get 方法 */
    public String getVoucherCode(){
    return voucherCode;
    }
    /*voucherCode set 方法 */
    public void setVoucherCode(String  voucherCode){
    this.voucherCode=voucherCode;
    }
    /*voucherName get 方法 */
    public String getVoucherName(){
    return voucherName;
    }
    /*voucherName set 方法 */
    public void setVoucherName(String  voucherName){
    this.voucherName=voucherName;
    }
    /*voucherIndex get 方法 */
    public String getVoucherIndex(){
    return voucherIndex;
    }
    /*voucherIndex set 方法 */
    public void setVoucherIndex(String  voucherIndex){
    this.voucherIndex=voucherIndex;
    }
    /*voucherStatus get 方法 */
    public String getVoucherStatus(){
    return voucherStatus;
    }
    /*voucherStatus set 方法 */
    public void setVoucherStatus(String  voucherStatus){
    this.voucherStatus=voucherStatus;
    }
    /*period get 方法 */
    public String getPeriod(){
    return period;
    }
    /*period set 方法 */
    public void setPeriod(String  period){
    this.period=period;
    }
    /*errorMsgPath get 方法 */
    public String getErrorMsgPath(){
    return errorMsgPath;
    }
    /*errorMsgPath set 方法 */
    public void setErrorMsgPath(String  errorMsgPath){
    this.errorMsgPath=errorMsgPath;
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

    public BigDecimal getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(BigDecimal receivableAmount) {
        this.receivableAmount = receivableAmount;
    }
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getOriCurrency() {
        return oriCurrency;
    }

    public void setOriCurrency(String oriCurrency) {
        this.oriCurrency = oriCurrency;
    }

    public BigDecimal getOriReceivableAmount() {
        return oriReceivableAmount;
    }

    public void setOriReceivableAmount(BigDecimal oriReceivableAmount) {
        this.oriReceivableAmount = oriReceivableAmount;
    }

    public Date getBillInDate() {
        return billInDate;
    }

    public void setBillInDate(Date billInDate) {
        this.billInDate = billInDate;
    }
}
