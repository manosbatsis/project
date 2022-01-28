package com.topideal.entity.dto.main;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.vo.main.CustomerBankMerchantRelModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * 客户银行信息
 * @author 杨创
 *
 */
public class CustomerBankDTO extends PageModel implements Serializable{

    /**
    * 自增长ID
    */
	@ApiModelProperty(value = "自增长ID")
    private Long id;
    /**
    * 客户id
    */
	@ApiModelProperty(value = "客户id")
    private Long customerId;
    /**
    * 客户名称
    */
	@ApiModelProperty(value = "客户名称")
    private String customerName;
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
    * 修改时间
    */
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
    /**
    * 创建时间
    */
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

    // 商家客户银行信息
	@ApiModelProperty(value = "客户银行对应的商家ids 多个用逗号隔开")
	private String merchantIdStr;
	@ApiModelProperty(value = "客户银行对应的商家名称   多个用逗号隔开")
	private String merchantNameStr;
    
    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*customerId get 方法 */
    public Long getCustomerId(){
    return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
    this.customerId=customerId;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
    return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
    this.customerName=customerName;
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
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
	public String getMerchantIdStr() {
		return merchantIdStr;
	}
	public void setMerchantIdStr(String merchantIdStr) {
		this.merchantIdStr = merchantIdStr;
	}
	public String getMerchantNameStr() {
		return merchantNameStr;
	}
	public void setMerchantNameStr(String merchantNameStr) {
		this.merchantNameStr = merchantNameStr;
	}
	
	






}
