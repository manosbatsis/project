package com.topideal.entity.vo.bill;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class PaymentItemModel extends PageModel implements Serializable{

    /**
    * ID
    */
    private Long id;
    /**
    * 应付账单Id
    */
    private Long paymentId;
    /**
    * 采购单ID
    */
    private Long purchaseId;
    /**
    * 采购单号
    */
    private String purchaseCode;
    /**
    * 项目id
    */
    private Long projectId;
    /**
    * 项目名称
    */
    private String projectName;
    /**
    * 商品id
    */
    private Long goodsId;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 数量
    */
    private Integer num;
    /**
    * 采购金额（不含税）
    */
    private BigDecimal purchaseAmount;
    /**
    * 采购金额（含税）
    */
    private BigDecimal purchaseTaxAmount;
    /**
    * 税额
    */
    private BigDecimal tax;
    /**
    * 本期结算金额（不含税）
    */
    private BigDecimal settlementAmount;
    /**
    * 本期结算税额
    */
    private BigDecimal settlementTax;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     * po单号
     */
    private String poNo;

    /**
     * 母品牌
     */
    private String superiorParentBrandName;
    /**
     * 母品牌id
     */
    private Long superiorParentBrandId;
    /**
     * 母品牌编码
     */
    private String superiorParentBrandCode;
    
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
    /*purchaseId get 方法 */
    public Long getPurchaseId(){
    return purchaseId;
    }
    /*purchaseId set 方法 */
    public void setPurchaseId(Long  purchaseId){
    this.purchaseId=purchaseId;
    }
    /*purchaseCode get 方法 */
    public String getPurchaseCode(){
    return purchaseCode;
    }
    /*purchaseCode set 方法 */
    public void setPurchaseCode(String  purchaseCode){
    this.purchaseCode=purchaseCode;
    }
    /*projectId get 方法 */
    public Long getProjectId(){
    return projectId;
    }
    /*projectId set 方法 */
    public void setProjectId(Long  projectId){
    this.projectId=projectId;
    }
    /*projectName get 方法 */
    public String getProjectName(){
    return projectName;
    }
    /*projectName set 方法 */
    public void setProjectName(String  projectName){
    this.projectName=projectName;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*purchaseAmount get 方法 */
    public BigDecimal getPurchaseAmount(){
    return purchaseAmount;
    }
    /*purchaseAmount set 方法 */
    public void setPurchaseAmount(BigDecimal  purchaseAmount){
    this.purchaseAmount=purchaseAmount;
    }
    /*purchaseTaxAmount get 方法 */
    public BigDecimal getPurchaseTaxAmount(){
    return purchaseTaxAmount;
    }
    /*purchaseTaxAmount set 方法 */
    public void setPurchaseTaxAmount(BigDecimal  purchaseTaxAmount){
    this.purchaseTaxAmount=purchaseTaxAmount;
    }
    /*tax get 方法 */
    public BigDecimal getTax(){
    return tax;
    }
    /*tax set 方法 */
    public void setTax(BigDecimal  tax){
    this.tax=tax;
    }
    /*settlementAmount get 方法 */
    public BigDecimal getSettlementAmount(){
    return settlementAmount;
    }
    /*settlementAmount set 方法 */
    public void setSettlementAmount(BigDecimal  settlementAmount){
    this.settlementAmount=settlementAmount;
    }
    /*settlementTax get 方法 */
    public BigDecimal getSettlementTax(){
    return settlementTax;
    }
    /*settlementTax set 方法 */
    public void setSettlementTax(BigDecimal  settlementTax){
    this.settlementTax=settlementTax;
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
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

    public String getSuperiorParentBrandName() {
        return superiorParentBrandName;
    }

    public void setSuperiorParentBrandName(String superiorParentBrandName) {
        this.superiorParentBrandName = superiorParentBrandName;
    }

    public Long getSuperiorParentBrandId() {
        return superiorParentBrandId;
    }

    public void setSuperiorParentBrandId(Long superiorParentBrandId) {
        this.superiorParentBrandId = superiorParentBrandId;
    }

    public String getSuperiorParentBrandCode() {
        return superiorParentBrandCode;
    }

    public void setSuperiorParentBrandCode(String superiorParentBrandCode) {
        this.superiorParentBrandCode = superiorParentBrandCode;
    }
}
