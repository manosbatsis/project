package com.topideal.entity.vo.reporting;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 结算价格记录表
 * @author lian_
 *
 */
public class SettlementPriceRecordModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 结算价格id
    */
    private Long settlementPriceId;
    /**
    * 价格
    */
    private BigDecimal price;
    /**
    * 生效年月
    */
    private String effectiveDate;
    /**
    * 结算币种  01人民币,02日元,03 澳元 ,04美元,05港币,06 欧元,07英镑
    */
    private String currency;
    /**
    * 创建日期
    */
    private Timestamp createDate;
    /**
     * 调价原因
     */
    private String adjustPriceResult;

    /**
     * 状态 001:待审核 , 013:待提交，021:已作废，032:已生效
     */
    private String status;

    /**
     * 审核者ID
     */
    private Long examinerId;
    /**
     * 审核时间
     */
    private Timestamp examineDate;
    /**
     * 审核者
     */
    private String examiner;
    /**
     * 修改人
     */
    private Long modifierId;
    /**
     * 修改者
     */
    private String modifier;
    /**
     * 修改时间
     */
    private Timestamp modifyDate;

    /**
     * 事业部名称
     */
    private String buName;
    /**
     *  事业部id
     */
    private Long buId;


    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*settlementPriceId get 方法 */
    public Long getSettlementPriceId(){
    return settlementPriceId;
    }
    /*settlementPriceId set 方法 */
    public void setSettlementPriceId(Long  settlementPriceId){
    this.settlementPriceId=settlementPriceId;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
    return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
    this.price=price;
    }
    /*effectiveDate get 方法 */
    public String getEffectiveDate(){
    return effectiveDate;
    }
    /*effectiveDate set 方法 */
    public void setEffectiveDate(String  effectiveDate){
    this.effectiveDate=effectiveDate;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
	public String getAdjustPriceResult() {
		return adjustPriceResult;
	}
	public void setAdjustPriceResult(String adjustPriceResult) {
		this.adjustPriceResult = adjustPriceResult;
	}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getExaminerId() {
        return examinerId;
    }

    public void setExaminerId(Long examinerId) {
        this.examinerId = examinerId;
    }

    public Timestamp getExamineDate() {
        return examineDate;
    }

    public void setExamineDate(Timestamp examineDate) {
        this.examineDate = examineDate;
    }

    public String getExaminer() {
        return examiner;
    }

    public void setExaminer(String examiner) {
        this.examiner = examiner;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }
}
