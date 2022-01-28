package com.topideal.webapi.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 商家信息
 * @author 
 *
 */
@ApiModel
public class MerchantInfoAddForm implements Serializable{


	 @ApiModelProperty(value = "令牌",required = true)
	 private String token;
	 @ApiModelProperty(value = "卓志编码",required = true)
     private String topidealCode;
	 @ApiModelProperty(value = "商家简称",required = true)
     private String name;
	 @ApiModelProperty(value = "备注")
     private String remark;
	 @ApiModelProperty(value = "联系电话")
     private String tel;
	 @ApiModelProperty(value = "新增非必填,修改必填")
     private Long id;
     //电子邮箱      企业
	 @ApiModelProperty(value = "接收清关资料邮箱")
     private String email;
	 @ApiModelProperty(value = "是否代理 0商家 1 代理商家")
     private String isProxy;
	 @ApiModelProperty(value = "授权码 ")
     private String permission;
	 @ApiModelProperty(value = "盘点结果通知邮箱")
     private String inventoryResultEmail;
	 @ApiModelProperty(value = "商品备案价申报系数",required = true)
     private Double priceDeclareRatio;
	 @ApiModelProperty(value = "提醒财务付款邮箱")
     private String financePayEmail;
	 @ApiModelProperty(value = "注册地址",required = true)
     private String registeredAddress;
	 @ApiModelProperty(value = "商家全称",required = true)
     private String fullName;
	 @ApiModelProperty(value = "勾稽百分比",required = true)
     private Double  articulationPercent;
	 @ApiModelProperty(value = "段用于判断编辑时商家名是否改动商家简称",required = true)
     private String yname;
	 @ApiModelProperty(value = "英文名称")
     private String englishName;
	 @ApiModelProperty(value = "成本核算币种",required = true)
     private String accountCurrency;
	@ApiModelProperty(value = "核算单价 1-标准成本单价 2-月末加权单价",required = true)
    private String accountPrice;
	@ApiModelProperty(value = "开户银行",required = true)
    private String depositBank;
	@ApiModelProperty(value = "银行账号",required = true)
    private String bankAccount;
	@ApiModelProperty(value = "银行账户",required = true)
    private String beneficiaryName;
	@ApiModelProperty(value = "开户行地址",required = true)
    private String bankAddress;
	@ApiModelProperty(value = "Swift Code",required = true)
    private String swiftCode;
	@ApiModelProperty(value = "英文注册地",required = true)
    private String englishRegisteredAddress;		
	@ApiModelProperty(value = "注册地类型")
	private String registeredType;
	@ApiModelProperty(value = "注册登记证")
	private String registrationCert;
	@ApiModelProperty("关联公司ids")
    private List<Long> merchantIdList;
	@ApiModelProperty("仓库信息")
	private List<MerchantInfoAddDepotForm> merchantDepotList;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTopidealCode() {
		return topidealCode;
	}
	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIsProxy() {
		return isProxy;
	}
	public void setIsProxy(String isProxy) {
		this.isProxy = isProxy;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public String getInventoryResultEmail() {
		return inventoryResultEmail;
	}
	public void setInventoryResultEmail(String inventoryResultEmail) {
		this.inventoryResultEmail = inventoryResultEmail;
	}
	public Double getPriceDeclareRatio() {
		return priceDeclareRatio;
	}
	public void setPriceDeclareRatio(Double priceDeclareRatio) {
		this.priceDeclareRatio = priceDeclareRatio;
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
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Double getArticulationPercent() {
		return articulationPercent;
	}
	public void setArticulationPercent(Double articulationPercent) {
		this.articulationPercent = articulationPercent;
	}
	public String getYname() {
		return yname;
	}
	public void setYname(String yname) {
		this.yname = yname;
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
	}
	public String getAccountPrice() {
		return accountPrice;
	}
	public void setAccountPrice(String accountPrice) {
		this.accountPrice = accountPrice;
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
	public String getEnglishRegisteredAddress() {
		return englishRegisteredAddress;
	}
	public void setEnglishRegisteredAddress(String englishRegisteredAddress) {
		this.englishRegisteredAddress = englishRegisteredAddress;
	}
	public String getRegisteredType() {
		return registeredType;
	}
	public void setRegisteredType(String registeredType) {
		this.registeredType = registeredType;
	}

	public List<Long> getMerchantIdList() {
		return merchantIdList;
	}
	public void setMerchantIdList(List<Long> merchantIdList) {
		this.merchantIdList = merchantIdList;
	}
	public List<MerchantInfoAddDepotForm> getMerchantDepotList() {
		return merchantDepotList;
	}
	public void setMerchantDepotList(List<MerchantInfoAddDepotForm> merchantDepotList) {
		this.merchantDepotList = merchantDepotList;
	}

	public String getRegistrationCert() {
		return registrationCert;
	}

	public void setRegistrationCert(String registrationCert) {
		this.registrationCert = registrationCert;
	}
}

