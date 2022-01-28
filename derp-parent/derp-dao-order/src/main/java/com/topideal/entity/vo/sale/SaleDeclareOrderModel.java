package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class SaleDeclareOrderModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 销售预申报单编号
    */
    private String code;
    /**
    * 客户ID
    */
    private Long customerId;
    /**
    * 客户名称
    */
    private String customerName;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * po单号
    */
    private String poNo;
    /**
    * 出库仓ID
    */
    private Long outDepotId;
    /**
    * 出库仓名称
    */
    private String outDepotName;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    *  事业部id
    */
    private Long buId;
    /**
    * LBX单号
    */
    private String lbxNo;
    /**
    * 入仓仓库id
    */
    private Long inDepotId;
    /**
    * 入仓仓库名称
    */
    private String inDepotName;
    /**
    * 付款条款 1-一次付款-付款发货 2-多次付款 3-一次付款-发货付款
    */
    private String paymentTerms;
    /**
    * 贸易条款 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW
    */
    private String tradeTerms;
    /**
    * 业务模式  1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结
    */
    private String businessModel;
    /**
    * 币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    */
    private String currency;
    /**
    * 理货单位 00-托盘 01-箱 02-件
    */
    private String tallyingUnit;
    /**
    * 装货港
    */
    private String loadPort;
    /**
    * 目的地名称
    */
    private String destination;
    /**
    * 目的港名称
    */
    private String destinationPort;
    /**
    * 卸货港
    */
    private String portDes;
    /**
    * 收货地点
    */
    private String deliveryAddr;
    /**
    * 进出口口岸
    */
    private String imExpPort;
    /**
    * 出库关区id
    */
    private Long outCustomsId;
    /**
    * 出库关区名称
    */
    private String outCustomsName;
    /**
    * 入库关区id
    */
    private Long inCustomsId;
    /**
    * 入库关区名称
    */
    private String inCustomsName;
    /**
    * 启运港
    */
    private String departurePort;
    /**
    * 装船时间
    */
    private Timestamp shipDate;
    /**
    * 备注
    */
    private String remark;
    /**
    * 运输方式 1-空运 2-海运 3-陆运 4-港到仓拖车 5-中欧铁路 6-其他
    */
    private String transport;
    /**
    * 境外发货人
    */
    private String shipper;
    /**
    * 承运商
    */
    private String carrier;
    /**
    * 发票单号
    */
    private String invoiceNo;
    /**
    * 报关合同号
    */
    private String contractNo;
    /**
    * 头程提单号
    */
    private String ladingBill;
    /**
    * 二程提单号
    */
    private String blNo;
    /**
    * 车次
    */
    private String trainNo;
    /**
    * 托数
    */
    private Integer torrNum;
    /**
    * 包装
    */
    private String pack;
    /**
    * 箱数
    */
    private Integer boxNum;
    /**
    * 托盘材质 01-塑料托盘 02-木质托盘 03-IPPC木托 04-纸箱
    */
    private String torrPackType;
    /**
    * 提单毛重
    */
    private Double billWeight;
    /**
    * 车型 及 数量
    */
    private String motorcycleType;
    /**
    * 唛头
    */
    private String mark;
    /**
    * 标准箱TEU
    */
    private String teu;
    /**
    * 订单状态 001-待打托 002待清关 003-待物流委托 004-已出库 005-部分上架 007-已上架 006已删除
    */
    private String status;
    /**
    * 接口状态 0-未推接口；1-已推接口；2-接口推送失败 用于海外仓推跨境宝
    */
    private String apiStatus;
    /**
    * 推送出仓指令时间
    */
    private Timestamp pushOutDate;
    /**
    * 打托时间
    */
    private Timestamp pallteizeDate;
    /**
    * 清关提交时间
    */
    private Timestamp customsSubmitDate;
    /**
    * 清关确认时间
    */
    private Timestamp customsConfirmDate;
    /**
    * 确认申报时间
    */
    private Timestamp confirmDeclarationDate;
    /**
    * 物流委托时间
    */
    private Timestamp logisticsCommissionDate;
    /**
    * 出库时间
    */
    private Timestamp deliverDate;
    /**
    * 上架时间
    */
    private Timestamp shelfDate;
    /**
    * 国家
    */
    private String country;
    /**
    * 收件人名称
    */
    private String receiverName;
    /**
    * 收件人地址
    */
    private String receiverAddress;
    /**
    * 邮寄方式 1.寄售 2.自提
    */
    private String mailMode;
    /**
    * 销售订单ID集合
    */
    private String saleOrderIds;
    /**
    * 销售订单编号集合
    */
    private String saleOrderCodes;
    /**
    * 创建人
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
    * 修改人
    */
    private Long modifier;
    /**
    * 修改人用户名
    */
    private String modifierUsername;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     * 首次上架时间
     */
     private Timestamp firstShelfDate;
     /**
     * 完结上架时间
     */
     private Timestamp endShelfDate;

    /**
     * 导出打托资料时间
     */
    private Timestamp exportPallteizeDate;


    public Timestamp getExportPallteizeDate() {
        return exportPallteizeDate;
    }

    public void setExportPallteizeDate(Timestamp exportPallteizeDate) {
        this.exportPallteizeDate = exportPallteizeDate;
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
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
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
    /*lbxNo get 方法 */
    public String getLbxNo(){
    return lbxNo;
    }
    /*lbxNo set 方法 */
    public void setLbxNo(String  lbxNo){
    this.lbxNo=lbxNo;
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
    /*paymentTerms get 方法 */
    public String getPaymentTerms(){
    return paymentTerms;
    }
    /*paymentTerms set 方法 */
    public void setPaymentTerms(String  paymentTerms){
    this.paymentTerms=paymentTerms;
    }
    /*tradeTerms get 方法 */
    public String getTradeTerms(){
    return tradeTerms;
    }
    /*tradeTerms set 方法 */
    public void setTradeTerms(String  tradeTerms){
    this.tradeTerms=tradeTerms;
    }
    /*businessModel get 方法 */
    public String getBusinessModel(){
    return businessModel;
    }
    /*businessModel set 方法 */
    public void setBusinessModel(String  businessModel){
    this.businessModel=businessModel;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*tallyingUnit get 方法 */
    public String getTallyingUnit(){
    return tallyingUnit;
    }
    /*tallyingUnit set 方法 */
    public void setTallyingUnit(String  tallyingUnit){
    this.tallyingUnit=tallyingUnit;
    }
    /*loadPort get 方法 */
    public String getLoadPort(){
    return loadPort;
    }
    /*loadPort set 方法 */
    public void setLoadPort(String  loadPort){
    this.loadPort=loadPort;
    }
    /*destination get 方法 */
    public String getDestination(){
    return destination;
    }
    /*destination set 方法 */
    public void setDestination(String  destination){
    this.destination=destination;
    }
    /*destinationPort get 方法 */
    public String getDestinationPort(){
    return destinationPort;
    }
    /*destinationPort set 方法 */
    public void setDestinationPort(String  destinationPort){
    this.destinationPort=destinationPort;
    }
    /*portDes get 方法 */
    public String getPortDes(){
    return portDes;
    }
    /*portDes set 方法 */
    public void setPortDes(String  portDes){
    this.portDes=portDes;
    }
    /*deliveryAddr get 方法 */
    public String getDeliveryAddr(){
    return deliveryAddr;
    }
    /*deliveryAddr set 方法 */
    public void setDeliveryAddr(String  deliveryAddr){
    this.deliveryAddr=deliveryAddr;
    }
    /*imExpPort get 方法 */
    public String getImExpPort(){
    return imExpPort;
    }
    /*imExpPort set 方法 */
    public void setImExpPort(String  imExpPort){
    this.imExpPort=imExpPort;
    }
    /*outCustomsId get 方法 */
    public Long getOutCustomsId(){
    return outCustomsId;
    }
    /*outCustomsId set 方法 */
    public void setOutCustomsId(Long  outCustomsId){
    this.outCustomsId=outCustomsId;
    }
    /*outCustomsName get 方法 */
    public String getOutCustomsName(){
    return outCustomsName;
    }
    /*outCustomsName set 方法 */
    public void setOutCustomsName(String  outCustomsName){
    this.outCustomsName=outCustomsName;
    }
    /*inCustomsId get 方法 */
    public Long getInCustomsId(){
    return inCustomsId;
    }
    /*inCustomsId set 方法 */
    public void setInCustomsId(Long  inCustomsId){
    this.inCustomsId=inCustomsId;
    }
    /*inCustomsName get 方法 */
    public String getInCustomsName(){
    return inCustomsName;
    }
    /*inCustomsName set 方法 */
    public void setInCustomsName(String  inCustomsName){
    this.inCustomsName=inCustomsName;
    }
    /*departurePort get 方法 */
    public String getDeparturePort(){
    return departurePort;
    }
    /*departurePort set 方法 */
    public void setDeparturePort(String  departurePort){
    this.departurePort=departurePort;
    }
    /*shipDate get 方法 */
    public Timestamp getShipDate(){
    return shipDate;
    }
    /*shipDate set 方法 */
    public void setShipDate(Timestamp  shipDate){
    this.shipDate=shipDate;
    }
    /*remark get 方法 */
    public String getRemark(){
    return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
    this.remark=remark;
    }
    /*transport get 方法 */
    public String getTransport(){
    return transport;
    }
    /*transport set 方法 */
    public void setTransport(String  transport){
    this.transport=transport;
    }
    /*shipper get 方法 */
    public String getShipper(){
    return shipper;
    }
    /*shipper set 方法 */
    public void setShipper(String  shipper){
    this.shipper=shipper;
    }
    /*carrier get 方法 */
    public String getCarrier(){
    return carrier;
    }
    /*carrier set 方法 */
    public void setCarrier(String  carrier){
    this.carrier=carrier;
    }
    /*invoiceNo get 方法 */
    public String getInvoiceNo(){
    return invoiceNo;
    }
    /*invoiceNo set 方法 */
    public void setInvoiceNo(String  invoiceNo){
    this.invoiceNo=invoiceNo;
    }
    /*contractNo get 方法 */
    public String getContractNo(){
    return contractNo;
    }
    /*contractNo set 方法 */
    public void setContractNo(String  contractNo){
    this.contractNo=contractNo;
    }
    /*ladingBill get 方法 */
    public String getLadingBill(){
    return ladingBill;
    }
    /*ladingBill set 方法 */
    public void setLadingBill(String  ladingBill){
    this.ladingBill=ladingBill;
    }
    /*blNo get 方法 */
    public String getBlNo(){
    return blNo;
    }
    /*blNo set 方法 */
    public void setBlNo(String  blNo){
    this.blNo=blNo;
    }
    /*trainNo get 方法 */
    public String getTrainNo(){
    return trainNo;
    }
    /*trainNo set 方法 */
    public void setTrainNo(String  trainNo){
    this.trainNo=trainNo;
    }
    /*torrNum get 方法 */
    public Integer getTorrNum(){
    return torrNum;
    }
    /*torrNum set 方法 */
    public void setTorrNum(Integer  torrNum){
    this.torrNum=torrNum;
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
    }
    /*billWeight get 方法 */
    public Double getBillWeight(){
    return billWeight;
    }
    /*billWeight set 方法 */
    public void setBillWeight(Double  billWeight){
    this.billWeight=billWeight;
    }
    /*motorcycleType get 方法 */
    public String getMotorcycleType(){
    return motorcycleType;
    }
    /*motorcycleType set 方法 */
    public void setMotorcycleType(String  motorcycleType){
    this.motorcycleType=motorcycleType;
    }
    /*mark get 方法 */
    public String getMark(){
    return mark;
    }
    /*mark set 方法 */
    public void setMark(String  mark){
    this.mark=mark;
    }
    /*teu get 方法 */
    public String getTeu(){
    return teu;
    }
    /*teu set 方法 */
    public void setTeu(String  teu){
    this.teu=teu;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    }
    /*apiStatus get 方法 */
    public String getApiStatus(){
    return apiStatus;
    }
    /*apiStatus set 方法 */
    public void setApiStatus(String  apiStatus){
    this.apiStatus=apiStatus;
    }
    /*pushOutDate get 方法 */
    public Timestamp getPushOutDate(){
    return pushOutDate;
    }
    /*pushOutDate set 方法 */
    public void setPushOutDate(Timestamp  pushOutDate){
    this.pushOutDate=pushOutDate;
    }
    /*pallteizeDate get 方法 */
    public Timestamp getPallteizeDate(){
    return pallteizeDate;
    }
    /*pallteizeDate set 方法 */
    public void setPallteizeDate(Timestamp  pallteizeDate){
    this.pallteizeDate=pallteizeDate;
    }
    /*customsSubmitDate get 方法 */
    public Timestamp getCustomsSubmitDate(){
    return customsSubmitDate;
    }
    /*customsSubmitDate set 方法 */
    public void setCustomsSubmitDate(Timestamp  customsSubmitDate){
    this.customsSubmitDate=customsSubmitDate;
    }
    /*customsConfirmDate get 方法 */
    public Timestamp getCustomsConfirmDate(){
    return customsConfirmDate;
    }
    /*customsConfirmDate set 方法 */
    public void setCustomsConfirmDate(Timestamp  customsConfirmDate){
    this.customsConfirmDate=customsConfirmDate;
    }
    /*confirmDeclarationDate get 方法 */
    public Timestamp getConfirmDeclarationDate(){
    return confirmDeclarationDate;
    }
    /*confirmDeclarationDate set 方法 */
    public void setConfirmDeclarationDate(Timestamp  confirmDeclarationDate){
    this.confirmDeclarationDate=confirmDeclarationDate;
    }
    /*logisticsCommissionDate get 方法 */
    public Timestamp getLogisticsCommissionDate(){
    return logisticsCommissionDate;
    }
    /*logisticsCommissionDate set 方法 */
    public void setLogisticsCommissionDate(Timestamp  logisticsCommissionDate){
    this.logisticsCommissionDate=logisticsCommissionDate;
    }
    /*deliverDate get 方法 */
    public Timestamp getDeliverDate(){
    return deliverDate;
    }
    /*deliverDate set 方法 */
    public void setDeliverDate(Timestamp  deliverDate){
    this.deliverDate=deliverDate;
    }
    /*shelfDate get 方法 */
    public Timestamp getShelfDate(){
    return shelfDate;
    }
    /*shelfDate set 方法 */
    public void setShelfDate(Timestamp  shelfDate){
    this.shelfDate=shelfDate;
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
    /*saleOrderIds get 方法 */
    public String getSaleOrderIds(){
    return saleOrderIds;
    }
    /*saleOrderIds set 方法 */
    public void setSaleOrderIds(String  saleOrderIds){
    this.saleOrderIds=saleOrderIds;
    }
    /*saleOrderCodes get 方法 */
    public String getSaleOrderCodes(){
    return saleOrderCodes;
    }
    /*saleOrderCodes set 方法 */
    public void setSaleOrderCodes(String  saleOrderCodes){
    this.saleOrderCodes=saleOrderCodes;
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
	public Timestamp getFirstShelfDate() {
		return firstShelfDate;
	}
	public void setFirstShelfDate(Timestamp firstShelfDate) {
		this.firstShelfDate = firstShelfDate;
	}
	public Timestamp getEndShelfDate() {
		return endShelfDate;
	}
	public void setEndShelfDate(Timestamp endShelfDate) {
		this.endShelfDate = endShelfDate;
	}






}
