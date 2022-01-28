package com.topideal.entity.vo.bill;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class AdvanceBillModel extends PageModel implements Serializable{

    /**
    * id主键
    */
    private Long id;
    /**
    * 预收账单号
    */
    private String code;
    /**
    * 商家id
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
    * 事业部id
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 结算币种
    */
    private String currency;
    /**
    * 账单日期
    */
    private Timestamp billDate;
    /**
    * 账单状态 00-待提交 01-待审核 02-待核销 03-部分核销 04-已核销 05-待作废 06-已作废
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
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * po单号
    */
    private String poNo;

    /**
     * 单据类型 4-销售订单
     */
    private String orderType;
    /**
    * NC渠道编码
    */
    private String ncChannelCode;
    /**
    * NC平台编码
    */
    private String ncPlatformCode;
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
    * 同步操作人id
    */
    private Long synchronizerId;
    /**
    * 同步操作人
    */
    private String synchronizer;
    /**
     * 开票状态 00-待开票  01-待签章  02-已作废 03-已签章
     */
    private String invoiceStatus;
    /**
     * 发票关联id
     */
    private Long invoiceId;
    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
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
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*billDate get 方法 */
    public Timestamp getBillDate(){
    return billDate;
    }
    /*billDate set 方法 */
    public void setBillDate(Timestamp  billDate){
    this.billDate=billDate;
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
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*ncChannelCode get 方法 */
    public String getNcChannelCode(){
    return ncChannelCode;
    }
    /*ncChannelCode set 方法 */
    public void setNcChannelCode(String  ncChannelCode){
    this.ncChannelCode=ncChannelCode;
    }
    /*ncPlatformCode get 方法 */
    public String getNcPlatformCode(){
    return ncPlatformCode;
    }
    /*ncPlatformCode set 方法 */
    public void setNcPlatformCode(String  ncPlatformCode){
    this.ncPlatformCode=ncPlatformCode;
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
    /*synchronizerId get 方法 */
    public Long getSynchronizerId(){
    return synchronizerId;
    }
    /*synchronizerId set 方法 */
    public void setSynchronizerId(Long  synchronizerId){
    this.synchronizerId=synchronizerId;
    }
    /*synchronizer get 方法 */
    public String getSynchronizer(){
    return synchronizer;
    }
    /*synchronizer set 方法 */
    public void setSynchronizer(String  synchronizer){
    this.synchronizer=synchronizer;
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
}
