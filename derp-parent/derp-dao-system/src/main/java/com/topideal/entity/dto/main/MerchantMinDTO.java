package com.topideal.entity.dto.main;

import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ApiModel 实体注解
 * @ApiModelProperty 实体字段注解
 * value 字段说明
 * position 字段排序从0开始
 * required 字段是否必填 true/false,
 * defaultValue 默认值
 * */
@ApiModel(value = "公司")
public class MerchantMinDTO implements Serializable{

     @ApiModelProperty(value = "公司Id 必填",position = 0)
     private Long id;
     @ApiModelProperty(value = "商家编码 必填",position = 1)
     private String code;
     @ApiModelProperty(value = "商家简称 必填",position = 2)
     private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

