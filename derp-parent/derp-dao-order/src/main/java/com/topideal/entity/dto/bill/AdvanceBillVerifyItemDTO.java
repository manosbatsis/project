package com.topideal.entity.dto.bill;

import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 预收账单核销记录DTO
 */
@ApiModel
public class AdvanceBillVerifyItemDTO extends PageModel implements Serializable {

    @ApiModelProperty(value = "id", required = false)
    private Long id;

    @ApiModelProperty(value = "预收账单Id", required = false)
    private Long advanceId;

    @ApiModelProperty(value = "核销金额", required = false)
    private BigDecimal price;

    @ApiModelProperty(value = "收款日期", required = false)
    private Date verifyDate;

    @ApiModelProperty(value = "收款日期字符串", required = false)
    private String verifyDateStr;

    @ApiModelProperty(value = "收款流水号", required = false)
    private String verifyNo;

    @ApiModelProperty(value = "核销人ID", required = false)
    private Long verifierId;

    @ApiModelProperty(value = "核销人名称", required = false)
    private String verifier;

    @ApiModelProperty(value = "创建时间", required = false)
    private Timestamp createDate;

    @ApiModelProperty(value = "修改时间", required = false)
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
    public void setPrice(BigDecimal price){
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

    public String getVerifyDateStr() {
        return verifyDateStr;
    }

    public void setVerifyDateStr(String verifyDateStr) {
        this.verifyDateStr = verifyDateStr;
    }
}
