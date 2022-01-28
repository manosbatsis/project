package com.topideal.entity.vo.main;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 商家信息
 * @author 
 *
 */
public class MerchantInfoModel extends PageModel implements Serializable{



	 @ApiModelProperty(value = "商家编码")
     private String code;
	 @ApiModelProperty(value = "卓志编码")
     private String topidealCode;
	 @ApiModelProperty(value = "商家简称")
     private String name;
	 @ApiModelProperty(value = "创建人")
     private Long creater;
	 @ApiModelProperty(value = "备注")
     private String remark;
	 @ApiModelProperty(value = "联系电话")
     private String tel;
	 @ApiModelProperty(value = "自增长ID")
     private Long id;
	 @ApiModelProperty(value = "电子邮箱 企业")
     private String email;
	 @ApiModelProperty(value = "创建日期")
     private Timestamp createDate;
	 @ApiModelProperty(value = "是否绑定商家")
     private String isBindUser;
	 @ApiModelProperty(value = "是否代理 0商家 1 代理商家")
     private String isProxy;
	 @ApiModelProperty(value = "授权码")
     private String permission;
	 @ApiModelProperty(value = "商家简称")
     private String simpleMerchant;
	 @ApiModelProperty(value = "修改时间")
     private Timestamp modifyDate;
	 @ApiModelProperty(value = "盘点结果通知邮箱")
     private String inventoryResultEmail;
	 @ApiModelProperty(value = "提醒财务付款邮箱")
     private String financePayEmail;
	 @ApiModelProperty(value = "注册地址")
     private String registeredAddress;
	 @ApiModelProperty(value = "商家全称")
     private String fullName;
	 @ApiModelProperty(value = "勾稽百分比")
     private Double  articulationPercent;
	 @ApiModelProperty(value = "商家简称")
     private String yname;
	 @ApiModelProperty(value = "英文名称")
     private String englishName;
	 @ApiModelProperty(value = "成本核算币种")
     private String accountCurrency;
	 @ApiModelProperty(value = "核算单价 1-标准成本单价 2-月末加权单价")
     private String accountPrice;
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
	 @ApiModelProperty(value = "英文注册地")
     private String englishRegisteredAddress;
     @ApiModelProperty(value = "注册地类型 1、境内；2、境外")
     private String registeredType;
    /**
     * 状态(1启用,0禁用)
     */
     @ApiModelProperty(value = "状态(1启用,0禁用)")
     private String status;
     @ApiModelProperty(value = "注册登记证")
     private String registrationCert;

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

