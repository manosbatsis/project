package com.topideal.entity.dto.sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class PreSaleOrderDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "ID")
    private Long id;
	@ApiModelProperty(value = "预售单编号")
    private String code;

	@ApiModelProperty(value = "订单状态:001:待审核,003:已审核,007:已完结")
    private String state;
	
	@ApiModelProperty(value = "订单状态（中文）")
    private String stateLabel;

	@ApiModelProperty(value = "公司ID")
    private Long merchantId;

	@ApiModelProperty(value = "公司名称")
    private String merchantName;

	@ApiModelProperty(value = "客户ID")
    private Long customerId;

	@ApiModelProperty(value = "客户名称")
    private String customerName;

	@ApiModelProperty(value = "业务模式  1 购销-整批结算  4.购销-实销实结")
    private String businessModel;
	
	@ApiModelProperty(value = "业务模式（中文）")
    private String businessModelLabel;

	@ApiModelProperty(value = "出仓仓库ID")
    private Long outDepotId;

	@ApiModelProperty(value = "出仓仓库名称")
    private String outDepotName;

	@ApiModelProperty(value = "事业部id")
    private Long buId;

	@ApiModelProperty(value = "事业部名称")
    private String buName;

	@ApiModelProperty(value = "po单号")
    private String poNo;

	@ApiModelProperty(value = "币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String currency;
	
	@ApiModelProperty(value = "币种（中文）")
    private String currencyLabel;

	@ApiModelProperty(value = "理货单位 00-托盘 01-箱 02-件")
    private String tallyingUnit;
	
	@ApiModelProperty(value = "理货单位（中文）")
    private String tallyingUnitLabel;

	@ApiModelProperty(value = "备注")
    private String remark;

	@ApiModelProperty(value = "审核人")
    private Long auditor;

	@ApiModelProperty(value = "审核人用户名")
    private String auditName;

	@ApiModelProperty(value = "审核时间")
    private Timestamp auditDate;

	@ApiModelProperty(value = "创建人")
    private Long creater;

	@ApiModelProperty(value = "创建人名称")
    private String createName;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value = "修改人")
    private Long modifier;

	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value = "修改人用户名")
    private String modifierUsername;
	
	@ApiModelProperty(value = "订单日期时间开始",hidden=true)
    private String createDateStartDate;
	
	@ApiModelProperty(value = "订单日期时间结束",hidden=true)
    private String createDateEndDate;

	@ApiModelProperty(value = "预售数量")
    private Integer totalNum;
	
	@ApiModelProperty(value = "预售总金额")
    private BigDecimal totalAmount;

	@ApiModelProperty(value = "商品信息-表体")
    private List<PreSaleOrderItemDTO> itemList;

	@ApiModelProperty(value = "事业部集合")
    private List<Long> buList;
	
	@ApiModelProperty(value = "主键id集合")
	private String ids;

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public List<PreSaleOrderItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<PreSaleOrderItemDTO> itemList) {
        this.itemList = itemList;
    }

    public String getStateLabel() {
        return stateLabel;
    }

    public void setStateLabel(String stateLabel) {
        this.stateLabel = stateLabel;
    }

    public String getBusinessModelLabel() {
        return businessModelLabel;
    }

    public void setBusinessModelLabel(String businessModelLabel) {
        this.businessModelLabel = businessModelLabel;
    }

    public String getCurrencyLabel() {
        return currencyLabel;
    }

    public void setCurrencyLabel(String currencyLabel) {
        this.currencyLabel = currencyLabel;
    }
    public String getTallyingUnitLabel() {
        return tallyingUnitLabel;
    }

    public void setTallyingUnitLabel(String tallyingUnitLabel) {
        this.tallyingUnitLabel = tallyingUnitLabel;
    }
    public String getCreateDateStartDate() {
        return createDateStartDate;
    }

    public void setCreateDateStartDate(String createDateStartDate) {
        this.createDateStartDate = createDateStartDate;
    }

    public String getCreateDateEndDate() {
        return createDateEndDate;
    }

    public void setCreateDateEndDate(String createDateEndDate) {
        this.createDateEndDate = createDateEndDate;
    }

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
    }
    /*state get 方法 */
    public String getState(){
    return state;
    }
    /*state set 方法 */
    public void setState(String  state){
        this.state=state;
        if(StringUtils.isNotBlank(state)){
            this.stateLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.preSaleOrder_stateList, state);
        }
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
    /*customerName get 方法 */
    public String getCustomerName(){
    return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
    this.customerName=customerName;
    }
    /*businessModel get 方法 */
    public String getBusinessModel(){
    return businessModel;
    }
    /*businessModel set 方法 */
    public void setBusinessModel(String  businessModel){
        this.businessModel=businessModel;
        if(StringUtils.isNotBlank(businessModel)){
            this.businessModelLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.preSaleOrder_businessModelList, businessModel);
        }
    }
    /*outDepotId get 方法 */
    public Long getOutDepotId(){
    return outDepotId;
    }
    /*outDepotId set 方法 */
    public void setOutDepotId(Long  outDepotId){
    this.outDepotId=outDepotId;
    }
    /*outDepotName get 方法 */
    public String getOutDepotName(){
    return outDepotName;
    }
    /*outDepotName set 方法 */
    public void setOutDepotName(String  outDepotName){
    this.outDepotName=outDepotName;
    }
    /*buId get 方法 */
    public Long getBuId(){
    return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    this.buId=buId;
    }
    /*buName get 方法 */
    public String getBuName(){
    return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
    this.buName=buName;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
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
    /*tallyingUnit get 方法 */
    public String getTallyingUnit(){
    return tallyingUnit;
    }
    /*tallyingUnit set 方法 */
    public void setTallyingUnit(String  tallyingUnit){
        this.tallyingUnit=tallyingUnit;
        if(StringUtils.isNotBlank(tallyingUnit)){
            this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
        }

    }
    /*remark get 方法 */
    public String getRemark(){
    return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
    this.remark=remark;
    }
    /*auditor get 方法 */
    public Long getAuditor(){
    return auditor;
    }
    /*auditor set 方法 */
    public void setAuditor(Long  auditor){
    this.auditor=auditor;
    }
    /*auditDate get 方法 */
    public Timestamp getAuditDate(){
    return auditDate;
    }
    /*auditDate set 方法 */
    public void setAuditDate(Timestamp  auditDate){
    this.auditDate=auditDate;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*modifier get 方法 */
    public Long getModifier(){
    return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
    this.modifier=modifier;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getModifierUsername() {
        return modifierUsername;
    }

    public void setModifierUsername(String modifierUsername) {
        this.modifierUsername = modifierUsername;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
}
