package com.topideal.entity.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 结算价格
 * @author lian_
 */
@ApiModel
public class SettlementPriceDTO extends PageModel implements Serializable{

    @ApiModelProperty(value = "商品id", required = false)
    private Long goodsId;

    @ApiModelProperty(value = "商家名称", required = false)
    private String merchantName;

    @ApiModelProperty(value = "商家id", required = false)
    private Long merchantId;

    @ApiModelProperty(value = "原产国id", required = false)
    private Long countyId;

    @ApiModelProperty(value = "结算价格", required = false)
    private BigDecimal price;

    @ApiModelProperty(value = "品类名称", required = false)
    private String productTypeName;

    @ApiModelProperty(value = "计量单位id", required = false)
    private Long unitId;

    @ApiModelProperty(value = "结算币种  01人民币,02日元,03 澳元 ,04美元,05港币,06 欧元,07英镑", required = false)
    private String currency;
    private String currencyLabel;

    @ApiModelProperty(value = "id", required = false)
    private Long id;

    @ApiModelProperty(value = "商品名称", required = false)
    private String goodsName;

    @ApiModelProperty(value = "条形码", required = false)
    private String barcode;

    @ApiModelProperty(value = "原产国名称", required = false)
    private String countyName;

    @ApiModelProperty(value = "商品货号", required = false)
    private String goodsNo;

    @ApiModelProperty(value = "品牌名称", required = false)
    private String brandName;

    @ApiModelProperty(value = "计量单位名称", required = false)
    private String unitName;

    @ApiModelProperty(value = "修改时间", required = false)
    private Timestamp modifyDate;

    @ApiModelProperty(value = "品类id", required = false)
    private Long productTypeId;

    @ApiModelProperty(value = "规格型号", required = false)
    private String goodsSpec;

    @ApiModelProperty(value = "品牌id", required = false)
    private Long brandId;

    @ApiModelProperty(value = "是否组合品  1 是 0 否", required = false)
    private String isGroup;
    private String isGroupLabel;

    @ApiModelProperty(value = "创建人ID", required = false)
    private Long createrId;

    @ApiModelProperty(value = "创建人", required = false)
    private String creater;

    @ApiModelProperty(value = "创建时间", required = false)
    private Timestamp createDate;

    @ApiModelProperty(value = "修改人", required = false)
    private Long modifierId;

    @ApiModelProperty(value = "修改者", required = false)
    private String modifier;

    @ApiModelProperty(value = "生效日期", required = false)
    private String effectiveDate;

    @ApiModelProperty(value = "货品id", required = false)
    private Long productId;

    @ApiModelProperty(value = "状态 001:待审核 , 013:待提交，021:已作废，032:已生效 ，033审核不通过", required = false)
    private String status;
    private String statusLabel ;

    @ApiModelProperty(value = "事业部名称", required = false)
    private String buName;

    @ApiModelProperty(value = "事业部id", required = false)
    private Long buId;

    private List<Long> buIds ;


    //=========拓展字段===================
    @ApiModelProperty(value = "用于限制页面可选日期", required = false)
    private String startDate;//用于限制页面可选日期
    // 调价原因
    @ApiModelProperty(value = "调价原因", required = false)
    private String adjustPriceResult;
    // 结算价格记录表中每次修改的时间
    @ApiModelProperty(value = "结算价格记录表中每次修改的时间", required = false)
    private Timestamp createDateRecord;

    @ApiModelProperty(value = "审核者ID", required = false)
    private Long examinerId;

    @ApiModelProperty(value = "审核时间", required = false)
    private Timestamp examineDate;

    @ApiModelProperty(value = "审核者", required = false)
    private String examiner;


	public Timestamp getCreateDateRecord() {
		return createDateRecord;
	}
	public void setCreateDateRecord(Timestamp createDateRecord) {
		this.createDateRecord = createDateRecord;
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
		this.statusLabel = DERP_REPORT.getLabelByKey(DERP_REPORT.settlementPrice_statusList, status) ;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
    /*goodsId get 方法 */
    public Long getGoodsId(){
        return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
        this.goodsId=goodsId;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName=merchantName;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*countyId get 方法 */
    public Long getCountyId(){
        return countyId;
    }
    /*countyId set 方法 */
    public void setCountyId(Long  countyId){
        this.countyId=countyId;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
        return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
        this.price=price;
    }
    /*productTypeName get 方法 */
    public String getProductTypeName(){
        return productTypeName;
    }
    /*productTypeName set 方法 */
    public void setProductTypeName(String  productTypeName){
        this.productTypeName=productTypeName;
    }
    /*unitId get 方法 */
    public Long getUnitId(){
        return unitId;
    }
    /*unitId set 方法 */
    public void setUnitId(Long  unitId){
        this.unitId=unitId;
    }
    /*currency get 方法 */
    public String getCurrency(){
        return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
        this.currency=currency;
        this.currencyLabel = DERP_REPORT.getLabelByKey(DERP.currencyCodeList, currency) ;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
        return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
        this.goodsName=goodsName;
    }
    /*barcode get 方法 */
    public String getBarcode(){
        return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
        this.barcode=barcode;
    }
    /*countyName get 方法 */
    public String getCountyName(){
        return countyName;
    }
    /*countyName set 方法 */
    public void setCountyName(String  countyName){
        this.countyName=countyName;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
        return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
        this.goodsNo=goodsNo;
    }
    /*brandName get 方法 */
    public String getBrandName(){
        return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
        this.brandName=brandName;
    }
    /*unitName get 方法 */
    public String getUnitName(){
        return unitName;
    }
    /*unitName set 方法 */
    public void setUnitName(String  unitName){
        this.unitName=unitName;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
        this.modifyDate=modifyDate;
    }
    /*productTypeId get 方法 */
    public Long getProductTypeId(){
        return productTypeId;
    }
    /*productTypeId set 方法 */
    public void setProductTypeId(Long  productTypeId){
        this.productTypeId=productTypeId;
    }
    /*goodsSpec get 方法 */
    public String getGoodsSpec(){
        return goodsSpec;
    }
    /*goodsSpec set 方法 */
    public void setGoodsSpec(String  goodsSpec){
        this.goodsSpec=goodsSpec;
    }
    /*brandId get 方法 */
    public Long getBrandId(){
        return brandId;
    }
    /*brandId set 方法 */
    public void setBrandId(Long  brandId){
        this.brandId=brandId;
    }
    /*isGroup get 方法 */
    public String getIsGroup(){
        return isGroup;
    }
    /*isGroup set 方法 */
    public void setIsGroup(String  isGroup){
        this.isGroup=isGroup;
        this.isGroupLabel=DERP_REPORT.getLabelByKey(DERP_REPORT.settlementPrice_isGroupList, isGroup) ;
    }
    /*effectiveDate get 方法 */
    public String getEffectiveDate(){
        return effectiveDate;
    }
    /*effectiveDate set 方法 */
    public void setEffectiveDate(String  effectiveDate){
        this.effectiveDate=effectiveDate;
    }
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

    public Long getCreaterId() {
        return createrId;
    }

    public void setCreaterId(Long createrId) {
        this.createrId = createrId;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
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
	public String getCurrencyLabel() {
		return currencyLabel;
	}
	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
	}
	public String getIsGroupLabel() {
		return isGroupLabel;
	}
	public void setIsGroupLabel(String isGroupLabel) {
		this.isGroupLabel = isGroupLabel;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	public List<Long> getBuIds() {
		return buIds;
	}
	public void setBuIds(List<Long> buIds) {
		this.buIds = buIds;
	}

}
