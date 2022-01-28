package com.topideal.order.webapi.bill.form;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.entity.dto.bill.PaymentCostItemDTO;
import com.topideal.entity.dto.bill.PaymentItemDTO;
import com.topideal.entity.dto.bill.PaymentSummaryDTO;
import com.topideal.entity.dto.bill.PaymentVerificateItemDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PaymentBillAddForm {

	@ApiModelProperty("票据")
	private String token ;
    /**
    * id主键
    */
	@ApiModelProperty("id主键")
    private Long id;
    /**
    * 应付账单号
    */
	@ApiModelProperty("应付账单号")
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
	@ApiModelProperty("供应商银行账号")
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
    * 事业部ID
    */
	@ApiModelProperty("事业部ID")
    private Long buId;
    /**
    * 事业部名称
    */
	@ApiModelProperty("事业部名称")
    private String buName;
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
	@ApiModelProperty("预计付款日期字符串")
    private String expectedPaymentDateStr;
    /**
    * 请款原因
    */
	@ApiModelProperty("请款原因")
    private String paymentReason;
    /**
    * 结算金额（不含税）
    */
	@ApiModelProperty("结算金额（不含税）")
    private BigDecimal settlementAmount;
    /**
    * 结算金额（含税）
    */
	@ApiModelProperty("结算金额（含税）")
    private BigDecimal settlementTaxAmount;
    /**
    * 税额
    */
	@ApiModelProperty("税额")
    private BigDecimal tax;
    /**
    * 应付总额
    */
	@ApiModelProperty("应付总额")
    private BigDecimal payableAmount;
	@ApiModelProperty("应付总额大写")
    private String payableAmountStr;
    /**
    * 已付总额
    */
	@ApiModelProperty("已付总额")
    private BigDecimal paymentAmount;
    /**
    * 待付款金额
    */
	@ApiModelProperty("待付款金额")
    private BigDecimal surplusAmount;
    /**
    * 账单状态 00-待提交、01-审核中、02-已驳回、03-待付款、04-已付款、05-待作废、06-已作废
    */
	@ApiModelProperty("账单状态00-待提交、01-审核中、02-已驳回、03-待付款、04-已付款、05-待作废、06-已作废")
    private String billStatus;
	private String billStatusLabel;
    /**
    * 审批方式 1-OA审批 2-经分销审批
    */
	@ApiModelProperty("审批方式 1-OA审批 2-经分销审批")
    private String auditMethod;
	private String auditMethodLabel;
    /**
    * OA流程单号
    */
	@ApiModelProperty("OA流程单号")
    private String requestId;
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
    * NC状态 1-未同步、2-已同步、3-待审核、4-待入erp、5-待入账、6-已入账、7-已关账
    */
    @ApiModelProperty("NC状态 1-未同步、2-已同步、3-待审核、4-待入erp、5-待入账、6-已入账、7-已关账")
    private String ncStatus;
    private String ncStatusLabel;
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
    * 同步操作人id
    */
    @ApiModelProperty("同步操作人id")
    private Long synchronizerId;
    /**
    * 同步操作人
    */
    @ApiModelProperty("同步操作人")
    private String synchronizer;
    
    @ApiModelProperty(hidden=true)
    private List<Long> buIds ;
    
    @ApiModelProperty("核销记录")
    List<PaymentVerificateItemDTO> verificateItemList ;
    
    @ApiModelProperty("费用记录")
    List<PaymentCostItemDTO> costList ;
    
    @ApiModelProperty("应付记录明细")
    List<PaymentItemDTO> itemList ;
    
    @ApiModelProperty("应付应付汇总")
    List<PaymentSummaryDTO> paymentSummaryList ;

    /**
     * 是否垫资 1：是 0否
     */
    @ApiModelProperty("是否垫资 1：是 0否")
    private String endowmentState;

    public String getEndowmentState() {
        return endowmentState;
    }

    public void setEndowmentState(String endowmentState) {
        this.endowmentState = endowmentState;
    }

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
    /*settlementAmount get 方法 */
    public BigDecimal getSettlementAmount(){
    return settlementAmount;
    }
    /*settlementAmount set 方法 */
    public void setSettlementAmount(BigDecimal  settlementAmount){
    this.settlementAmount=settlementAmount;
    }
    /*settlementTaxAmount get 方法 */
    public BigDecimal getSettlementTaxAmount(){
    return settlementTaxAmount;
    }
    /*settlementTaxAmount set 方法 */
    public void setSettlementTaxAmount(BigDecimal  settlementTaxAmount){
    this.settlementTaxAmount=settlementTaxAmount;
    }
    /*tax get 方法 */
    public BigDecimal getTax(){
    return tax;
    }
    /*tax set 方法 */
    public void setTax(BigDecimal  tax){
    this.tax=tax;
    }
    /*payableAmount get 方法 */
    public BigDecimal getPayableAmount(){
    return payableAmount;
    }
    /*payableAmount set 方法 */
    public void setPayableAmount(BigDecimal  payableAmount){
    this.payableAmount=payableAmount;
    }
    /*paymentAmount get 方法 */
    public BigDecimal getPaymentAmount(){
    return paymentAmount;
    }
    /*paymentAmount set 方法 */
    public void setPaymentAmount(BigDecimal  paymentAmount){
    this.paymentAmount=paymentAmount;
    }
    /*surplusAmount get 方法 */
    public BigDecimal getSurplusAmount(){
    return surplusAmount;
    }
    /*surplusAmount set 方法 */
    public void setSurplusAmount(BigDecimal  surplusAmount){
    this.surplusAmount=surplusAmount;
    }
    /*billStatus get 方法 */
    public String getBillStatus(){
    return billStatus;
    }
    /*billStatus set 方法 */
    public void setBillStatus(String  billStatus){
    this.billStatus=billStatus;
    this.billStatusLabel=DERP_ORDER.getLabelByKey(DERP_ORDER.paymentBill_billStatusList, billStatus);
    }
    /*auditMethod get 方法 */
    public String getAuditMethod(){
    return auditMethod;
    }
    /*auditMethod set 方法 */
    public void setAuditMethod(String  auditMethod){
    this.auditMethod=auditMethod;
    this.auditMethodLabel=DERP_ORDER.getLabelByKey(DERP_ORDER.paymentBill_auditMethodList, auditMethod);
    }
    /*requestId get 方法 */
    public String getRequestId(){
    return requestId;
    }
    /*requestId set 方法 */
    public void setRequestId(String  requestId){
    this.requestId=requestId;
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
    /*modifierId get 方法 */
    public Long getModifierId(){
    return modifierId;
    }
    /*modifierId set 方法 */
    public void setModifierId(Long  modifierId){
    this.modifierId=modifierId;
    }
    /*modifier get 方法 */
    public String getModifier(){
    return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(String  modifier){
    this.modifier=modifier;
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
    this.ncStatusLabel=DERP_ORDER.getLabelByKey(DERP_ORDER.paymentBill_ncStatusList, ncSynStatus);
    }
    /*ncStatus get 方法 */
    public String getNcStatus(){
    return ncStatus;
    }
    /*ncStatus set 方法 */
    public void setNcStatus(String  ncStatus){
    this.ncStatus=ncStatus;
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
	public String getNcStatusLabel() {
		return ncStatusLabel;
	}
	public void setNcStatusLabel(String ncStatusLabel) {
		this.ncStatusLabel = ncStatusLabel;
	}
	public List<Long> getBuIds() {
		return buIds;
	}
	public void setBuIds(List<Long> buIds) {
		this.buIds = buIds;
	}
	public List<PaymentVerificateItemDTO> getVerificateItemList() {
		return verificateItemList;
	}
	public void setVerificateItemList(List<PaymentVerificateItemDTO> verificateItemList) {
		this.verificateItemList = verificateItemList;
	}
	public List<PaymentCostItemDTO> getCostList() {
		return costList;
	}
	public void setCostList(List<PaymentCostItemDTO> costList) {
		this.costList = costList;
	}
	public List<PaymentItemDTO> getItemList() {
		return itemList;
	}
	public void setItemList(List<PaymentItemDTO> itemList) {
		this.itemList = itemList;
	}
	public List<PaymentSummaryDTO> getPaymentSummaryList() {
		return paymentSummaryList;
	}
	public void setPaymentSummaryList(List<PaymentSummaryDTO> paymentSummaryList) {
		this.paymentSummaryList = paymentSummaryList;
	}
	public String getPayableAmountStr() {
		return payableAmountStr;
	}
	public void setPayableAmountStr(String payableAmountStr) {
		this.payableAmountStr = payableAmountStr;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getExpectedPaymentDateStr() {
		return expectedPaymentDateStr;
	}
	public void setExpectedPaymentDateStr(String expectedPaymentDateStr) {
		this.expectedPaymentDateStr = expectedPaymentDateStr;
	}






}