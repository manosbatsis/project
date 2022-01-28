package com.topideal.entity.vo.bill;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class TocSettlementReceiveBillVerifyItemModel extends PageModel implements Serializable{

    /**
    * ID
    */
    private Long id;
    /**
    * 账单Id
    */
    private Long billId;
    /**
    * 核销金额
    */
    private BigDecimal price;
    /**
    * 收款日期
    */
    private Timestamp receiveDate;
    /**
    * 收款流水号
    */
    private String receiceNo;
    /**
    * 核销时间
    */
    private Timestamp verifyDate;
    /**
    * 核销人ID
    */
    private Long verifyId;
    /**
    * 核销人名称
    */
    private String verifier;
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

    //核销月份
    private String verifyMonth;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*billId get 方法 */
    public Long getBillId(){
    return billId;
    }
    /*billId set 方法 */
    public void setBillId(Long  billId){
    this.billId=billId;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
    return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
    this.price=price;
    }
    /*receiveDate get 方法 */
    public Timestamp getReceiveDate(){
    return receiveDate;
    }
    /*receiveDate set 方法 */
    public void setReceiveDate(Timestamp  receiveDate){
    this.receiveDate=receiveDate;
    }
    /*receiceNo get 方法 */
    public String getReceiceNo(){
    return receiceNo;
    }
    /*receiceNo set 方法 */
    public void setReceiceNo(String  receiceNo){
    this.receiceNo=receiceNo;
    }
    /*verifyDate get 方法 */
    public Timestamp getVerifyDate(){
    return verifyDate;
    }
    /*verifyDate set 方法 */
    public void setVerifyDate(Timestamp  verifyDate){
    this.verifyDate=verifyDate;
    }
    /*verifyId get 方法 */
    public Long getVerifyId(){
    return verifyId;
    }
    /*verifyId set 方法 */
    public void setVerifyId(Long  verifyId){
    this.verifyId=verifyId;
    }
    /*verifier get 方法 */
    public String getVerifier(){
    return verifier;
    }
    /*verifier set 方法 */
    public void setVerifier(String  verifier){
    this.verifier=verifier;
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

    public String getVerifyMonth() {
        return verifyMonth;
    }

    public void setVerifyMonth(String verifyMonth) {
        this.verifyMonth = verifyMonth;
    }
}
