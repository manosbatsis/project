package com.topideal.entity.dto.bill;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PaymentVerificateItemDTO extends PageModel{

    /**
    * 记录ID
    */
	@ApiModelProperty("记录ID")
    private Long id;
    /**
    * 应付账单
    */
	@ApiModelProperty("应付账单")
    private Long paymentId;
    /**
    * 核销单号
    */
	@ApiModelProperty("核销单号")
    private String relCode;
    /**
    * 单据类型1-预付 2-NC
    */
	@ApiModelProperty("单据类型1-预付 2-NC")
    private String billStatus;
	@ApiModelProperty("单据类型中文1-预付 2-NC")
    private String billStatusLabel;
    /**
    * 付款人ID
    */
	@ApiModelProperty("付款人ID")
    private Long draweeId;
    /**
    * 付款人
    */
	@ApiModelProperty("付款人")
    private String drawee;
    /**
    * 付款时间
    */
	@ApiModelProperty("付款时间")
    private Timestamp paymentDate;
    /**
    * 付款流水单号
    */
	@ApiModelProperty("付款流水单号")
    private String serialNo;
    /**
    * 待核销金额
    */
	@ApiModelProperty("待核销金额")
    private BigDecimal verificateAmount;
    /**
    * 本次核销金额
    */
	@ApiModelProperty("本次核销金额")
    private BigDecimal currentVerificateAmount;
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
	
	//勾稽预付款单---采购订单
	@ApiModelProperty(value="采购单号")
    private String purchaseCode;
	/**
    * 勾稽预付款单---申请付款金额
    */
    @ApiModelProperty(value="预付款金额", required=true)
    private BigDecimal prepaymentAmount;
    /**
     * nc同步唯一id
     */
    @ApiModelProperty(value=" nc同步唯一id")
    private Long ncId;
    
    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*paymentId get 方法 */
    public Long getPaymentId(){
    return paymentId;
    }
    /*paymentId set 方法 */
    public void setPaymentId(Long  paymentId){
    this.paymentId=paymentId;
    }
    /*relCode get 方法 */
    public String getRelCode(){
    return relCode;
    }
    /*relCode set 方法 */
    public void setRelCode(String  relCode){
    this.relCode=relCode;
    }
    /*billStatus get 方法 */
    public String getBillStatus(){
    return billStatus;
    }
    /*billStatus set 方法 */
    public void setBillStatus(String  billStatus){
    this.billStatus=billStatus;
    this.billStatusLabel=DERP_ORDER.getLabelByKey(DERP_ORDER.paymentBillVerificate_billStatusList, billStatus);
    }
    /*draweeId get 方法 */
    public Long getDraweeId(){
    return draweeId;
    }
    /*draweeId set 方法 */
    public void setDraweeId(Long  draweeId){
    this.draweeId=draweeId;
    }
    /*drawee get 方法 */
    public String getDrawee(){
    return drawee;
    }
    /*drawee set 方法 */
    public void setDrawee(String  drawee){
    this.drawee=drawee;
    }
    /*paymentDate get 方法 */
    public Timestamp getPaymentDate(){
    return paymentDate;
    }
    /*paymentDate set 方法 */
    public void setPaymentDate(Timestamp  paymentDate){
    this.paymentDate=paymentDate;
    }
    /*serialNo get 方法 */
    public String getSerialNo(){
    return serialNo;
    }
    /*serialNo set 方法 */
    public void setSerialNo(String  serialNo){
    this.serialNo=serialNo;
    }
    /*verificateAmount get 方法 */
    public BigDecimal getVerificateAmount(){
    return verificateAmount;
    }
    /*verificateAmount set 方法 */
    public void setVerificateAmount(BigDecimal  verificateAmount){
    this.verificateAmount=verificateAmount;
    }
    /*currentVerificateAmount get 方法 */
    public BigDecimal getCurrentVerificateAmount(){
    return currentVerificateAmount;
    }
    /*currentVerificateAmount set 方法 */
    public void setCurrentVerificateAmount(BigDecimal  currentVerificateAmount){
    this.currentVerificateAmount=currentVerificateAmount;
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
	public String getBillStatusLabel() {
		return billStatusLabel;
	}
	public void setBillStatusLabel(String billStatusLabel) {
		this.billStatusLabel = billStatusLabel;
	}
	public String getPurchaseCode() {
		return purchaseCode;
	}
	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}
	public BigDecimal getPrepaymentAmount() {
		return prepaymentAmount;
	}
	public void setPrepaymentAmount(BigDecimal prepaymentAmount) {
		this.prepaymentAmount = prepaymentAmount;
	}
	public Long getNcId() {
		return ncId;
	}
	public void setNcId(Long ncId) {
		this.ncId = ncId;
	}






}
