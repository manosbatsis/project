package com.topideal.order.webapi.common.form;

import com.topideal.common.constant.DERP_ORDER;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

public class SettlementConfigBeanForm implements Serializable {
    @ApiModelProperty(value = "token", required = false)
    private String token;
    @ApiModelProperty(value = "customerId", required = false)
    private Long customerId;
    @ApiModelProperty(value = "parentId", required = false)
    private Long parentId;
    @ApiModelProperty(value = "level", required = false)
    private String level;
    @ApiModelProperty(value = "适用类型:1.To B 2.To C", required = false)
    private String type;
    @ApiModelProperty(value = "适用类型:1.To B 2.To C", required = false)
    private String typeLabel;
    @ApiModelProperty(value = "适用模块:1.应付 2.应收 3.预付 4.预收", required = false)
    private String moduleType;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        this.typeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.settlementConfig_typeList, type) ;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }
}
