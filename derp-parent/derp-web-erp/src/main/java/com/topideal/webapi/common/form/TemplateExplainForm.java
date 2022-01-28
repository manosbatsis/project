package com.topideal.webapi.common.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: Wilson Lau
 * @Date: 2021/10/29 11:54
 * @Description: 模板说明form
 */
@ApiModel
public class TemplateExplainForm {

    @ApiModelProperty(value = "令牌", required = true)
    private String token;

    @ApiModelProperty(value = "编码", required = false)
    private String code;

    @ApiModelProperty(value = "名称", required = false)
    private String name;

    @ApiModelProperty(value = "模板类型")
    private String type;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
