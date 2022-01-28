package com.topideal.entity.dto.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 预收账单DTO
 */
@ApiModel
public class AdvanceBillDTO extends PageModel implements Serializable {

    @ApiModelProperty(value = "token", required = false)
    private String token;

    @ApiModelProperty(value = "id", required = false)
    private Long id;

    @ApiModelProperty(value = "预收账单号", required = false)
    private String code;

    @ApiModelProperty(value = "商家id", required = false)
    private Long merchantId;

    @ApiModelProperty(value = "商家名称", required = false)
    private String merchantName;

    @ApiModelProperty(value = "客户id", required = false)
    private Long customerId;

    @ApiModelProperty(value = "客户名称", required = false)
    private String customerName;

    @ApiModelProperty(value = "事业部id", required = false)
    private Long buId;

    @ApiModelProperty(value = "事业部名称", required = false)
    private String buName;

    @ApiModelProperty(value = "结算币种", required = false)
    private String currency;

    @ApiModelProperty(value = "账单日期", required = false)
    private Timestamp billDate;

    @ApiModelProperty(value = "单据类型", required = false)
    private String orderType;
    private String orderTypeLabel;

    @ApiModelProperty(value = "账单状态 00-待提交 01-待审核 03-待收款  02-待核销 04-已核销 05-作废待审 06-已作废", required = false)
    private String billStatus;
    private String billStatusLabel;

    @ApiModelProperty(value = "创建人ID", required = false)
    private Long createrId;

    @ApiModelProperty(value = "创建人", required = false)
    private String creater;

    @ApiModelProperty(value = "创建时间", required = false)
    private Timestamp createDate;

    @ApiModelProperty(value = "修改时间", required = false)
    private Timestamp modifyDate;

    @ApiModelProperty(value = "po单号", required = false)
    private String poNo;

    @ApiModelProperty(value = "业务单据号", required = false)
    private String relCode;

    @ApiModelProperty(value = "NC渠道编码", required = false)
    private String ncChannelCode;

    @ApiModelProperty(value = "NC平台编码", required = false)
    private String ncPlatformCode;

    @ApiModelProperty(value = "NC状态 1-待审核 2-待入erp 3-待入账 4-已入账 5-已关账 6-待作废 7-待红冲 8-已作废 9-已红冲 10-未同步 11-已同步", required = false)
    private String ncStatus;
    private String ncStatusLabel;

    @ApiModelProperty(value = "NC同步时间", required = false)
    private Timestamp ncSynDate;

    @ApiModelProperty(value = "NC单据号", required = false)
    private String ncCode;

    @ApiModelProperty(value = "凭证编号", required = false)
    private String voucherCode;

    @ApiModelProperty(value = "凭证名称", required = false)
    private String voucherName;

    @ApiModelProperty(value = "凭证序号", required = false)
    private String voucherIndex;

    @ApiModelProperty(value = "凭证状态：1-正常，0-作废", required = false)
    private String voucherStatus;
    private String voucherStatusLabel;

    @ApiModelProperty(value = "会计期间", required = false)
    private String period;

    @ApiModelProperty(value = "同步操作人id", required = false)
    private Long synchronizerId;

    @ApiModelProperty(value = "同步操作人", required = false)
    private String synchronizer;

    //参数字段
    @ApiModelProperty(value = "当前登录用户绑定的事业部集合", required = false)
    private List<Long> buList;

    @ApiModelProperty(value = "页面查询的PO单号", required = false)
    private List<String> poNos;

    @ApiModelProperty(value = "页面查询关联业务单号", required = false)
    private List<String> relCodes;

    @ApiModelProperty(value = "预收账单明细表体", required = false)
    private List<AdvanceBillItemDTO> itemList;

    @ApiModelProperty(value = "预收账单收款记录", required = false)
    private List<AdvanceBillVerifyItemDTO> verifyList;
    
    @ApiModelProperty(value = "预收账单核销记录", required = false)
    private List<ReceiveBillVerifyItemDTO> receiveVerifyList;
    
