package com.topideal.order.webapi.bill.form;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.entity.dto.bill.AdvanceBillItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel
public class AdvanceBillSaveForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "id", required = false)
    private Long id;

    @ApiModelProperty(value = "预收账单号", required = false)
    private String code;

    @ApiModelProperty(value = "商家id", required = false)
    private Long merchantId;

    @ApiModelProperty(value = "商家名称", required = false)
    private String merchantName;

    @ApiModelProperty(value = "客户id", required = false)
    private Long customerId;

    @ApiModelProperty(value = "客户名称", required = false)
    private String customerName;

    @ApiModelProperty(value = "事业部id", required = false)
    private Long buId;

    @ApiModelProperty(value = "事业部名称", required = false)
    private String buName;

    @ApiModelProperty(value = "结算币种", required = false)
    private String currency;

    @ApiModelProperty(value = "单据类型", required = true)
    private String orderType;
    private String orderTypeLabel;

    @ApiModelProperty(value = "po单号", required = false)
    private String poNo;

    @ApiModelProperty(value = "业务单据号", required = false)
    private String relCode;

    @ApiModelProperty(value = "预收账单明细表体", required = false)
    private List<AdvanceBillItemDTO> itemList;

    @ApiModelProperty(value = "是否是提交状态 0：是 1否", required = false)
    private String type;

    @ApiModelProperty(value = "费项id", required = false)
    private Long projectId;

    @ApiModelProperty(value = "费项名称", required = false)
    private String projectName;

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

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
        this.orderTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.advanceBill_orderTypeList, orderType);
    }

    public String getOrderTypeLabel() {
        return orderTypeLabel;
    }

    public void setOrderTypeLabel(String orderTypeLabel) {
        this.orderTypeLabel = orderTypeLabel;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getRelCode() {
        return relCode;
    }

    public void setRelCode(String relCode) {
        this.relCode = relCode;
    }

    public List<AdvanceBillItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<AdvanceBillItemDTO> itemList) {
        this.itemList = itemList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
