package com.topideal.entity.dto.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @Description: 清关资料商品明细
 * @Author: Chen Yiluan
 * @Date: 2021/02/24 14:09
 **/
@ApiModel
public class CustomsDeclareItemDTO {

    // 采购总金额
    @ApiModelProperty("采购总金额")
    private BigDecimal amount;
    // 批次号
    @ApiModelProperty("批次号")
    private String batchNo;
    // 子合同号
    @ApiModelProperty("子合同号")
    private String contractNo;
    // 商品id
    @ApiModelProperty("商品id")
    private Long goodsId;
    // 数量
    @ApiModelProperty("数量")
    private Integer num;
    // 成分占比
    @ApiModelProperty("成分占比")
    private String constituentRatio;
    // 箱号
    @ApiModelProperty("箱号")
    private String boxNo;
    // 单价
    @ApiModelProperty("单价")
    private BigDecimal price;
    // 商品货号
    @ApiModelProperty("商品货号")
    private String goodsNo;
    // 商品名称
    @ApiModelProperty("商品名称")
    private String goodsName;
    // 商品编码
    @ApiModelProperty("商品编码")
    private String goodsCode;
    // 毛重
    @ApiModelProperty("毛重")
    private Double grossWeight;
    // 净重
    @ApiModelProperty("净重")
    private Double netWeight;
    // 品牌名称
    @ApiModelProperty("品牌名称")
    private String brandName;
    //单位
    @ApiModelProperty("单位")
    private String unit;
    //托盘号
    @ApiModelProperty("托盘号")
    private String palletNo;
    //箱数
    @ApiModelProperty("箱数")
    private Integer cartons;
    //商品规格型号
    @ApiModelProperty("商品规格型号")
    private String goodsSpec;
    //条形码
    @ApiModelProperty("条形码")
    private String barcode;
    @ApiModelProperty("原产地")
    private String countryName ;

    @ApiModelProperty("海关商品备案号")
    private String customsGoodsRecordNo ;
    @ApiModelProperty("申报要素")
    private String declareFactor ;
    @ApiModelProperty("总毛重")
    private Double totalGrossWeight;
    @ApiModelProperty("总净重")
    private Double totalNetWeight;
    @ApiModelProperty("美元单价")
    private BigDecimal usPrice;
    @ApiModelProperty("美元总价")
    private BigDecimal usAmount;
    @ApiModelProperty("生产企业")
    private String manufacturer;
    @ApiModelProperty("hs编码")
    private String hsCode;
    @ApiModelProperty("商品类别")
    private String productTypeName;
    // 保质天数
    @ApiModelProperty("保质天数")
    private Integer shelfLifeDays;

    @ApiModelProperty("账册备案料号")
    private String materialAccount;

    @ApiModelProperty("售卖商品名称（中文）")
    private String saleGoodNames;

    @ApiModelProperty("EMS编码")
    private String emsCode;

    @ApiModelProperty("SKU编码")
    private String skuCode;

    @ApiModelProperty("箱规")
    private Integer boxToUnit;

    @ApiModelProperty("大小（长*宽*高）")
    private String volume;

    @ApiModelProperty("金二项号")
    private String jinerxiang;

    @ApiModelProperty("商品英文名称")
    private String englishGoodsName;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getConstituentRatio() {
        return constituentRatio;
    }

    public void setConstituentRatio(String constituentRatio) {
        this.constituentRatio = constituentRatio;
    }

    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
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

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getPalletNo() {
        return palletNo;
    }

    public void setPalletNo(String palletNo) {
        this.palletNo = palletNo;
    }

    public Integer getCartons() {
        return cartons;
    }

    public void setCartons(Integer cartons) {
        this.cartons = cartons;
    }

    public String getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCustomsGoodsRecordNo() {
        return customsGoodsRecordNo;
    }

    public void setCustomsGoodsRecordNo(String customsGoodsRecordNo) {
        this.customsGoodsRecordNo = customsGoodsRecordNo;
    }

    public String getDeclareFactor() {
        return declareFactor;
    }

    public void setDeclareFactor(String declareFactor) {
        this.declareFactor = declareFactor;
    }

    public Double getTotalGrossWeight() {
        return totalGrossWeight;
    }

    public void setTotalGrossWeight(Double totalGrossWeight) {
        this.totalGrossWeight = totalGrossWeight;
    }

    public Double getTotalNetWeight() {
        return totalNetWeight;
    }

	public BigDecimal getUsPrice() {
		return usPrice;
	}

	public void setUsPrice(BigDecimal usPrice) {
		this.usPrice = usPrice;
	}

	public BigDecimal getUsAmount() {
		return usAmount;
	}

	public void setUsAmount(BigDecimal usAmount) {
		this.usAmount = usAmount;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getHsCode() {
		return hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public String getProductTypeName() {
		return productTypeName;
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	public void setTotalNetWeight(Double totalNetWeight) {
		this.totalNetWeight = totalNetWeight;
	}

    public Integer getShelfLifeDays() {
        return shelfLifeDays;
    }

    public void setShelfLifeDays(Integer shelfLifeDays) {
        this.shelfLifeDays = shelfLifeDays;
    }

	public String getMaterialAccount() {
		return materialAccount;
	}

	public void setMaterialAccount(String materialAccount) {
		this.materialAccount = materialAccount;
	}

	public String getSaleGoodNames() {
		return saleGoodNames;
	}

	public void setSaleGoodNames(String saleGoodNames) {
		this.saleGoodNames = saleGoodNames;
	}

	public String getEmsCode() {
		return emsCode;
	}

	public void setEmsCode(String emsCode) {
		this.emsCode = emsCode;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public Integer getBoxToUnit() {
		return boxToUnit;
	}

	public void setBoxToUnit(Integer boxToUnit) {
		this.boxToUnit = boxToUnit;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getJinerxiang() {
		return jinerxiang;
	}

	public void setJinerxiang(String jinerxiang) {
		this.jinerxiang = jinerxiang;
	}

    public String getEnglishGoodsName() {
        return englishGoodsName;
    }

    public void setEnglishGoodsName(String englishGoodsName) {
        this.englishGoodsName = englishGoodsName;
    }
}
