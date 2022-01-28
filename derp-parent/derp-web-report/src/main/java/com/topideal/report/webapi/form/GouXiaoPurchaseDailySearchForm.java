package com.topideal.report.webapi.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 购销采购日报搜索参数
 */
@ApiModel
public class GouXiaoPurchaseDailySearchForm extends PageForm implements Serializable {
    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "id", required = false)
    private Long id;

    @ApiModelProperty(value = "分类id", required = false)
    private Long productTypeId;

    @ApiModelProperty(value = "品牌id", required = false)
    private Long brandId;

    @ApiModelProperty(value = "客户id", required = false)
    private Long customerId;

    @ApiModelProperty(value = "报表归属日期 yyyy-MM-dd", required = false)
    private String reportDate;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }
}
