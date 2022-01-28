package com.topideal.entity.vo.order;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class BuMoveInventoryModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 移库单号
    */
    private String code;
    /**
    * 来源业务单号
    */
    private String businessNo;
    /**
    * 单据来源  1：手工导入；2：系统自动生成
    */
    private String orderSource;
    /**
    * 移库状态 017-待移库,018-已移库,027-移库中
    */
    private String status;
    /**
    * 出库仓库ID
    */
    private Long depotId;
    /**
    * 出库仓库名称
    */
    private String depotName;
    /**
    * 移入事业部id
    */
    private Long inBuId;
    /**
    * 移入事业部名称
    */
    private String inBuName;
    /**
    * 移出事业部id
    */
    private Long outBuId;
    /**
    * 移出事业部名称
    */
    private String outBuName;
    /**
    * 移库日期
    */
    private Timestamp moveDate;
    /**
    * 公司ID
    */
    private Long merchantId;
    /**
    * 公司名称
    */
    private String merchantName;
    /**
    * 创建人id
    */
    private Long creater;
    /**
    * 创建人名称
    */
    private String createName;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     * 单据类别 DSDD:电商订单、XSDD：销售订单
     */
    private String orderType;
    //'移库币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    private String currency;
    //'移库币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    private String agreementCurrency;

    /**
     * 理货单位 00-托盘 01-箱 02-件
     */
    private String tallyingUnit;
    /**
     * 审核人
     */
    private Long auditor;
    /**
     * 审核人名称
     */
    private String auditName;
    /**
     * 审核时间
     */
    private Timestamp auditDate;

    /*id get 方法 */
    public Long getId() {
        return id;
    }

    /*id set 方法 */
    public void setId(Long id) {
        this.id = id;
    }

    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
    }
    /*businessNo get 方法 */
    public String getBusinessNo(){
    return businessNo;
    }
    /*businessNo set 方法 */
    public void setBusinessNo(String  businessNo){
    this.businessNo=businessNo;
    }
    /*orderSource get 方法 */
    public String getOrderSource(){
    return orderSource;
    }
    /*orderSource set 方法 */
    public void setOrderSource(String  orderSource){
    this.orderSource=orderSource;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
    return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
    this.depotId=depotId;
    }
    /*depotName get 方法 */
    public String getDepotName(){
    return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
    this.depotName=depotName;
    }
    /*inBuId get 方法 */
    public Long getInBuId(){
    return inBuId;
    }
    /*inBuId set 方法 */
    public void setInBuId(Long  inBuId){
    this.inBuId=inBuId;
    }
    /*inBuName get 方法 */
    public String getInBuName(){
    return inBuName;
    }
    /*inBuName set 方法 */
    public void setInBuName(String  inBuName){
    this.inBuName=inBuName;
    }
    /*outBuId get 方法 */
    public Long getOutBuId(){
    return outBuId;
    }
    /*outBuId set 方法 */
    public void setOutBuId(Long  outBuId){
    this.outBuId=outBuId;
    }
    /*outBuName get 方法 */
    public String getOutBuName(){
    return outBuName;
    }
    /*outBuName set 方法 */
    public void setOutBuName(String  outBuName){
    this.outBuName=outBuName;
    }
    /*moveDate get 方法 */
    public Timestamp getMoveDate(){
    return moveDate;
    }
    /*moveDate set 方法 */
    public void setMoveDate(Timestamp  moveDate){
    this.moveDate=moveDate;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
    return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
    this.merchantId=merchantId;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
    return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
    this.merchantName=merchantName;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*createName get 方法 */
    public String getCreateName(){
    return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
    this.createName=createName;
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
    /*orderType get 方法 */
    public String getOrderType(){
    return orderType;
    }
    /*orderType set 方法 */
    public void setOrderType(String  orderType){
    this.orderType=orderType;
    }
	public String getCurrency() {
		return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAgreementCurrency() {
        return agreementCurrency;
    }

    public void setAgreementCurrency(String agreementCurrency) {
        this.agreementCurrency = agreementCurrency;
    }

    public String getTallyingUnit() {
        return tallyingUnit;
    }

    public void setTallyingUnit(String tallyingUnit) {
        this.tallyingUnit = tallyingUnit;
    }

    public Long getAuditor() {
        return auditor;
    }

    public void setAuditor(Long auditor) {
        this.auditor = auditor;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public Timestamp getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Timestamp auditDate) {
        this.auditDate = auditDate;
    }
}
