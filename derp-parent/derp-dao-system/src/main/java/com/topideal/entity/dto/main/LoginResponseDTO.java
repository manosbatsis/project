package com.topideal.entity.dto.main;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @ApiModel 实体注解
 * @ApiModelProperty 实体字段注解
 * value 字段说明
 * position 字段排序从0开始
 * required 字段是否必填 true/false,
 * defaultValue 默认值
 * */
@ApiModel
public class LoginResponseDTO {

    @ApiModelProperty(value = "令牌",position = 0)
    private String token;
    @ApiModelProperty(value = "用户绑定公司",position = 1)
    private List<MerchantMinDTO> merchantList;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public List<MerchantMinDTO> getMerchantList() {
        return merchantList;
    }

    public void setMerchantList(List<MerchantMinDTO> merchantList) {
        this.merchantList = merchantList;
    }
}

