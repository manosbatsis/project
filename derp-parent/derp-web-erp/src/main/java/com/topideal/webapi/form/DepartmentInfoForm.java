package com.topideal.webapi.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel
public class DepartmentInfoForm extends PageForm implements Serializable {
    @ApiModelProperty(value = "令牌",required = true)
    private String token;

    @ApiModelProperty(value = "id",required = false)
    private Long id;

    @ApiModelProperty(value = "编码",required = false)
    private String code;

    @ApiModelProperty(value = "名称",required = false)
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
