package com.topideal.entity.dto.main;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * 客户信息表
 */
public class CustomerInfoDTO extends PageModel implements Serializable {

	@ApiModelProperty(value = "法人电话")
	private String legalTel;
	@ApiModelProperty(value = "客户编码")
	private String code;
	@ApiModelProperty(value = "法人国籍")
	private String legalNationality;
	@ApiModelProperty(value = "公司注册地址")
	private String companyAddr;
	@ApiModelProperty(value = "公司电话")
	private String oTelNo;
	@ApiModelProperty(value = "创建人")
    private Long creater;
	@ApiModelProperty(value = "营业执照号")
	private String creditCode;
	@ApiModelProperty(value = "经营范围")
	private String operationScope;
	@ApiModelProperty(value = "组织机构代码")
	private String orgCode;
	@ApiModelProperty(value = "法人代表")
	private String legalPerson;
	@ApiModelProperty(value = "商家名称")
	private String name;
	@ApiModelProperty(value = "英文名")
	private String enName;
	@ApiModelProperty(value = "id")
	private Long id;
	@ApiModelProperty(value = "法人身份证")
	private String legalCardNo;
	@ApiModelProperty(value = "创建日期")
	private Timestamp createDate;
	@ApiModelProperty(value = "客户类型(1内部,2外部)")
    private String type;
	@ApiModelProperty(value = "typeLabel")
    private String typeLabel;
	@ApiModelProperty(value = "业务员")
    private String salesman;
	@ApiModelProperty(value = "状态(1使用中,0已禁用)")
    private String status;
	@ApiModelProperty(value = "id")
    private String statusLabel;
	@ApiModelProperty(value = "主数据客户id")
    private String mainId;
	@ApiModelProperty(value = "修改时间")
    private Timestamp updateDate;
	@ApiModelProperty(value = "授权码")
	private String authNo;
	@ApiModelProperty(value = "客户简称")
	private String simpleName;
	@ApiModelProperty(value = "企业性质")
    private String nature;
	@ApiModelProperty(value = "结算方式")
    private String settlementMode;
	@ApiModelProperty(value = "结算方式")
    private String settlementModeLabel;
	@ApiModelProperty(value = "传真")
    private String fax;
	@ApiModelProperty(value = "Email")
    private String email;
	@ApiModelProperty(value = "企业地址")
    private String businessAddress;
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
	@ApiModelProperty(value = "客户类型 1客户2供应商")
	private String cusType;
	@ApiModelProperty(value = "cusTypeLabel")
	private String cusTypeLabel ;
	@ApiModelProperty(value = "币种 ")
	private String currency;
	@ApiModelProperty(value = "currencyLabel")
	private String currencyLabel ;

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
	@ApiModelProperty(value = "客户等级编码")
    private String codeGrade;
	@ApiModelProperty(value = "省市区代码")
    private String cityCode;
	@ApiModelProperty(value = "英文简称")
    private String enSimpleName;
	@ApiModelProperty(value = "企业英文地址")
    private String enBusinessAddress;
	@ApiModelProperty(value = "备注")
    private String remark;
	@ApiModelProperty(value = "来源 1-主数据  2-录入/导入")
    private String source;
	@ApiModelProperty(value = "sourceLabel")
    private String sourceLabel;
	@ApiModelProperty(value = "内部公司ID")
    private Long innerMerchantId;
	@ApiModelProperty(value = "内部公司")
    private String innerMerchantName;
	@ApiModelProperty(value = "税号")
    private String taxNo;
	@ApiModelProperty(value = "账期")
    private String accountPeriod;
	@ApiModelProperty(value = "accountPeriodLabel")
    private String accountPeriodLabel;
	@ApiModelProperty(value = "列表查询关联商家Id")
    private Long merchantId ;
	@ApiModelProperty(value = "NC平台编码")
    private String ncPlatformCode ;
	@ApiModelProperty(value = "ncPlatformCodeLabel")
    private String ncPlatformCodeLabel;
	@ApiModelProperty(value = "NC渠道编码")
    private String ncChannelCode ;
	@ApiModelProperty(value = "ncChannelCodeLabel")
	private String ncChannelCodeLabel;
	@ApiModelProperty(value = "中文客户名称")
    private String chinaName;
	@ApiModelProperty(value = "结算类型")
    private String settlementType ;
	@ApiModelProperty(value = "中间表id")
	private Long relId;
	@ApiModelProperty(value = "关联公司逗号隔开用于显示")
	private String merchantNameStr;
	@ApiModelProperty(value = "是否启用采购价格管理 1-启用 0-不启用")
    private String purchasePriceManage;   
	@ApiModelProperty(value = "purchasePriceManageLabel")
    private String purchasePriceManageLabel;
	@ApiModelProperty(value = "渠道分类  1-2C平台 2-线下2B 3-线上2B")
    private String channelClassify;
    @ApiModelProperty(value = "channelClassifyLabel")
    private String channelClassifyLabel;
    
    

