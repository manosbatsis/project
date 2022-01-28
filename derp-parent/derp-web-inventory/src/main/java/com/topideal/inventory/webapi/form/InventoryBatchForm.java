package com.topideal.inventory.webapi.form;

import com.topideal.common.system.ibatis.PageModel;
import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 批次库存明细
 *
 * @author lian_
 */
@ApiModel
public class InventoryBatchForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "令牌", required = true)
    private String token;

    @ApiModelProperty(value = "商品货号")
    private String goodsNo;

    @ApiModelProperty(value = "仓库id")
    private Long depotId;

    @ApiModelProperty(value = "公司ID")
    private Long merchantId;
    //商品名称
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "筛选字段效期标识")
    private String validityType;

    @ApiModelProperty(value = "筛选字段效期标识")
    private String validityTypes;

    @ApiModelProperty(value = "条形码")
    private String barcode;

    @ApiModelProperty(value = "品牌id")
    private Long brandId;

    @ApiModelProperty(value = "仓库id, 多个以英文逗号隔开")
    private String depotIds;

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getValidityType() {
        return validityType;
    }

    public void setValidityType(String validityType) {
        this.validityType = validityType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getValidityTypes() {
        return validityTypes;
    }

    public void setValidityTypes(String validityTypes) {
        this.validityTypes = validityTypes;
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

    public String getDepotIds() {
        return depotIds;
    }

    public void setDepotIds(String depotIds) {
        this.depotIds = depotIds;
    }
}

