package com.topideal.webapi.form;

import java.io.Serializable;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * 客户信息表
 */
public class CustomerInfoAddForm extends PageModel implements Serializable {
	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	@ApiModelProperty(value = "id 新增非必填,修改必填")
	private Long id;	
	@ApiModelProperty(value = "商家名称")
	private String name;
	@ApiModelProperty(value = "客户简称")
	private String simpleName;
	@ApiModelProperty(value = "营业执照号")
	private String creditCode;
	@ApiModelProperty(value = "组织机构代码")
	private String orgCode;
	@ApiModelProperty(value = "企业性质")
    private String nature;
	@ApiModelProperty(value = "币种")
	private String currency;
	@ApiModelProperty(value = "中文客户名称")
    private String chinaName;
	@ApiModelProperty(value = "英文简称")
    private String enSimpleName;
	@ApiModelProperty(value = "英文名")
	private String enName;
	@ApiModelProperty(value = "公司注册地址")
	private String companyAddr;
	@ApiModelProperty(value = "客户等级编码")
    private String codeGrade;
	@ApiModelProperty(value = "授权码")
	private String authNo;
	@ApiModelProperty(value = "主数据客户id")
    private String mainId;
	@ApiModelProperty(value = "客户类型(1内部,2外部)")
    private String type;
	@ApiModelProperty(value = "内部公司ID")
    private Long innerMerchantId;
	@ApiModelProperty(value = "经营范围")
	private String operationScope;
	@ApiModelProperty(value = "税号")
    private String taxNo;
	@ApiModelProperty(value = "NC平台编码")
    private String ncPlatformCode ;
	@ApiModelProperty(value = "NC渠道编码")
    private String ncChannelCode ;
	@ApiModelProperty(value = "法人代表")
 	private String legalPerson;
	@ApiModelProperty(value = "法人国籍")
	private String legalNationality;
	@ApiModelProperty(value = "法人身份证")
	private String legalCardNo;
	@ApiModelProperty(value = "法人电话")
	private String legalTel;
	@ApiModelProperty(value = "公司电话")
	private String oTelNo;		
	@ApiModelProperty(value = "渠道分类  1-2C平台 2-线下2B 3-线上2B")
    private String channelClassify;
	@ApiModelProperty(value = "开户银行")
	private String depositBank;
	@ApiModelProperty(value = "银行账号")
	private String bankAccount;
	@ApiModelProperty(value = "开户行地址")
	private String bankAddress;
	@ApiModelProperty(value = "Swift Code")
	private String swiftCode;
	@ApiModelProperty(value = "银行账户")
	private String beneficiaryName;
	
	
	
	@ApiModelProperty(value = "json")
	private String json;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSimpleName() {
		return simpleName;
	}
	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}
	public String getCreditCode() {
		return creditCode;
	}
	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getChinaName() {
		return chinaName;
	}
	public void setChinaName(String chinaName) {
		this.chinaName = chinaName;
	}
	public String getEnSimpleName() {
		return enSimpleName;
	}
	public void setEnSimpleName(String enSimpleName) {
		this.enSimpleName = enSimpleName;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	public String getCompanyAddr() {
		return companyAddr;
	}
	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}
	public String getCodeGrade() {
		return codeGrade;
	}
	public void setCodeGrade(String codeGrade) {
		this.codeGrade = codeGrade;
	}
	public String getAuthNo() {
		return authNo;
	}
	public void setAuthNo(String authNo) {
		this.authNo = authNo;
	}
	public String getMainId() {
		return mainId;
	}
	public void setMainId(String mainId) {
		this.mainId = mainId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getInnerMerchantId() {
		return innerMerchantId;
	}
	public void setInnerMerchantId(Long innerMerchantId) {
		this.innerMerchantId = innerMerchantId;
	}
	public String getOperationScope() {
		return operationScope;
	}
	public void setOperationScope(String operationScope) {
		this.operationScope = operationScope;
	}
	public String getTaxNo() {
		return taxNo;
	}
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}
	public String getNcPlatformCode() {
		return ncPlatformCode;
	}
	public void setNcPlatformCode(String ncPlatformCode) {
		this.ncPlatformCode = ncPlatformCode;
	}
	public String getNcChannelCode() {
		return ncChannelCode;
	}
	public void setNcChannelCode(String ncChannelCode) {
		this.ncChannelCode = ncChannelCode;
	}
	public String getLegalPerson() {
		return legalPerson;
	}
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	public String getLegalNationality() {
		return legalNationality;
	}
	public void setLegalNationality(String legalNationality) {
		this.legalNationality = legalNationality;
	}
	public String getLegalCardNo() {
		return legalCardNo;
	}
	public void setLegalCardNo(String legalCardNo) {
		this.legalCardNo = legalCardNo;
	}
	public String getLegalTel() {
		return legalTel;
	}
	public void setLegalTel(String legalTel) {
		this.legalTel = legalTel;
	}
	public String getoTelNo() {
		return oTelNo;
	}
	public void setoTelNo(String oTelNo) {
		this.oTelNo = oTelNo;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public String getChannelClassify() {
		return channelClassify;
	}
	public void setChannelClassify(String channelClassify) {
		this.channelClassify = channelClassify;
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
	public String getBeneficiaryName() {
		return beneficiaryName;
	}
	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}
		
	
}
