package com.topideal.entity.vo.bill;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class PaymentVerificateItemModel extends PageModel implements Serializable{

    /**
    * 记录ID
    */
    private Long id;
    /**
    * 应付账单
    */
    private Long paymentId;
    /**
    * 核销单号
    */
    private String relCode;
    /**
    * 单据类型1-预付 2-NC
    */
    private String billStatus;
    /**
    * 付款人ID
    */
    private Long draweeId;
    /**
    * 付款人
    */
    private String drawee;
    /**
    * 付款时间
    */
    private Timestamp paymentDate;
    /**
    * 付款流水单号
    */
    private String serialNo;
    /**
    * 待核销金额
    */
    private BigDecimal verificateAmount;
    /**
    * 本次核销金额
    */
    private BigDecimal currentVerificateAmount;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     * nc同步唯一id
     */
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
	public Long getNcId() {
		return ncId;
	}
	public void setNcId(Long ncId) {
		this.ncId = ncId;
	}






}
