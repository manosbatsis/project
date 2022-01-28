package com.topideal.order.webapi.bill.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 列表页搜索参数
 */
@ApiModel
public class ReceiveBillAddForm implements Serializable {

    @ApiModelProperty(value = "客户ID", required = false)
    private Long customerId;

    @ApiModelProperty(value = "事业部ID", required = false)
    private Long buId;

    @ApiModelProperty(value = "关联业务单号")
    private String relCode;

    @ApiModelProperty(value = "单据类型 1-上架单 2-账单出库单 3-预售单 4-销售订单 5-采购SD单 6-融资申请单")
    private String orderType;

    @ApiModelProperty(value = "是否增加残损金额 0-是 1-否")
    private String isAddWorn;

    @ApiModelProperty(value = "币种")
    private String currency;


    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getRelCode() {
        return relCode;
    }

    public void setRelCode(String relCode) {
        this.relCode = relCode;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getIsAddWorn() {
        return isAddWorn;
    }

    public void setIsAddWorn(String isAddWorn) {
        this.isAddWorn = isAddWorn;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
