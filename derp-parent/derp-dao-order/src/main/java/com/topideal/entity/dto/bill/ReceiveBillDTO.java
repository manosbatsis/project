package com.topideal.entity.dto.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 应收账单dto
 * @Author: Chen Yiluan
 * @Date: 2020/07/27 15:03
 **/
@ApiModel
public class ReceiveBillDTO extends PageModel implements Serializable {

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "应收账单号")
	private String code;

	@ApiModelProperty(value = "关联业务单号")
	private String relCode;

	@ApiModelProperty(value = "公司id")
	private Long merchantId;

	@ApiModelProperty(value = "公司名称")
	private String merchantName;

	@ApiModelProperty(value = "客户id")
	private Long customerId;

	@ApiModelProperty(value = "客户名称")
	private String customerName;

	@ApiModelProperty(value = "事业部id")
	private Long buId;

	@ApiModelProperty(value = "事业部名称")
	private String buName;

	@ApiModelProperty(value = "发票号码")
	private String invoiceNo;

	@ApiModelProperty(value = "结算币种")
	private String currency;
	@ApiModelProperty(value = "结算币种中文")
	private String currencyLabel;

	@ApiModelProperty(value = "账单日期")
	private Timestamp billDate;
	@ApiModelProperty(value = "账单月份")
	private String billMonth;

	@ApiModelProperty(value = "账单状态 00-待提交 01-待审核 02-待核销 03-部分核销 04-已核销")
	private String billStatus;
	@ApiModelProperty(value = "账单状态中文")
	private String billStatusLabel;
	@ApiModelProperty(value = "账单状态，多个以英文逗号隔开")
	private String billStates;

	@ApiModelProperty(value = "开票状态 00-待开票  01-待签章  02-已作废 03-已签章")
	private String invoiceStatus;
	@ApiModelProperty(value = "开票状态中文")
	private String invoiceStatusLabel;

	@ApiModelProperty(value = "创建人ID")
	private Long createrId;

	@ApiModelProperty(value = "创建人")
	private String creater;

	@ApiModelProperty(value = "创建时间")
	private Timestamp createDate;

	@ApiModelProperty(value = "修改时间")
	private Timestamp modifyDate;

	@ApiModelProperty(value = "开票时间")
	private Timestamp invoiceDate;

	@ApiModelProperty(value = "应收金额")
	private BigDecimal receivablePrice;

	@ApiModelProperty(value = "已收金额")
	private BigDecimal receivedPrice;

	@ApiModelProperty(value = "未收金额")
	private BigDecimal uncollectedPrice;

	@ApiModelProperty(value = "当前登录用户绑定的事业部集合")
	private List<Long> buList;

	@ApiModelProperty(value = "po号")
	private String poNo;

	@ApiModelProperty(value = "单据类型 1-上架单 2-账单出库单 3-预售单 4-销售订单 5-采购SD单 6-融资申请单")
	private String orderType;
	@ApiModelProperty(value = "单据类型中文")
	private String orderTypeLabel;

	@ApiModelProperty(value = "账期")
	private String accountPeriod;

	@ApiModelProperty(value = "补扣款明细")
	List<Map<String, Object>> costItemList = new ArrayList<>();

	@ApiModelProperty(value = "发票id")
	private Long invoiceId;

	@ApiModelProperty(value = "发票路径")
	private String invoicePath;

	@ApiModelProperty(value = "po号集合")
	private List<String> poNos;

	@ApiModelProperty(value = "关联业务单号集合")
	private List<String> relCodes;

	@ApiModelProperty(value = "数量")
	private BigDecimal num;

	@ApiModelProperty(value = "销售金额")
	private BigDecimal amount;

	@ApiModelProperty(value = "业务单集合")
	private List<Map<String, Object>> orderList = new ArrayList<>();

	@ApiModelProperty(value = "NC渠道编码")
	private String ncChannelCode;
	@ApiModelProperty(value = "NC渠道名称")
	private String ncChannelName;

	@ApiModelProperty(value = "结算类型 1-应收,2-预收")
	private String settlementType;
	@ApiModelProperty(value = "结算类型中文")
	private String settlementTypeLabel;

	@ApiModelProperty(value = "销售模式 1-代销 2-购销")
	private String saleModel;
	@ApiModelProperty(value = "销售模式中文")
	private String saleModelLabel;

	@ApiModelProperty(value = "NC平台编码")
	private String ncPlatformCode;
	@ApiModelProperty(value = "NC平台名称")
	private String ncPlatformName;

	@ApiModelProperty(value = "NC状态1-待审核 2-待入erp 3-待入账 4-已入账 5-已关账 6-未同步 7-已同步")
	private String ncStatus;
	@ApiModelProperty(value = "NC状态中文")
	private String ncStatusLabel;

	@ApiModelProperty(value = "NC同步时间")
	private Timestamp ncSynDate;

	@ApiModelProperty(value = "NC单据号")
	private String ncCode;

	@ApiModelProperty(value = "凭证编号")
	private String voucherCode;

	@ApiModelProperty(value = "凭证名称")
	private String voucherName;

	@ApiModelProperty(value = "凭证序号")
	private String voucherIndex;

	@ApiModelProperty(value = "凭证状态：1-正常，0-作废")
	private String voucherStatus;
	@ApiModelProperty(value = "凭证状态中文")
	private String voucherStatusLabel;

	@ApiModelProperty(value = "会计期间")
	private String period;

	@ApiModelProperty(value = "同步操作人id")
	private Long synchronizerId;

	@ApiModelProperty(value = "同步操作人")
	private String synchronizer;

	@ApiModelProperty(value = "是否增加残损金额 0-是 1-否")
	private String isAddWorn;

	@ApiModelProperty(value = "关联销售订单")
	private String saleOrderCode;

	@ApiModelProperty(value = "分类：1-商销收入 2-GTN核销 3-采购rebate\n")
	private String sortType;
	@ApiModelProperty(value = "分类中文")
    private String sortTypeLabel;

    @ApiModelProperty(value = "补扣款明细")
    private List<ReceiveBillCostItemDTO> receiveBillCostItemDTOS;

    @ApiModelProperty(value = "发票月份")
    private String invoiceMonth;

    private String ids;

    @ApiModelProperty(value = "账单状态集合", hidden = true)
    private List<String> billStatusList;
    /**
     * 入账时间
     */
    @ApiModelProperty(value = "入账时间")
    private Timestamp creditDate;
    
    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    /* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* code get 方法 */
	public String getCode() {
		return code;
	}

	/* code set 方法 */
	public void setCode(String code) {
		this.code = code;
	}

	/* relCode get 方法 */
	public String getRelCode() {
		return relCode;
	}

	/* relCode set 方法 */
	public void setRelCode(String relCode) {
		this.relCode = relCode;
	}

	/* merchantId get 方法 */
	public Long getMerchantId() {
		return merchantId;
	}

	/* merchantId set 方法 */
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	/* merchantName get 方法 */
	public String getMerchantName() {
		return merchantName;
	}

	/* merchantName set 方法 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/* customerId get 方法 */
	public Long getCustomerId() {
		return customerId;
	}

	/* customerId set 方法 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	/* customerName get 方法 */
	public String getCustomerName() {
		return customerName;
	}

	/* customerName set 方法 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/* buId get 方法 */
	public Long getBuId() {
		return buId;
	}

	/* buId set 方法 */
	public void setBuId(Long buId) {
		this.buId = buId;
	}

	/* buName get 方法 */
	public String getBuName() {
		return buName;
	}

	/* buName set 方法 */
	public void setBuName(String buName) {
		this.buName = buName;
	}

	/* invoiceNo get 方法 */
	public String getInvoiceNo() {
		return invoiceNo;
	}

	/* invoiceNo set 方法 */
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	/* currency get 方法 */
	public String getCurrency() {
		return currency;
	}

	/* currency set 方法 */
	public void setCurrency(String currency) {
		this.currency = currency;
		this.currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency);
	}

	public Timestamp getBillDate() {
		return billDate;
	}

	public void setBillDate(Timestamp billDate) {
		this.billDate = billDate;
	}

	/* billStatus get 方法 */
	public String getBillStatus() {
		return billStatus;
	}

	/* billStatus set 方法 */
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
		this.billStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_billStatusList, billStatus);
	}

	/* invoiceStatus get 方法 */
	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	/* invoiceStatus set 方法 */
	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
		this.invoiceStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_invoiceStatusList, invoiceStatus);
	}

	/* createrId get 方法 */
	public Long getCreaterId() {
		return createrId;
	}

	/* createrId set 方法 */
	public void setCreaterId(Long createrId) {
		this.createrId = createrId;
	}

	/* creater get 方法 */
	public String getCreater() {
		return creater;
	}

	/* creater set 方法 */
	public void setCreater(String creater) {
		this.creater = creater;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/* modifyDate get 方法 */
	public Timestamp getModifyDate() {
		return modifyDate;
	}

	/* modifyDate set 方法 */
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getBillStatusLabel() {
		return billStatusLabel;
	}

	public void setBillStatusLabel(String billStatusLabel) {
		this.billStatusLabel = billStatusLabel;
	}

	public String getInvoiceStatusLabel() {
		return invoiceStatusLabel;
	}

	public void setInvoiceStatusLabel(String invoiceStatusLabel) {
		this.invoiceStatusLabel = invoiceStatusLabel;
	}

	public BigDecimal getReceivablePrice() {
		return receivablePrice;
	}

	public void setReceivablePrice(BigDecimal receivablePrice) {
		this.receivablePrice = receivablePrice;
	}

	public BigDecimal getReceivedPrice() {
		return receivedPrice;
	}

	public void setReceivedPrice(BigDecimal receivedPrice) {
		this.receivedPrice = receivedPrice;
	}

	public BigDecimal getUncollectedPrice() {
		return uncollectedPrice;
	}

	public void setUncollectedPrice(BigDecimal uncollectedPrice) {
		this.uncollectedPrice = uncollectedPrice;
	}

	public List<Long> getBuList() {
		return buList;
	}

	public void setBuList(List<Long> buList) {
		this.buList = buList;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
		this.orderTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_orderTypeList, orderType);
	}

	public String getOrderTypeLabel() {
		return orderTypeLabel;
	}

	public void setOrderTypeLabel(String orderTypeLabel) {
		this.orderTypeLabel = orderTypeLabel;
	}

	public String getCurrencyLabel() {
		return currencyLabel;
	}

	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
	}

	public List<Map<String, Object>> getCostItemList() {
		return costItemList;
	}

	public void setCostItemList(List<Map<String, Object>> costItemList) {
		this.costItemList = costItemList;
	}

	public String getBillMonth() {
		return billMonth;
	}

	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}

	public String getAccountPeriod() {
		return accountPeriod;
	}

	public void setAccountPeriod(String accountPeriod) {
		this.accountPeriod = accountPeriod;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getInvoicePath() {
		return invoicePath;
	}

	public void setInvoicePath(String invoicePath) {
		this.invoicePath = invoicePath;
	}

	public Timestamp getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Timestamp invoiceDate) {
		this.invoiceDate = invoiceDate;
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

	public BigDecimal getNum() {
		return num;
	}

	public void setNum(BigDecimal num) {
		this.num = num;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public List<Map<String, Object>> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Map<String, Object>> orderList) {
		this.orderList = orderList;
	}

	public String getNcChannelCode() {
		return ncChannelCode;
	}

	public void setNcChannelCode(String ncChannelCode) {
		this.ncChannelCode = ncChannelCode;
		this.ncChannelName = DERP_ORDER.getLabelByKey(DERP_ORDER.channel_codeList, ncChannelCode);
	}

	public String getSettlementType() {
		return settlementType;
	}

	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
		this.settlementTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.settlement_typeList, settlementType);
	}

	public String getSaleModel() {
		return saleModel;
	}

	public void setSaleModel(String saleModel) {
		this.saleModel = saleModel;
		this.saleModelLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_saleModeList, saleModel);
	}

	public String getNcChannelName() {
		return ncChannelName;
	}

	public void setNcChannelName(String ncChannelName) {
		this.ncChannelName = ncChannelName;
	}

	public String getSettlementTypeLabel() {
		return settlementTypeLabel;
	}

	public void setSettlementTypeLabel(String settlementTypeLabel) {
		this.settlementTypeLabel = settlementTypeLabel;
	}

	public String getSaleModelLabel() {
		return saleModelLabel;
	}

	public void setSaleModelLabel(String saleModelLabel) {
		this.saleModelLabel = saleModelLabel;
	}

	public String getNcPlatformCode() {
		return ncPlatformCode;
	}

	public void setNcPlatformCode(String ncPlatformCode) {
		this.ncPlatformCode = ncPlatformCode;
		this.ncPlatformName = DERP_ORDER.getLabelByKey(DERP_ORDER.platform_codeList, ncPlatformCode);
	}

	public String getNcPlatformName() {
		return ncPlatformName;
	}

	public void setNcPlatformName(String ncPlatformName) {
		this.ncPlatformName = ncPlatformName;
	}

	public String getNcStatus() {
		return ncStatus;
	}

	public void setNcStatus(String ncStatus) {
		this.ncStatus = ncStatus;
		this.ncStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_nvSynList, ncStatus) ;
	}

	public String getNcStatusLabel() {
		return ncStatusLabel;
	}

	public void setNcStatusLabel(String ncStatusLabel) {
		this.ncStatusLabel = ncStatusLabel;
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

	public String getVoucherStatusLabel() {
		return voucherStatusLabel;
	}

	public void setVoucherStatusLabel(String voucherStatusLabel) {
		this.voucherStatusLabel = voucherStatusLabel;
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

	public String getIsAddWorn() {
		return isAddWorn;
	}

	public void setIsAddWorn(String isAddWorn) {
		this.isAddWorn = isAddWorn;
	}

	public String getSaleOrderCode() {
		return saleOrderCode;
	}

	public void setSaleOrderCode(String saleOrderCode) {
		this.saleOrderCode = saleOrderCode;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
		this.sortTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_sortTypeList, sortType) ;
	}

	public String getSortTypeLabel() {
		return sortTypeLabel;
	}

	public void setSortTypeLabel(String sortTypeLabel) {
		this.sortTypeLabel = sortTypeLabel;
	}

	public List<ReceiveBillCostItemDTO> getReceiveBillCostItemDTOS() {
		return receiveBillCostItemDTOS;
	}

	public void setReceiveBillCostItemDTOS(List<ReceiveBillCostItemDTO> receiveBillCostItemDTOS) {
        this.receiveBillCostItemDTOS = receiveBillCostItemDTOS;
    }

    public String getInvoiceMonth() {
        return invoiceMonth;
    }

    public void setInvoiceMonth(String invoiceMonth) {
        this.invoiceMonth = invoiceMonth;
    }

    public List<String> getBillStatusList() {
        return billStatusList;
    }

    public void setBillStatusList(List<String> billStatusList) {
        this.billStatusList = billStatusList;
    }

	public Timestamp getCreditDate() {
		return creditDate;
	}

	public void setCreditDate(Timestamp creditDate) {
		this.creditDate = creditDate;
	}

	public String getBillStates() {
		return billStates;
	}

	public void setBillStates(String billStates) {
		this.billStates = billStates;
	}
}
