package com.topideal.entity.dto.main;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商家信息
 * @author
 *
 */
public class MerchantInfoDTO extends PageModel implements Serializable{


    //商家编码
	@ApiModelProperty("商家编码")
    private String code;
    //卓志编码
	@ApiModelProperty("商家编码")
    private String topidealCode;
    //商家简称
	@ApiModelProperty("商家简称")
    private String name;
    //创建人
	@ApiModelProperty("创建人")
    private Long creater;
    //备注
	@ApiModelProperty("备注")
    private String remark;
    //联系电话
	@ApiModelProperty("联系电话")
    private String tel;
    //自增长ID
	@ApiModelProperty("自增长ID")
    private Long id;
    //电子邮箱      企业
	@ApiModelProperty("电子邮箱      企业")
    private String email;
    //创建日期
	@ApiModelProperty("创建日期")
    private Timestamp createDate;
    //是否绑定商家
	@ApiModelProperty("是否绑定商家")
    private String isBindUser;
    //是否代理 0商家 1 代理商家
	@ApiModelProperty("是否代理 0商家 1 代理商家")
    private String isProxy;
	@ApiModelProperty("是否代理 0商家 1 代理商家Label")
    private String isProxyLabel;
    //授权码
	@ApiModelProperty("授权码")
    private String permission;
    //商家简称
	@ApiModelProperty("商家简称")
    private String simpleMerchant;
    //修改时间
	@ApiModelProperty("修改时间")
    private Timestamp modifyDate;
    //盘点结果通知邮箱
	@ApiModelProperty("盘点结果通知邮箱")
    private String inventoryResultEmail;
    //提醒财务付款邮箱
	@ApiModelProperty("提醒财务付款邮箱")
    private String financePayEmail;
    //注册地址
	@ApiModelProperty("注册地址")
    private String registeredAddress;
    //商家全称
	@ApiModelProperty("商家全称")
    private String fullName;
    //勾稽百分比
	@ApiModelProperty("勾稽百分比")
    private Double  articulationPercent;
    //附加字段用于判断编辑时商家名是否改动
    //商家简称
	@ApiModelProperty("商家简称")
    private String yname;

    //英文名称
	@ApiModelProperty("英文名称")
    private String englishName;

    /**
     * 成本核算币种
     */
	@ApiModelProperty("成本核算币种")
    private String accountCurrency;
	@ApiModelProperty("成本核算币种Label")
    private String accountCurrencyLabel;
    /**
     * 核算单价 1-标准成本单价 2-月末加权单价
     */
	@ApiModelProperty("核算单价 1-标准成本单价 2-月末加权单价")
    private String accountPrice;
	@ApiModelProperty("核算单价 1-标准成本单价 2-月末加权单价label")
    private String accountPriceLabel;
    /**
     * 开户银行
     */
	@ApiModelProperty("开户银行")
    private String depositBank;
    /**
     * 银行账号
     */
	@ApiModelProperty("银行账号")
    private String bankAccount;
    /**
     * 银行账户
     */
	@ApiModelProperty("银行账户")
    private String beneficiaryName;
    /**
     * 开户行地址
     */
	@ApiModelProperty("开户行地址")
    private String bankAddress;
    /**
     * Swift Code
     */
	@ApiModelProperty("Swift Code")
    private String swiftCode;
    // 英文注册地
	@ApiModelProperty("英文注册地")
    private String englishRegisteredAddress;

    // 结算类型
	@ApiModelProperty("结算类型")
    private String settlementType;
	@ApiModelProperty("结算类型Label")
    private String settlementTypeLabel;

    // 账期
	@ApiModelProperty("账期")
    private String accountPeriod;
	@ApiModelProperty("账期Label")
    private String accountPeriodLabel;
    //是否启用采购价格管理 1-启用 0-不启用
	@ApiModelProperty("是否启用采购价格管理 1-启用 0-不启用")
    private String salePriceManage;
	@ApiModelProperty("是否启用采购价格管理 1-启用 0-不启用Label")
    private String salePriceManageLabel;
    //是否启用销售价格管理 1-启用 0-不启用
	@ApiModelProperty("是否启用销售价格管理 1-启用 0-不启用")
    private String purchasePriceManage;
	@ApiModelProperty("是否启用销售价格管理 1-启用 0-不启用Label")
    private String purchasePriceManageLabel;
    // 销售模式
	@ApiModelProperty("销售模式")
    private String businessModel;
	@ApiModelProperty("销售模式Label")
    private String businessModelLabel;
    // 税率
	@ApiModelProperty("商家id")
    private BigDecimal rate;
    /**
     * 申报系数
     */
	@ApiModelProperty("申报系数")
    private Double priceDeclareRatio;