    @ApiModelProperty(value = "预收账单操作日志记录", required = false)
    private List<AdvanceBillOperateItemDTO> operateList;

    @ApiModelProperty(value = "预收金额", required = false)
    private BigDecimal sumAmount;

    @ApiModelProperty(value = "结算总金额大写", required = false)
    private String sumAmountStr;

    @ApiModelProperty(value = "账单月份", required = false)
    private String billMonth;

    @ApiModelProperty(value = "核销流水号", required = false)
    private String verifyNos;
    @ApiModelProperty(value = "核销人", required = false)
    private String verifyName;
    @ApiModelProperty(value = "核销日期", required = false)
    private Timestamp verifyDate;
    @ApiModelProperty(value = "收款时间", required = false)
    private Timestamp receiveDate;
    //id集合
    @ApiModelProperty(hidden = true)
    private String ids;
    @ApiModelProperty(value = "关联应收id", hidden = true)
    private Long receiveBillId;

    @ApiModelProperty(value = "已收金额", hidden = true)
    private BigDecimal advanceVerifyPrice;
    @ApiModelProperty(value = "待核销金额", hidden = true)
    private BigDecimal receiveVerifyPrice;
    /**
     * 开票状态 00-待开票  01-待签章  02-已作废 03-已签章
     */
    @ApiModelProperty(value = "开票状态 00-待开票  01-待签章  02-已作废 03-已签章")
    private String invoiceStatus;
    @ApiModelProperty(value = "开票状态 00-待开票  01-待签章  02-已作废 03-已签章")
    private String invoiceStatusLabel;
    /**
     * 发票关联id
     */
    @ApiModelProperty(value = "发票关联id")
    private Long invoiceId;
    @ApiModelProperty(value = "开票时间")
    private String invoiceDate;
    
    @ApiModelProperty(value = "发票号")
    private String invoiceNo;
    @ApiModelProperty(value = "发票文件地址")
    private String invoicePath;
    
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Timestamp getBillDate() {
        return billDate;
    }

    public void setBillDate(Timestamp billDate) {
        this.billDate = billDate;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
        this.billStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.advanceBill_billStatusList, billStatus);
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

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getNcChannelCode() {
        return ncChannelCode;
    }

    public void setNcChannelCode(String ncChannelCode) {
        this.ncChannelCode = ncChannelCode;
    }

    public String getNcPlatformCode() {
        return ncPlatformCode;
    }

    public void setNcPlatformCode(String ncPlatformCode) {
        this.ncPlatformCode = ncPlatformCode;
    }

    public String getNcStatus() {
        return ncStatus;
    }

