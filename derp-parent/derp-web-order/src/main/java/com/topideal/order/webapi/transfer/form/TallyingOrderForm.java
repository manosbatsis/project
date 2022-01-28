package com.topideal.order.webapi.transfer.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 调拨理货单
 * @author lian_
 */
@ApiModel
public class TallyingOrderForm extends PageForm {

    @ApiModelProperty(value = "令牌",required = true)
    private String token;
    @ApiModelProperty(value = "商家id")
    private Long merchantId;
    @ApiModelProperty(value = "理货单号")
    private String code;
    @ApiModelProperty(value = "调入仓库id")
    private Long depotId;
    @ApiModelProperty(value = "合同号")
    private String contractNo;
    @ApiModelProperty(value = "理货单状态")
    private String state;
    @ApiModelProperty(value = "调拨单号")
    private String orderCode;
    @ApiModelProperty(value = "理货起始时间")
    private String tallyingStartDate;
    @ApiModelProperty(value = "理货结束时间")
    private String tallyingEndDate;
    @ApiModelProperty(value = "事业部id")
    private Long buId;
    @ApiModelProperty(value = "理货单id")
    private Long id;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String status) {
        this.state = status;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getTallyingStartDate() {
        return tallyingStartDate;
    }

    public void setTallyingStartDate(String tallyingStartDate) {
        this.tallyingStartDate = tallyingStartDate;
    }

    public String getTallyingEndDate() {
        return tallyingEndDate;
    }

    public void setTallyingEndDate(String tallyingEndDate) {
        this.tallyingEndDate = tallyingEndDate;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

