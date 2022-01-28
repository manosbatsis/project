package com.topideal.mongo.entity;


/**
 * 客户信息表
 */
public class CustomerInfoMogo {
	/**
    * 自增长ID
    */
    private Long customerid;
    /**
    * 主数据同步表关联id
    */
    private Long mainRelId;
    /**
    * 客户编码
    */
    private String code;
    /**
    * 客户名称
    */
    private String name;
    /**
    * 营业执照号
    */
    private String creditCode;
    /**
    * 客户等级编码
    */
    private String codeGrade;
    /**
    * 省市区代码
    */
    private String cityCode;
    /**
    * 组织机构代码
    */
    private String orgCode;
    /**
    * 英文名
    */
    private String enName;
    /**
    * 英文简称
    */
    private String enSimpleName;
    /**
    * 公司注册地址
    */
    private String companyAddr;
    /**
    * 经营范围
    */
    private String operationScope;
    /**
    * 法人代表
    */
    private String legalPerson;
    /**
    * 法人国籍
    */
    private String legalNationality;
    /**
    * 法人身份证
    */
    private String legalCardNo;
    /**
    * 法人电话
    */
    private String legalTel;
    /**
    * 公司电话
    */
    private String oTelNo;
    /**
    * 客户类型(1内部,2外部)
    */
    private String type;
    /**
    * 业务员
    */
    private String salesman;
    /**
    *  状态(1使用中,0已禁用)
    */
    private String status;
    /**
    * 主数据客户id
    */
    private String mainId;
    /**
    * 授权码
    */
    private String authNo;
    /**
    * 客户简称
    */
    private String simpleName;
    /**
    * 企业性质
    */
    private String nature;
    /**
    * 结算方式(1.预付 2.到付 3.月结)
    */
    private String settlementMode;
    /**
    * 企业地址
    */
    private String businessAddress;
    /**
    * 企业英文地址
    */
    private String enBusinessAddress;
    /**
    * 传真
    */
    private String fax;
    /**
    * Email
    */
    private String email;
    /**
    * 类型  1 客户  2 供应商
    */
    private String cusType;
    /**
    * 币种 
    */
    private String currency;
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
    /**
    * 备注
    */
    private String remark;
    /**
    * 创建人
    */
    private Long creater;

    /**
     * 来源 1-主数据  2-录入/导入
     */
    private String source;

    /**
     * 内部公司ID
     */
    private Long innerMerchantId;
    /**
     * 内部公司
     */
    private String innerMerchantName;
	// 税号
	private String taxNo;
	/**
	 * NC平台编码
	 */
	private String ncPlatformCode ;

	/**
	 * NC渠道编码
	 */
	private String ncChannelCode ;
    /**
     * 中文客户名称
     */
    private String chinaName;
    
	public String getChinaName() {
		return chinaName;
	}
	public void setChinaName(String chinaName) {
		this.chinaName = chinaName;
	}
	public Long getCustomerid() {
		return customerid;
	}
	public void setCustomerId(Long customerid) {
		this.customerid = customerid;
	}
	public Long getMainRelId() {
		return mainRelId;
	}
	public void setMainRelId(Long mainRelId) {
		this.mainRelId = mainRelId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public String getCodeGrade() {
		return codeGrade;
	}
	public void setCodeGrade(String codeGrade) {
		this.codeGrade = codeGrade;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	public String getEnSimpleName() {
		return enSimpleName;
	}
	public void setEnSimpleName(String enSimpleName) {
		this.enSimpleName = enSimpleName;
	}
	public String getCompanyAddr() {
		return companyAddr;
	}
	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}
	public String getOperationScope() {
		return operationScope;
	}
	public void setOperationScope(String operationScope) {
		this.operationScope = operationScope;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSalesman() {
		return salesman;
	}
	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMainId() {
		return mainId;
	}
	public void setMainId(String mainId) {
		this.mainId = mainId;
	}
	public String getAuthNo() {
		return authNo;
	}
	public void setAuthNo(String authNo) {
		this.authNo = authNo;
	}
	public String getSimpleName() {
		return simpleName;
	}
	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public String getSettlementMode() {
		return settlementMode;
	}
	public void setSettlementMode(String settlementMode) {
		this.settlementMode = settlementMode;
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
	public String getCusType() {
		return cusType;
	}
	public void setCusType(String cusType) {
		this.cusType = cusType;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getCreater() {
		return creater;
	}
	public void setCreater(Long creater) {
		this.creater = creater;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Long getInnerMerchantId() {
		return innerMerchantId;
	}
	public void setInnerMerchantId(Long innerMerchantId) {
		this.innerMerchantId = innerMerchantId;
	}
	public String getInnerMerchantName() {
		return innerMerchantName;
	}
	public void setInnerMerchantName(String innerMerchantName) {
		this.innerMerchantName = innerMerchantName;
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

}
