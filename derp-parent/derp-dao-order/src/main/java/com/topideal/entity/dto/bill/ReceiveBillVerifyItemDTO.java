package com.topideal.entity.dto.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ReceiveBillVerifyItemDTO extends PageModel implements Serializable{

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Long id;
    /**
     * 应收账单Id
     */
    @ApiModelProperty(value = "应收账单Id")
    private Long billId;
    /**
     * 核销金额
     */
    @ApiModelProperty(value = "核销金额")
    private BigDecimal price;
    /**
     * 收款日期
     */
    @ApiModelProperty(value = "收款日期")
    private Timestamp receiveDate;
    @ApiModelProperty(value = "收款日期字符串")
    private String receiveDateStr;
    /**
     * 收款流水号
     */
    @ApiModelProperty(value = "收款流水号")
    private String receiceNo;
    /**
     * 核销时间
     */
    @ApiModelProperty(value = "核销时间")
    private Timestamp verifyDate;
    /**
     * 核销人ID
     */
    @ApiModelProperty(value = " 核销人ID")
    private Long verifyId;
    /**
     * 核销人名称
     */
    @ApiModelProperty(value = "核销人名称")
    private String verifier;
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
     * 关联预收单id
     */
    @ApiModelProperty(value = "关联预收单id")
    private Long advanceId;
    /**
     * 关联预收单单号
     */
    @ApiModelProperty(value = "关联预收单单号")
    private String advanceCode;
    /**
     * 单据类型 1-预收账单 2-收款单
     */
    @ApiModelProperty(value = "单据类型 1-预收账单 2-收款单")
    private String type;
    @ApiModelProperty(value = "单据类型 1-预收账单 2-收款单")
    private String typeLabel;

    /**
     * nc同步唯一id
     */
    @ApiModelProperty(value = "nc同步唯一id")
    private Long ncId;

    @ApiModelProperty(value = "核销月份")
    private String verifyMonth;




    //nc同步状态 1-正常 2-作废
    private String ncState;
    // 应收账单号
    private String receiveCode;


    /*id get 方法 */
    public Long getId() {
        return id;
    }

    /*id set 方法 */
    public void setId(Long id) {
        this.id = id;
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

    public String getReceiveDateStr() {
        return receiveDateStr;
    }

    public void setReceiveDateStr(String receiveDateStr) {
        this.receiveDateStr = receiveDateStr;
    }

    public Long getAdvanceId() {
        return advanceId;
    }

    public void setAdvanceId(Long advanceId) {
        this.advanceId = advanceId;
    }

    public String getAdvanceCode() {
        return advanceCode;
    }

    public void setAdvanceCode(String advanceCode) {
        this.advanceCode = advanceCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        this.typeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBillVerify_typeList, type);
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public Long getNcId() {
        return ncId;
    }

    public void setNcId(Long ncId) {
        this.ncId = ncId;
    }

    public String getNcState() {
        return ncState;
    }

    public void setNcState(String ncState) {
        this.ncState = ncState;
    }

	public String getReceiveCode() {
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
	}

    public String getVerifyMonth() {
        return verifyMonth;
    }

    public void setVerifyMonth(String verifyMonth) {
        this.verifyMonth = verifyMonth;
    }
}
