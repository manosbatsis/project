package com.topideal.entity.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;
/**
 * 经销存汇总报表
 * @author lian_
 */
public class BuInventorySummaryDTO extends PageModel implements Serializable{
	@ApiModelProperty(value = "id")
	private Long id;
	 //商家ID
	@ApiModelProperty(value = "商家ID")
    private Long merchantId;
    //商家卓志编码
	@ApiModelProperty(value = "商家卓志编码")
    private String topidealCode;
	//商家名称
	@ApiModelProperty(value = "商家名称")
    private String merchantName;
    //仓库ID
	@ApiModelProperty(value = "仓库ID")
    private Long depotId;
    //仓库编码
	@ApiModelProperty(value = "仓库编码")
    private String depotCode;
    //仓库名称
	@ApiModelProperty(value = "仓库名称")
    private String depotName;
    //归属月份
    @ApiModelProperty(value = "归属月份")
    private String ownMonth;
    //属性说明
    @ApiModelProperty(value = "产品名称")
    private String productName;
    /** 品牌id*/
    @ApiModelProperty(value = "品牌id")
    private Long brandId;
     /** 品牌名称*/
    @ApiModelProperty(value = "品牌名称*")
    private String brandName;
    //条码
    @ApiModelProperty(value = "条码")
    private String barcode;
    //工厂编码
    @ApiModelProperty(value = "工厂编码")
    private String factoryNo;
    /**理货单位 0 托盘 1箱  2件*/
    @ApiModelProperty(value = "理货单位 0 托盘 1箱  2件")
    private String unit;
    //单价HKD
    @ApiModelProperty(value = "单价HKD")
    private BigDecimal supplyMinPrice;
    //本月期初库存
    @ApiModelProperty(value = "本月期初库存")
    private Integer monthBeginNum;
    /**本月正常品期初库存*/
    @ApiModelProperty(value = "本月正常品期初库存")
    private Integer monthBeginNormalNum;
    //本月入库数量
    @ApiModelProperty(value = "本月入库数量")
    private Integer monthInstorageNum;
    //本月出库数量
    @ApiModelProperty(value = "本月出库数量")
    private Integer monthOutstorageNum;
    @ApiModelProperty(value = "来货残损数量")
    private Integer inDamagedNum;//来货残损数量
    @ApiModelProperty(value = "出库残损数量")
    private Integer outDamagedNum;//出库残损数量
    //本月损益数量
    @ApiModelProperty(value = "本月损益数量")
    private Integer monthProfitlossNum;
    //本月期末库存
    @ApiModelProperty(value = "本月期末库存")
    private Integer monthEndNum;
    /**本月正常品期末库存*/
    @ApiModelProperty(value = "本月正常品期末库存")
    private Integer monthEndNormalNum;
    //仓库现存量
    @ApiModelProperty(value = "仓库现存量")
    private Integer availableNum;
    //本月销售未确认数量
    @ApiModelProperty(value = "本月销售未确认数量")
    private Integer monthSaleUnconfirmedNum;
    //本月采购未上架数量
    @ApiModelProperty(value = "本月采购未上架数量")
    private Integer monthPurchaseNotshelfNum;
    //本月期末库存
    @ApiModelProperty(value = "本月期末库存")
    private BigDecimal monthEndAmount;
    //修改时间
    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
    //创建时间
    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    @ApiModelProperty(value = "单位")
    private String unitLabel;

	/**
	 * 本月残次品期初库存量
	 */
    @ApiModelProperty(value = "本月残次品期初库存量")
	private Integer monthBeginDamagedNum;
	/**
	 * 本月残次品期末库存量
	 */
    @ApiModelProperty(value = "本月残次品期末库存量")
	private Integer monthEndDamagedNum;
	/**
	 * 好品损益
	 */
    @ApiModelProperty(value = "好品损益")
	private Integer profitlossGoodNum;
	/**
	 * 坏品损益
	 */
    @ApiModelProperty(value = "坏品损益")
	private Integer profitlossDamagedNum;

	/**
	 * 正常品过期量
	 */
    @ApiModelProperty(value = "正常品过期量")
	private Integer monthNormalExpireNum;

