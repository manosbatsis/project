package com.topideal.report.webapi.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 获取审批记录分页数据
 */
@ApiModel
public class SettlementPriceExamineForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "条形码", required = false)
    private String barcode;

    @ApiModelProperty(value = "生效日期", required = false)
    private String effectiveDate;

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

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }
}
