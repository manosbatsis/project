package com.topideal.entity.dto.main;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class SupplierMerchandisePriceDTO extends PageModel implements Serializable{

    /**
    * 主键ID
    */

	@ApiModelProperty(value = "主键ID")
    private Long id;
    /**
    * 公司ID
    */
	@ApiModelProperty(value = "公司ID")
    private Long merchantId;
    /**
    * 公司名称
    */
	@ApiModelProperty(value = "公司名称")
    private String merchantName;
    /**
    * 供应商id
    */
	@ApiModelProperty(value = "供应商id")
    private Long customerId;
    /**
    * 供应商编号
    */
	@ApiModelProperty(value = "供应商编号")
    private String customerCode;
    /**
    * 供应商名称
    */
	@ApiModelProperty(value = "供应商编号")
    private String customerName;
    /**
    * 标准条码
    */
	@ApiModelProperty(value = "标准条码")
    private String commbarcode;
    /**
     * 标准条码List
     */
	@ApiModelProperty(value = "标准条码List")
    private List commbarcodeList;
    /**
    * 商品名称
    */
	@ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
    * 品牌id
    */
	@ApiModelProperty(value = "品牌id")
    private Long brandId;
    /**
    * 品牌名称
    */
	@ApiModelProperty(value = "品牌名称")
    private String brandName;
    /**
    * 规格型号
    */
	@ApiModelProperty(value = "规格型号")
    private String spec;
    /**
    * 币种
    */
	@ApiModelProperty(value = "币种")
    private String currency;
	@ApiModelProperty(value = "币种Label")
    private String currencyLabel;
    /**
    * 供货价
    */
	@ApiModelProperty(value = "供货价")
    private BigDecimal supplyPrice;
    private String supplyPriceLabel;
    /**
    * 订单状态:001:待审核,003:已审核
    */
	@ApiModelProperty(value = "订单状态:001:待审核,003:已审核,002:待提交,004：已驳回,005:待作废，006：已作废")
    private String state;
	@ApiModelProperty(value = "订单状态:001:待审核,003:已审核,002:待提交,004：已驳回,005:待作废，006：已作废Label")
    private String stateLabel;
    /**
    * 报价生效时间
    */
	@ApiModelProperty(value = "报价生效时间")
    private Timestamp effectiveDate;
	@ApiModelProperty(value = "报价生效时间Str")
    private String effectiveDateStr ;
    /**
    * 报价失效时间
    */
	@ApiModelProperty(value = "报价失效时间")
    private Timestamp expiryDate;
	@ApiModelProperty(value = "报价失效时间Str")
    private String expiryDateStr;
    /**
    * 创建人
    */
	@ApiModelProperty(value = "创建人")
    private Long creater;
    /**
    * 创建人名称
    */
	@ApiModelProperty(value = "创建人名称")
    private String createName;
    /**
    * 创建时间
    */
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    /**
    * 审核人
    */
	@ApiModelProperty(value = "审核人")
    private Long auditor;
    /**
    * 审核人名称
    */
	@ApiModelProperty(value = "审核人名称")
    private String auditName;
    /**
    * 审核时间
    */
	@ApiModelProperty(value = "审核时间")
    private Timestamp auditDate;
    /**
    * 修改人
    */
	@ApiModelProperty(value = "修改人")
    private Long modifier;
    /**
    * 修改人名称
    */
	@ApiModelProperty(value = "修改人名称")
    private String modifierName;
    /**
    * 修改时间
    */
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    //事业部名称
	@ApiModelProperty(value = "事业部名称")
    private String buName;
    //事业部id
	@ApiModelProperty(value = "事业部id")
    private Long buId;
    //提交人名称
	@ApiModelProperty(value = "提交人名称")
    private String submitName;
    //提交人id
	@ApiModelProperty(value = "提交人id")
    private Long submitId;
    //提交时间
	@ApiModelProperty(value = "提交时间")
    private Timestamp submitDate;

    //待提交数量
	@ApiModelProperty(value = "待提交数量")
    private Long submitNum;
    //待审核数量
	@ApiModelProperty(value = "待审核数量")
    private Long auditNum;
    //已审核数量
	@ApiModelProperty(value = "已审核数量")
    private Long beAuditNum;
    //已驳回数量
	@ApiModelProperty(value = "已驳回数量")
    private Long rejectNum;
    @ApiModelProperty(value = "待作废数量")
    private Long invalidNum;
    @ApiModelProperty(value = "已作废数量")
    private Long areadyInvalidNum;
    //全部数量
	@ApiModelProperty(value = "全部数量")
    private Long allNum;

    //作废原因
    @ApiModelProperty(value = "作废原因")
    private String cancelReason;

    //备注
    @ApiModelProperty(value = "备注")
    private String remark;

    //编码(暂时用于关联附件)
    @ApiModelProperty(value = "编码(暂时用于关联附件)")
    private String code;

    /**
     * 条形码
     */
    @ApiModelProperty(value = "条形码")
    private String barcode;
    /**
     * 库位类型ID
     */
    @ApiModelProperty(value = "库位类型ID")
    private Long stockLocationTypeId;
    /**
     * 库位类型名称
     */
    @ApiModelProperty(value = "库位类型名称")
    private String stockLocationTypeName;

    /**
     * 数据来源
     */
    @ApiModelProperty(value = "数据来源")
    private String dataSource;
    private String dataSourceLabel;

    private List<Long> supplierList;

    private String customerIdsStr;

    private List<Long> ids;


    public Long getInvalidNum() {
        return invalidNum;
    }

    public void setInvalidNum(Long invalidNum) {
        this.invalidNum = invalidNum;
    }

    public Long getAreadyInvalidNum() {
        return areadyInvalidNum;
    }

    public void setAreadyInvalidNum(Long areadyInvalidNum) {
        this.areadyInvalidNum = areadyInvalidNum;
    }

    public String getCustomerIdsStr() {
        return customerIdsStr;
    }

    public void setCustomerIdsStr(String customerIdsStr) {
        this.customerIdsStr = customerIdsStr;
    }

    public List<Long> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<Long> supplierList) {
        this.supplierList = supplierList;
    }

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
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
    /*customerId get 方法 */
    public Long getCustomerId(){
    return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
    this.customerId=customerId;
    }
    /*customerCode get 方法 */
    public String getCustomerCode(){
    return customerCode;
    }
    /*customerCode set 方法 */
    public void setCustomerCode(String  customerCode){
    this.customerCode=customerCode;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
    return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
    this.customerName=customerName;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*brandId get 方法 */
    public Long getBrandId(){
    return brandId;
    }
    /*brandId set 方法 */
    public void setBrandId(Long  brandId){
    this.brandId=brandId;
    }
    /*brandName get 方法 */
    public String getBrandName(){
    return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
    this.brandName=brandName;
    }
    /*spec get 方法 */
    public String getSpec(){
    return spec;
    }
    /*spec set 方法 */
    public void setSpec(String  spec){
    this.spec=spec;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
        this.currency=currency;
        if(StringUtils.isNotBlank(currency)){
            this.currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency);
        }
    }
    /*supplyPrice get 方法 */
    public BigDecimal getSupplyPrice(){
    return supplyPrice;
    }
    /*supplyPrice set 方法 */
    public void setSupplyPrice(BigDecimal  supplyPrice){
    this.supplyPrice=supplyPrice;
    this.supplyPriceLabel = supplyPrice.toPlainString();
    }

    public String getSupplyPriceLabel() {
        return supplyPriceLabel;
    }

    public void setSupplyPriceLabel(String supplyPriceLabel) {
        this.supplyPriceLabel = supplyPriceLabel;
    }
    /*state get 方法 */
    public String getState(){
    return state;
    }
    /*state set 方法 */
    public void setState(String  state){
        this.state=state;
        if(StringUtils.isNotBlank(state)){
            this.stateLabel = DERP_SYS.getLabelByKey(DERP_SYS.supplierMerchandisePrice_statusList, state);
        }
    }
    /*effectiveDate get 方法 */
    public Timestamp getEffectiveDate(){
    return effectiveDate;
    }
    /*effectiveDate set 方法 */
    public void setEffectiveDate(Timestamp  effectiveDate){
    this.effectiveDate=effectiveDate;
    }
    /*expiryDate get 方法 */
    public Timestamp getExpiryDate(){
    return expiryDate;
    }
    /*expiryDate set 方法 */
    public void setExpiryDate(Timestamp  expiryDate){
    this.expiryDate=expiryDate;
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
    /*auditor get 方法 */
    public Long getAuditor(){
    return auditor;
    }
    /*auditor set 方法 */
    public void setAuditor(Long  auditor){
    this.auditor=auditor;
    }
    /*auditName get 方法 */
    public String getAuditName(){
    return auditName;
    }
    /*auditName set 方法 */
    public void setAuditName(String  auditName){
    this.auditName=auditName;
    }
    /*auditDate get 方法 */
    public Timestamp getAuditDate(){
    return auditDate;
    }
    /*auditDate set 方法 */
    public void setAuditDate(Timestamp  auditDate){
    this.auditDate=auditDate;
    }
    /*modifier get 方法 */
    public Long getModifier(){
    return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
    this.modifier=modifier;
    }
    /*modifierName get 方法 */
    public String getModifierName(){
    return modifierName;
    }
    /*modifierName set 方法 */
    public void setModifierName(String  modifierName){
    this.modifierName=modifierName;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }

    public String getStateLabel() {
        return stateLabel;
    }

    public void setStateLabel(String stateLabel) {
        this.stateLabel = stateLabel;
    }

    public String getCurrencyLabel() {
        return currencyLabel;
    }

    public void setCurrencyLabel(String currencyLabel) {
        this.currencyLabel = currencyLabel;
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

    public String getSubmitName() {
        return submitName;
    }

    public void setSubmitName(String submitName) {
        this.submitName = submitName;
    }

    public Long getSubmitId() {
        return submitId;
    }

    public void setSubmitId(Long submitId) {
        this.submitId = submitId;
    }

    public Long getSubmitNum() {
        return submitNum;
    }

    public void setSubmitNum(Long submitNum) {
        this.submitNum = submitNum;
    }

    public Long getAuditNum() {
        return auditNum;
    }

    public void setAuditNum(Long auditNum) {
        this.auditNum = auditNum;
    }

    public Long getBeAuditNum() {
        return beAuditNum;
    }

    public void setBeAuditNum(Long beAuditNum) {
        this.beAuditNum = beAuditNum;
    }

    public Long getRejectNum() {
        return rejectNum;
    }

    public void setRejectNum(Long rejectNum) {
        this.rejectNum = rejectNum;
    }

    public Long getAllNum() {
        return allNum;
    }

    public void setAllNum(Long allNum) {
        this.allNum = allNum;
    }

    public Timestamp getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Timestamp submitDate) {
        this.submitDate = submitDate;
    }

    public String getEffectiveDateStr() {
        return effectiveDateStr;
    }

    public void setEffectiveDateStr(String effectiveDateStr) {
        this.effectiveDateStr = effectiveDateStr;
    }

    public String getExpiryDateStr() {
        return expiryDateStr;
    }

    public void setExpiryDateStr(String expiryDateStr) {
        this.expiryDateStr = expiryDateStr;
    }
	public List getCommbarcodeList() {
		return commbarcodeList;
	}
	public void setCommbarcodeList(List commbarcodeList) {
		this.commbarcodeList = commbarcodeList;
	}

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getStockLocationTypeId() {
        return stockLocationTypeId;
    }

    public void setStockLocationTypeId(Long stockLocationTypeId) {
        this.stockLocationTypeId = stockLocationTypeId;
    }

    public String getStockLocationTypeName() {
        return stockLocationTypeName;
    }

    public void setStockLocationTypeName(String stockLocationTypeName) {
        this.stockLocationTypeName = stockLocationTypeName;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
        this.dataSourceLabel = DERP_SYS.getLabelByKey(DERP_SYS.supplierMerchandisePrice_dataSourceList, dataSource);
    }

    public String getDataSourceLabel() {
        return dataSourceLabel;
    }

    public void setDataSourceLabel(String dataSourceLabel) {
        this.dataSourceLabel = dataSourceLabel;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
