package com.topideal.order.webapi.bill.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel
public class ToCReceiveBillCostSaveForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "id", required = true)
    private Long id;

    @ApiModelProperty(value = "补扣款表体", required = true)
    private List<ToCReceiveBillCostItemForm> costItem;

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

    public List<ToCReceiveBillCostItemForm> getCostItem() {
        return costItem;
    }

    public void setCostItem(List<ToCReceiveBillCostItemForm> costItem) {
        this.costItem = costItem;
    }
}
