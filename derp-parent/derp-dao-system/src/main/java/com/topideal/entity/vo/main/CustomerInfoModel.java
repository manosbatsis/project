package com.topideal.entity.vo.main;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class CustomerInfoModel extends PageModel implements Serializable{

    /**
    * 自增长ID
    */
	@ApiModelProperty(value = "id")
    private Long id;
    /**
    * 客户编码
    */
	@ApiModelProperty(value = "客户编码")
    private String code;
    /**
    * 客户名称
    */
	@ApiModelProperty(value = "客户名称")
    private String name;
    /**
    * 营业执照号
    */
	@ApiModelProperty(value = "营业执照号")
    private String creditCode;
    /**
    * 客户等级编码
    */
	@ApiModelProperty(value = "客户等级编码")
    private String codeGrade;
    /**
    * 省市区代码
    */
	@ApiModelProperty(value = "省市区代码")
    private String cityCode;
    /**
    * 组织机构代码
    */
	@ApiModelProperty(value = "组织机构代码")
    private String orgCode;
    /**
    * 英文名
    */
	@ApiModelProperty(value = "英文名")
    private String enName;
    /**
    * 英文简称
    */
	@ApiModelProperty(value = "英文简称")
    private String enSimpleName;
    /**
    * 公司注册地址
    */
	@ApiModelProperty(value = "公司注册地址")
    private String companyAddr;
    /**
    * 经营范围
    */
	@ApiModelProperty(value = "经营范围")
    private String operationScope;
    /**
    * 法人代表
    */
	@ApiModelProperty(value = "法人代表")
    private String legalPerson;
    /**
    * 法人国籍
    */
	@ApiModelProperty(value = "法人国籍")
    private String legalNationality;
    /**
    * 法人身份证
    */
	@ApiModelProperty(value = "法人身份证")
    private String legalCardNo;
    /**
    * 法人电话
    */
	@ApiModelProperty(value = "法人电话")
    private String legalTel;
    /**
    * 公司电话
    */
	@ApiModelProperty(value = "公司电话")
    private String oTelNo;
    /**
    * 客户类型(1内部,2外部)
    */
	@ApiModelProperty(value = "客户类型(1内部,2外部)")
    private String type;
    /**
    * 业务员
    */
	@ApiModelProperty(value = "业务员")
    private String salesman;
    /**
    *  状态(1使用中,0已禁用)
    */
	@ApiModelProperty(value = "状态(1使用中,0已禁用)")
    private String status;
    /**
    * 主数据客户id
    */
	@ApiModelProperty(value = "主数据客户id")
    private String mainId;
    /**
    * 授权码
    */
	@ApiModelProperty(value = "授权码")
    private String authNo;
    /**
    * 客户简称
    */
	@ApiModelProperty(value = "客户简称")
    private String simpleName;
    /**
    * 企业性质
    */
	@ApiModelProperty(value = "企业性质")
    private String nature;
    /**
    * 结算方式(1.预付 2.到付 3.月结)
    */
	@ApiModelProperty(value = "结算方式(1.预付 2.到付 3.月结)")
    private String settlementMode;
    /**
    * 企业地址
    */
	@ApiModelProperty(value = "企业地址")
    private String businessAddress;
    /**
    * 企业英文地址
    */
	@ApiModelProperty(value = "企业英文地址")
    private String enBusinessAddress;
    /**
    * 传真
    */
	@ApiModelProperty(value = "传真")
    private String fax;
    /**
    * Email
    */
	@ApiModelProperty(value = "Email")
    private String email;
    /**
    * 类型  1 客户  2 供应商
    */
	@ApiModelProperty(value = "类型  1 客户  2 供应商")
    private String cusType;
    /**
    * 币种 
    */
	@ApiModelProperty(value = "币种 ")
    private String currency;
    /**
    * 开户银行
    */
	@ApiModelProperty(value = "开户银行")
    private String depositBank;
    /**
    * 银行账号
    */
	@ApiModelProperty(value = "银行账号")
    private String bankAccount;
    /**
    * 银行账户
    */
	@ApiModelProperty(value = "银行账户")
    private String beneficiaryName;
    /**
    * 开户行地址
    */
	@ApiModelProperty(value = "开户行地址")
    private String bankAddress;
    /**
    * Swift Code
    */
	@ApiModelProperty(value = "Swift Code")
    private String swiftCode;
    /**
    * 备注
    */
	@ApiModelProperty(value = "备注")
    private String remark;
    /**
    * 创建人
    */
	@ApiModelProperty(value = "创建人")
    private Long creater;
    /**
    * 创建时间
    */
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    /**
    * 修改时间
    */
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    /**
     * 来源 1-主数据  2-录入/导入
     */
	@ApiModelProperty(value = "来源 1-主数据  2-录入/导入")
    private String source;

    /**
     * 内部公司ID
     */
	@ApiModelProperty(value = "内部公司ID")
    private Long innerMerchantId;
    /**
     * 内部公司
     */
	@ApiModelProperty(value = "内部公司")
    private String innerMerchantName;
    // 税号
	@ApiModelProperty(value = "税号")
    private String taxNo;
    
    /**
     * NC平台编码
     */
	@ApiModelProperty(value = "NC平台编码")
    private String ncPlatformCode ;
    
    /**
     * NC渠道编码
     */
	@ApiModelProperty(value = "NC渠道编码")
    private String ncChannelCode ;
    /**
     * 中文客户名称
     */
	@ApiModelProperty(value = "中文客户名称")
    private String chinaName;

    /**
     * 渠道分类  1-2C平台 2-线下2B 3-线上2B
     */
	@ApiModelProperty(value = "渠道分类  1-2C平台 2-线下2B 3-线上2B")
    private String channelClassify;
    

    public String getChinaName() {
		return chinaName;
	}
	public void setChinaName(String chinaName) {
		this.chinaName = chinaName;
	}
	public String getTaxNo() {
		return taxNo;
	}
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
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
    /*name get 方法 */
    public String getName(){
    return name;
    }
    /*name set 方法 */
    public void setName(String  name){
    this.name=name;
    }
    /*creditCode get 方法 */
    public String getCreditCode(){
    return creditCode;
    }
    /*creditCode set 方法 */
    public void setCreditCode(String  creditCode){
    this.creditCode=creditCode;
    }
    /*codeGrade get 方法 */
    public String getCodeGrade(){
    return codeGrade;
    }
    /*codeGrade set 方法 */
    public void setCodeGrade(String  codeGrade){
    this.codeGrade=codeGrade;
    }
    /*cityCode get 方法 */
    public String getCityCode(){
    return cityCode;
    }
    /*cityCode set 方法 */
    public void setCityCode(String  cityCode){
    this.cityCode=cityCode;
    }
    /*orgCode get 方法 */
    public String getOrgCode(){
    return orgCode;
    }
    /*orgCode set 方法 */
    public void setOrgCode(String  orgCode){
    this.orgCode=orgCode;
    }
    /*enName get 方法 */
    public String getEnName(){
    return enName;
    }
    /*enName set 方法 */
    public void setEnName(String  enName){
    this.enName=enName;
    }
    /*enSimpleName get 方法 */
    public String getEnSimpleName(){
    return enSimpleName;
    }
    /*enSimpleName set 方法 */
    public void setEnSimpleName(String  enSimpleName){
    this.enSimpleName=enSimpleName;
    }
    /*companyAddr get 方法 */
    public String getCompanyAddr(){
    return companyAddr;
    }
    /*companyAddr set 方法 */
    public void setCompanyAddr(String  companyAddr){
    this.companyAddr=companyAddr;
    }
    /*operationScope get 方法 */
    public String getOperationScope(){
    return operationScope;
    }
    /*operationScope set 方法 */
    public void setOperationScope(String  operationScope){
    this.operationScope=operationScope;
    }
    /*legalPerson get 方法 */
    public String getLegalPerson(){
    return legalPerson;
    }
    /*legalPerson set 方法 */
    public void setLegalPerson(String  legalPerson){
    this.legalPerson=legalPerson;
    }
    /*legalNationality get 方法 */
    public String getLegalNationality(){
    return legalNationality;
    }
    /*legalNationality set 方法 */
    public void setLegalNationality(String  legalNationality){
    this.legalNationality=legalNationality;
    }
    /*legalCardNo get 方法 */
    public String getLegalCardNo(){
    return legalCardNo;
    }
    /*legalCardNo set 方法 */
    public void setLegalCardNo(String  legalCardNo){
    this.legalCardNo=legalCardNo;
    }
    /*legalTel get 方法 */
    public String getLegalTel(){
    return legalTel;
    }
    /*legalTel set 方法 */
    public void setLegalTel(String  legalTel){
    this.legalTel=legalTel;
    }
    /*oTelNo get 方法 */
    public String getOTelNo(){
    return oTelNo;
    }
    /*oTelNo set 方法 */
    public void setOTelNo(String  oTelNo){
    this.oTelNo=oTelNo;
    }
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
    }
    /*salesman get 方法 */
    public String getSalesman(){
    return salesman;
    }
    /*salesman set 方法 */
    public void setSalesman(String  salesman){
    this.salesman=salesman;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    }
    /*mainId get 方法 */
    public String getMainId(){
    return mainId;
    }
    /*mainId set 方法 */
    public void setMainId(String  mainId){
    this.mainId=mainId;
    }
    /*authNo get 方法 */
    public String getAuthNo(){
    return authNo;
    }
    /*authNo set 方法 */
    public void setAuthNo(String  authNo){
    this.authNo=authNo;
    }
    /*simpleName get 方法 */
    public String getSimpleName(){
    return simpleName;
    }
    /*simpleName set 方法 */
    public void setSimpleName(String  simpleName){
    this.simpleName=simpleName;
    }
    /*nature get 方法 */
    public String getNature(){
    return nature;
    }
    /*nature set 方法 */
    public void setNature(String  nature){
    this.nature=nature;
    }
    /*settlementMode get 方法 */
    public String getSettlementMode(){
    return settlementMode;
    }
    /*settlementMode set 方法 */
    public void setSettlementMode(String  settlementMode){
    this.settlementMode=settlementMode;
    }
    /*businessAddress get 方法 */
    public String getBusinessAddress(){
    return businessAddress;
    }
    /*businessAddress set 方法 */
    public void setBusinessAddress(String  businessAddress){
    this.businessAddress=businessAddress;
    }
    /*enBusinessAddress get 方法 */
    public String getEnBusinessAddress(){
    return enBusinessAddress;
    }
    /*enBusinessAddress set 方法 */
    public void setEnBusinessAddress(String  enBusinessAddress){
    this.enBusinessAddress=enBusinessAddress;
    }
    /*fax get 方法 */
    public String getFax(){
    return fax;
    }
    /*fax set 方法 */
    public void setFax(String  fax){
    this.fax=fax;
    }
    /*email get 方法 */
    public String getEmail(){
    return email;
    }
    /*email set 方法 */
    public void setEmail(String  email){
    this.email=email;
    }
    /*cusType get 方法 */
    public String getCusType(){
    return cusType;
    }
    /*cusType set 方法 */
    public void setCusType(String  cusType){
    this.cusType=cusType;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*depositBank get 方法 */
    public String getDepositBank(){
    return depositBank;
    }
    /*depositBank set 方法 */
    public void setDepositBank(String  depositBank){
    this.depositBank=depositBank;
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
    /*bankAddress get 方法 */
    public String getBankAddress(){
    return bankAddress;
    }
    /*bankAddress set 方法 */
    public void setBankAddress(String  bankAddress){
    this.bankAddress=bankAddress;
    }
    /*swiftCode get 方法 */
    public String getSwiftCode(){
    return swiftCode;
    }
    /*swiftCode set 方法 */
    public void setSwiftCode(String  swiftCode){
    this.swiftCode=swiftCode;
    }
    /*remark get 方法 */
    public String getRemark(){
    return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
    this.remark=remark;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
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

    public String getoTelNo() {
        return oTelNo;
    }

    public void setoTelNo(String oTelNo) {
        this.oTelNo = oTelNo;
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

    public String getChannelClassify() {
        return channelClassify;
    }

    public void setChannelClassify(String channelClassify) {
        this.channelClassify = channelClassify;
    }
}
