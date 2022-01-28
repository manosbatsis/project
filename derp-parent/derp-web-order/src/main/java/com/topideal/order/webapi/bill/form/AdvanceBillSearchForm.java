package com.topideal.order.webapi.bill.form;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 单据搜索
 */
@ApiModel
public class AdvanceBillSearchForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "客户id", required = false)
    private Long customerId;

    @ApiModelProperty(value = "事业部id", required = false)
    private Long buId;

    @ApiModelProperty(value = "结算币种", required = false)
    private String currency;

    @ApiModelProperty(value = "业务单据号,多个以&分开", required = false)
    private String relCodeStr;

    @ApiModelProperty(value = "po号,多个以&分开", required = false)
    private String poNoStr;

    @ApiModelProperty(value = "单据类型 1-销售单", required = true)
    private String orderType;
    private String orderTypeLabel;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getRelCodeStr() {
        return relCodeStr;
    }

    public void setRelCodeStr(String relCodeStr) {
        this.relCodeStr = relCodeStr;
    }

    public String getPoNoStr() {
        return poNoStr;
    }

    public void setPoNoStr(String poNoStr) {
        this.poNoStr = poNoStr;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderTypeLabel() {
        return orderTypeLabel;
    }

    public void setOrderTypeLabel(String orderTypeLabel) {
        this.orderTypeLabel = orderTypeLabel;
        this.orderTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_orderTypeList, orderType);
    }
    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
