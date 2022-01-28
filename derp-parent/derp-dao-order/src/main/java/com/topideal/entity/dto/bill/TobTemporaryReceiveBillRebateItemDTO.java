package com.topideal.entity.dto.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class TobTemporaryReceiveBillRebateItemDTO extends PageModel implements Serializable{

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("账单Id")
    private Long billId;

    @ApiModelProperty("销售SD单号")
    private String relSdCode;

    @ApiModelProperty("商品id")
    private Long goodsId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品货号")
    private String goodsNo;

    @ApiModelProperty("销售单价")
    private BigDecimal price;

    @ApiModelProperty("上架好品量")
    private Integer shelfNum;

    @ApiModelProperty("销售SD类型id")
    private Long sdTypeId;

    @ApiModelProperty("销售SD类型名称")
    private String sdTypeName;

    @ApiModelProperty("暂估返利金额")
    private BigDecimal rebateAmount;

    @ApiModelProperty("核销暂估返利金额")
    private BigDecimal verifyRebateAmount;

    @ApiModelProperty("创建时间")
    private Timestamp createDate;

    @ApiModelProperty("修改时间")
    private Timestamp modifyDate;

    @ApiModelProperty("账单Id集合")
    private List<Long> billIds;
    
    /**
     * 映射费项id
     */
    @ApiModelProperty("映射费项id")
     private Long projectId;
     /**
     * 映射费项名称
     */
    @ApiModelProperty("映射费项名称")
     private String projectName;

    @ApiModelProperty(hidden = true)
    private String poNo;
    @ApiModelProperty(hidden = true)
    private BigDecimal verifyAmount;
    @ApiModelProperty("应收账单号")
    private String receiveCode;

    /**
     * 母品牌
     */
    @ApiModelProperty("母品牌")
    private String parentBrandName;
     /**
     * 母品牌id
     */
    @ApiModelProperty("母品牌id")
    private Long parentBrandId;
     /**
     * 母品牌编码
     */
    @ApiModelProperty("母品牌编码")
    private String parentBrandCode;

    @ApiModelProperty("sd比例")
    private double sdRatio;
    /**
     * 标准品牌id
     */
    @ApiModelProperty("标准品牌id")
    private Long brandId;
    /**
     * 标准品牌名称
    */
    @ApiModelProperty("标准品牌名称")
    private String brandName;
    /**
     * 销售币种
     */
    @ApiModelProperty("销售币种")
    private String currency;

    /**
     * 是否红冲单：0-否，1-是
     */
    @ApiModelProperty("是否红冲单：0-否，1-是")
    private String isWriteOff;

    /**
     * 原销售SD单号
     */
    @ApiModelProperty("原销售SD单号")
    private String originalSaleSdOrderCode;
    
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
    /*relSdCode get 方法 */
    public String getRelSdCode(){
    return relSdCode;
    }
    /*relSdCode set 方法 */
    public void setRelSdCode(String  relSdCode){
    this.relSdCode=relSdCode;
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
    /*price get 方法 */
    public BigDecimal getPrice(){
    return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
    this.price=price;
    }
    /*shelfNum get 方法 */
    public Integer getShelfNum(){
    return shelfNum;
    }
    /*shelfNum set 方法 */
    public void setShelfNum(Integer  shelfNum){
    this.shelfNum=shelfNum;
    }
    /*sdTypeId get 方法 */
    public Long getSdTypeId(){
    return sdTypeId;
    }
    /*sdTypeId set 方法 */
    public void setSdTypeId(Long  sdTypeId){
    this.sdTypeId=sdTypeId;
    }
    /*sdTypeName get 方法 */
    public String getSdTypeName(){
    return sdTypeName;
    }
    /*sdTypeName set 方法 */
    public void setSdTypeName(String  sdTypeName){
    this.sdTypeName=sdTypeName;
    }
    /*rebateAmount get 方法 */
    public BigDecimal getRebateAmount(){
    return rebateAmount;
    }
    /*rebateAmount set 方法 */
    public void setRebateAmount(BigDecimal  rebateAmount){
    this.rebateAmount=rebateAmount;
    }
    /*verifyRebateAmount get 方法 */
    public BigDecimal getVerifyRebateAmount(){
    return verifyRebateAmount;
    }
    /*verifyRebateAmount set 方法 */
    public void setVerifyRebateAmount(BigDecimal  verifyRebateAmount){
    this.verifyRebateAmount=verifyRebateAmount;
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

    public List<Long> getBillIds() {
        return billIds;
    }

    public void setBillIds(List<Long> billIds) {
        this.billIds = billIds;
    }
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public BigDecimal getVerifyAmount() {
        return verifyAmount;
    }

    public void setVerifyAmount(BigDecimal verifyAmount) {
        this.verifyAmount = verifyAmount;
    }

    public String getReceiveCode() {
        return receiveCode;
    }

    public void setReceiveCode(String receiveCode) {
        this.receiveCode = receiveCode;
    }
	public String getParentBrandName() {
		return parentBrandName;
	}
	public void setParentBrandName(String parentBrandName) {
		this.parentBrandName = parentBrandName;
	}
	public Long getParentBrandId() {
		return parentBrandId;
	}
	public void setParentBrandId(Long parentBrandId) {
		this.parentBrandId = parentBrandId;
	}
	public String getParentBrandCode() {
		return parentBrandCode;
	}
	public void setParentBrandCode(String parentBrandCode) {
		this.parentBrandCode = parentBrandCode;
	}

    public double getSdRatio() {
        return sdRatio;
    }

    public void setSdRatio(double sdRatio) {
        this.sdRatio = sdRatio;
    }

	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIsWriteOff() {
        return isWriteOff;
    }

    public void setIsWriteOff(String isWriteOff) {
        this.isWriteOff = isWriteOff;
    }

    public String getOriginalSaleSdOrderCode() {
        return originalSaleSdOrderCode;
    }

    public void setOriginalSaleSdOrderCode(String originalSaleSdOrderCode) {
        this.originalSaleSdOrderCode = originalSaleSdOrderCode;
    }
}
