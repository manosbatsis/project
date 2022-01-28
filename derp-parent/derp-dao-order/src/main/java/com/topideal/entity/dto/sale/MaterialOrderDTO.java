package com.topideal.entity.dto.sale;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MaterialOrderDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;
   
	@ApiModelProperty(value = "领料单编号")
    private String code;
   
	@ApiModelProperty(value = "客户ID(供应商)")
    private Long customerId;
   
	@ApiModelProperty(value = "客户名称")
    private String customerName;
    
	@ApiModelProperty(value = "商家ID")
    private Long merchantId;
   
	@ApiModelProperty(value = "商家名称")
    private String merchantName;
  
	@ApiModelProperty(value = "出库仓ID")
    private Long outDepotId;
   
	@ApiModelProperty(value = "出库仓名称")
    private String outDepotName;
   
	@ApiModelProperty(value = "出库仓编码")
    private String outDepotCode;
    
	@ApiModelProperty(value = "po单号")
    private String poNo;

	@ApiModelProperty(value = "交货日期")
    private Timestamp deliveryDate;

	@ApiModelProperty(value = "订单状态:001:待审核,002:审核中,003:已审核,004:出库中,005:已出库,006:已删除")
    private String state;
	@ApiModelProperty(value = "订单状态(中文)")
    private String stateLabel;

	@ApiModelProperty(value = "备注")
    private String remark;

	@ApiModelProperty(value = "领料单总数量")
    private Integer totalNum;

	@ApiModelProperty(value = "入仓仓库id")
    private Long inDepotId;

	@ApiModelProperty(value = "入仓仓库")
    private String inDepotName;

	@ApiModelProperty(value = "合同号")
    private String contractNo;

	@ApiModelProperty(value = "申报地国检编码")
    private String inspectNo;

	@ApiModelProperty(value = "申报地海关编码")
    private String customsNo;

	@ApiModelProperty(value = "入仓仓库编码")
    private String inDepotCode;

	@ApiModelProperty(value = "卓志编码")
    private String topidealCode;

	@ApiModelProperty(value = "是否同关区（0-否，1-是）")
    private String isSameArea;

	@ApiModelProperty(value = "理货单位 00-托盘 01-箱 02-件")
    private String tallyingUnit;
	@ApiModelProperty(value = "理货单位(中文)")
    private String tallyingUnitLabel;

	@ApiModelProperty(value = "目的地")
    private String destination;
	
	@ApiModelProperty(value = "目的地（中文）")
    private String destinationLabel;

	@ApiModelProperty(value = "国家")
    private String country;

	@ApiModelProperty(value = "收件人名称")
    private String receiverName;

	@ApiModelProperty(value = "收件人地址")
    private String receiverAddress;

	@ApiModelProperty(value = "邮寄方式 1.寄售 2.自提")
    private String mailMode;

	@ApiModelProperty(value = "币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String currency;
	@ApiModelProperty(value = "币种(中文)")
    private String currencyLabel;

	@ApiModelProperty(value = " 事业部名称")
    private String buName;

	@ApiModelProperty(value = "事业部id")
    private Long buId;

	@ApiModelProperty(value = "提单毛重")
    private Double billWeight;

	@ApiModelProperty(value = "运输方式 1-空运 2-海运 3-陆运 4-港到仓拖车 5-中欧铁路 6-其他")
    private String transport;
	@ApiModelProperty(value = "运输方式（中文）")
    private String transportLabel;

	@ApiModelProperty(value = "标准箱TEU")
    private String teu;

	@ApiModelProperty(value = "车次")
    private String trainno;

	@ApiModelProperty(value = "承运商")
    private String carrier;

	@ApiModelProperty(value = "托数")
    private Integer torusNumber;

	@ApiModelProperty(value = "出库地点")
    private String outdepotAddr;

	@ApiModelProperty(value = "付款条约")
    private String payRules;

	@ApiModelProperty(value = "发票单号")
    private String invoiceNo;

	@ApiModelProperty(value = "卸货港")
    private String portDes;

	@ApiModelProperty(value = "包装")
    private String pack;

	@ApiModelProperty(value = "箱数")
    private Integer boxNum;
  
	@ApiModelProperty(value = "托盘材质 01-塑料托盘 02-木质托盘 03-IPPC木托 04-纸箱")
    private String torrPackType;
	@ApiModelProperty(value = "托盘材质（中文）")
    private String torrPackTypeLabel;

	@ApiModelProperty(value = "出库关区id")
    private Long outCustomsId;

	@ApiModelProperty(value = "出库关区编码")
    private String outCustomsCode;
 
	@ApiModelProperty(value = "出库关区名称")
    private String outPlatformCustoms;

	@ApiModelProperty(value = "入库关区id")
    private Long inCustomsId;

	@ApiModelProperty(value = "入库关区编码")
    private String inCustomsCode;

	@ApiModelProperty(value = "入库关区名称")
    private String inPlatformCustoms;
 
	@ApiModelProperty(value = "业务场景")
    private String model;

	@ApiModelProperty(value = "服务类型")
    private String serveTypes;

	@ApiModelProperty(value = "创建人",hidden=true)
    private Long creater;

	@ApiModelProperty(value = "创建人名称",hidden=true)
    private String createName;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value = "审核人",hidden=true)
    private Long auditor;

	@ApiModelProperty(value = "审核人名称",hidden=true)
    private String auditName;

	@ApiModelProperty(value = "审核时间",hidden=true)
    private Timestamp auditDate;

	@ApiModelProperty(value = "修改人",hidden=true)
    private Long modifier;

	@ApiModelProperty(value = "修改人用户名",hidden=true)
    private String modifierUsername;

	@ApiModelProperty(value = "修改时间",hidden=true)
    private Timestamp modifyDate;

	@ApiModelProperty(value = "商品信息")
    private List<MaterialOrderItemDTO> itemList;
	
	@ApiModelProperty(value = "事业部集合")
	private List<Long> buList;
	 
	@ApiModelProperty(value = "主键集合ids")
	private String ids;

    @ApiModelProperty(value = "库位类型id")
    private Long stockLocationTypeId;

    @ApiModelProperty(value = "库位类型")
    private String stockLocationTypeName;
		 
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
    /*outDepotCode get 方法 */
    public String getOutDepotCode(){
    	return outDepotCode;
    }
    /*outDepotCode set 方法 */
    public void setOutDepotCode(String  outDepotCode){
    	this.outDepotCode=outDepotCode;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    	return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    	this.poNo=poNo;
    }
    /*deliveryDate get 方法 */
    public Timestamp getDeliveryDate(){
    	return deliveryDate;
    }
    /*deliveryDate set 方法 */
    public void setDeliveryDate(Timestamp  deliveryDate){
    	this.deliveryDate=deliveryDate;
    }
    /*state get 方法 */
    public String getState(){
    	return state;
    }
    /*state set 方法 */
    public void setState(String  state){
    	this.state=state;
    	this.stateLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.materialOrder_stateList, state);
    }
    /*remark get 方法 */
    public String getRemark(){
    	return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
    	this.remark=remark;
    }
    /*totalNum get 方法 */
    public Integer getTotalNum(){
    	return totalNum;
    }
    /*totalNum set 方法 */
    public void setTotalNum(Integer  totalNum){
    	this.totalNum=totalNum;
    }
    /*inDepotId get 方法 */
    public Long getInDepotId(){
    	return inDepotId;
    }
    /*inDepotId set 方法 */
    public void setInDepotId(Long  inDepotId){
    	this.inDepotId=inDepotId;
    }
    /*inDepotName get 方法 */
    public String getInDepotName(){
    	return inDepotName;
    }
    /*inDepotName set 方法 */
    public void setInDepotName(String  inDepotName){
    	this.inDepotName=inDepotName;
    }
    /*contractNo get 方法 */
    public String getContractNo(){
    	return contractNo;
    }
    /*contractNo set 方法 */
    public void setContractNo(String  contractNo){
    	this.contractNo=contractNo;
    }
    /*inspectNo get 方法 */
    public String getInspectNo(){
    	return inspectNo;
    }
    /*inspectNo set 方法 */
    public void setInspectNo(String  inspectNo){
    	this.inspectNo=inspectNo;
    }
    /*customsNo get 方法 */
    public String getCustomsNo(){
    	return customsNo;
    }
    /*customsNo set 方法 */
    public void setCustomsNo(String  customsNo){
    	this.customsNo=customsNo;
    }
    /*inDepotCode get 方法 */
    public String getInDepotCode(){
    	return inDepotCode;
    }
    /*inDepotCode set 方法 */
    public void setInDepotCode(String  inDepotCode){
    	this.inDepotCode=inDepotCode;
    }
    /*topidealCode get 方法 */
    public String getTopidealCode(){
    	return topidealCode;
    }
    /*topidealCode set 方法 */
    public void setTopidealCode(String  topidealCode){
    	this.topidealCode=topidealCode;
    }
    /*isSameArea get 方法 */
    public String getIsSameArea(){
    	return isSameArea;
    }
    /*isSameArea set 方法 */
    public void setIsSameArea(String  isSameArea){
    	this.isSameArea=isSameArea;
    }
    /*tallyingUnit get 方法 */
    public String getTallyingUnit(){
    	return tallyingUnit;
    }
    /*tallyingUnit set 方法 */
    public void setTallyingUnit(String  tallyingUnit){
    	this.tallyingUnit=tallyingUnit;
    	this.tallyingUnitLabel =  DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
    }
    /*destination get 方法 */
    public String getDestination(){
    	return destination;
    }
    /*destination set 方法 */
    public void setDestination(String  destination){
    	this.destination=destination;
    	this.destinationLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleOrder_destinationList, destination);
    }
    /*country get 方法 */
    public String getCountry(){
    	return country;
    }
    /*country set 方法 */
    public void setCountry(String  country){
    	this.country=country;
    }
    /*receiverName get 方法 */
    public String getReceiverName(){
    	return receiverName;
    }
    /*receiverName set 方法 */
    public void setReceiverName(String  receiverName){
    	this.receiverName=receiverName;
    }
    /*receiverAddress get 方法 */
    public String getReceiverAddress(){
    	return receiverAddress;
    }
    /*receiverAddress set 方法 */
    public void setReceiverAddress(String  receiverAddress){
    	this.receiverAddress=receiverAddress;
    }
    /*mailMode get 方法 */
    public String getMailMode(){
    	return mailMode;
    }
    /*mailMode set 方法 */
    public void setMailMode(String  mailMode){
    	this.mailMode=mailMode;
    }
    /*currency get 方法 */
    public String getCurrency(){
    	return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    	this.currency=currency;
    	this.currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency);
    }
    /*buName get 方法 */
    public String getBuName(){
    	return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
    	this.buName=buName;
    }
    /*buId get 方法 */
    public Long getBuId(){
    	return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    	this.buId=buId;
    }
    /*billWeight get 方法 */
    public Double getBillWeight(){
    	return billWeight;
    }
    /*billWeight set 方法 */
    public void setBillWeight(Double  billWeight){
    	this.billWeight=billWeight;
    }
    /*transport get 方法 */
    public String getTransport(){
    	return transport;
    }
    /*transport set 方法 */
    public void setTransport(String  transport){
    	this.transport=transport;
    	this.transportLabel = DERP.getLabelByKey(DERP.transportList, transport);
    }
    /*teu get 方法 */
    public String getTeu(){
    	return teu;
    }
    /*teu set 方法 */
    public void setTeu(String  teu){
    	this.teu=teu;
    }
    /*trainno get 方法 */
    public String getTrainno(){
    	return trainno;
    }
    /*trainno set 方法 */
    public void setTrainno(String  trainno){
    	this.trainno=trainno;
    }
    /*carrier get 方法 */
    public String getCarrier(){
    	return carrier;
    }
    /*carrier set 方法 */
    public void setCarrier(String  carrier){
    	this.carrier=carrier;
    }
    /*torusNumber get 方法 */
    public Integer getTorusNumber(){
    	return torusNumber;
    }
    /*torusNumber set 方法 */
    public void setTorusNumber(Integer  torusNumber){
    	this.torusNumber=torusNumber;
    }
    /*outdepotAddr get 方法 */
    public String getOutdepotAddr(){
    	return outdepotAddr;
    }
    /*outdepotAddr set 方法 */
    public void setOutdepotAddr(String  outdepotAddr){
    	this.outdepotAddr=outdepotAddr;
    }
    /*payRules get 方法 */
    public String getPayRules(){
    	return payRules;
    }
    /*payRules set 方法 */
    public void setPayRules(String  payRules){
    	this.payRules=payRules;
    }
    /*invoiceNo get 方法 */
    public String getInvoiceNo(){
    	return invoiceNo;
    }
    /*invoiceNo set 方法 */
    public void setInvoiceNo(String  invoiceNo){
    	this.invoiceNo=invoiceNo;
    }
    /*portDes get 方法 */
    public String getPortDes(){
    	return portDes;
    }
    /*portDes set 方法 */
    public void setPortDes(String  portDes){
    	this.portDes=portDes;
    }
    /*pack get 方法 */
    public String getPack(){
    	return pack;
    }
    /*pack set 方法 */
    public void setPack(String  pack){
    	this.pack=pack;
    }
    /*boxNum get 方法 */
    public Integer getBoxNum(){
    	return boxNum;
    }
    /*boxNum set 方法 */
    public void setBoxNum(Integer  boxNum){
    	this.boxNum=boxNum;
    }
    /*torrPackType get 方法 */
    public String getTorrPackType(){
    	return torrPackType;
    }
    /*torrPackType set 方法 */
    public void setTorrPackType(String  torrPackType){
    	this.torrPackType=torrPackType;
    	this.torrPackTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.order_torrpacktypeList, torrPackType);
    }
    /*outCustomsId get 方法 */
    public Long getOutCustomsId(){
    	return outCustomsId;
    }
    /*outCustomsId set 方法 */
    public void setOutCustomsId(Long  outCustomsId){
    	this.outCustomsId=outCustomsId;
    }
    /*outCustomsCode get 方法 */
    public String getOutCustomsCode(){
    	return outCustomsCode;
    }
    /*outCustomsCode set 方法 */
    public void setOutCustomsCode(String  outCustomsCode){
    	this.outCustomsCode=outCustomsCode;
    }
    /*outPlatformCustoms get 方法 */
    public String getOutPlatformCustoms(){
    	return outPlatformCustoms;
    }
    /*outPlatformCustoms set 方法 */
    public void setOutPlatformCustoms(String  outPlatformCustoms){
    	this.outPlatformCustoms=outPlatformCustoms;
    }
    /*inCustomsId get 方法 */
    public Long getInCustomsId(){
    	return inCustomsId;
    }
    /*inCustomsId set 方法 */
    public void setInCustomsId(Long  inCustomsId){
    	this.inCustomsId=inCustomsId;
    }
    /*inCustomsCode get 方法 */
    public String getInCustomsCode(){
    	return inCustomsCode;
    }
    /*inCustomsCode set 方法 */
    public void setInCustomsCode(String  inCustomsCode){
    	this.inCustomsCode=inCustomsCode;
    }
    /*inPlatformCustoms get 方法 */
    public String getInPlatformCustoms(){
    	return inPlatformCustoms;
    }
    /*inPlatformCustoms set 方法 */
    public void setInPlatformCustoms(String  inPlatformCustoms){
    	this.inPlatformCustoms=inPlatformCustoms;
    }
    /*model get 方法 */
    public String getModel(){
    	return model;
    }
    /*model set 方法 */
    public void setModel(String  model){
    	this.model=model;
    }
    /*serveTypes get 方法 */
    public String getServeTypes(){
    	return serveTypes;
    }
    /*serveTypes set 方法 */
    public void setServeTypes(String  serveTypes){
    	this.serveTypes=serveTypes;
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
    /*modifierUsername get 方法 */
    public String getModifierUsername(){
    	return modifierUsername;
    }
    /*modifierUsername set 方法 */
    public void setModifierUsername(String  modifierUsername){
    	this.modifierUsername=modifierUsername;
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
	public List<MaterialOrderItemDTO> getItemList() {
		return itemList;
	}
	public void setItemList(List<MaterialOrderItemDTO> itemList) {
		this.itemList = itemList;
	}
	public List<Long> getBuList() {
		return buList;
	}
	public void setBuList(List<Long> buList) {
		this.buList = buList;
	}
	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}
	public String getTransportLabel() {
		return transportLabel;
	}
	public String getTorrPackTypeLabel() {
		return torrPackTypeLabel;
	}
    public String getCurrencyLabel() {
        return currencyLabel;
    }
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getDestinationLabel() {
		return destinationLabel;
	}

    public void setStateLabel(String stateLabel) {
        this.stateLabel = stateLabel;
    }

    public void setTallyingUnitLabel(String tallyingUnitLabel) {
        this.tallyingUnitLabel = tallyingUnitLabel;
    }

    public void setDestinationLabel(String destinationLabel) {
        this.destinationLabel = destinationLabel;
    }

    public void setCurrencyLabel(String currencyLabel) {
        this.currencyLabel = currencyLabel;
    }

    public void setTransportLabel(String transportLabel) {
        this.transportLabel = transportLabel;
    }

    public void setTorrPackTypeLabel(String torrPackTypeLabel) {
        this.torrPackTypeLabel = torrPackTypeLabel;
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
}
