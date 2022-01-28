package com.topideal.mongo.entity;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商家店铺关联表
 * @author lian_
 *
 */
public class MerchantShopRelMongo {

		/**
	    * 店铺编码
	    */
		@ApiModelProperty(value = "店铺编码")
	    private String shopCode;
	    /**
	    * 店铺名称
	    */
		@ApiModelProperty(value = "店铺名称")
	    private String shopName;
	    /**
	    * 客户名称
	    */
		@ApiModelProperty(value = "客户名称")
	    private String customerName;
	    /**
	    * 操作员
	    */
		@ApiModelProperty(value = "操作员")
	    private String operator;
	    /**
	    * 商家名称
	    */
		@ApiModelProperty(value = "商家名称")
	    private String merchantName;
	    /**
	    * 卓志编码
	    */
		@ApiModelProperty(value = "卓志编码")
	    private String topidealCode;
	    /**
	    * 商家id
	    */
		@ApiModelProperty(value = "商家id")
	    private Long merchantId;
	    /**
	    * 客户ID
	    */
		@ApiModelProperty(value = "客户ID")
	    private Long customerId;
	    /**
	    * shopId
	    */
		@ApiModelProperty(value = "shopId")
	    private Long shopId;
	    /**
	    * 状态(1使用中,0已禁用)
	    */
		@ApiModelProperty(value = "状态(1使用中,0已禁用)")
	    private String status;
		@ApiModelProperty(value = "/仓库id")
	    private Long depotId;//仓库id
		@ApiModelProperty(value = "仓库名称")
	    private String depotName;// 仓库名称
		@ApiModelProperty(value = "订单是否拆解")
		private String isDismantle; //订单是否拆解

		  /**
	     * 电商平台编码
	     */
		@ApiModelProperty(value = "电商平台编码")
	    private String storePlatformCode;
	    /**
	     * 电商平台名称
	     */
		@ApiModelProperty(value = "电商平台名称")
	    private String storePlatformName;
	    /**
	     * 运营类型值编码
	     */
		@ApiModelProperty(value = "运营类型值编码")
	    private String shopTypeCode;
	    /**
	     * 运营类型名称
	     */
		@ApiModelProperty(value = "运营类型名称")
	    private String shopTypeName;
	    /**
	     * 数据来源编码
	     */
		@ApiModelProperty(value = "数据来源编码")
	    private String dataSourceCode;
	    /**
	     * 数据来源名称
	     */
		@ApiModelProperty(value = "数据来源名称")
	    private String dataSourceName;

		/**
		 * 开店事业部id
		 */
		@ApiModelProperty(value = "开店事业部id")
		private Long buId;
		/**
		 * 开店事业部名称
		 */
		@ApiModelProperty(value = "开店事业部名称")
		private String buName;

		/**
		 * 店铺类型值编码：DZD：单主店、WBD：外部店
		 */
		@ApiModelProperty(value = "店铺类型值编码：DZD：单主店、WBD：外部店")
		private String storeTypeCode;
		/**
		 * 店铺类型名称
		 */
		@ApiModelProperty(value = "店铺类型名称")
		private String storeTypeName;
		/**
		 * 店铺统一ID
		 */
		@ApiModelProperty(value = "店铺统一ID")
		private String shopUnifyId;
	/**
	 * 菜鸟查询接口session_key
	 */
	@ApiModelProperty(value = "菜鸟查询接口session_key")
	private String sessionKey;
	/**
	 * 菜鸟查询接口app_key
	 */
	@ApiModelProperty(value = "菜鸟查询接口app_key")
	private String appKey;
	/**
	 * 菜鸟查询接口app_secret
	 */
	@ApiModelProperty(value = "菜鸟查询接口app_secret")
	private String appSecret;
	/**
	 * 是否同步菜鸟商品 1-是 0-否
	 */
	@ApiModelProperty(value = "是否同步菜鸟商品 1-是 0-否")
	private String isSycnMerchandise;

	/**
	 * 专营母品牌id
	 */
	@ApiModelProperty(value = "专营母品牌id")
	private Long superiorParentBrandId;
	/**
	 * 专营母品牌名称
	 */
	@ApiModelProperty(value = "专营母品牌名称")
	private String superiorParentBrandName;
	/**
	 * 专营母品牌NC编码
	 */
	@ApiModelProperty(value = "专营母品牌NC编码")
	private String superiorParentBrandNcCode;

