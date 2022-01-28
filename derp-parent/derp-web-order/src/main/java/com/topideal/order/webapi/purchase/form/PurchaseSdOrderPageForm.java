package com.topideal.order.webapi.purchase.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PurchaseSdOrderPageForm extends PageForm {

    @ApiModelProperty(value="令牌", required = true)
    private String token ;

    @ApiModelProperty("采购SD单ID")
    private Long id;

    @ApiModelProperty("采购SD单编码")
    private String code;

    /**
     * 采购订单编号
     */
    @ApiModelProperty("采购订单编号")
    private String purchaseCode;
    /**
     * 商家ID
     */
    @ApiModelProperty("商家ID")
    private Long merchantId;
    /**
     * 商家名称
     */
    @ApiModelProperty("商家名称")
    private String merchantName;
    /**
     * 事业部名称
     */
    @ApiModelProperty("事业部名称")
    private String buName;
    /**
     * 事业部id
     */
    @ApiModelProperty("事业部id")
    private Long buId;

    @ApiModelProperty("PO号，多个以‘&’逗号隔开")
    private String poNos;

    @ApiModelProperty("采购订单号，多个以‘&’逗号隔开")
    private String purchaseCodes;

    @ApiModelProperty("入库时间字段串")
    private String inboundDateStr;
    @ApiModelProperty("入库开始时间字段串")
    private String inboundStartDateStr;
    @ApiModelProperty("入库结束时间字段串")
    private String inboundEndDateStr;

    @ApiModelProperty("采购SD单id,多个以‘，’隔开")
    private String ids;

    @ApiModelProperty("SD单类型")
    private String type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPurchaseCode() {
        return purchaseCode;
    }

    public void setPurchaseCode(String purchaseCode) {
        this.purchaseCode = purchaseCode;
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

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getPoNos() {
        return poNos;
    }

    public void setPoNos(String poNos) {
        this.poNos = poNos;
    }

    public String getPurchaseCodes() {
        return purchaseCodes;
    }

    public void setPurchaseCodes(String purchaseCodes) {
        this.purchaseCodes = purchaseCodes;
    }

    public String getInboundDateStr() {
        return inboundDateStr;
    }

    public void setInboundDateStr(String inboundDateStr) {
        this.inboundDateStr = inboundDateStr;
    }

    public String getInboundStartDateStr() {
        return inboundStartDateStr;
    }

    public void setInboundStartDateStr(String inboundStartDateStr) {
        this.inboundStartDateStr = inboundStartDateStr;
    }

    public String getInboundEndDateStr() {
        return inboundEndDateStr;
    }

    public void setInboundEndDateStr(String inboundEndDateStr) {
        this.inboundEndDateStr = inboundEndDateStr;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
