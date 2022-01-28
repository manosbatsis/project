package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PlatformTemporaryCostOrderForm extends PageForm {

    @ApiModelProperty(value = "令牌",required = true)
    private String token;

    @ApiModelProperty(value = "外部交易单号")
    private String externalCode;

    @ApiModelProperty(value = "发货日期开始时间")
    private String deliverStartDate;

    @ApiModelProperty(value = "发货日期束时间")
    private String deliverEndDate;

    @ApiModelProperty(value = "电商平台编码")
    private String storePlatformCode;

    @ApiModelProperty(value = "平台费项id")
    private Long platformSettlementId;

    @ApiModelProperty(value = "电商订单号")
    private String orderCode;

    @ApiModelProperty(value = "暂估费用单号")
    private String code;

    @ApiModelProperty(value="ids字符串拼接")
    private String ids;


    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public String getDeliverStartDate() {
        return deliverStartDate;
    }

    public void setDeliverStartDate(String deliverStartDate) {
        this.deliverStartDate = deliverStartDate;
    }

    public String getDeliverEndDate() {
        return deliverEndDate;
    }

    public void setDeliverEndDate(String deliverEndDate) {
        this.deliverEndDate = deliverEndDate;
    }

    public String getStorePlatformCode() {
        return storePlatformCode;
    }

    public void setStorePlatformCode(String storePlatformCode) {
        this.storePlatformCode = storePlatformCode;
    }

    public Long getPlatformSettlementId() {
        return platformSettlementId;
    }

    public void setPlatformSettlementId(Long platformSettlementId) {
        this.platformSettlementId = platformSettlementId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
