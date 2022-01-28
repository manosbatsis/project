package com.topideal.entity.dto.sale;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class BuMoveInventoryDTO extends PageModel implements Serializable{

   
	@ApiModelProperty(value = "id")
    private Long id;
   
	@ApiModelProperty(value = "移库单号")
    private String code;
  
	@ApiModelProperty(value = "来源业务单号")
    private String businessNo;
   
	@ApiModelProperty(value = "单据来源  1：手工导入；2：系统自动生成")
    private String orderSource;
	@ApiModelProperty(value = "单据来源（中文）")
    private String orderSourceLabel;
  
	@ApiModelProperty(value = "移库状态 017-待移库,018-已移库,027-移库中")
    private String status;
	@ApiModelProperty(value = " 移库状态（中文）")
    private String statusLabel;
    
	@ApiModelProperty(value = "出库仓库ID")
    private Long depotId;
   
	@ApiModelProperty(value = "出库仓库名称")
    private String depotName;
   
	@ApiModelProperty(value = "移入事业部ID")
    private Long inBuId;
   
	@ApiModelProperty(value = "移入事业部名称")
    private String inBuName;
   
	@ApiModelProperty(value = "移出事业部id")
    private Long outBuId;
  
	@ApiModelProperty(value = "移出事业部名称")
    private String outBuName;
   
	@ApiModelProperty(value = "移库日期")
    private Timestamp moveDate;
   
	@ApiModelProperty(value = "公司ID")
    private Long merchantId;
    
	@ApiModelProperty(value = "公司名称")
    private String merchantName;
    
	@ApiModelProperty(value = "创建人id")
    private Long creater;
    
	@ApiModelProperty(value = "创建人名称")
    private String createName;
   
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
   
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
   
	@ApiModelProperty(value = "来源单据类别 DSDD:电商订单、XSDD：销售订单  KCYKD:库存移库单")
    private String orderType;
	@ApiModelProperty(value = "来源单据类别 DSDD:电商订单、XSDD：销售订单  KCYKD:库存移库单")
    private String orderTypeLabel;
    // 商品信息-表体
	@ApiModelProperty(value = "商品信息-表体")
    private List<BuMoveInventoryItemDTO> itemList;

    // 扩展字段
	@ApiModelProperty(value = "创建时间开始",hidden=true)
    private String createStartDate;// 创建时间开始
	@ApiModelProperty(value = "创建时间结束",hidden=true)
    private String createEndDate;//创建时间结束
	@ApiModelProperty(value = "移库日期开始",hidden=true)
    private String moveStartDate;// 移库日期开始
	@ApiModelProperty(value = "移库日期结束",hidden=true)
    private String moveEndDate;//移库日期结束
    // 当前登录用户绑定的移出事业部集合
	@ApiModelProperty(value = "移出事业部集合")
    private List<Long> outBuList;
    // 当前登录用户绑定的移入事业部集合
	@ApiModelProperty(value = "入事业部集合")
    private List<Long> inBuList;
    //'移库币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
	@ApiModelProperty(value = "移库币种")
    private String currency;
    @ApiModelProperty(value = "移库币种（中文）")
    private String currencyLabel;
    //'移库币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    @ApiModelProperty(value = "协议币种")
    private String agreementCurrency;
    @ApiModelProperty(value = "协议币种（中文）")
    private String agreementCurrencyLabel;
    @ApiModelProperty(value = "主键id集合")
    private String ids;

    @ApiModelProperty(value = "理货单位 00-托盘 01-箱 02-件")
    private String tallyingUnit;
    @ApiModelProperty(value = "理货单位 00-托盘 01-箱 02-件")
    private String tallyingUnitLabel;

    @ApiModelProperty(value = "审核人")
    private Long auditor;

    @ApiModelProperty(value = "审核人名称")
    private String auditName;

    @ApiModelProperty(value = "审核时间")
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
        if(orderSource!=null){
            this.orderSourceLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.buMoveInventory_orderSourceList, orderSource);
        }
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
        this.status=status;
        if(status!=null){
            this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.buMoveInventory_statusList, status);
        }
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

    public String getOrderSourceLabel() {
        return orderSourceLabel;
    }

    public void setOrderSourceLabel(String orderSourceLabel) {
        this.orderSourceLabel = orderSourceLabel;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public List<BuMoveInventoryItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<BuMoveInventoryItemDTO> itemList) {
        this.itemList = itemList;
    }

    public String getCreateStartDate() {
        return createStartDate;
    }

    public void setCreateStartDate(String createStartDate) {
        this.createStartDate = createStartDate;
    }

    public String getCreateEndDate() {
        return createEndDate;
    }

    public void setCreateEndDate(String createEndDate) {
        this.createEndDate = createEndDate;
    }

    public String getMoveStartDate() {
        return moveStartDate;
    }

    public void setMoveStartDate(String moveStartDate) {
        this.moveStartDate = moveStartDate;
    }

    public String getMoveEndDate() {
        return moveEndDate;
    }

    public void setMoveEndDate(String moveEndDate) {
        this.moveEndDate = moveEndDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
        this.orderTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.buMoveInventory_orderTypeList, orderType);
    }

    public List<Long> getOutBuList() {
        return outBuList;
    }

    public void setOutBuList(List<Long> outBuList) {
        this.outBuList = outBuList;
    }

    public List<Long> getInBuList() {
        return inBuList;
    }

    public void setInBuList(List<Long> inBuList) {
        this.inBuList = inBuList;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
        if(StringUtils.isNotBlank(currency)){
            this.currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency);
        }
    }

    public String getCurrencyLabel() {
        return currencyLabel;
    }

    public void setCurrencyLabel(String currencyLabel) {
        this.currencyLabel = currencyLabel;
    }

    public String getAgreementCurrency() {
        return agreementCurrency;
    }

    public void setAgreementCurrency(String agreementCurrency) {
        this.agreementCurrency = agreementCurrency;
        if(StringUtils.isNotBlank(agreementCurrency)){
            this.agreementCurrencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, agreementCurrency);
        }
    }

    public String getAgreementCurrencyLabel() {
        return agreementCurrencyLabel;
    }

    public void setAgreementCurrencyLabel(String agreementCurrencyLabel) {
        this.agreementCurrencyLabel = agreementCurrencyLabel;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getTallyingUnit() {
        return tallyingUnit;
    }

    public void setTallyingUnit(String tallyingUnit) {
        this.tallyingUnit = tallyingUnit;
        this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
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

	public String getOrderTypeLabel() {
		return orderTypeLabel;
	}

	public void setOrderTypeLabel(String orderTypeLabel) {
		this.orderTypeLabel = orderTypeLabel;
	}

	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}

	public void setTallyingUnitLabel(String tallyingUnitLabel) {
		this.tallyingUnitLabel = tallyingUnitLabel;
	}
    
    
}
