package com.topideal.order.webapi.transfer.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 调拨入库
 *
 * @author lian_
 */
@ApiModel
public class TransferOutDepotForm extends PageForm {

    @ApiModelProperty(value = "令牌",required = true)
    private String token;
    @ApiModelProperty(value = "商家id")
    private Long merchantId;
    @ApiModelProperty(value = "调拨入单号")
    private String code;
    @ApiModelProperty(value = "调出仓库id")
    private Long outDepotId;
    @ApiModelProperty(value = "调入仓库id")
    private Long inDepotId;
    @ApiModelProperty(value = "合同号")
    private String contractNo;
    @ApiModelProperty(value = "单据状态")
    private String status;
    @ApiModelProperty(value = "调拨单号")
    private String transferOrderCode;
    @ApiModelProperty(value = "调入起始时间")
    private String transferStartDate;
    @ApiModelProperty(value = "调入结束时间")
    private String transferEndDate;
    @ApiModelProperty(value = "事业部id")
    private Long buId;
    @ApiModelProperty(value = "调拨出库单id")
    private Long id;
    @ApiModelProperty(value = "id集合,多个用英文逗号隔开")
    private String ids;

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

    public Long getOutDepotId() {
        return outDepotId;
    }

    public void setOutDepotId(Long outDepotId) {
        this.outDepotId = outDepotId;
    }

    public Long getInDepotId() {
        return inDepotId;
    }

    public void setInDepotId(Long inDepotId) {
        this.inDepotId = inDepotId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransferOrderCode() {
        return transferOrderCode;
    }

    public void setTransferOrderCode(String transferOrderCode) {
        this.transferOrderCode = transferOrderCode;
    }

    public String getTransferStartDate() {
        return transferStartDate;
    }

    public void setTransferStartDate(String transferStartDate) {
        this.transferStartDate = transferStartDate;
    }

    public String getTransferEndDate() {
        return transferEndDate;
    }

    public void setTransferEndDate(String transferEndDate) {
        this.transferEndDate = transferEndDate;
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

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}

