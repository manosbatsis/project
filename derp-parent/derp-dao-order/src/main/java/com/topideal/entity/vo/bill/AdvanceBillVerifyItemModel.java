package com.topideal.entity.vo.bill;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;

public class AdvanceBillVerifyItemModel extends PageModel implements Serializable{

    /**
    * ID
    */
    private Long id;
    /**
    * 预收账单Id
    */
    private Long advanceId;
    /**
    * 核销金额
    */
    private BigDecimal price;
    /**
    * 收款日期
    */
    private Date verifyDate;
    /**
    * 收款流水号
    */
    private String verifyNo;
    /**
    * 核销人ID
    */
    private Long verifierId;
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

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*advanceId get 方法 */
    public Long getAdvanceId(){
    return advanceId;
    }
    /*advanceId set 方法 */
    public void setAdvanceId(Long  advanceId){
    this.advanceId=advanceId;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
    return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
    this.price=price;
    }
    /*verifyDate get 方法 */
    public Date getVerifyDate(){
    return verifyDate;
    }
    /*verifyDate set 方法 */
    public void setVerifyDate(Date  verifyDate){
    this.verifyDate=verifyDate;
    }
    /*verifyNo get 方法 */
    public String getVerifyNo(){
    return verifyNo;
    }
    /*verifyNo set 方法 */
    public void setVerifyNo(String  verifyNo){
    this.verifyNo=verifyNo;
    }
    /*verifierId get 方法 */
    public Long getVerifierId(){
    return verifierId;
    }
    /*verifierId set 方法 */
    public void setVerifierId(Long  verifierId){
    this.verifierId=verifierId;
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






}
