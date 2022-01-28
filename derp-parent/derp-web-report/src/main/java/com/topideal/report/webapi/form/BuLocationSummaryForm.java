package com.topideal.report.webapi.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: Wilson Lau
 * @Date: 2022/1/18 16:20
 * @Description: 库位进销存汇总
 */
public class BuLocationSummaryForm extends PageForm implements Serializable {

    private String token;

    @ApiModelProperty(value = "条形码")
    private String barcode;

    @ApiModelProperty(value = "仓库ID")
    private Long depotId;

    @ApiModelProperty(value = "仓库名称")
    private String depotName;

    @ApiModelProperty(value = "归属月份")
    private String month;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "标准品牌id")
    private Long brandId;

    @ApiModelProperty(value = "标准品牌名称")
    private String brandName;

    @ApiModelProperty(value = "事业部ID")
    private Long buId;

    @ApiModelProperty(value = "事业部名称")
    private String buName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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
}