    public void setNcStatus(String ncStatus) {
        this.ncStatus = ncStatus;
        this.ncStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_nvSynList, ncStatus) ;
    }

    public Timestamp getNcSynDate() {
        return ncSynDate;
    }

    public void setNcSynDate(Timestamp ncSynDate) {
        this.ncSynDate = ncSynDate;
    }

    public String getNcCode() {
        return ncCode;
    }

    public void setNcCode(String ncCode) {
        this.ncCode = ncCode;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getVoucherIndex() {
        return voucherIndex;
    }

    public void setVoucherIndex(String voucherIndex) {
        this.voucherIndex = voucherIndex;
    }

    public String getVoucherStatus() {
        return voucherStatus;
    }

    public void setVoucherStatus(String voucherStatus) {
        this.voucherStatus = voucherStatus;
        this.voucherStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_voucherStatusList, voucherStatus) ;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Long getSynchronizerId() {
        return synchronizerId;
    }

    public void setSynchronizerId(Long synchronizerId) {
        this.synchronizerId = synchronizerId;
    }

    public String getSynchronizer() {
        return synchronizer;
    }

    public void setSynchronizer(String synchronizer) {
        this.synchronizer = synchronizer;
    }

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public String getBillStatusLabel() {
        return billStatusLabel;
    }

    public void setBillStatusLabel(String billStatusLabel) {
        this.billStatusLabel = billStatusLabel;
    }

    public String getNcStatusLabel() {
        return ncStatusLabel;
    }

    public void setNcStatusLabel(String ncStatusLabel) {
        this.ncStatusLabel = ncStatusLabel;
    }

    public String getVoucherStatusLabel() {
        return voucherStatusLabel;
    }

    public void setVoucherStatusLabel(String voucherStatusLabel) {
        this.voucherStatusLabel = voucherStatusLabel;
    }

    public List<String> getPoNos() {
        return poNos;
    }

    public void setPoNos(List<String> poNos) {
        this.poNos = poNos;
    }

    public List<String> getRelCodes() {
        return relCodes;
    }

    public void setRelCodes(List<String> relCodes) {
        this.relCodes = relCodes;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
        this.orderTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.advanceBill_orderTypeList, orderType);
    }

    public String getOrderTypeLabel() {
        return orderTypeLabel;
    }

    public void setOrderTypeLabel(String orderTypeLabel) {
        this.orderTypeLabel = orderTypeLabel;
    }

    public String getRelCode() {
        return relCode;
    }

    public void setRelCode(String relCode) {
        this.relCode = relCode;
    }

    public List<AdvanceBillItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<AdvanceBillItemDTO> itemList) {
        this.itemList = itemList;
    }

    public BigDecimal getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(BigDecimal sumAmount) {
        this.sumAmount = sumAmount;
    }

    public List<AdvanceBillVerifyItemDTO> getVerifyList() {
        return verifyList;
    }

    public void setVerifyList(List<AdvanceBillVerifyItemDTO> verifyList) {
        this.verifyList = verifyList;
    }

    public List<AdvanceBillOperateItemDTO> getOperateList() {
        return operateList;
    }

    public void setOperateList(List<AdvanceBillOperateItemDTO> operateList) {
        this.operateList = operateList;
    }

    public String getSumAmountStr() {
        return sumAmountStr;
    }

    public void setSumAmountStr(String sumAmountStr) {
        this.sumAmountStr = sumAmountStr;
    }

    public String getVerifyNos() {
        return verifyNos;
    }

    public void setVerifyNos(String verifyNos) {
        this.verifyNos = verifyNos;
    }

    public String getVerifyName() {
        return verifyName;
    }

    public void setVerifyName(String verifyName) {
        this.verifyName = verifyName;
    }

    public Timestamp getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Timestamp verifyDate) {
        this.verifyDate = verifyDate;
    }

    public Timestamp getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Timestamp receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Long getReceiveBillId() {
        return receiveBillId;
    }

    public void setReceiveBillId(Long receiveBillId) {
        this.receiveBillId = receiveBillId;
    }

	public List<ReceiveBillVerifyItemDTO> getReceiveVerifyList() {
		return receiveVerifyList;
	}

	public void setReceiveVerifyList(List<ReceiveBillVerifyItemDTO> receiveVerifyList) {
		this.receiveVerifyList = receiveVerifyList;
	}

	public BigDecimal getAdvanceVerifyPrice() {
		return advanceVerifyPrice;
	}

	public void setAdvanceVerifyPrice(BigDecimal advanceVerifyPrice) {
		this.advanceVerifyPrice = advanceVerifyPrice;
	}

	public BigDecimal getReceiveVerifyPrice() {
		return receiveVerifyPrice;
	}

	public void setReceiveVerifyPrice(BigDecimal receiveVerifyPrice) {
		this.receiveVerifyPrice = receiveVerifyPrice;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
        this.invoiceStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.advanceBill_invoiceStatusList, invoiceStatus);

	}

	public Long getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getInvoiceStatusLabel() {
		return invoiceStatusLabel;
	}

	public void setInvoiceStatusLabel(String invoiceStatusLabel) {
		this.invoiceStatusLabel = invoiceStatusLabel;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoicePath() {
		return invoicePath;
	}

	public void setInvoicePath(String invoicePath) {
		this.invoicePath = invoicePath;
	}
	
	
}
