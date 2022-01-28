package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;
import com.topideal.entity.vo.sale.SaleReturnDeclareOrderItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class SaleReturnDeclareOrderForm  extends PageForm {

    @ApiModelProperty(value = "令牌",required = true)
    private String token;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "销售退预申报单编号")
    private String code;

    @ApiModelProperty(value = "客户ID")
    private Long customerId;

    @ApiModelProperty(value = "商家ID")
    private Long merchantId;

    @ApiModelProperty(value = "事业部id")
    private Long buId;

    @ApiModelProperty(value = "退货类型 1-购销退货")
    private String returnType;

    @ApiModelProperty(value = "退出库仓ID")
    private Long outDepotId;

    @ApiModelProperty(value = "退入仓仓库id")
    private Long inDepotId;

    @ApiModelProperty(value = "币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String currency;

    @ApiModelProperty(value = "po单号")
    private String poNo;

    @ApiModelProperty(value = "退货原因")
    private String returnReason;

    @ApiModelProperty(value = "理货单位 00-托盘 01-箱 02-件")
    private String tallyingUnit;

    @ApiModelProperty(value = "LBX单号")
    private String lbxNo;

    @ApiModelProperty(value = "贸易条款 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW")
    private String tradeTerms;

    @ApiModelProperty(value = "装货港")
    private String loadPort;

    @ApiModelProperty(value = "卸货港编码")
    private String portDestNo;

    @ApiModelProperty(value = "收货地点")
    private String deliveryAddr;

    @ApiModelProperty(value = "境外发货人")
    private String shipper;

    @ApiModelProperty(value = "发票单号")
    private String invoiceNo;

    @ApiModelProperty(value = "报关合同号")
    private String contractNo;

    @ApiModelProperty(value = "提单毛重")
    private Double billWeight;

    @ApiModelProperty(value = "箱数")
    private Integer boxNum;

    @ApiModelProperty(value = "托数")
    private Integer torrNum;

    @ApiModelProperty(value = "包装")
    private String pack;

    @ApiModelProperty(value = "托盘材质 01-塑料托盘 02-木质托盘 03-IPPC木托 04-纸箱")
    private String torrPackType;

    @ApiModelProperty(value = "运输方式 1-空运 2-海运 3-陆运 4-港到仓拖车 5-中欧铁路 6-其他")
    private String transport;

    @ApiModelProperty(value = "出库关区id")
    private Long outCustomsId;

    @ApiModelProperty(value = "入库关区id")
    private Long inCustomsId;

    @ApiModelProperty(value = "头程提单号")
    private String ladingBill;

    @ApiModelProperty(value = "唛头")
    private String mark;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "订单状态 001-待审核、002-已审核、003-待入仓、004-已入仓、005-已完结、006-已删除")
    private String status;

    @ApiModelProperty(value = "销售退货单ID集合")
    private String saleReturnOrderIds;

    @ApiModelProperty(value = "销售退货单编号集合")
    private String saleReturnOrderCodes;

    @ApiModelProperty(value = "商品明细")
    private List<SaleReturnDeclareOrderItemModel> itemList ;

    @ApiModelProperty(value = "销售退预申报id集合")
    private String ids;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public Long getOutDepotId() {
        return outDepotId;
    }

    public void setOutDepotId(Long outDepotId) {
        this.outDepotId = outDepotId;
    }

    public Long getInDepotId() {
        return inDepotId;
    }

    public void setInDepotId(Long inDepotId) {
        this.inDepotId = inDepotId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getTallyingUnit() {
        return tallyingUnit;
    }

    public void setTallyingUnit(String tallyingUnit) {
        this.tallyingUnit = tallyingUnit;
    }

    public String getLbxNo() {
        return lbxNo;
    }

    public void setLbxNo(String lbxNo) {
        this.lbxNo = lbxNo;
    }

    public String getTradeTerms() {
        return tradeTerms;
    }

    public void setTradeTerms(String tradeTerms) {
        this.tradeTerms = tradeTerms;
    }

    public String getLoadPort() {
        return loadPort;
    }

    public void setLoadPort(String loadPort) {
        this.loadPort = loadPort;
    }

    public String getPortDestNo() {
        return portDestNo;
    }

    public void setPortDestNo(String portDestNo) {
        this.portDestNo = portDestNo;
    }

    public String getDeliveryAddr() {
        return deliveryAddr;
    }

    public void setDeliveryAddr(String deliveryAddr) {
        this.deliveryAddr = deliveryAddr;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public Double getBillWeight() {
        return billWeight;
    }

    public void setBillWeight(Double billWeight) {
        this.billWeight = billWeight;
    }

    public Integer getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(Integer boxNum) {
        this.boxNum = boxNum;
    }

    public Integer getTorrNum() {
        return torrNum;
    }

    public void setTorrNum(Integer torrNum) {
        this.torrNum = torrNum;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getTorrPackType() {
        return torrPackType;
    }

    public void setTorrPackType(String torrPackType) {
        this.torrPackType = torrPackType;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public Long getOutCustomsId() {
        return outCustomsId;
    }

    public void setOutCustomsId(Long outCustomsId) {
        this.outCustomsId = outCustomsId;
    }

    public Long getInCustomsId() {
        return inCustomsId;
    }

    public void setInCustomsId(Long inCustomsId) {
        this.inCustomsId = inCustomsId;
    }

    public String getLadingBill() {
        return ladingBill;
    }

    public void setLadingBill(String ladingBill) {
        this.ladingBill = ladingBill;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSaleReturnOrderIds() {
        return saleReturnOrderIds;
    }

    public void setSaleReturnOrderIds(String saleReturnOrderIds) {
        this.saleReturnOrderIds = saleReturnOrderIds;
    }

    public String getSaleReturnOrderCodes() {
        return saleReturnOrderCodes;
    }

    public void setSaleReturnOrderCodes(String saleReturnOrderCodes) {
        this.saleReturnOrderCodes = saleReturnOrderCodes;
    }

    public List<SaleReturnDeclareOrderItemModel> getItemList() {
        return itemList;
    }

    public void setItemList(List<SaleReturnDeclareOrderItemModel> itemList) {
        this.itemList = itemList;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
