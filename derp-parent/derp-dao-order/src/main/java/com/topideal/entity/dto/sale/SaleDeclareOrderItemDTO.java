package com.topideal.entity.dto.sale;
import java.io.Serializable;
import java.math.BigDecimal;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SaleDeclareOrderItemDTO extends PageModel implements Serializable{
	@ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "销售预申报单ID")
    private Long declareOrderId;

	@ApiModelProperty(value = "销售订单ID")
    private Long saleOrderId;

	@ApiModelProperty(value = "销售订单编号")
    private String saleOrderCode;

	@ApiModelProperty(value = "po单号")
    private String poNo;

	@ApiModelProperty(value = "商品id")
    private Long goodsId;

	@ApiModelProperty(value = "商品编码")
    private String goodsCode;

	@ApiModelProperty(value = "商品名称")
    private String goodsName;

	@ApiModelProperty(value = "商品货号")
    private String goodsNo;
	
	@ApiModelProperty(value = "条形码")
    private String barcode;
	
	@ApiModelProperty(value = "标准条码")
    private String commbarcode;
    
	@ApiModelProperty(value = "申报数量")
    private Integer num;

	@ApiModelProperty(value = "销售单价")
    private BigDecimal salePrice;

	@ApiModelProperty(value = "申报单价")
    private BigDecimal price;

	@ApiModelProperty(value = "申报总金额")
    private BigDecimal amount;

	@ApiModelProperty(value = "品牌类型")
    private String brandName;

	@ApiModelProperty(value = "毛重")
    private Double grossWeight;

	@ApiModelProperty(value = "净重")
    private Double netWeight;

	@ApiModelProperty(value = "总毛重")
    private Double grossWeightSum;

	@ApiModelProperty(value = "总净重")
    private Double netWeightSum;

	@ApiModelProperty(value = "箱数")
    private Integer boxNum;

	@ApiModelProperty(value = "箱号")
    private String boxNo;

	@ApiModelProperty(value = "托盘号")
    private String torrNo;

	@ApiModelProperty(value = "批次号")
    private String batchNo;

	@ApiModelProperty(value = "成分占比")
    private String constituentRatio;

	@ApiModelProperty(value = "序号")
    private Integer seq;
	
	//扩展字段 用于出库
	@ApiModelProperty(value = "好品量")
    private Integer transferNum;
	
	@ApiModelProperty(value = "坏品量")
    private Integer wornNum;
	
	@ApiModelProperty(value = "生产日期")
    private String productionDate;
	
	@ApiModelProperty(value = "失效日期")
    private String overdueDate;
	
	//用于选择商品弹窗
	@ApiModelProperty(value = "已选择商品id")
    private String unNeedIds;
	@ApiModelProperty(value = "销售订单ID集合")
    private String saleOrderIds;
	@ApiModelProperty(value = "公司id")
    private Long merchantId;
	@ApiModelProperty(value = "公司名称")
    private String merchantName;
	@ApiModelProperty(value = "单位")
    private String unitName;
	@ApiModelProperty(value = "工厂编码")
	private String factoryNo;
	@ApiModelProperty(value = "平台关区")
	private String customsArea;

	@ApiModelProperty(value = "预申报编号")
	private String saleDeclareCode;

	@ApiModelProperty(value = "销售明细id")
	private Long saleItemId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDeclareOrderId() {
		return declareOrderId;
	}

	public void setDeclareOrderId(Long declareOrderId) {
		this.declareOrderId = declareOrderId;
	}

	public Long getSaleOrderId() {
		return saleOrderId;
	}

	public void setSaleOrderId(Long saleOrderId) {
		this.saleOrderId = saleOrderId;
	}

	public String getSaleOrderCode() {
		return saleOrderCode;
	}

	public void setSaleOrderCode(String saleOrderCode) {
		this.saleOrderCode = saleOrderCode;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
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

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public Double getGrossWeightSum() {
		return grossWeightSum;
	}

	public void setGrossWeightSum(Double grossWeightSum) {
		this.grossWeightSum = grossWeightSum;
	}

	public Double getNetWeightSum() {
		return netWeightSum;
	}

	public void setNetWeightSum(Double netWeightSum) {
		this.netWeightSum = netWeightSum;
	}

	public Integer getBoxNum() {
		return boxNum;
	}

	public void setBoxNum(Integer boxNum) {
		this.boxNum = boxNum;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public String getTorrNo() {
		return torrNo;
	}

	public void setTorrNo(String torrNo) {
		this.torrNo = torrNo;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getConstituentRatio() {
		return constituentRatio;
	}

	public void setConstituentRatio(String constituentRatio) {
		this.constituentRatio = constituentRatio;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	
	public Integer getTransferNum() {
		return transferNum;
	}

	public void setTransferNum(Integer transferNum) {
		this.transferNum = transferNum;
	}

	public Integer getWornNum() {
		return wornNum;
	}

	public void setWornNum(Integer wornNum) {
		this.wornNum = wornNum;
	}

	public String getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}

	public String getOverdueDate() {
		return overdueDate;
	}

	public void setOverdueDate(String overdueDate) {
		this.overdueDate = overdueDate;
	}

	public String getUnNeedIds() {
		return unNeedIds;
	}

	public void setUnNeedIds(String unNeedIds) {
		this.unNeedIds = unNeedIds;
	}

	public String getSaleOrderIds() {
		return saleOrderIds;
	}

	public void setSaleOrderIds(String saleOrderIds) {
		this.saleOrderIds = saleOrderIds;
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

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getFactoryNo() {
		return factoryNo;
	}

	public void setFactoryNo(String factoryNo) {
		this.factoryNo = factoryNo;
	}

	public String getCustomsArea() {
		return customsArea;
	}

	public void setCustomsArea(String customsArea) {
		this.customsArea = customsArea;
	}

	public String getSaleDeclareCode() {
		return saleDeclareCode;
	}

	public void setSaleDeclareCode(String saleDeclareCode) {
		this.saleDeclareCode = saleDeclareCode;
	}

	public Long getSaleItemId() {
		return saleItemId;
	}

	public void setSaleItemId(Long saleItemId) {
		this.saleItemId = saleItemId;
	}
}