	@ApiModelProperty(value = "币种")
	private String currency;

	@ApiModelProperty(value = "币种Label")
	private String currencyLabel;

	@ApiModelProperty(value = "事业部库位类型ID")
	private Long stockLocationTypeId;
	@ApiModelProperty(value = "事业部库位类型名称")
	private String stockLocationTypeName;

		public String getStorePlatformCode() {
			return storePlatformCode;
		}
		public void setStorePlatformCode(String storePlatformCode) {
			this.storePlatformCode = storePlatformCode;
		}
		public String getStorePlatformName() {
			return storePlatformName;
		}
		public void setStorePlatformName(String storePlatformName) {
			this.storePlatformName = storePlatformName;
		}
		public String getShopTypeCode() {
			return shopTypeCode;
		}
		public void setShopTypeCode(String shopTypeCode) {
			this.shopTypeCode = shopTypeCode;
		}
		public String getShopTypeName() {
			return shopTypeName;
		}
		public void setShopTypeName(String shopTypeName) {
			this.shopTypeName = shopTypeName;
		}
		public String getDataSourceCode() {
			return dataSourceCode;
		}
		public void setDataSourceCode(String dataSourceCode) {
			this.dataSourceCode = dataSourceCode;
		}
		public String getDataSourceName() {
			return dataSourceName;
		}
		public void setDataSourceName(String dataSourceName) {
			this.dataSourceName = dataSourceName;
		}
		public String getShopCode() {
			return shopCode;
		}
		public void setShopCode(String shopCode) {
			this.shopCode = shopCode;
		}
		public String getShopName() {
			return shopName;
		}
		public void setShopName(String shopName) {
			this.shopName = shopName;
		}
		public String getCustomerName() {
			return customerName;
		}
		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}
		public String getOperator() {
			return operator;
		}
		public void setOperator(String operator) {
			this.operator = operator;
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
		public Long getMerchantId() {
			return merchantId;
		}
		public void setMerchantId(Long merchantId) {
			this.merchantId = merchantId;
		}
		public Long getCustomerId() {
			return customerId;
		}
		public void setCustomerId(Long customerId) {
			this.customerId = customerId;
		}
		public Long getShopId() {
			return shopId;
		}
		public void setShopId(Long shopId) {
			this.shopId = shopId;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
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

		public String getIsDismantle() {
			return isDismantle;
		}

		public void setIsDismantle(String isDismantle) {
			this.isDismantle = isDismantle;
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

		public String getStoreTypeCode() {
			return storeTypeCode;
		}

		public void setStoreTypeCode(String storeTypeCode) {
			this.storeTypeCode = storeTypeCode;
		}

		public String getStoreTypeName() {
			return storeTypeName;
		}

		public void setStoreTypeName(String storeTypeName) {
			this.storeTypeName = storeTypeName;
		}

		public String getShopUnifyId() {
			return shopUnifyId;
		}

		public void setShopUnifyId(String shopUnifyId) {
			this.shopUnifyId = shopUnifyId;
		}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getIsSycnMerchandise() {
		return isSycnMerchandise;
	}

	public void setIsSycnMerchandise(String isSycnMerchandise) {
		this.isSycnMerchandise = isSycnMerchandise;
	}

	public Long getSuperiorParentBrandId() {
		return superiorParentBrandId;
	}

	public void setSuperiorParentBrandId(Long superiorParentBrandId) {
		this.superiorParentBrandId = superiorParentBrandId;
	}

	public String getSuperiorParentBrandName() {
		return superiorParentBrandName;
	}

	public void setSuperiorParentBrandName(String superiorParentBrandName) {
		this.superiorParentBrandName = superiorParentBrandName;
	}

	public String getSuperiorParentBrandNcCode() {
		return superiorParentBrandNcCode;
	}

	public void setSuperiorParentBrandNcCode(String superiorParentBrandNcCode) {
		this.superiorParentBrandNcCode = superiorParentBrandNcCode;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrencyLabel() {
		return currencyLabel;
	}

	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
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
}
