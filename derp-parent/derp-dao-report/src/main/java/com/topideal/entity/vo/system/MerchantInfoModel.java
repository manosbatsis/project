package com.topideal.entity.vo.system;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商家信息
 * @author zhanghx
 */
public class MerchantInfoModel extends PageModel implements Serializable {

	// 商家编码
	private String code;
	// 卓志编码
	private String topidealCode;
	// 商家简称
	private String name;
	// 创建人
	private Long creater;
	// 备注
	private String remark;
	// 联系电话
	private String tel;
	// 自增长ID
	private Long id;
	// 电子邮箱 企业
	private String email;
	// 创建日期
	private Timestamp createDate;
	// 是否绑定商家
	private String isBindUser;
	// 是否代理 0商家 1 代理商家
	private String isProxy;
	// 授权码
	private String permission;
	// 商家简称
	private String simpleMerchant;
	// 修改时间
	private Timestamp modifyDate;
	// 盘点结果通知邮箱
	private String inventoryResultEmail;
	// 提醒财务付款邮箱
	private String financePayEmail;
	// 注册地址
	private String registeredAddress;
	// 商家全称
	private String fullName;
	//英文名称
	private String englishName;
	//勾稽百分比
	private Double  articulationPercent;
	/**
	 * 成本核算币种
	 */
	private String accountCurrency;
	/**
	 * 核算单价 1-标准成本单价 2-月末加权单价
	 */
	private String accountPrice;
	/**
	 * 开户银行
	 */
	private String depositBank;
	/**
	 * 银行账号
	 */
	private String bankAccount;
	/**
	 * 银行账户
	 */
	private String beneficiaryName;
	/**
	 * 开户行地址
	 */
	private String bankAddress;
	/**
	 * Swift Code
	 */
	private String swiftCode;
	
    // 英文注册地
    private String englishRegisteredAddress;
    //注册地类型 1、境内；2、境外
    private String registeredType;
	/**
	 * 状态(1启用,0禁用)
	 */
	private String status;

	/**
	 * 注册登记证
	 */
	private String registrationCert;

     public String getEnglishRegisteredAddress() {
		return englishRegisteredAddress;
	}
	public void setEnglishRegisteredAddress(String englishRegisteredAddress) {
		this.englishRegisteredAddress = englishRegisteredAddress;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
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

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getIsBindUser() {
		return isBindUser;
	}

	public void setIsBindUser(String isBindUser) {
		this.isBindUser = isBindUser;
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

	public String getSimpleMerchant() {
		return simpleMerchant;
	}

	public void setSimpleMerchant(String simpleMerchant) {
		this.simpleMerchant = simpleMerchant;
	}

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public Double getArticulationPercent() {
		return articulationPercent;
	}

	public void setArticulationPercent(Double articulationPercent) {
		this.articulationPercent = articulationPercent;
	}

	public String getRegisteredType() {
		return registeredType;
	}

	public void setRegisteredType(String registeredType) {
		this.registeredType = registeredType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRegistrationCert() {
		return registrationCert;
	}

	public void setRegistrationCert(String registrationCert) {
		this.registrationCert = registrationCert;
	}
}
