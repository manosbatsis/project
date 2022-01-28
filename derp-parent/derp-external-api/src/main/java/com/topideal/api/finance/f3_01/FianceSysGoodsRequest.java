package com.topideal.api.finance.f3_01;

import java.math.BigDecimal;

/**
 * 3.12 金融同步商品接口 表头
 * @author 杨创
 * 2021-04-22 
 */
public class FianceSysGoodsRequest {
	
	
	private String goodsNo;//外部系统商品编码  必填
	private String goodsName;//商品名称   必填
	private String status;//状态 10：新增  20：修改   必填
	private String goodsCategory;//商品分类 非 必填
	private String goodsBrand;//品牌 非  必填
	private String goodsSpec;//规格型号   必填
	private String goodsPacktype;//包装方式  非  必填
	private String goodsType;//商品类型  非  必填
	private String merchantId;	//货主编码  必填
	private String merchantName;//	货主名称  必填
	private BigDecimal recordPrice;//商品备案价格  必填
	private String contractsUnit;//合同单位  必填
	private String goodsBarcode;//商品条形码  必填
	private String goodsHsCode;//HS编码 非 必填
	private String compGoodsNo;//企业商品自编号 非 必填
	private String goodsEnName;//商品英文名称非 必填
	private String goodsDesc;//商品描述非 必填
	private Integer goodsQualityDays;//保质天数 必填	
	private String taxCode;//	税费编号非 必填
	private BigDecimal taxRate;//	综合税率非 必填
	private String yTaxCode;//	行邮税号非 必填
	private BigDecimal yTaxRate;//	行邮税率 非 必填
	private String produceComp;//	生产厂家非 必填
	private String produceCompAddr;//	生产厂家地址非 必填
	private String qtpUnit;//	标准单位  必填
	private String qtyUnit2;//	第二法定计量单位 非 必填
	private String kgs;//	毛重  非 必填
	private String net;//	净重 非 必填
	private String ingredient;//	成分 非 必填
	private String poisonStr;//	毒害成分标识 非 必填
	private String additiveStr;//	食品添加剂标识 非 必填
	private String custassemCountry;//	海关原产国 必填
	private Integer isCombined;//	是否组合品 必填  整数  1：是  0：否
	private String note;//	备注 非 必填
	private String modifier;//	修改人 非 必填
	private String modTime;//	修改日期 非 必填
	private String disableTime;//	禁用日期 非 必填
	private Integer isRecord;//	是否备案  //必填 整数，1：是，2：否
	private BigDecimal averagePrince;//	年平均售价 非 必填
	private String sourceCode;//	物流企业  必填 卓志：10亚晋：20
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getGoodsCategory() {
		return goodsCategory;
	}
	public void setGoodsCategory(String goodsCategory) {
		this.goodsCategory = goodsCategory;
	}
	public String getGoodsBrand() {
		return goodsBrand;
	}
	public void setGoodsBrand(String goodsBrand) {
		this.goodsBrand = goodsBrand;
	}
	public String getGoodsSpec() {
		return goodsSpec;
	}
	public void setGoodsSpec(String goodsSpec) {
		this.goodsSpec = goodsSpec;
	}
	public String getGoodsPacktype() {
		return goodsPacktype;
	}
	public void setGoodsPacktype(String goodsPacktype) {
		this.goodsPacktype = goodsPacktype;
	}
	public String getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public BigDecimal getRecordPrice() {
		return recordPrice;
	}
	public void setRecordPrice(BigDecimal recordPrice) {
		this.recordPrice = recordPrice;
	}
	public String getContractsUnit() {
		return contractsUnit;
	}
	public void setContractsUnit(String contractsUnit) {
		this.contractsUnit = contractsUnit;
	}
	public String getGoodsBarcode() {
		return goodsBarcode;
	}
	public void setGoodsBarcode(String goodsBarcode) {
		this.goodsBarcode = goodsBarcode;
	}
	public String getGoodsHsCode() {
		return goodsHsCode;
	}
	public void setGoodsHsCode(String goodsHsCode) {
		this.goodsHsCode = goodsHsCode;
	}
	public String getCompGoodsNo() {
		return compGoodsNo;
	}
	public void setCompGoodsNo(String compGoodsNo) {
		this.compGoodsNo = compGoodsNo;
	}
	public String getGoodsEnName() {
		return goodsEnName;
	}
	public void setGoodsEnName(String goodsEnName) {
		this.goodsEnName = goodsEnName;
	}
	public String getGoodsDesc() {
		return goodsDesc;
	}
	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	public Integer getGoodsQualityDays() {
		return goodsQualityDays;
	}
	public void setGoodsQualityDays(Integer goodsQualityDays) {
		this.goodsQualityDays = goodsQualityDays;
	}
	public String getTaxCode() {
		return taxCode;
	}
	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	public BigDecimal getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	public String getyTaxCode() {
		return yTaxCode;
	}
	public void setyTaxCode(String yTaxCode) {
		this.yTaxCode = yTaxCode;
	}
	public BigDecimal getyTaxRate() {
		return yTaxRate;
	}
	public void setyTaxRate(BigDecimal yTaxRate) {
		this.yTaxRate = yTaxRate;
	}
	public String getProduceComp() {
		return produceComp;
	}
	public void setProduceComp(String produceComp) {
		this.produceComp = produceComp;
	}
	public String getProduceCompAddr() {
		return produceCompAddr;
	}
	public void setProduceCompAddr(String produceCompAddr) {
		this.produceCompAddr = produceCompAddr;
	}
	public String getQtpUnit() {
		return qtpUnit;
	}
	public void setQtpUnit(String qtpUnit) {
		this.qtpUnit = qtpUnit;
	}
	public String getQtyUnit2() {
		return qtyUnit2;
	}
	public void setQtyUnit2(String qtyUnit2) {
		this.qtyUnit2 = qtyUnit2;
	}
	public String getKgs() {
		return kgs;
	}
	public void setKgs(String kgs) {
		this.kgs = kgs;
	}
	public String getNet() {
		return net;
	}
	public void setNet(String net) {
		this.net = net;
	}
	public String getIngredient() {
		return ingredient;
	}
	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}
	public String getPoisonStr() {
		return poisonStr;
	}
	public void setPoisonStr(String poisonStr) {
		this.poisonStr = poisonStr;
	}
	public String getAdditiveStr() {
		return additiveStr;
	}
	public void setAdditiveStr(String additiveStr) {
		this.additiveStr = additiveStr;
	}
	public String getCustassemCountry() {
		return custassemCountry;
	}
	public void setCustassemCountry(String custassemCountry) {
		this.custassemCountry = custassemCountry;
	}
	public Integer getIsCombined() {
		return isCombined;
	}
	public void setIsCombined(Integer isCombined) {
		this.isCombined = isCombined;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getModTime() {
		return modTime;
	}
	public void setModTime(String modTime) {
		this.modTime = modTime;
	}
	public String getDisableTime() {
		return disableTime;
	}
	public void setDisableTime(String disableTime) {
		this.disableTime = disableTime;
	}
	public Integer getIsRecord() {
		return isRecord;
	}
	public void setIsRecord(Integer isRecord) {
		this.isRecord = isRecord;
	}
	public BigDecimal getAveragePrince() {
		return averagePrince;
	}
	public void setAveragePrince(BigDecimal averagePrince) {
		this.averagePrince = averagePrince;
	}
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	
	
	
	
}
