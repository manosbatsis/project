package com.topideal.entity.vo.order;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 采购订单表体
 *
 * @author lianchenxing
 */
public class PurchaseOrderItemModel extends PageModel implements Serializable {

	// 采购总金额
	private BigDecimal amount;
	// 子合同号p
	private String contractNo;
	// 采购订单ID
	private Long purchaseOrderId;
	// 商品id
	private Long goodsId;
	// 采购单价
	private BigDecimal price;
	// 采购数量
	private Integer num;
	// id
	private Long id;
	// 创建时间
	private Timestamp createDate;
	// 创建人
	private Long creater;
	// 箱号
	private String boxNo;
	// 批次号
	private String batchNo;
	// 成分占比
	private String constituentRatio;
	// 毛重
	private Double grossWeight;
	// 净重
	private Double netWeight;
	// 商品货号
	private String goodsNo;
	// 单位
	private String unit;
	// 商品名称
	private String goodsName;
	// 商品条形码
	private String barcode;
	// 商品编码
	private String goodsCode;
	// 工厂编码
	private String factoryNo;
	// 品牌名称
	private String brandName;
	// 创建人用户名
	private String createName;
	//采购单位(00-托盘，01-箱，02-件)
	private String purchaseUnit;
	//修改时间
	private Timestamp modifyDate;
	//总毛重
	private Double grossWeightSum;
	//总净重
	private Double netWeightSum;
	//备注
	private String remark;

	// 拓展字段
	// 订单编码
	private String orderCode;
	//po号
	private String poNo;
	//规格
	private String spec ;
	//本位币单价
	private BigDecimal tgtPrice;
	//本位币总价
	private BigDecimal tgtAmount;
	//发票金额
	private BigDecimal invoiceAmount;
	//采购转销售获取销售价格
	private JSONArray priceJOSN;
	//仓库为海外仓，箱托*价格
	private BigDecimal toUnitNum;
	/**
	 * 采购含税单价
	 */
	private BigDecimal taxPrice;
	/**
	 * 采购含税总金额
	 */
	private BigDecimal taxAmount;
	/**
	 * 税额
	 */
	private BigDecimal tax;
	/**
	 * 税率
	 */
	private Double taxRate;

	private Integer unInwarehouseNum ;

	private String type;
	/**
	 * 剩余效期区间1:0＜X≤1/10 ; 2: 1/10＜X≤1/5 ; 3: 1/5＜X≤1/3 ; 4: 1/3＜X≤1/2 ; 5: 1/2＜X≤2/3 ; 6: 2/3以上 ; 7:过期品(为负) ; 8: 残次品
	 */
	private String effectiveInterval;

	private String effectiveIntervalLable;

	public JSONArray getPriceJOSN() {
		return priceJOSN;
	}

	public void setPriceJOSN(JSONArray priceJOSN) {
		this.priceJOSN = priceJOSN;
	}

	public BigDecimal getToUnitNum() {
		return toUnitNum;
	}

	public void setToUnitNum(BigDecimal toUnitNum) {
		this.toUnitNum = toUnitNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getConstituentRatio() {
		return constituentRatio;
	}

	public void setConstituentRatio(String constituentRatio) {
		this.constituentRatio = constituentRatio;
	}

	public String getPurchaseUnit() {
		return purchaseUnit;
	}

	public void setPurchaseUnit(String purchaseUnit) {
		this.purchaseUnit = purchaseUnit;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	/* createName get 方法 */
	public String getCreateName() {
		return createName;
	}

	/* createName set 方法 */
	public void setCreateName(String createName) {
		this.createName = createName;
	}

	/* brandName get 方法 */
	public String getBrandName() {
		return brandName;
	}

	/* brandName set 方法 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/* grossWeight get 方法 */
	public Double getGrossWeight() {
		return grossWeight;
	}

	/* grossWeight set 方法 */
	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	/* netWeight get 方法 */
	public Double getNetWeight() {
		return netWeight;
	}

	/* netWeight set 方法 */
	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	/* boxNo get 方法 */
	public String getBoxNo() {
		return boxNo;
	}

	/* boxNo get 方法 */
	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	/* batchNo get 方法 */
	public String getBatchNo() {
		return batchNo;
	}

	/* batchNo set 方法 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	/* goodsCode get 方法 */
	public String getGoodsCode() {
		return goodsCode;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/* creater get 方法 */
	public Long getCreater() {
		return creater;
	}

	/* creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}

	/* goodsCode set 方法 */
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	/* goodsNo get 方法 */
	public String getGoodsNo() {
		return goodsNo;
	}

	/* goodsNo set 方法 */
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	/* unit get 方法 */
	public String getUnit() {
		return unit;
	}

	/* unit set 方法 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/* goodsName get 方法 */
	public String getGoodsName() {
		return goodsName;
	}

	/* goodsName set 方法 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	/* barcode get 方法 */
	public String getBarcode() {
		return barcode;
	}

	/* barcode set 方法 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	/* amount get 方法 */
	public BigDecimal getAmount() {
		return amount;
	}

	/* amount set 方法 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/* contractNo get 方法 */
	public String getContractNo() {
		return contractNo;
	}

	/* contractNo set 方法 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	/* purchaseOrderId get 方法 */
	public Long getPurchaseOrderId() {
		return purchaseOrderId;
	}

	/* purchaseOrderId set 方法 */
	public void setPurchaseOrderId(Long purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	/* goodsId get 方法 */
	public Long getGoodsId() {
		return goodsId;
	}

	/* goodsId set 方法 */
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	/* price get 方法 */
	public BigDecimal getPrice() {
		return price;
	}

	/* price set 方法 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/* num get 方法 */
	public Integer getNum() {
		return num;
	}

	/* num set 方法 */
	public void setNum(Integer num) {
		this.num = num;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	public String getFactoryNo() {
		return factoryNo;
	}

	public void setFactoryNo(String factoryNo) {
		this.factoryNo = factoryNo;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public BigDecimal getTgtPrice() {
		return tgtPrice;
	}

	public void setTgtPrice(BigDecimal tgtPrice) {
		this.tgtPrice = tgtPrice;
	}

	public BigDecimal getTgtAmount() {
		return tgtAmount;
	}

	public void setTgtAmount(BigDecimal tgtAmount) {
		this.tgtAmount = tgtAmount;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public BigDecimal getTaxPrice() {
		return taxPrice;
	}

	public void setTaxPrice(BigDecimal taxPrice) {
		this.taxPrice = taxPrice;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public Integer getUnInwarehouseNum() {
		return unInwarehouseNum;
	}

	public void setUnInwarehouseNum(Integer unInwarehouseNum) {
		this.unInwarehouseNum = unInwarehouseNum;
	}

	public String getEffectiveInterval() {
		return effectiveInterval;
	}

	public void setEffectiveInterval(String effectiveInterval) {
		this.effectiveInterval = effectiveInterval;
	}

	public String getEffectiveIntervalLable() {
		return effectiveIntervalLable;
	}

	public void setEffectiveIntervalLable(String effectiveIntervalLable) {
		if(StringUtils.isNotBlank(this.effectiveInterval)) {
			effectiveIntervalLable = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_effectiveIntervalList, this.effectiveInterval);
		}
	}

}