	/**
	 * 本期调拨在途
	 */
    @ApiModelProperty(value = "本期调拨在途")
	private Integer monthTransferNotshelfNum;
	    
    /*查询条件非数据表字段start*/
    private List<Long> parentBrandIds;//标准品牌id
    private List<Long> depotListIds;//仓库id集合
    /**
    * 事业部ID
    */
    @ApiModelProperty(value = "事业部ID")
    private Long buId;
    /**
    * 事业部名称
    */
    @ApiModelProperty(value = "事业部名称")
    private String buName;
    /**
    * 事业部编码
    */
    @ApiModelProperty(value = "事业部编码")
    private String buCode;

	/**
	 * 本期来货残次
	 */
    @ApiModelProperty(value = "本期来货残次")
	private Integer monthInBadNum;
	/**
	 * 本期出库残次
	 */
    @ApiModelProperty(value = "本期出库残次")
	private Integer monthOutBadNum;

	//标准条码
    @ApiModelProperty(value = "标准条码")
	private String commbarcode;
	
	 /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    /**
     * 商品货号
     */
    @ApiModelProperty(value = "商品货号")
    private String goodsNo;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
     * 上级母品牌
     */
    @ApiModelProperty(value = "上级母品牌")
    private String superiorParentBrand;
    
	@ApiModelProperty(value = "事业部集合")
	private List<Long> buList;
    /*查询条件非数据表字段end*/
    
