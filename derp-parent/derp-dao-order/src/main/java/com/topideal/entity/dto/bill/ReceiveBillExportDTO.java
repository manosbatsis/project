package com.topideal.entity.dto.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel
public class ReceiveBillExportDTO implements Serializable {

    @ApiModelProperty(value = "应收明细")
    private List<ReceiveBillItemDTO> itemList;

    @ApiModelProperty(value = "费用明细")
    private List<ReceiveBillCostItemDTO> costItemList;

    public List<ReceiveBillItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<ReceiveBillItemDTO> itemList) {
        this.itemList = itemList;
    }

    public List<ReceiveBillCostItemDTO> getCostItemList() {
        return costItemList;
    }

    public void setCostItemList(List<ReceiveBillCostItemDTO> costItemList) {
        this.costItemList = costItemList;
    }
}
