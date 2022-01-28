package com.topideal.order.webapi.bill.form;

import com.topideal.common.system.webapi.PageForm;
import com.topideal.entity.dto.bill.ReceiveBillCostItemDTO;
import com.topideal.entity.dto.bill.ReceiveBillItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 应收账单保存明细
 * @Author: Chen Yiluan
 * @Date: 2021/06/19 19:03
 **/
@ApiModel
public class ReceiveBillSubmitForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "账单id", required = false)
    private Long billId;

    @ApiModelProperty(value = "核销账单id", required = false)
    private String advanceIds;

    @ApiModelProperty(value = "费用明细集合", required = false)
    private List<ReceiveBillCostItemDTO> costItemList;

    @ApiModelProperty(value = "应收明细集合", required = false)
    private List<ReceiveBillItemDTO> itemList;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getAdvanceIds() {
        return advanceIds;
    }

    public void setAdvanceIds(String advanceIds) {
        this.advanceIds = advanceIds;
    }

    public List<ReceiveBillCostItemDTO> getCostItemList() {
        return costItemList;
    }

    public void setCostItemList(List<ReceiveBillCostItemDTO> costItemList) {
        this.costItemList = costItemList;
    }

    public List<ReceiveBillItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<ReceiveBillItemDTO> itemList) {
        this.itemList = itemList;
    }
}