    //注册地类型 1、境内；2、境外
	@ApiModelProperty("注册地类型 1、境内；2、境外")
    private String registeredType;
	@ApiModelProperty("注册地类型 1、境内；2、境外Label")
    private String registeredTypeLabel;
    /**
     * 状态(1启用,0禁用)
     */
	@ApiModelProperty("状态(1启用,0禁用)")
    private String status;
	@ApiModelProperty("状态(1启用,0禁用)label")
    private String statusLabel;

    @ApiModelProperty(value = "注册登记证")
    private String registrationCert;

    public String getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(String settlementType) {
        this.settlementType = settlementType;
        this.settlementTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.settlement_typeList,settlementType);
    }

    public String getSettlementTypeLabel() {
        return settlementTypeLabel;
    }

    public void setSettlementTypeLabel(String settlementTypeLabel) {
        this.settlementTypeLabel = settlementTypeLabel;
    }

    public String getAccountPeriod() {
        return accountPeriod;
    }

    public void setAccountPeriod(String accountPeriod) {
        this.accountPeriod = accountPeriod;
        this.accountPeriodLabel = DERP_SYS.getLabelByKey(DERP_SYS.customerInfo_accountPeriodList, accountPeriod) ;

    }

    public String getAccountPeriodLabel() {
        return accountPeriodLabel;
    }

    public void setAccountPeriodLabel(String accountPeriodLabel) {
        this.accountPeriodLabel = accountPeriodLabel;
    }

    public String getEnglishRegisteredAddress() {
        return englishRegisteredAddress;
    }
    public void setEnglishRegisteredAddress(String englishRegisteredAddress) {
        this.englishRegisteredAddress = englishRegisteredAddress;
    }

    public String getYname() {
        return yname;
    }
    public void setYname(String yname) {
        this.yname = yname;
    }
    public Double getArticulationPercent() {
        return articulationPercent;
    }
    public void setArticulationPercent(Double articulationPercent) {
        this.articulationPercent = articulationPercent;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getInventoryResultEmail() {
        return inventoryResultEmail;
    }
    public void setInventoryResultEmail(String inventoryResultEmail) {
        this.inventoryResultEmail = inventoryResultEmail;
    }
    public String getFinancePayEmail() {
        return financePayEmail;
    }
    public void setFinancePayEmail(String financePayEmail) {
        this.financePayEmail = financePayEmail;
    }
    public String getRegisteredAddress() {
        return registeredAddress;
    }
    public void setRegisteredAddress(String registeredAddress) {
        this.registeredAddress = registeredAddress;
    }
    public Timestamp getModifyDate() {
        return modifyDate;
    }
    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }
    /*simpleMerchant get 方法 */
    public String getSimpleMerchant(){
        return simpleMerchant;
    }
    /*simpleMerchant set 方法 */
    public void setSimpleMerchant(String  simpleMerchant){
        this.simpleMerchant=simpleMerchant;
    }
    /*permission get 方法 */
    public String getPermission(){
        return permission;
    }
    /*permission set 方法 */
    public void setPermission(String  permission){
        this.permission=permission;
    }
    /*isProxy get 方法 */
    public String getIsProxy(){
        return isProxy;
    }
    /*isProxy set 方法 */
    public void setIsProxy(String  isProxy){
        this.isProxy=isProxy;
        if(isProxy != null) {
            this.isProxyLabel = DERP_SYS.getLabelByKey(DERP_SYS.merchantInfo_isProxyList, isProxy);
        }
    }


    /*isBindUser get 方法 */
    public String getIsBindUser(){
        return isBindUser;
    }
    /*isBindUser set 方法 */
    public void setIsBindUser(String  isBindUser){
        this.isBindUser=isBindUser;
    }
    /*code get 方法 */
    public String getCode(){
        return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
        this.code=code;
    }
    /*topidealCode get 方法 */
    public String getTopidealCode(){
        return topidealCode;
    }
    /*topidealCode set 方法 */
    public void setTopidealCode(String  topidealCode){
        this.topidealCode=topidealCode;
    }
    /*name get 方法 */
    public String getName(){
        return name;
    }
    /*name set 方法 */
    public void setName(String  name){
        this.name=name;
    }
    /*creatorId get 方法 */
    public Long getCreater(){
        return creater;
    }
    /*creatorId set 方法 */
    public void setCreater(Long  creater){
        this.creater=creater;
    }
    /*remark get 方法 */
    public String getRemark(){
        return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
        this.remark=remark;
    }
    /*tel get 方法 */
    public String getTel(){
        return tel;
    }
    /*tel set 方法 */
    public void setTel(String  tel){
        this.tel=tel;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*email get 方法 */
    public String getEmail(){
        return email;
    }
    /*email set 方法 */
    public void setEmail(String  email){
        this.email=email;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
    public String getIsProxyLabel() {
        return isProxyLabel;
    }
    public void setIsProxyLabel(String isProxyLabel) {
        this.isProxyLabel = isProxyLabel;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getAccountCurrency() {
        return accountCurrency;
    }

    public void setAccountCurrency(String accountCurrency) {
        this.accountCurrency = accountCurrency;
        this.accountCurrencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, accountCurrency);
    }

    public String getAccountPrice() {
        return accountPrice;
    }

    public void setAccountPrice(String accountPrice) {
        this.accountPrice = accountPrice;
        this.accountPriceLabel = DERP_SYS.getLabelByKey(DERP_SYS.merchantInfo_accountPriceList, accountPrice);
    }

    public String getDepositBank() {
        return depositBank;
    }

    public void setDepositBank(String depositBank) {
        this.depositBank = depositBank;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getAccountCurrencyLabel() {
        return accountCurrencyLabel;
    }

    public void setAccountCurrencyLabel(String accountCurrencyLabel) {
        this.accountCurrencyLabel = accountCurrencyLabel;
    }

    public String getAccountPriceLabel() {
        return accountPriceLabel;
    }

    public void setAccountPriceLabel(String accountPriceLabel) {
        this.accountPriceLabel = accountPriceLabel;
    }

    public String getSalePriceManage() {
        return salePriceManage;
    }

    public void setSalePriceManage(String salePriceManage) {
        this.salePriceManage = salePriceManage;
        this.salePriceManageLabel = DERP_SYS.getLabelByKey(DERP_SYS.customerMerchantRel_salePriceManageList, salePriceManage);
    }

    public String getSalePriceManageLabel() {
        return salePriceManageLabel;
    }

    public void setSalePriceManageLabel(String salePriceManageLabel) {
        this.salePriceManageLabel = salePriceManageLabel;
    }

    public String getPurchasePriceManage() {
        return purchasePriceManage;
    }

    public void setPurchasePriceManage(String purchasePriceManage) {
        this.purchasePriceManage = purchasePriceManage;
        this.purchasePriceManageLabel = DERP_SYS.getLabelByKey(DERP_SYS.customerMerchantRel_purchasePriceManageList, purchasePriceManage);

    }

    public String getPurchasePriceManageLabel() {
        return purchasePriceManageLabel;
    }

    public void setPurchasePriceManageLabel(String purchasePriceManageLabel) {
        this.purchasePriceManageLabel = purchasePriceManageLabel;
    }

    public String getBusinessModel() {
        return businessModel;
    }

    public void setBusinessModel(String businessModel) {
        this.businessModel = businessModel;
        this.businessModelLabel = DERP_SYS.getLabelByKey(DERP_SYS.customerMerchantRel_businessModelList, businessModel);

    }

    public String getBusinessModelLabel() {
        return businessModelLabel;
    }

    public void setBusinessModelLabel(String businessModelLabel) {
        this.businessModelLabel = businessModelLabel;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Double getPriceDeclareRatio() {
        return priceDeclareRatio;
    }

    public void setPriceDeclareRatio(Double priceDeclareRatio) {
        this.priceDeclareRatio = priceDeclareRatio;
    }

    public String getRegisteredType() {
        return registeredType;
    }

    public void setRegisteredType(String registeredType) {
        this.registeredType = registeredType;
        this.registeredTypeLabel= DERP_SYS.getLabelByKey(DERP_SYS.merchantInfo_registeredTypeList, registeredType) ;
    }

    public String getRegisteredTypeLabel() {
        return registeredTypeLabel;
    }

    public void setRegisteredTypeLabel(String registeredTypeLabel) {
        this.registeredTypeLabel = registeredTypeLabel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.statusLabel= DERP_SYS.getLabelByKey(DERP_SYS.merchantInfo_statusList, status) ;
    }

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

    public String getRegistrationCert() {
        return registrationCert;
    }

    public void setRegistrationCert(String registrationCert) {
        this.registrationCert = registrationCert;
    }
}

