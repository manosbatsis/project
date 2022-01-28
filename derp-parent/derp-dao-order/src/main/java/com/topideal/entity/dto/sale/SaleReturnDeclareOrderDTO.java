package com.topideal.entity.dto.sale;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.vo.sale.SaleReturnDeclareOrderItemModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SaleReturnDeclareOrderDTO extends PageModel implements Serializable{

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "销售退预申报单编号")
    private String code;

    @ApiModelProperty(value = "客户ID")
    private Long customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "商家ID")
    private Long merchantId;

    @ApiModelProperty(value = "商家名称")
    private String merchantName;

    @ApiModelProperty(value = "事业部id")
    private Long buId;

    @ApiModelProperty(value = "事业部名称")
    private String buName;

    @ApiModelProperty(value = "退货类型 1-购销退货")
    private String returnType;

    @ApiModelProperty(value = "退出库仓ID")
    private Long outDepotId;

    @ApiModelProperty(value = "退出库仓名称")
    private String outDepotName;

    @ApiModelProperty(value = "退入仓仓库id")
    private Long inDepotId;

    @ApiModelProperty(value = "退入仓仓库名称")
    private String inDepotName;

    @ApiModelProperty(value = "币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String currency;
    @ApiModelProperty(value = "币种（中文）)")
    private String currencyLabel;

    @ApiModelProperty(value = "po单号")
    private String poNo;

    @ApiModelProperty(value = "退货原因")
    private String returnReason;

    @ApiModelProperty(value = "理货单位 00-托盘 01-箱 02-件")
    private String tallyingUnit;
    @ApiModelProperty(value = "理货单位（中文）")
    private String tallyingUnitLabel;

    @ApiModelProperty(value = "LBX单号")
    private String lbxNo;

    @ApiModelProperty(value = "贸易条款 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW")
    private String tradeTerms;
    @ApiModelProperty(value = "贸易条款（中文）")
    private String tradeTermsLabel;

    @ApiModelProperty(value = "装货港")
    private String loadPort;

    @ApiModelProperty(value = "卸货港编码")
    private String portDestNo;
    @ApiModelProperty(value = "卸货港(中文)")
    private String portDestLabel;

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
    @ApiModelProperty(value = "托盘材质 01-塑料托盘 02-木质托盘 03-IPPC木托 04-纸箱")
    private String torrPackTypeLabel;

    @ApiModelProperty(value = "运输方式 1-空运 2-海运 3-陆运 4-港到仓拖车 5-中欧铁路 6-其他")
    private String transport;
    @ApiModelProperty(value = "运输方式 (中文)")
    private String transportLabel;

    @ApiModelProperty(value = "出库关区id")
    private Long outCustomsId;

    @ApiModelProperty(value = "出库关区名称")
    private String outCustomsName;

    @ApiModelProperty(value = "入库关区id")
    private Long inCustomsId;

    @ApiModelProperty(value = "入库关区名称")
    private String inCustomsName;

    @ApiModelProperty(value = "头程提单号")
    private String ladingBill;

    @ApiModelProperty(value = "唛头")
    private String mark;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "订单状态 001-待审核、002-已审核、003-待入仓、004-已入仓、005-已完结、006-已删除")
    private String status;
    @ApiModelProperty(value = "订单状态（中文）")
    private String statusLabel;

    @ApiModelProperty(value = "接口状态 0-未推接口；1-已推接口")
    private String apiStatus;
    @ApiModelProperty(value = "接口状态（中文）")
    private String apiStatusLabel;

    @ApiModelProperty(value = "销售退货单ID集合")
    private String saleReturnOrderIds;

    @ApiModelProperty(value = "销售退货单编号集合")
    private String saleReturnOrderCodes;

    @ApiModelProperty(value = "创建人")
    private Long creater;

    @ApiModelProperty(value = "创建人名称")
    private String createName;

    @ApiModelProperty(value = "创建日期")
    private Timestamp createDate;

    @ApiModelProperty(value = "修改人")
    private Long modifier;

    @ApiModelProperty(value = "修改人名称")
    private String modifierUsername;

    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    @ApiModelProperty(value = "审核人")
    private Long auditer;

    @ApiModelProperty(value = "审核人名称")
    private String auditUsername;

    @ApiModelProperty(value = "审核日期")
    private Timestamp auditerDate;

    @ApiModelProperty(value = "商品明细")
    private List<SaleReturnDeclareOrderItemModel> itemList ;

    @ApiModelProperty(value = "事业部集合")
    private List<Long> buList;

    @ApiModelProperty(value = "销售退预申报id集合")
    private String ids;

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
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

    public String getOutDepotName() {
        return outDepotName;
    }

    public void setOutDepotName(String outDepotName) {
        this.outDepotName = outDepotName;
    }

    public Long getInDepotId() {
        return inDepotId;
    }

    public void setInDepotId(Long inDepotId) {
        this.inDepotId = inDepotId;
    }

    public String getInDepotName() {
        return inDepotName;
    }

    public void setInDepotName(String inDepotName) {
        this.inDepotName = inDepotName;
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
        if(StringUtils.isNotBlank(tallyingUnit)){
            this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
        }
    }

    public String getTallyingUnitLabel() {
        return tallyingUnitLabel;
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
        if(StringUtils.isNotBlank(tradeTerms)){
            this.tradeTermsLabel = DERP.getLabelByKey(DERP_ORDER.saleReturnDeclare_tradeTermsList, tradeTerms);
        }
    }

    public String getTradeTermsLabel() {
        return tradeTermsLabel;
    }

    public void setTradeTermsLabel(String tradeTermsLabel) {
        this.tradeTermsLabel = tradeTermsLabel;
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
        if(StringUtils.isNotBlank(portDestNo)){
            this.portDestLabel = DERP.getLabelByKey(DERP.portDestList,portDestNo );
        }
    }

    public String getPortDestLabel() {
        return portDestLabel;
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
        if(StringUtils.isNotBlank(torrPackType)) {
        	this.torrPackTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.order_torrpacktypeList, torrPackType);
        }
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
        if(StringUtils.isNotBlank(transport)) {
        	this.transportLabel = DERP.getLabelByKey(DERP.transportList, transport);
        }
    }

    public Long getOutCustomsId() {
        return outCustomsId;
    }

    public void setOutCustomsId(Long outCustomsId) {
        this.outCustomsId = outCustomsId;
    }

    public String getOutCustomsName() {
        return outCustomsName;
    }

    public void setOutCustomsName(String outCustomsName) {
        this.outCustomsName = outCustomsName;
    }

    public Long getInCustomsId() {
        return inCustomsId;
    }

    public void setInCustomsId(Long inCustomsId) {
        this.inCustomsId = inCustomsId;
    }

    public String getInCustomsName() {
        return inCustomsName;
    }

    public void setInCustomsName(String inCustomsName) {
        this.inCustomsName = inCustomsName;
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
        if(StringUtils.isNotBlank(status)){
            this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleReturnDeclare_statusList, status);
        }
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public String getApiStatus() {
        return apiStatus;
    }

    public void setApiStatus(String apiStatus) {
        this.apiStatus = apiStatus;
        if(StringUtils.isNotBlank(apiStatus)){
            this.apiStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleReturnDeclare_apiStatusList, apiStatus);
        }
    }

    public String getApiStatusLabel() {
        return apiStatusLabel;
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

    public Long getCreater() {
        return creater;
    }

    public void setCreater(Long creater) {
        this.creater = creater;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public String getModifierUsername() {
        return modifierUsername;
    }

    public void setModifierUsername(String modifierUsername) {
        this.modifierUsername = modifierUsername;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Long getAuditer() {
        return auditer;
    }

    public void setAuditer(Long auditer) {
        this.auditer = auditer;
    }

    public String getAuditUsername() {
        return auditUsername;
    }

    public void setAuditUsername(String auditUsername) {
        this.auditUsername = auditUsername;
    }

    public Timestamp getAuditerDate() {
        return auditerDate;
    }

    public void setAuditerDate(Timestamp auditerDate) {
        this.auditerDate = auditerDate;
    }

    public List<SaleReturnDeclareOrderItemModel> getItemList() {
        return itemList;
    }

    public void setItemList(List<SaleReturnDeclareOrderItemModel> itemList) {
        this.itemList = itemList;
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

	public String getTorrPackTypeLabel() {
		return torrPackTypeLabel;
	}

	public String getTransportLabel() {
		return transportLabel;
	}
    
}
