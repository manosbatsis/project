package com.topideal.webapi.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 光区配置表form
 */
public class CustomsAreaConfigForm extends PageForm implements Serializable {
    @ApiModelProperty(value = "令牌",required = true)
    private String token;
    @ApiModelProperty(value = "关区代码")
    private String code;
    @ApiModelProperty(value = "平台关区")
    private String name;

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
}
