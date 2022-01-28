package com.topideal.entity.dto.purchase;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class FinancingOrderItemDTO {

    /**
    * 商品ID
    */
	@ApiModelProperty(value = "商品ID", required = false)
    private Long goodsId;
    /**
    * 商品名称
    */
	@ApiModelProperty(value = "商品名称", required = false)
    private String goodsName;
    /**
    * 商品货号
    */
	@ApiModelProperty(value = "商品货号", required = false)
    private String goodsNo;
    /**
    * 原产地
    */
	@ApiModelProperty(value = "原产地", required = false)
    private String originCountry;
    /**
    * 商品保质天数
    */
	@ApiModelProperty(value = "商品保质天数", required = false)
    private Integer qualityGuaranteeDates;
    /**
    * 采购数量
    */
	@ApiModelProperty(value = "采购数量", required = false)
    private Integer purchaseQuantity;
    /**
    * 采购金额
    */
	@ApiModelProperty(value = "采购金额", required = false)
    private BigDecimal purchaseAmount;
    /**
    * 采购金额
    */
	@ApiModelProperty(value = "采购单价", required = false)
    private BigDecimal purchasePrice;
	
	@ApiModelProperty(value = "条形码", required = false)
    private String barcode;
	
	@ApiModelProperty(value = "规格型号", required = false)
    private String spec;

    @ApiModelProperty(value = "导出序号", hidden = true )
    private String exportIndex;

    @ApiModelProperty(value = "导出母品牌名称", hidden = true )
    private String exportSuperiorParentBrand;

    @ApiModelProperty(value = "导出单位", hidden = true )
    private String exportUnit;
    
    @ApiModelProperty(value = "标准条码", required = false)
    private String commbarcode;

    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*originCountry get 方法 */
    public String getOriginCountry(){
    return originCountry;
    }
    /*originCountry set 方法 */
    public void setOriginCountry(String  originCountry){
    this.originCountry=originCountry;
    }
    /*qualityGuaranteeDates get 方法 */
    public Integer getQualityGuaranteeDates(){
    return qualityGuaranteeDates;
    }
    /*qualityGuaranteeDates set 方法 */
    public void setQualityGuaranteeDates(Integer  qualityGuaranteeDates){
    this.qualityGuaranteeDates=qualityGuaranteeDates;
    }
    /*purchaseQuantity get 方法 */
    public Integer getPurchaseQuantity(){
    return purchaseQuantity;
    }
    /*purchaseQuantity set 方法 */
    public void setPurchaseQuantity(Integer  purchaseQuantity){
    this.purchaseQuantity=purchaseQuantity;
    }
    /*purchaseAmount get 方法 */
    public BigDecimal getPurchaseAmount(){
    return purchaseAmount;
    }
    /*purchaseAmount set 方法 */
    public void setPurchaseAmount(BigDecimal  purchaseAmount){
    this.purchaseAmount=purchaseAmount;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}

    public String getExportIndex() {
        return exportIndex;
    }

    public void setExportIndex(String exportIndex) {
        this.exportIndex = exportIndex;
    }

    public String getExportSuperiorParentBrand() {
        return exportSuperiorParentBrand;
    }

    public void setExportSuperiorParentBrand(String exportSuperiorParentBrand) {
        this.exportSuperiorParentBrand = exportSuperiorParentBrand;
    }

    public String getExportUnit() {
        return exportUnit;
    }

    public void setExportUnit(String exportUnit) {
        this.exportUnit = exportUnit;
    }
	public String getCommbarcode() {
		return commbarcode;
	}
	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}
    
    
}