	public String getSuperiorParentBrand() {
		return superiorParentBrand;
	}
	public void setSuperiorParentBrand(String superiorParentBrand) {
		this.superiorParentBrand = superiorParentBrand;
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
	public String getBuCode() {
		return buCode;
	}
	public void setBuCode(String buCode) {
		this.buCode = buCode;
	}
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
	public String getTopidealCode() {
		return topidealCode;
	}
	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public Long getDepotId() {
		return depotId;
	}
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}
	public String getDepotCode() {
		return depotCode;
	}
	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}
	public String getDepotName() {
		return depotName;
	}
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}
	public String getOwnMonth() {
		return ownMonth;
	}
	public void setOwnMonth(String ownMonth) {
		this.ownMonth = ownMonth;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
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
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getFactoryNo() {
		return factoryNo;
	}
	public void setFactoryNo(String factoryNo) {
		this.factoryNo = factoryNo;
	}
	public String getUnit() {
		return unit;
	}
	
	public BigDecimal getSupplyMinPrice() {
		return supplyMinPrice;
	}
	public void setSupplyMinPrice(BigDecimal supplyMinPrice) {
		this.supplyMinPrice = supplyMinPrice;
	}
	public Integer getMonthBeginNum() {
		return monthBeginNum;
	}
	public void setMonthBeginNum(Integer monthBeginNum) {
		this.monthBeginNum = monthBeginNum;
	}
	public Integer getMonthBeginNormalNum() {
		return monthBeginNormalNum;
	}
	public void setMonthBeginNormalNum(Integer monthBeginNormalNum) {
		this.monthBeginNormalNum = monthBeginNormalNum;
	}
	public Integer getMonthInstorageNum() {
		return monthInstorageNum;
	}
	public void setMonthInstorageNum(Integer monthInstorageNum) {
		this.monthInstorageNum = monthInstorageNum;
	}
	public Integer getMonthOutstorageNum() {
		return monthOutstorageNum;
	}
	public void setMonthOutstorageNum(Integer monthOutstorageNum) {
		this.monthOutstorageNum = monthOutstorageNum;
	}
	public Integer getInDamagedNum() {
		return inDamagedNum;
	}
	public void setInDamagedNum(Integer inDamagedNum) {
		this.inDamagedNum = inDamagedNum;
	}
	public Integer getOutDamagedNum() {
		return outDamagedNum;
	}
	public void setOutDamagedNum(Integer outDamagedNum) {
		this.outDamagedNum = outDamagedNum;
	}
	public Integer getMonthProfitlossNum() {
		return monthProfitlossNum;
	}
	public void setMonthProfitlossNum(Integer monthProfitlossNum) {
		this.monthProfitlossNum = monthProfitlossNum;
	}
	public Integer getMonthEndNum() {
		return monthEndNum;
	}
	public void setMonthEndNum(Integer monthEndNum) {
		this.monthEndNum = monthEndNum;
	}
	public Integer getMonthEndNormalNum() {
		return monthEndNormalNum;
	}
	public void setMonthEndNormalNum(Integer monthEndNormalNum) {
		this.monthEndNormalNum = monthEndNormalNum;
	}
	public Integer getAvailableNum() {
		return availableNum;
	}
	public void setAvailableNum(Integer availableNum) {
		this.availableNum = availableNum;
	}
	public Integer getMonthSaleUnconfirmedNum() {
		return monthSaleUnconfirmedNum;
	}
	public void setMonthSaleUnconfirmedNum(Integer monthSaleUnconfirmedNum) {
		this.monthSaleUnconfirmedNum = monthSaleUnconfirmedNum;
	}
	public Integer getMonthPurchaseNotshelfNum() {
		return monthPurchaseNotshelfNum;
	}
	public void setMonthPurchaseNotshelfNum(Integer monthPurchaseNotshelfNum) {
		this.monthPurchaseNotshelfNum = monthPurchaseNotshelfNum;
	}
	public BigDecimal getMonthEndAmount() {
		return monthEndAmount;
	}
	public void setMonthEndAmount(BigDecimal monthEndAmount) {
		this.monthEndAmount = monthEndAmount;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public List<Long> getParentBrandIds() {
		return parentBrandIds;
	}
	public void setParentBrandIds(List<Long> parentBrandIds) {
		this.parentBrandIds = parentBrandIds;
	}

	public Integer getMonthBeginDamagedNum() {
		return monthBeginDamagedNum;
	}

	public void setMonthBeginDamagedNum(Integer monthBeginDamagedNum) {
		this.monthBeginDamagedNum = monthBeginDamagedNum;
	}

	public Integer getMonthEndDamagedNum() {
		return monthEndDamagedNum;
	}

	public void setMonthEndDamagedNum(Integer monthEndDamagedNum) {
		this.monthEndDamagedNum = monthEndDamagedNum;
	}

	public Integer getProfitlossGoodNum() {
		return profitlossGoodNum;
	}

	public void setProfitlossGoodNum(Integer profitlossGoodNum) {
		this.profitlossGoodNum = profitlossGoodNum;
	}

	public Integer getProfitlossDamagedNum() {
		return profitlossDamagedNum;
	}

	public void setProfitlossDamagedNum(Integer profitlossDamagedNum) {
		this.profitlossDamagedNum = profitlossDamagedNum;
	}

	public Integer getMonthNormalExpireNum() {
		return monthNormalExpireNum;
	}

	public void setMonthNormalExpireNum(Integer monthNormalExpireNum) {
		this.monthNormalExpireNum = monthNormalExpireNum;
	}
	public List<Long> getDepotListIds() {
		return depotListIds;
	}
	public void setDepotListIds(List<Long> depotListIds) {
		this.depotListIds = depotListIds;
	}
	public String getUnitLabel() {
		return unitLabel;
	}
	public void setUnitLabel(String unitLabel) {
		this.unitLabel = unitLabel;
	}
	public void setUnit(String unit) {
		this.unit = unit;
		this.unitLabel = DERP.getLabelByKey(DERP.inventory_unitList, unit);
	}

	public Integer getMonthTransferNotshelfNum() {
		return monthTransferNotshelfNum;
	}

	public void setMonthTransferNotshelfNum(Integer monthTransferNotshelfNum) {
		this.monthTransferNotshelfNum = monthTransferNotshelfNum;
	}

	public Integer getMonthInBadNum() {
		return monthInBadNum;
	}

	public void setMonthInBadNum(Integer monthInBadNum) {
		this.monthInBadNum = monthInBadNum;
	}

	public Integer getMonthOutBadNum() {
		return monthOutBadNum;
	}

	public void setMonthOutBadNum(Integer monthOutBadNum) {
		this.monthOutBadNum = monthOutBadNum;
	}

	public String getCommbarcode() {
		return commbarcode;
	}

	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
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
	public List<Long> getBuList() {
		return buList;
	}
	public void setBuList(List<Long> buList) {
		this.buList = buList;
	}
	
}

