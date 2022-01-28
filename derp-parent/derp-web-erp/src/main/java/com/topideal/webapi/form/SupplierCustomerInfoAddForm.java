package com.topideal.webapi.form;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 客户信息表
 */
public class SupplierCustomerInfoAddForm  implements Serializable {
	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	@ApiModelProperty(value = "id 编辑必填,新增非必填",required = true)
	private Long id;
	@ApiModelProperty(value = "deleteIds,要删除的客商关系,非必填 ",required = true)
	private String deleteIds;	
	@ApiModelProperty(value = "来源 1-主数据  2-录入/导入")
    private String source;
	@ApiModelProperty(value = "客户简称")
	private String simpleName;
	@ApiModelProperty(value = "客户名称",required = true)
	private String name;
	@ApiModelProperty(value = "营业执照号",required = true)
	private String creditCode;
	@ApiModelProperty(value = "组织机构代码",required = true)
	private String orgCode;
	@ApiModelProperty(value = "客户类型(1内部,2外部)",required = true)
    private String type;	
	@ApiModelProperty(value = "客户等级编码")
    private String codeGrade;
	@ApiModelProperty(value = "企业性质")
    private String nature;
	@ApiModelProperty(value = "中文客户名称")
    private String chinaName;
	@ApiModelProperty(value = "英文简称")
    private String enSimpleName;
	@ApiModelProperty(value = "英文名")
	private String enName;
	@ApiModelProperty(value = "主数据客户id")
    private String mainId;
	@ApiModelProperty(value = "业务员")
    private String salesman;
	@ApiModelProperty(value = "省市区代码")
    private String cityCode;
	@ApiModelProperty(value = "公司注册地址")
	private String companyAddr;
	@ApiModelProperty(value = "企业地址")
    private String businessAddress;
	@ApiModelProperty(value = "企业英文地址")
    private String enBusinessAddress;
	@ApiModelProperty(value = "币种 01 人民币 02 日元 03 澳元 04 美元 05 港币 06 欧元  07 英镑")
	private String currency;	
	@ApiModelProperty(value = "内部公司ID")
    private Long innerMerchantId;
	@ApiModelProperty(value = "经营范围")
	private String operationScope;
	@ApiModelProperty(value = "传真")
    private String fax;
	@ApiModelProperty(value = "Email")
    private String email;
	@ApiModelProperty(value = "备注")
    private String remark;
	@ApiModelProperty(value = "开户银行")
	private String depositBank;
	@ApiModelProperty(value = "银行账号")
	private String bankAccount;
	@ApiModelProperty(value = "银行账户")
	private String beneficiaryName;
	@ApiModelProperty(value = "开户行地址")
	private String bankAddress;
	@ApiModelProperty(value = "Swift Code")
	private String swiftCode;
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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSimpleName() {
		return simpleName;
	}
	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCodeGrade() {
		return codeGrade;
	}
	public void setCodeGrade(String codeGrade) {
		this.codeGrade = codeGrade;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
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
	public String getMainId() {
		return mainId;
	}
	public void setMainId(String mainId) {
		this.mainId = mainId;
	}
	public String getSalesman() {
		return salesman;
	}
	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getCompanyAddr() {
		return companyAddr;
	}
	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}
	public String getBusinessAddress() {
		return businessAddress;
	}
	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}
	public String getEnBusinessAddress() {
		return enBusinessAddress;
	}
	public void setEnBusinessAddress(String enBusinessAddress) {
		this.enBusinessAddress = enBusinessAddress;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
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
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getDeleteIds() {
		return deleteIds;
	}
	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}
	
	
	
	


}
