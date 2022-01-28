package com.topideal.order.webapi.bill.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel
public class TobTemporaryReceiveBillForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "事业部ID", required = false)
    private Long buId;

    @ApiModelProperty(value = "客户ID", required = false)
    private Long customerId;

    @ApiModelProperty(value = "销售类型", required = false)
    private String saleType;

    @ApiModelProperty(value = "po号", required = false)
    private String poNo;

    @ApiModelProperty(value = "上架月份", required = false)
    private String shelfMonth;

    @ApiModelProperty(value = "应收结算状态", required = false)
    private String status;

    @ApiModelProperty(value = "id集合,多个用英文逗号隔开")
    private String ids;

    @ApiModelProperty(value = "To B暂估核销id")
    private Long receiveId ;

    /**
     * 返利结算状态 1-已上架未结算 2-部分结算 5-已结算
     */
    @ApiModelProperty(value = "返利结算状态")
    private String rebateStatus;

    /**
     * 销售订单号
     */
    @ApiModelProperty(value = "销售订单号")
    private String orderCode;
    /**
     * 上架单号
     */
    @ApiModelProperty(value = "上架单号")
    private String shelfCode;
    /**
     * 销售币种
     */
    @ApiModelProperty(value = "销售币种")
    private String currency;

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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getShelfMonth() {
        return shelfMonth;
    }

    public void setShelfMonth(String shelfMonth) {
        this.shelfMonth = shelfMonth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Long getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(Long receiveId) {
        this.receiveId = receiveId;
    }

    public String getRebateStatus() {
        return rebateStatus;
    }

    public void setRebateStatus(String rebateStatus) {
        this.rebateStatus = rebateStatus;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getShelfCode() {
        return shelfCode;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