	public String getChannelClassify() {
		return channelClassify;
	}

	public void setChannelClassify(String channelClassify) {
		this.channelClassify = channelClassify;
		 if( channelClassify != null) {
	        	this.channelClassifyLabel = DERP_SYS.getLabelByKey(DERP_SYS.customerInfo_channelClassifyList, channelClassify);
	     }
	}

	public String getChannelClassifyLabel() {
		return channelClassifyLabel;
	}

	public void setChannelClassifyLabel(String channelClassifyLabel) {
		this.channelClassifyLabel = channelClassifyLabel;
	}

	public Long getRelId() {
		return relId;
	}

	public void setRelId(Long relId) {
		this.relId = relId;
	}

	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	/*businessAddress get 方法 */
    public String getBusinessAddress(){
        return businessAddress;
    }
    /*businessAddress set 方法 */
    public void setBusinessAddress(String  businessAddress){
        this.businessAddress=businessAddress;
    }
    /*email get 方法 */
    public String getEmail(){
        return email;
    }
    /*email set 方法 */
    public void setEmail(String  email){
        this.email=email;
    }
    /*fax get 方法 */
    public String getFax(){
        return fax;
    }
    /*fax set 方法 */
    public void setFax(String  fax){
        this.fax=fax;
    }
    /*settlementMode get 方法 */
    public String getSettlementMode(){
        return settlementMode;
    }
    /*settlementMode set 方法 */
    public void setSettlementMode(String  settlementMode){
        this.settlementMode=settlementMode;
        if( settlementMode != null) {
        	this.settlementModeLabel = DERP_SYS.getLabelByKey(DERP_SYS.supplier_settlementModeList, settlementMode);
        }
    }
    /*nature get 方法 */
    public String getNature(){
        return nature;
    }
    /*nature set 方法 */
    public void setNature(String  nature){
        this.nature=nature;
    }
    /*updateDate get 方法 */
    public Timestamp getUpdateDate(){
        return updateDate;
    }
    /*updateDate set 方法 */
    public void setUpdateDate(Timestamp  updateDate){
        this.updateDate=updateDate;
    }
	/* mainId get 方法 */
	public String getMainId() {
		return mainId;
	}
	/* mainId set 方法 */
	public void setMainId(String mainId) {
		this.mainId = mainId;
	}
	/* type get 方法 */
	public String getType() {
		return type;
	}
	/* type set 方法 */
	public void setType(String type) {
		this.type = type;
		if(type != null) {
			this.typeLabel = DERP_SYS.getLabelByKey(DERP_SYS.customerInfo_typeList, type);
		}
	}
	/* salesman get 方法 */
	public String getSalesman() {
		return salesman;
	}
	/* salesman set 方法 */
	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}
	/* status get 方法 */
	public String getStatus() {
		return status;
	}
	/* status set 方法 */
	public void setStatus(String status) {
		this.status = status;
		if(status != null) {
			this.statusLabel = DERP_SYS.getLabelByKey(DERP_SYS.customerInfo_statusList, status);
		}
	}

	/* cusType get 方法 */
	public String getCusType() {
		return cusType;
	}

	/* cusType set 方法 */
	public void setCusType(String cusType) {
		this.cusType = cusType;
		if(cusType != null) {
			this.cusTypeLabel = DERP_SYS.getLabelByKey(DERP_SYS.customerInfo_cusTypeList, cusType);
		}
	}

	/* legalTel get 方法 */
	public String getLegalTel() {
		return legalTel;
	}

	/* legalTel set 方法 */
	public void setLegalTel(String legalTel) {
		this.legalTel = legalTel;
	}

	/* code get 方法 */
	public String getCode() {
		return code;
	}

	/* code set 方法 */
	public void setCode(String code) {
		this.code = code;
	}

	/* legalNationality get 方法 */
	public String getLegalNationality() {
		return legalNationality;
	}

	/* legalNationality set 方法 */
	public void setLegalNationality(String legalNationality) {
		this.legalNationality = legalNationality;
	}

	/* companyAddr get 方法 */
	public String getCompanyAddr() {
		return companyAddr;
	}

	/* companyAddr set 方法 */
	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}

	/* legalTel2 get 方法 */
	public String getOTelNo() {
		return oTelNo;
	}

	/* legalTel2 set 方法 */
	public void setOTelNo(String oTelNo) {
		this.oTelNo = oTelNo;
	}
	/* creater set 方法 */
	public Long getCreater() {
		return creater;
	}
	/* creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}
	/* creditCode get 方法 */
	public String getCreditCode() {
		return creditCode;
	}

	/* creditCode set 方法 */
	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}

	/* operationScope get 方法 */
	public String getOperationScope() {
		return operationScope;
	}

	/* operationScope set 方法 */
	public void setOperationScope(String operationScope) {
		this.operationScope = operationScope;
	}

	/* orgCode get 方法 */
	public String getOrgCode() {
		return orgCode;
	}

	/* orgCode set 方法 */
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	/* legalPerson get 方法 */
	public String getLegalPerson() {
		return legalPerson;
	}

	/* legalPerson set 方法 */
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	/* name get 方法 */
	public String getName() {
		return name;
	}

	/* name set 方法 */
	public void setName(String name) {
		this.name = name;
	}

	/* enName get 方法 */
	public String getEnName() {
		return enName;
	}

	/* enName set 方法 */
	public void setEnName(String enName) {
		this.enName = enName;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* legalCardNo get 方法 */
	public String getLegalCardNo() {
		return legalCardNo;
	}

	/* legalCardNo set 方法 */
	public void setLegalCardNo(String legalCardNo) {
		this.legalCardNo = legalCardNo;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
		if(currency != null) {
			this.currencyLabel = DERP_SYS.getLabelByKey(DERP.currencyCodeList, currency);
		}
	}
	public String getTypeLabel() {
		return typeLabel;
	}
	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	public String getCusTypeLabel() {
		return cusTypeLabel;
	}
	public void setCusTypeLabel(String cusTypeLabel) {
		this.cusTypeLabel = cusTypeLabel;
	}
	public String getCurrencyLabel() {
		return currencyLabel;
	}
	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
	}
	public String getSettlementModeLabel() {
		return settlementModeLabel;
	}
	public void setSettlementModeLabel(String settlementModeLabel) {
		this.settlementModeLabel = settlementModeLabel;
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
	public String getoTelNo() {
		return oTelNo;
	}
	public void setoTelNo(String oTelNo) {
		this.oTelNo = oTelNo;
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
	public String getEnSimpleName() {
		return enSimpleName;
	}
	public void setEnSimpleName(String enSimpleName) {
		this.enSimpleName = enSimpleName;
	}
	public String getEnBusinessAddress() {
		return enBusinessAddress;
	}
	public void setEnBusinessAddress(String enBusinessAddress) {
		this.enBusinessAddress = enBusinessAddress;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
		this.sourceLabel = DERP_SYS.getLabelByKey(DERP_SYS.customerInfo_sourceList, source) ;
	}
	public String getSourceLabel() {
		return sourceLabel;
	}
	public void setSourceLabel(String sourceLabel) {
		this.sourceLabel = sourceLabel;
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
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getTaxNo() {
		return taxNo;
	}
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
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
	public String getNcPlatformCode() {
		return ncPlatformCode;
	}
	public void setNcPlatformCode(String ncPlatformCode) {
		this.ncPlatformCode = ncPlatformCode;
		this.ncPlatformCodeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.platform_codeList, this.ncPlatformCode);
	}
	public String getNcChannelCode() {
		return ncChannelCode;
	}
	public void setNcChannelCode(String ncChannelCode) {
		this.ncChannelCode = ncChannelCode;
		this.ncChannelCodeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.channel_codeList, this.ncChannelCode);
	}

	public String getNcPlatformCodeLabel() {
		return ncPlatformCodeLabel;
	}

	public void setNcPlatformCodeLabel(String ncPlatformCodeLabel) {
		this.ncPlatformCodeLabel = ncPlatformCodeLabel;
	}

	public String getNcChannelCodeLabel() {
		return ncChannelCodeLabel;
	}

	public void setNcChannelCodeLabel(String ncChannelCodeLabel) {
		this.ncChannelCodeLabel = ncChannelCodeLabel;
	}

	public String getSettlementType() {
		return settlementType;
	}

	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}

	public String getChinaName() {
		return chinaName;
	}

	public void setChinaName(String chinaName) {
		this.chinaName = chinaName;
	}

	public String getMerchantNameStr() {
		return merchantNameStr;
	}

	public void setMerchantNameStr(String merchantNameStr) {
		this.merchantNameStr = merchantNameStr;
	}

	public String getPurchasePriceManage() {
		return purchasePriceManage;
	}

	public void setPurchasePriceManage(String purchasePriceManage) {
		this.purchasePriceManage = purchasePriceManage;
		this.purchasePriceManageLabel = DERP_ORDER.getLabelByKey(DERP_SYS.customerInfo_purchasePriceManageList, this.ncChannelCode);
	}

	public String getPurchasePriceManageLabel() {
		return purchasePriceManageLabel;
	}

	public void setPurchasePriceManageLabel(String purchasePriceManageLabel) {
		this.purchasePriceManageLabel = purchasePriceManageLabel;
	}
	
}
