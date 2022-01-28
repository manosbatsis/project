package com.topideal.webapi.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: Wilson Lau
 * @Date: 2021/10/29 12:01
 * @Description:
 */
@ApiModel
public class TemplateExplainDTO {

    @ApiModelProperty(value = "序号")
    private Integer no;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "模板类型")
    private String type;

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
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

    public TemplateExplainDTO() {
    }

    public TemplateExplainDTO(Integer no, String code, String name, String type) {
        this.no = no;
        this.code = code;
        this.name = name;
        this.type = type;
    }
}
