package com.topideal.entity.dto;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Author: Wilson Lau
 * @Date: 2022/1/18 16:22
 * @Description: 库位进销存汇总
 */
public class BuLocationSummaryDTO extends PageModel implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * 商家ID
     */
    private Long merchantId;
    /**
     * 商家名称
     */
    private String merchantName;
    /**
     * 仓库ID
     */
    private Long depotId;
    /**
     * 仓库名称
     */
    private String depotName;
    /**
     * 事业部ID
     */
    private Long buId;
    /**
     * 事业部名称
     */
    private String buName;
    /**
     * 归属月份
     */
    private String month;
    /**
     * 条码
     */
    private String barcode;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 标准品牌id
     */
    private Long brandId;
    /**
     * 标准品牌名称
     */
    private String brandName;
    /**
     * 上级母品牌
     */
    private String superiorParentBrand;
    /**
     * 标准条码
     */
    private String commbarcode;
    /**
     * 库位类型id
     */
    private Long stockLocationTypeId;
    /**
     * 库位类型
     */
    private String stockLocationTypeName;
    /**
     * 本月入库数量
     */
    private Integer monthInstorageNum;
    /**
     * 本月出库数量
     */
    private Integer monthOutstorageNum;
    /**
     * 本月损益数量
     */
    private Integer monthProfitlossNum;
    /**
     * 本月调整数量
     */
    private Integer monthAdjustmentNum;
    /**
     * 本月期初库存
     */
    private Integer monthBeginNum;
    /**
     * 本月正常品期初库存
     */
    private Integer monthBeginNormalNum;
    /**
     * 本月残次品期初库存
     */
    private Integer monthBeginDamagedNum;
    /**
     * 本月期末库存
     */
    private Integer monthEndNum;
    /**
     * 本月正常品期末库存
     */
    private Integer monthEndNormalNum;
    /**
     * 本月残次品期末库存量
     */
    private Integer monthEndDamagedNum;
    /**
     * 修改时间
     */
    private Timestamp modifyDate;
    /**
     * 创建时间
     */
    private Timestamp createDate;

    /**
     * 累计采购未上架数量
     */
    private Integer addPurchaseNotshelfNum;
    /**
     * 累计调拨在途
     */
    private Integer addTransferNotshelfNum;

    /**
     * 理货单位 0 托盘 1箱  2件
     */
    private String unit;

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

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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

    public String getSuperiorParentBrand() {
        return superiorParentBrand;
    }

    public void setSuperiorParentBrand(String superiorParentBrand) {
        this.superiorParentBrand = superiorParentBrand;
    }

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
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

    public Integer getMonthProfitlossNum() {
        return monthProfitlossNum;
    }

    public void setMonthProfitlossNum(Integer monthProfitlossNum) {
        this.monthProfitlossNum = monthProfitlossNum;
    }

    public Integer getMonthAdjustmentNum() {
        return monthAdjustmentNum;
    }

    public void setMonthAdjustmentNum(Integer monthAdjustmentNum) {
        this.monthAdjustmentNum = monthAdjustmentNum;
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

    public Integer getMonthBeginDamagedNum() {
        return monthBeginDamagedNum;
    }

    public void setMonthBeginDamagedNum(Integer monthBeginDamagedNum) {
        this.monthBeginDamagedNum = monthBeginDamagedNum;
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

    public Integer getMonthEndDamagedNum() {
        return monthEndDamagedNum;
    }

    public void setMonthEndDamagedNum(Integer monthEndDamagedNum) {
        this.monthEndDamagedNum = monthEndDamagedNum;
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

    public Integer getAddPurchaseNotshelfNum() {
        return addPurchaseNotshelfNum;
    }

    public void setAddPurchaseNotshelfNum(Integer addPurchaseNotshelfNum) {
        this.addPurchaseNotshelfNum = addPurchaseNotshelfNum;
    }

    public Integer getAddTransferNotshelfNum() {
        return addTransferNotshelfNum;
    }

    public void setAddTransferNotshelfNum(Integer addTransferNotshelfNum) {
        this.addTransferNotshelfNum = addTransferNotshelfNum;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
