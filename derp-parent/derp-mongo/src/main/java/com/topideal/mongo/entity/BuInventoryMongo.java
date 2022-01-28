package com.topideal.mongo.entity;

/**
 * 事业部库存
 * @author 杨创
 *2020-06-23
 */
public class BuInventoryMongo {
		//事业部库存id
		private Long buInventoryId;
		//商家ID
	    private Long merchantId;
	    //商家名称
	    private String merchantName;
	    //商家卓志编码
	    private String topidealCode;
	    //仓库ID
	    private Long depotId;
	    //仓库名称
	    private String depotName;
	    //仓库编码
	    private String depotCode;
	    //仓库类型(a-保税仓，b-备查仓，c-海外仓，d-中转仓,e-暂存区，f-销毁区）
	    private String depotType;
	    //事业部ID
	    private Long buId;
	    //事业部名称
	    private String buName;
	    //商品ID
	    private Long goodsId;
	    //商品名称
	    private String goodsName;
	    //商品货号
	    private String goodsNo;
	    //标准条码
	    private String commbarcode;
	    //条形码
	    private String barcode;
	    //品牌id
	    private Long brandId;
	    //品牌名称
	    private String brandName;
	    //库存余量
	    private Integer surplusNum;
	    //坏货数量
	    private Integer wornNum;
	    //好品数量
	    private Integer okNum;
	    //理货单位 0 托盘 1箱  2 件
	    private String unit;
	    //月份
	    private String month;
	    //创建人
	    private Long creater;
		public Long getBuInventoryId() {
			return buInventoryId;
		}
		public void setBuInventoryId(Long buInventoryId) {
			this.buInventoryId = buInventoryId;
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
		public String getTopidealCode() {
			return topidealCode;
		}
		public void setTopidealCode(String topidealCode) {
			this.topidealCode = topidealCode;
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
		public String getDepotCode() {
			return depotCode;
		}
		public void setDepotCode(String depotCode) {
			this.depotCode = depotCode;
		}
		public String getDepotType() {
			return depotType;
		}
		public void setDepotType(String depotType) {
			this.depotType = depotType;
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
		public String getCommbarcode() {
			return commbarcode;
		}
		public void setCommbarcode(String commbarcode) {
			this.commbarcode = commbarcode;
		}
		public String getBarcode() {
			return barcode;
		}
		public void setBarcode(String barcode) {
			this.barcode = barcode;
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
		public Integer getSurplusNum() {
			return surplusNum;
		}
		public void setSurplusNum(Integer surplusNum) {
			this.surplusNum = surplusNum;
		}
		public Integer getWornNum() {
			return wornNum;
		}
		public void setWornNum(Integer wornNum) {
			this.wornNum = wornNum;
		}
		public Integer getOkNum() {
			return okNum;
		}
		public void setOkNum(Integer okNum) {
			this.okNum = okNum;
		}
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
		public String getMonth() {
			return month;
		}
		public void setMonth(String month) {
			this.month = month;
		}
		public Long getCreater() {
			return creater;
		}
		public void setCreater(Long creater) {
			this.creater = creater;
		}
	    
	    
	    
	    
	    
}
