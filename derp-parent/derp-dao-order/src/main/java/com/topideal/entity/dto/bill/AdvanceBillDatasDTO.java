package com.topideal.entity.dto.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class AdvanceBillDatasDTO extends AdvanceBillDataDTO{

    @ApiModelProperty(value = "商家id", required = false)
    private Long merchantId;

    @ApiModelProperty(value = "页面查询的PO单号", required = false)
    private List<String> poNos;

    @ApiModelProperty(value = "页面查询关联业务单号", required = false)
    private List<String> relCodes;

    @ApiModelProperty(value = "当前登录用户绑定的事业部集合", required = false)
    private List<Long> buList;

    @ApiModelProperty(value = "页面查询关联业务单号,过滤", required = false)
    private List<String> checkRelCodes;

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public List<String> getPoNos() {
        return poNos;
    }

    public void setPoNos(List<String> poNos) {
        this.poNos = poNos;
    }

    public List<String> getRelCodes() {
        return relCodes;
    }

    public void setRelCodes(List<String> relCodes) {
        this.relCodes = relCodes;
    }

    public List<String> getCheckRelCodes() {
        return checkRelCodes;
    }

    public void setCheckRelCodes(List<String> checkRelCodes) {
        this.checkRelCodes = checkRelCodes;
    }
}
