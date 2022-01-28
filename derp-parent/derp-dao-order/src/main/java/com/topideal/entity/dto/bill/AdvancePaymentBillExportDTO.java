package com.topideal.entity.dto.bill;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class AdvancePaymentBillExportDTO extends PageModel implements Serializable{

    /**
    * id主键
    */
    @ApiModelProperty("id主键")
    private Long id;
    /**
    * 预付账单号
    */
    @ApiModelProperty("预付账单号")
    private String code;
    /**
    * 商家id
    */
    @ApiModelProperty("商家id")
    private Long merchantId;
    /**
    * 商家名称
    */
    @ApiModelProperty("商家名称")
    private String merchantName;
    /**
    * 供应商id
    */
    @ApiModelProperty("供应商id")
    private Long supplierId;
    /**
    * 供应商名称
    */
    @ApiModelProperty("供应商名称")
    private String supplierName;
    /**
    * 供应商银行账号
    */
    @ApiModelProperty("供应商银行账号")
    private String bankAccount;
    /**
    * 供应商银行账户
    */
    @ApiModelProperty("供应商银行账户")
    private String beneficiaryName;
    /**
    * 供应商开户银行
    */
    @ApiModelProperty("供应商开户银行")
    private String depositBank;
    /**
    * 供应商Swift Code
    */
    @ApiModelProperty("供应商Swift Code")
    private String swiftCode;
    /**
    * 供应商开户行地址
    */
    @ApiModelProperty("供应商开户行地址")
    private String bankAddress;
    /**
    * 事业部名称
    */
    @ApiModelProperty("事业部名称")
    private String buName;
    @ApiModelProperty("事业部Id")
    private Long buId;
    //页面查询用户事业部关联ID
    @ApiModelProperty(hidden=true)
    private List<Long> buIds ;
    /**
    * 结算币种
    */
    @ApiModelProperty("结算币种")
    private String currency;
    /**
    * 账单日期
    */
    @ApiModelProperty("账单日期")
    private Timestamp billDate;
    /**
    * 预计付款日期
    */
    @ApiModelProperty("预计付款日期")
    private Timestamp expectedPaymentDate;
    /**
    * 请款原因
    */
    @ApiModelProperty("请款原因")
    private String paymentReason;
    /**
    * 采购金额
    */
    @ApiModelProperty("采购金额")
    private BigDecimal purchaseAmount;
    /**
    * 预付总额
    */
    @ApiModelProperty("预付总额")
    private BigDecimal prepaymentAmount;
    /**
     * 预付总额
     */
     @ApiModelProperty("预付总额大写")
     private String prepaymentAmountStr;
    /**
    * 已付总额
    */
    @ApiModelProperty("预付总额")
    private BigDecimal paymentAmount;
    /**
    * 待核销金额
    */
    @ApiModelProperty("待核销金额")
    private BigDecimal verificationAmount;
    /**
    * 账单状态 00-待提交、01-审核中、02-已驳回、03-待付款、04-待作废、05-已作废、06-待核销、07已核销
    */
    @ApiModelProperty("账单状态 00-待提交、01-审核中、02-已驳回、03-待付款、04-待作废、05-已作废、06-待核销、07已核销")
    private String billStatus;
    @ApiModelProperty("账单状态中文 00-待提交、01-审核中、02-已驳回、03-待付款、04-待作废、05-已作废、06-待核销、07已核销")
    private String billStatusLabel;
    /**
    * 审批方式 1-OA审批 2-经分销审批
    */
    @ApiModelProperty("审批方式 1-OA审批 2-经分销审批")
    private String auditMethod;
    @ApiModelProperty("审批方式 1-OA审批 2-经分销审批")
    private String auditMethodLabel;
    /**
     * 创建人ID
     */
    @ApiModelProperty("创建人ID")
    private Long createrId;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String creater;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Timestamp createDate;
    /**
     * 修改人ID
     */
    @ApiModelProperty("修改人ID")
    private Long modifierId;
    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String modifier;
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Timestamp modifyDate;
    /**
    * NC同步时间
    */
    @ApiModelProperty("NC同步时间")
    private Timestamp ncSynDate;
    /**
    * NC同步状态
    */
    @ApiModelProperty("NC同步状态")
    private String ncSynStatus;
    /**
    * NC单据号
    */
    @ApiModelProperty("NC单据号")
    private String ncCode;
    /**
    * 凭证编号
    */
    @ApiModelProperty("凭证编号")
    private String voucherCode;
    /**
    * 凭证名称
    */
    @ApiModelProperty("凭证名称")
    private String voucherName;
    /**
    * 凭证序号
    */
    @ApiModelProperty("凭证序号")
    private String voucherIndex;
    /**
    * 凭证状态：1-正常，0-作废
    */
    @ApiModelProperty("凭证状态：1-正常，0-作废")
    private String voucherStatus;
    /**
    * 会计期间
    */
    @ApiModelProperty("会计期间")
    private String period;
    /**
    * 同步操作人id
    */
    @ApiModelProperty("同步操作人id")
    private Long synchronizerId;
    /**
    * 同步操作人
    */
    @ApiModelProperty("同步操作人")
    private String synchronizer;

    @ApiModelProperty(value = "预计付款日期开始", required = false)
    private String expectedPaymentDateStart;

    @ApiModelProperty(value = "预计付款日期结束", required = false)
    private String expectedPaymentDateEnd;

    @ApiModelProperty(value = "付款明细", required = false)
    List<AdvancePaymentBillItemExportDTO> itemList ;
    @ApiModelProperty(value = "付款记录明细", required = false)
    List<AdvancePaymentRecordItemExportDTO> recordItemList ;
    
    @ApiModelProperty(value = "预付单导出ID，多个以','隔开", required = false)
    private String ids;

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
    /*supplierId get 方法 */
    public Long getSupplierId(){
    return supplierId;
    }
    /*supplierId set 方法 */
    public void setSupplierId(Long  supplierId){
    this.supplierId=supplierId;
    }
    /*supplierName get 方法 */
    public String getSupplierName(){
    return supplierName;
    }
    /*supplierName set 方法 */
    public void setSupplierName(String  supplierName){
    this.supplierName=supplierName;
    }
    /*bankAccount get 方法 */
    public String getBankAccount(){
    return bankAccount;
    }
    /*bankAccount set 方法 */
    public void setBankAccount(String  bankAccount){
    this.bankAccount=bankAccount;
    }
    /*beneficiaryName get 方法 */
    public String getBeneficiaryName(){
    return beneficiaryName;
    }
    /*beneficiaryName set 方法 */
    public void setBeneficiaryName(String  beneficiaryName){
    this.beneficiaryName=beneficiaryName;
    }
    /*depositBank get 方法 */
    public String getDepositBank(){
    return depositBank;
    }
    /*depositBank set 方法 */
    public void setDepositBank(String  depositBank){
    this.depositBank=depositBank;
    }
    /*swiftCode get 方法 */
    public String getSwiftCode(){
    return swiftCode;
    }
    /*swiftCode set 方法 */
    public void setSwiftCode(String  swiftCode){
    this.swiftCode=swiftCode;
    }
    /*bankAddress get 方法 */
    public String getBankAddress(){
    return bankAddress;
    }
    /*bankAddress set 方法 */
    public void setBankAddress(String  bankAddress){
    this.bankAddress=bankAddress;
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
    /*expectedPaymentDate get 方法 */
    public Timestamp getExpectedPaymentDate(){
    return expectedPaymentDate;
    }
    /*expectedPaymentDate set 方法 */
    public void setExpectedPaymentDate(Timestamp  expectedPaymentDate){
    this.expectedPaymentDate=expectedPaymentDate;
    }
    /*paymentReason get 方法 */
    public String getPaymentReason(){
    return paymentReason;
    }
    /*paymentReason set 方法 */
    public void setPaymentReason(String  paymentReason){
    this.paymentReason=paymentReason;
    }
    /*purchaseAmount get 方法 */
    public BigDecimal getPurchaseAmount(){
    return purchaseAmount;
    }
    /*purchaseAmount set 方法 */
    public void setPurchaseAmount(BigDecimal  purchaseAmount){
    this.purchaseAmount=purchaseAmount;
    }
    /*prepaymentAmount get 方法 */
    public BigDecimal getPrepaymentAmount(){
    return prepaymentAmount;
    }
    /*prepaymentAmount set 方法 */
    public void setPrepaymentAmount(BigDecimal  prepaymentAmount){
    this.prepaymentAmount=prepaymentAmount;
    }
    /*paymentAmount get 方法 */
    public BigDecimal getPaymentAmount(){
    return paymentAmount;
    }
    /*paymentAmount set 方法 */
    public void setPaymentAmount(BigDecimal  paymentAmount){
    this.paymentAmount=paymentAmount;
    }
    /*verificationAmount get 方法 */
    public BigDecimal getVerificationAmount(){
    return verificationAmount;
    }
    /*verificationAmount set 方法 */
    public void setVerificationAmount(BigDecimal  verificationAmount){
    this.verificationAmount=verificationAmount;
    }
    /*billStatus get 方法 */
    public String getBillStatus(){
    return billStatus;
    }
    /*billStatus set 方法 */
    public void setBillStatus(String  billStatus){
    this.billStatus=billStatus;
    this.billStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.advancePaymentBill_billStatusList, billStatus) ;
    }
    /*auditMethod get 方法 */
    public String getAuditMethod(){
    return auditMethod;
    }
    /*auditMethod set 方法 */
    public void setAuditMethod(String  auditMethod){
    this.auditMethod=auditMethod;
        this.auditMethodLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.advancePaymentBill_auditMethodList, auditMethod) ;
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
    /*ncSynDate get 方法 */
    public Timestamp getNcSynDate(){
    return ncSynDate;
    }
    /*ncSynDate set 方法 */
    public void setNcSynDate(Timestamp  ncSynDate){
    this.ncSynDate=ncSynDate;
    }
    /*ncSynStatus get 方法 */
    public String getNcSynStatus(){
    return ncSynStatus;
    }
    /*ncSynStatus set 方法 */
    public void setNcSynStatus(String  ncSynStatus){
    this.ncSynStatus=ncSynStatus;
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

    public String getBillStatusLabel() {
        return billStatusLabel;
    }

    public void setBillStatusLabel(String billStatusLabel) {
        this.billStatusLabel = billStatusLabel;
    }

    public String getAuditMethodLabel() {
        return auditMethodLabel;
    }

    public void setAuditMethodLabel(String auditMethodLabel) {
        this.auditMethodLabel = auditMethodLabel;
    }

    public String getExpectedPaymentDateStart() {
        return expectedPaymentDateStart;
    }

    public void setExpectedPaymentDateStart(String expectedPaymentDateStart) {
        this.expectedPaymentDateStart = expectedPaymentDateStart;
    }

    public String getExpectedPaymentDateEnd() {
        return expectedPaymentDateEnd;
    }

    public void setExpectedPaymentDateEnd(String expectedPaymentDateEnd) {
        this.expectedPaymentDateEnd = expectedPaymentDateEnd;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public List<Long> getBuIds() {
        return buIds;
    }

    public void setBuIds(List<Long> buIds) {
        this.buIds = buIds;
    }

    public Long getCreaterId() {
        return createrId;
    }

    public void setCreaterId(Long createrId) {
        this.createrId = createrId;
    }
	public String getPrepaymentAmountStr() {
		return prepaymentAmountStr;
	}
	public void setPrepaymentAmountStr(String prepaymentAmountStr) {
		this.prepaymentAmountStr = prepaymentAmountStr;
	}
	public Long getModifierId() {
		return modifierId;
	}
	public void setModifierId(Long modifierId) {
		this.modifierId = modifierId;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public List<AdvancePaymentBillItemExportDTO> getItemList() {
		return itemList;
	}
	public void setItemList(List<AdvancePaymentBillItemExportDTO> itemList) {
		this.itemList = itemList;
	}
	public List<AdvancePaymentRecordItemExportDTO> getRecordItemList() {
		return recordItemList;
	}
	public void setRecordItemList(List<AdvancePaymentRecordItemExportDTO> recordItemList) {
		this.recordItemList = recordItemList;
	}
    
    
}
