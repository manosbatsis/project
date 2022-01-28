package com.topideal.order.webapi.common.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SdTypeConfigForm extends PageForm {

    @ApiModelProperty(value = "令牌", required = true)
    private String token;
    
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "SD类型名称")
    private String sdTypeName;

    @ApiModelProperty(value = "简称")
    private String sdSimpleName;

    @ApiModelProperty(value = "类型 1-单比例 2-多比例")
    private String type;

    @ApiModelProperty(value = "状态 1- 启用 2-禁用")
    private String status;

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

    public String getSdTypeName() {
        return sdTypeName;
    }

    public void setSdTypeName(String sdTypeName) {
        this.sdTypeName = sdTypeName;
    }

    public String getSdSimpleName() {
        return sdSimpleName;
    }

    public void setSdSimpleName(String sdSimpleName) {
        this.sdSimpleName = sdSimpleName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
