package com.topideal.common.system.webapi;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PageForm {
    @ApiModelProperty(value = "每页数量")
    private Integer pageSize=10;//每一页记录数
    @ApiModelProperty(value = "开始位置")
    private Integer begin=0;//开始位置

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getBegin() {
        return begin;
    }

    public void setBegin(Integer begin) {
        this.begin = begin;
    }
}
