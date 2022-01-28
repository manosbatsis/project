package com.topideal.entity.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

public class BuFinanceWarehouseDetailsDTO extends PageModel implements Serializable{


	@ApiModelProperty(value = "id")
    private Long id;
	@ApiModelProperty(value = "商家id")
    private Long merchantId;
	@ApiModelProperty(value = "商家名称")
    private String merchantName;
	@ApiModelProperty(value = "入库仓库id")
    private Long inDepotId;
	@ApiModelProperty(value = "入库仓库名称")
    private String inDepotName;
	@ApiModelProperty(value = "事业部ID")
    private Long buId;
	@ApiModelProperty(value = "事业部名称")
    private String buName;
	@ApiModelProperty(value = "订单id")
    private Long orderId;
	@ApiModelProperty(value = "订单编码")
    private String orderCode;
	@ApiModelProperty(value = "订单日期 (订单创建日期)")
    private Timestamp orderCreateDate;
	@ApiModelProperty(value = "入库单id")
    private Long warehouseId;
	@ApiModelProperty(value = "入库单编码")
    private String warehouseCode;
	@ApiModelProperty(value = "供应商id")
    private Long supplierId;
	@ApiModelProperty(value = "供应商名称")
    private String supplierName;
	@ApiModelProperty(value = "入库时间 (入库单创建时间)")
    private Timestamp warehouseCreateDate;
	@ApiModelProperty(value = "po单号")
    private String poNo;
	@ApiModelProperty(value = "开发票时间(发票上面的时间)")
    private Timestamp drawInvoiceDate;
	@ApiModelProperty(value = "发票号")
    private String invoiceNo;
	@ApiModelProperty(value = "商品id")
    private Long goodsId;
	@ApiModelProperty(value = "商品名称")
    private String goodsName;
	@ApiModelProperty(value = "商品货号")
    private String goodsNo;
	@ApiModelProperty(value = "商品条码")
    private String barcode;
	@ApiModelProperty(value = "标准条码")
    private String commbarcode;
	@ApiModelProperty(value = "理货单位(00-托盘，01-箱，02-件)")
    private String tallyingUnit;
	@ApiModelProperty(value = "采购币种 (通用)币种代码 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String orderCurrency;
	@ApiModelProperty(value = "采购金额")
    private BigDecimal orderAmount;
	@ApiModelProperty(value = " 数量")
    private Integer warehouseNum;
	@ApiModelProperty(value = "入库币种 (通用)币种代码 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String warehouseCurrency;
	@ApiModelProperty(value = "入库单价(结算价格单价)")
    private BigDecimal warehousePrice;
	@ApiModelProperty(value = "入库金额")
    private BigDecimal warehouseAmount;
	@ApiModelProperty(value = "归属月份 YYYY-MM")
    private String month;
	@ApiModelProperty(value = "勾稽时间")
    private Timestamp relDate;
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
	@ApiModelProperty(value = "采购单价")
    private BigDecimal orderPrice;
	@ApiModelProperty(value = "创建人id")
    private Long creater;
	@ApiModelProperty(value = "创建人名称")
    private String createName;
	@ApiModelProperty(value = "订单类型 1-采购退货入库 2-采购退货出库")
    private String orderType;
	@ApiModelProperty(value = "品牌id")
    private Long brandId;
	@ApiModelProperty(value = "品牌名称")
    private String brandName;
	@ApiModelProperty(value = "上级母品牌")
    private String superiorParentBrand;
	@ApiModelProperty(value = "sd 本币金额")
    private BigDecimal sdTgtAmount;
	@ApiModelProperty(value = "订单币种")
	private String orderCurrencyLabel;
	@ApiModelProperty(value = "单位")
	private String tallyingUnitLabel;
	@ApiModelProperty(value = "入库币种")
	private String warehouseCurrencyLabel;
	@ApiModelProperty(value = "币种")
	private String orderTypeLabel;
	@ApiModelProperty(value = "开始月份")
	private String monthStart;
	@ApiModelProperty(value = "结束月份")
	private String monthEnd;
	
	/*查询条件非数据表字段start*/
    private List<Long> parentBrandIds;//标准品牌id
	@ApiModelProperty(value = "事业部集合")
	private List<Long> buList;
	/**
	 * 库位类型id
	 */
	@ApiModelProperty(value = "库位类型id")
	private Long stockLocationTypeId;
	/**
	 * 库位类型名称
	 */
	@ApiModelProperty(value = "库位类型名称")
	private String stockLocationTypeName;
	/**
	 * 单价差异
	 */
	@ApiModelProperty(value = "单价差异")
	private BigDecimal differencePrice;
	/**
	 * 金额差异
	 */
	@ApiModelProperty(value = "金额差异")
	private BigDecimal differenceAmount;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public Timestamp getOrderCreateDate() {
		return orderCreateDate;
	}
	public void setOrderCreateDate(Timestamp orderCreateDate) {
		this.orderCreateDate = orderCreateDate;
	}
	public Long getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getWarehouseCode() {
		return warehouseCode;
	}
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public Timestamp getWarehouseCreateDate() {
		return warehouseCreateDate;
	}
	public void setWarehouseCreateDate(Timestamp warehouseCreateDate) {
		this.warehouseCreateDate = warehouseCreateDate;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public Timestamp getDrawInvoiceDate() {
		return drawInvoiceDate;
	}
	public void setDrawInvoiceDate(Timestamp drawInvoiceDate) {
		this.drawInvoiceDate = drawInvoiceDate;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getCommbarcode() {
		return commbarcode;
	}
	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}
	public String getTallyingUnit() {
		return tallyingUnit;
	}
	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
		this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
	}
	public String getOrderCurrency() {
		return orderCurrency;
	}
	public void setOrderCurrency(String orderCurrency) {
		this.orderCurrency = orderCurrency;
		this.setOrderCurrencyLabel(DERP.getLabelByKey(DERP.currencyCodeList, orderCurrency));
	}
	public BigDecimal getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}
	public Integer getWarehouseNum() {
		return warehouseNum;
	}
	public void setWarehouseNum(Integer warehouseNum) {
		this.warehouseNum = warehouseNum;
	}
	public String getWarehouseCurrency() {
		return warehouseCurrency;
	}
	public void setWarehouseCurrency(String warehouseCurrency) {
		this.warehouseCurrency = warehouseCurrency;
		this.setWarehouseCurrencyLabel(DERP.getLabelByKey(DERP.currencyCodeList, warehouseCurrency));
		
	}
	public BigDecimal getWarehousePrice() {
		return warehousePrice;
	}
	public void setWarehousePrice(BigDecimal warehousePrice) {
		this.warehousePrice = warehousePrice;
	}
	public BigDecimal getWarehouseAmount() {
		return warehouseAmount;
	}
	public void setWarehouseAmount(BigDecimal warehouseAmount) {
		this.warehouseAmount = warehouseAmount;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public Timestamp getRelDate() {
		return relDate;
	}
	public void setRelDate(Timestamp relDate) {
		this.relDate = relDate;
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
	public BigDecimal getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
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
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
		this.setOrderTypeLabel(DERP.getLabelByKey(DERP_REPORT.financeWarehouseDetails_orderTypeList, orderType));
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getSuperiorParentBrand() {
		return superiorParentBrand;
	}
	public void setSuperiorParentBrand(String superiorParentBrand) {
		this.superiorParentBrand = superiorParentBrand;
	}
	public BigDecimal getSdTgtAmount() {
		return sdTgtAmount;
	}
	public void setSdTgtAmount(BigDecimal sdTgtAmount) {
		this.sdTgtAmount = sdTgtAmount;
	}
	public String getOrderCurrencyLabel() {
		return orderCurrencyLabel;
	}
	public void setOrderCurrencyLabel(String orderCurrencyLabel) {
		this.orderCurrencyLabel = orderCurrencyLabel;
	}
	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}
	public void setTallyingUnitLabel(String tallyingUnitLabel) {
		this.tallyingUnitLabel = tallyingUnitLabel;
	}
	public String getWarehouseCurrencyLabel() {
		return warehouseCurrencyLabel;
	}
	public void setWarehouseCurrencyLabel(String warehouseCurrencyLabel) {
		this.warehouseCurrencyLabel = warehouseCurrencyLabel;
	}
	public String getOrderTypeLabel() {
		return orderTypeLabel;
	}
	public void setOrderTypeLabel(String orderTypeLabel) {
		this.orderTypeLabel = orderTypeLabel;
	}
	public String getMonthStart() {
		return monthStart;
	}
	public void setMonthStart(String monthStart) {
		this.monthStart = monthStart;
	}
	public String getMonthEnd() {
		return monthEnd;
	}
	public void setMonthEnd(String monthEnd) {
		this.monthEnd = monthEnd;
	}
	public List<Long> getParentBrandIds() {
		return parentBrandIds;
	}
	public void setParentBrandIds(List<Long> parentBrandIds) {
		this.parentBrandIds = parentBrandIds;
	}
	public List<Long> getBuList() {
		return buList;
	}
	public void setBuList(List<Long> buList) {
		this.buList = buList;
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

	public BigDecimal getDifferencePrice() {
		return differencePrice;
	}

	public void setDifferencePrice(BigDecimal differencePrice) {
		this.differencePrice = differencePrice;
	}

	public BigDecimal getDifferenceAmount() {
		return differenceAmount;
	}

	public void setDifferenceAmount(BigDecimal differenceAmount) {
		this.differenceAmount = differenceAmount;
	}
}
