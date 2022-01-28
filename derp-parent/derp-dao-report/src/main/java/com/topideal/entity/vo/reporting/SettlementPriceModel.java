package com.topideal.entity.vo.reporting;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 结算价格
 * @author lian_
 */
public class SettlementPriceModel extends PageModel implements Serializable{

    /**
    * 商品id
    */
    private Long goodsId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 商家id
    */
    private Long merchantId;
    /**
    * 原产国id
    */
    private Long countyId;
    /**
    * 结算价格
    */
    private BigDecimal price;
    /**
    * 品类名称
    */
    private String productTypeName;
    /**
    * 计量单位id
    */
    private Long unitId;
    /**
    * 结算币种  01人民币,02日元,03 澳元 ,04美元,05港币,06 欧元,07英镑'
    */
    private String currency;
    /**
    * id
    */
    private Long id;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 原产国名称
    */
    private String countyName;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 品牌名称
    */
    private String brandName;
    /**
    * 计量单位名称
    */
    private String unitName;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 品类id
    */
    private Long productTypeId;
    /**
    * 规格型号
    */
    private String goodsSpec;
    /**
    * 品牌id
    */
    private Long brandId;
    /**
    * 是否组合品  1 是 0 否
    */
    private String isGroup;
    /**
     * 创建人ID
     */
    private Long createrId;
    /**
     * 创建人
     */
    private String creater;
    /**
     * 创建时间
     */
    private Timestamp createDate;
    /**
     * 修改人
     */
    private Long modifierId;
    /**
     * 修改者
     */
    private String modifier;
    /**
    * 生效日期
    */
    private String effectiveDate;
    // 货品id
    private Long productId;
    /**
    * 状态 001:待审核 , 013:待提交，021:已作废，032:已生效 ，033审核不通过
    */
    private String status;

    /**
     * 事业部名称
     */
    private String buName;
    /**
     *  事业部id
     */
    private Long buId;

    
    //=========拓展字段===================
    private String startDate;//用于限制页面可选日期
    // 调价原因
    private String adjustPriceResult;
    // 结算价格记录表中每次修改的时间
    private Timestamp createDateRecord;

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
	// 结算价格记录表
 /*  private List<SettlementPriceRecordModel> recordList;
*/
   /* public List<SettlementPriceRecordModel> getRecordList() {
    	return recordList;
	}
	public void setRecordList(List<SettlementPriceRecordModel> recordList) {
		this.recordList = recordList;
	}*/
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
}
