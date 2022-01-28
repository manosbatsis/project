package com.topideal.api.sapience.sapience001;
/**
 * 
向卓普信推送商品档案请求实体
 * @author 杨创
 *
 */
public class SapienceRequest {
	//private String goodsCategory;//商品分类  不能填
	//private String goodsBrand;//品牌不能填
	//private String goodsType;//商品类型   不能填
	private String contractsUnit;//合同单位 不能填
	private String qtpUnit;// 标准单位 非必填
	private String qtyUnit2;// 第二法定计量单位  非必填
	private String goodsNo;//商品编码 必填
	private String goodsName;//商品名称 必填
	private String status;//String10：新增20：修改 必填
	private String goodsSpec;//规格型号
	private String goodsPacktype;//包装方式
	private String merchantId;//货主编码
	private String merchantName;//货主名称
	private String recordPrice;//商品备案价格
	private String goodsBarcode;//商品条形码
	private String goodsHsCode;// HS编码
	private String compGoodsNo;// 企业商品自编号  非必填
	private String goodsEnName;// 商品英文名称 非必填
	private String goodsDesc;// 商品描述 非必填
	private Integer goodsQualityDays;// 保质天数 非必填
	private String taxCode;//税费编号 非必填
	private Double taxRate;//综合税率 非必填
	private String yTaxCode;// 行邮税率 非必填
	private Double yTaxRate;//行邮税率 非必填
	private String produceComp;// 生产厂家 非必填
	private String produceCompAddr;// 生产厂家地址 非必填
	private String kgs;// 毛重  非必填
	private String net;// 净重  非必填
	private String ingredient;// 成分  非必填
	private String poisonStr;// 毒害成分标识 非必填
	private String additiveStr;// 食品添加剂标识 非必填
	private String custassemCountry;// 海关原产国 非必填
	private Integer isCombined;// 是否组合品 整数  1：是  0：否 非必填
	private String note;//备注 非必填
	private String modifier;// 修改人 非必填
	private String modTime;// 修改日期 yyyy-mm-dd HH:mi:ss 非必填
	private String disableTime;// 禁用日期  yyyy-mm-dd HH:mi:ss 非必填
	private Integer isRecord;// 是否备案  整数，1：是，2：否  必填
	private String sourceCode;// 来源系统   卓志10 ,亚晋20,海仓30,纵腾40,普洛斯50,小码大众60,其他70,利丰:80,兰网90,宁兴优贝100   必填
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
	public String getRecordPrice() {
		return recordPrice;
	}
	public void setRecordPrice(String recordPrice) {
		this.recordPrice = recordPrice;
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
	public Double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}
	public String getyTaxCode() {
		return yTaxCode;
	}
	public void setyTaxCode(String yTaxCode) {
		this.yTaxCode = yTaxCode;
	}
	public Double getyTaxRate() {
		return yTaxRate;
	}
	public void setyTaxRate(Double yTaxRate) {
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
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
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
	public String getContractsUnit() {
		return contractsUnit;
	}
	public void setContractsUnit(String contractsUnit) {
		this.contractsUnit = contractsUnit;
	}
	
	
	
	
	
}
