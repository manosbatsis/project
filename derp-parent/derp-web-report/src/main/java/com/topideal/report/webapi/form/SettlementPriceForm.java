package com.topideal.report.webapi.form;

import com.topideal.entity.dto.SettlementPriceItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 成本单价新增参数
 */
@ApiModel
public class SettlementPriceForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "id", required = false)
    private Long id;

    @ApiModelProperty(value = "事业部id", required = false)
    private Long buId;

    @ApiModelProperty(value="成本单价详情",required = false)
    private List<SettlementPriceItemDTO> itemList;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public List<SettlementPriceItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<SettlementPriceItemDTO> itemList) {
        this.itemList = itemList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
