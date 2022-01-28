package com.topideal.mongo.entity;

import java.sql.Timestamp;

/**
 * 爬虫账单生成出库清单日志
 */
public class CrawlerBillMongo {
	private String platformType;//平台类型  1、云集  2、唯品
	private String billCode;//账单号
	private String saleOutDepotCode;//出库单号
	private String goodsNo;//商品货号
	private String goodsName;//商品名称
	private String barcode;//条形码
	private Integer status;//状态   1-成功，0-失败
	private Integer num;//还未出库的数量
	private String errorMsg;//失败原因
	private Long createDate;//创建时间
	private Long crawlerBillId;//账单ID
	private Long depotId;//销售出仓仓库ID
	private String depotName;//销售出仓仓库名称
	private Long merchantId;//商家ID
	private String merchantName;//商家名称
	private String poNo;//po号
	
	public String getPlatformType() {
		return platformType;
	}
	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getSaleOutDepotCode() {
		return saleOutDepotCode;
	}
	public void setSaleOutDepotCode(String saleOutDepotCode) {
		this.saleOutDepotCode = saleOutDepotCode;
	}
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
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}
	public Long getCrawlerBillId() {
		return crawlerBillId;
	}
	public void setCrawlerBillId(Long crawlerBillId) {
		this.crawlerBillId = crawlerBillId;
	}
	public Long getDepotId() {
		return depotId;
	}
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}
	public String getDepotName() {
		return depotName;
	}
	public void setDepotName(String depotName) {
		this.depotName = depotName;
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
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	
}
