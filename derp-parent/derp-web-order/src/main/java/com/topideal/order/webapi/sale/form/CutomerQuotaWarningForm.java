package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModelProperty;

public class CutomerQuotaWarningForm extends PageForm {

    @ApiModelProperty(value = "令牌",required = true)
    private String token;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "事业部ID")
    private Long buId;

    @ApiModelProperty(value = "客户ID")
    private Long customerId;

    @ApiModelProperty(value = "主键ids集合，用逗号隔开")
    private String ids;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
