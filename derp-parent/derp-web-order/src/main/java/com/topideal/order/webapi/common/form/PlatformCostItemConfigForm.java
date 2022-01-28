package com.topideal.order.webapi.common.form;

import com.topideal.entity.dto.common.PlatformCostConfigItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class PlatformCostItemConfigForm {

    @ApiModelProperty(value = "令牌", required = true)
    private String token;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "公司ID")
    private Long merchantId;

    @ApiModelProperty(value = "公司名称")
    private String merchantName;

    @ApiModelProperty(value = "事业部ID")
    private Long buId;

    @ApiModelProperty(value = "事业部名称")
    private String buName;

    @ApiModelProperty(value = "客户ID")
    private Long customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "生效月份字符串")
    private String effectiveDateStr;

    @ApiModelProperty(value = "失效月份字符串")
    private String expirationDateStr;

    @ApiModelProperty(value = "电商平台编码：第e仓：100011111；京东自营：100044998；京东POP-拜耳：10002075；菜鸟：1000000310；拼多多：1000004790")
    private String storePlatformCode;

    @ApiModelProperty(value = "电商平台名称")
    private String storePlatformName;

    @ApiModelProperty(value = "关联配置集合")
    private List<PlatformCostConfigItemDTO> itemList;

    @ApiModelProperty(value = "操作 1-保存，2-审核")
    private String operate;

    public String getStorePlatformCode() {
        return storePlatformCode;
    }

    public void setStorePlatformCode(String storePlatformCode) {
        this.storePlatformCode = storePlatformCode;
    }

    public String getStorePlatformName() {
        return storePlatformName;
    }

    public void setStorePlatformName(String storePlatformName) {
        this.storePlatformName = storePlatformName;
    }

    public List<PlatformCostConfigItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<PlatformCostConfigItemDTO> itemList) {
        this.itemList = itemList;
    }

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

    public String getEffectiveDateStr() {
        return effectiveDateStr;
    }

    public void setEffectiveDateStr(String effectiveDateStr) {
        this.effectiveDateStr = effectiveDateStr;
    }

    public String getExpirationDateStr() {
        return expirationDateStr;
    }

    public void setExpirationDateStr(String expirationDateStr) {
        this.expirationDateStr = expirationDateStr;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }


}
