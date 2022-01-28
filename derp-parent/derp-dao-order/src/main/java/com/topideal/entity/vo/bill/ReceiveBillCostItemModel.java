package com.topideal.entity.vo.bill;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

public class ReceiveBillCostItemModel extends PageModel implements Serializable{

    /**
    * ID
    */
    private Long id;
    /**
    * 应收账单Id
    */
    private Long billId;
    /**
    * 项目id
    */
    private Long projectId;
    /**
    * 项目名称
    */
    private String projectName;
    /**
    * po单号
    */
    private String poNo;
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
    * 发票描述
    */
    private String invoiceDescription;
    /**
    * 费用金额（不含税）
    */
    private BigDecimal price;
    /**
    * 数量
    */
    private Integer num;
    /**
    * 补扣款类型 0-补款 1-扣款
    */
    private String billType;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 母品牌
    */
    private String parentBrandName;
    /**
    * 母品牌id
    */
    private Long parentBrandId;
    /**
    * 母品牌编码
    */
    private String parentBrandCode;
    /**
    * 科目编码
    */
    private String subjectCode;
    /**
    * 科目名称
    */
    private String subjectName;
    /**
    * 备注
    */
    private String remark;
    /**
    * NC收支费项名称
    */
    private String paymentSubjectName;
    /**
    * NC收支费项id
    */
    private Long paymentSubjectId;
    /**
    * 核销平台
    */
    private String verifyPlatformCode;
    /**
    * 税率
    */
    private Double taxRate;
    /**
    * 税额
    */
    private BigDecimal tax;
    /**
    * 费用金额（含税）
    */
    private BigDecimal taxAmount;

    /**
     * 平台商品编码
     */
    private String platformGoodsCode;

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
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
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
    /*invoiceDescription get 方法 */
    public String getInvoiceDescription(){
    return invoiceDescription;
    }
    /*invoiceDescription set 方法 */
    public void setInvoiceDescription(String  invoiceDescription){
    this.invoiceDescription=invoiceDescription;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
    return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
    this.price=price;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*billType get 方法 */
    public String getBillType(){
    return billType;
    }
    /*billType set 方法 */
    public void setBillType(String  billType){
    this.billType=billType;
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
    /*parentBrandName get 方法 */
    public String getParentBrandName(){
    return parentBrandName;
    }
    /*parentBrandName set 方法 */
    public void setParentBrandName(String  parentBrandName){
    this.parentBrandName=parentBrandName;
    }
    /*parentBrandId get 方法 */
    public Long getParentBrandId(){
    return parentBrandId;
    }
    /*parentBrandId set 方法 */
    public void setParentBrandId(Long  parentBrandId){
    this.parentBrandId=parentBrandId;
    }
    /*parentBrandCode get 方法 */
    public String getParentBrandCode(){
    return parentBrandCode;
    }
    /*parentBrandCode set 方法 */
    public void setParentBrandCode(String  parentBrandCode){
    this.parentBrandCode=parentBrandCode;
    }
    /*subjectCode get 方法 */
    public String getSubjectCode(){
    return subjectCode;
    }
    /*subjectCode set 方法 */
    public void setSubjectCode(String  subjectCode){
    this.subjectCode=subjectCode;
    }
    /*subjectName get 方法 */
    public String getSubjectName(){
    return subjectName;
    }
    /*subjectName set 方法 */
    public void setSubjectName(String  subjectName){
    this.subjectName=subjectName;
    }
    /*remark get 方法 */
    public String getRemark(){
    return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
    this.remark=remark;
    }
    /*paymentSubjectName get 方法 */
    public String getPaymentSubjectName(){
    return paymentSubjectName;
    }
    /*paymentSubjectName set 方法 */
    public void setPaymentSubjectName(String  paymentSubjectName){
    this.paymentSubjectName=paymentSubjectName;
    }
    /*paymentSubjectId get 方法 */
    public Long getPaymentSubjectId(){
    return paymentSubjectId;
    }
    /*paymentSubjectId set 方法 */
    public void setPaymentSubjectId(Long  paymentSubjectId){
    this.paymentSubjectId=paymentSubjectId;
    }
    /*verifyPlatformCode get 方法 */
    public String getVerifyPlatformCode(){
    return verifyPlatformCode;
    }
    /*verifyPlatformCode set 方法 */
    public void setVerifyPlatformCode(String  verifyPlatformCode){
    this.verifyPlatformCode=verifyPlatformCode;
    }
    /*taxRate get 方法 */
    public Double getTaxRate(){
    return taxRate;
    }
    /*taxRate set 方法 */
    public void setTaxRate(Double  taxRate){
    this.taxRate=taxRate;
    }
    /*tax get 方法 */
    public BigDecimal getTax(){
    return tax;
    }
    /*tax set 方法 */
    public void setTax(BigDecimal  tax){
    this.tax=tax;
    }
    /*taxAmount get 方法 */
    public BigDecimal getTaxAmount(){
    return taxAmount;
    }
    /*taxAmount set 方法 */
    public void setTaxAmount(BigDecimal  taxAmount){
    this.taxAmount=taxAmount;
    }

    public String getPlatformGoodsCode() {
        return platformGoodsCode;
    }

    public void setPlatformGoodsCode(String platformGoodsCode) {
        this.platformGoodsCode = platformGoodsCode;
    }
}
