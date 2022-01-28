package com.topideal.order.webapi.bill.form;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

@ApiModel
public class AdvancePaymentRecordItemForm extends PageForm implements Serializable{

	@ApiModelProperty("票据")
	private String token ;
    /**
    * ID
    */
	@ApiModelProperty("记录ID")
    private Long id;
    /**
    * 预付款单Id
    */
	@ApiModelProperty("预付款单Id")
    private Long advancePaymentId;
    /**
    * 本次付款日期
    */
	@ApiModelProperty("本次付款日期")
    private Timestamp paymentDate;
	@ApiModelProperty("本次付款日期字符串,格式yyyy-MM-dd")
    private String paymentDateStr;
    /**
    * 本次付款金额
    */
	@ApiModelProperty("本次付款金额")
    private BigDecimal paymentAmount;
    /**
    * 付款流水号
    */
	@ApiModelProperty("付款流水号")
    private String serialNo;
	
	/**
    * 核销单号-NC回传
    */
	@ApiModelProperty("核销单号-NC回传")
    private String verificationNo;
	
    /**
    * 创建人ID
    */
	@ApiModelProperty("创建人ID")
    private Long creatorId;
    /**
    * 创建人名
    */
	@ApiModelProperty("创建人名")
    private String creatorName;
    /**
    * 创建时间
    */
	@ApiModelProperty("创建时间")
    private Timestamp createDate;
    /**
    * 修改时间
    */
	@ApiModelProperty("修改时间")
    private Timestamp modifyDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*advancePaymentId get 方法 */
    public Long getAdvancePaymentId(){
    return advancePaymentId;
    }
    /*advancePaymentId set 方法 */
    public void setAdvancePaymentId(Long  advancePaymentId){
    this.advancePaymentId=advancePaymentId;
    }
    /*paymentDate get 方法 */
    public Timestamp getPaymentDate(){
    return paymentDate;
    }
    /*paymentDate set 方法 */
    public void setPaymentDate(Timestamp  paymentDate){
    this.paymentDate=paymentDate;
    }
    /*paymentAmount get 方法 */
    public BigDecimal getPaymentAmount(){
    return paymentAmount;
    }
    /*paymentAmount set 方法 */
    public void setPaymentAmount(BigDecimal  paymentAmount){
    this.paymentAmount=paymentAmount;
    }
    /*serialNo get 方法 */
    public String getSerialNo(){
    return serialNo;
    }
    /*serialNo set 方法 */
    public void setSerialNo(String  serialNo){
    this.serialNo=serialNo;
    }
    /*creatorId get 方法 */
    public Long getCreatorId(){
    return creatorId;
    }
    /*creatorId set 方法 */
    public void setCreatorId(Long  creatorId){
    this.creatorId=creatorId;
    }
    /*creatorName get 方法 */
    public String getCreatorName(){
    return creatorName;
    }
    /*creatorName set 方法 */
    public void setCreatorName(String  creatorName){
    this.creatorName=creatorName;
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
	public String getVerificationNo() {
		return verificationNo;
	}
	public void setVerificationNo(String verificationNo) {
		this.verificationNo = verificationNo;
	}
	public String getPaymentDateStr() {
		return paymentDateStr;
	}
	public void setPaymentDateStr(String paymentDateStr) {
		this.paymentDateStr = paymentDateStr;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}






}
