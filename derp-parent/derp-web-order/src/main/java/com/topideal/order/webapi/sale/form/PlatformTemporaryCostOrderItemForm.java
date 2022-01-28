package com.topideal.order.webapi.sale.form;

import com.topideal.entity.dto.common.PlatformTemporaryCostOrderItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel
public class PlatformTemporaryCostOrderItemForm implements Serializable {
    @ApiModelProperty(value = "令牌",required = true)
    private String token;

    @ApiModelProperty(value = "id",required = true)
    private Long id;

    @ApiModelProperty(value = "令牌",required = true)
    private List<PlatformTemporaryCostOrderItemDTO> itemList;

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

    public List<PlatformTemporaryCostOrderItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<PlatformTemporaryCostOrderItemDTO> itemList) {
        this.itemList = itemList;
    }
}
