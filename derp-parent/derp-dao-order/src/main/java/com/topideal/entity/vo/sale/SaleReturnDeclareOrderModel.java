package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class SaleReturnDeclareOrderModel extends PageModel implements Serializable{

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
    *  事业部id
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 退货类型 1-购销退货
    */
    private String returnType;
    /**
    * 退出库仓ID
    */
    private Long outDepotId;
    /**
    * 退出库仓名称
    */
    private String outDepotName;
    /**
    * 退入仓仓库id
    */
    private Long inDepotId;
    /**
    * 退入仓仓库名称
    */
    private String inDepotName;
    /**
    * 币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    */
    private String currency;
    /**
    * po单号
    */
    private String poNo;
    /**
    * 退货原因
    */
    private String returnReason;
    /**
    * 理货单位 00-托盘 01-箱 02-件
    */
    private String tallyingUnit;
    /**
    * LBX单号
    */
    private String lbxNo;
    /**
    * 贸易条款 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW
    */
    private String tradeTerms;
    /**
    * 装货港
    */
    private String loadPort;
    /**
    *  卸货港编码
    */
    private String portDestNo;
    /**
    * 收货地点
    */
    private String deliveryAddr;
    /**
    * 境外发货人
    */
    private String shipper;
    /**
    * 发票单号
    */
    private String invoiceNo;
    /**
    * 报关合同号
    */
    private String contractNo;
    /**
    * 提单毛重
    */
    private Double billWeight;
    /**
    * 箱数
    */
    private Integer boxNum;
    /**
    * 托数
    */
    private Integer torrNum;
    /**
    * 包装
    */
    private String pack;
    /**
    * 托盘材质 01-塑料托盘 02-木质托盘 03-IPPC木托 04-纸箱
    */
    private String torrPackType;
    /**
    * 运输方式 1-空运 2-海运 3-陆运 4-港到仓拖车 5-中欧铁路 6-其他
    */
    private String transport;
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
    * 头程提单号
    */
    private String ladingBill;
    /**
    * 唛头
    */
    private String mark;
    /**
    * 备注
    */
    private String remark;
    /**
    * 订单状态 001-待审核、002-已审核、003-待入仓、004-已入仓、005-已完结、006-已删除
    */
    private String status;
    /**
    * 接口状态 0-未推接口；1-已推接口；
    */
    private String apiStatus;
    /**
    * 销售退货单ID集合
    */
    private String saleReturnOrderIds;
    /**
    * 销售退货单编号集合
    */
    private String saleReturnOrderCodes;
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
    * 审核人
    */
    private Long auditer;
    /**
    * 审核人用户名
    */
    private String auditUsername;
    /**
    * 审核时间
    */
    private Timestamp auditerDate;

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
    /*returnType get 方法 */
    public String getReturnType(){
    return returnType;
    }
    /*returnType set 方法 */
    public void setReturnType(String  returnType){
    this.returnType=returnType;
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
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*returnReason get 方法 */
    public String getReturnReason(){
    return returnReason;
    }
    /*returnReason set 方法 */
    public void setReturnReason(String  returnReason){
    this.returnReason=returnReason;
    }
    /*tallyingUnit get 方法 */
    public String getTallyingUnit(){
    return tallyingUnit;
    }
    /*tallyingUnit set 方法 */
    public void setTallyingUnit(String  tallyingUnit){
    this.tallyingUnit=tallyingUnit;
    }
    /*lbxNo get 方法 */
    public String getLbxNo(){
    return lbxNo;
    }
    /*lbxNo set 方法 */
    public void setLbxNo(String  lbxNo){
    this.lbxNo=lbxNo;
    }
    /*tradeTerms get 方法 */
    public String getTradeTerms(){
    return tradeTerms;
    }
    /*tradeTerms set 方法 */
    public void setTradeTerms(String  tradeTerms){
    this.tradeTerms=tradeTerms;
    }
    /*loadPort get 方法 */
    public String getLoadPort(){
    return loadPort;
    }
    /*loadPort set 方法 */
    public void setLoadPort(String  loadPort){
    this.loadPort=loadPort;
    }
    /*portDestNo get 方法 */
    public String getPortDestNo(){
    return portDestNo;
    }
    /*portDestNo set 方法 */
    public void setPortDestNo(String  portDestNo){
    this.portDestNo=portDestNo;
    }
    /*deliveryAddr get 方法 */
    public String getDeliveryAddr(){
    return deliveryAddr;
    }
    /*deliveryAddr set 方法 */
    public void setDeliveryAddr(String  deliveryAddr){
    this.deliveryAddr=deliveryAddr;
    }
    /*shipper get 方法 */
    public String getShipper(){
    return shipper;
    }
    /*shipper set 方法 */
    public void setShipper(String  shipper){
    this.shipper=shipper;
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
    /*billWeight get 方法 */
    public Double getBillWeight(){
    return billWeight;
    }
    /*billWeight set 方法 */
    public void setBillWeight(Double  billWeight){
    this.billWeight=billWeight;
    }
    /*boxNum get 方法 */
    public Integer getBoxNum(){
    return boxNum;
    }
    /*boxNum set 方法 */
    public void setBoxNum(Integer  boxNum){
    this.boxNum=boxNum;
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
    /*torrPackType get 方法 */
    public String getTorrPackType(){
    return torrPackType;
    }
    /*torrPackType set 方法 */
    public void setTorrPackType(String  torrPackType){
    this.torrPackType=torrPackType;
    }
    /*transport get 方法 */
    public String getTransport(){
    return transport;
    }
    /*transport set 方法 */
    public void setTransport(String  transport){
    this.transport=transport;
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
    /*ladingBill get 方法 */
    public String getLadingBill(){
    return ladingBill;
    }
    /*ladingBill set 方法 */
    public void setLadingBill(String  ladingBill){
    this.ladingBill=ladingBill;
    }
    /*mark get 方法 */
    public String getMark(){
    return mark;
    }
    /*mark set 方法 */
    public void setMark(String  mark){
    this.mark=mark;
    }
    /*remark get 方法 */
    public String getRemark(){
    return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
    this.remark=remark;
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
    /*saleReturnOrderIds get 方法 */
    public String getSaleReturnOrderIds(){
    return saleReturnOrderIds;
    }
    /*saleReturnOrderIds set 方法 */
    public void setSaleReturnOrderIds(String  saleReturnOrderIds){
    this.saleReturnOrderIds=saleReturnOrderIds;
    }
    /*saleReturnOrderCodes get 方法 */
    public String getSaleReturnOrderCodes(){
    return saleReturnOrderCodes;
    }
    /*saleReturnOrderCodes set 方法 */
    public void setSaleReturnOrderCodes(String  saleReturnOrderCodes){
    this.saleReturnOrderCodes=saleReturnOrderCodes;
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
    /*auditer get 方法 */
    public Long getAuditer(){
    return auditer;
    }
    /*auditer set 方法 */
    public void setAuditer(Long  auditer){
    this.auditer=auditer;
    }
    /*auditUsername get 方法 */
    public String getAuditUsername(){
    return auditUsername;
    }
    /*auditUsername set 方法 */
    public void setAuditUsername(String  auditUsername){
    this.auditUsername=auditUsername;
    }
    /*auditerDate get 方法 */
    public Timestamp getAuditerDate(){
    return auditerDate;
    }
    /*auditerDate set 方法 */
    public void setAuditerDate(Timestamp  auditerDate){
    this.auditerDate=auditerDate;
    }






}
