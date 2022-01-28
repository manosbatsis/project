package com.topideal.report.webapi.form;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 成本单价列表搜索参数
 */
@ApiModel
public class SettlementPriceSearchForm extends PageForm implements Serializable {
    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "条形码", required = false)
    private String barcode;

    @ApiModelProperty(value = "商品名称", required = false)
    private String goodsName;

    @ApiModelProperty(value = "品牌id", required = false)
    private Long brandId;

    @ApiModelProperty(value = "是否组合品  1 是 0 否", required = false)
    private String isGroup;

    @ApiModelProperty(value = "状态 001:待审核 , 013:待提交，021:已作废，032:已生效 ，033审核不通过", required = false)
    private String status;
    private String statusLabel ;


    @ApiModelProperty(value = "事业部id", required = false)
    private Long buId;


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

    public String getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(String isGroup) {
        this.isGroup = isGroup;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.statusLabel = DERP_REPORT.getLabelByKey(DERP_REPORT.settlementPrice_statusList, status) ;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }
}
