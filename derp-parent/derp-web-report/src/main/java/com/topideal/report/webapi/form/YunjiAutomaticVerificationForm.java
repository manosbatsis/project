package com.topideal.report.webapi.form;

import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel
public class YunjiAutomaticVerificationForm extends PageModel implements Serializable {

    @ApiModelProperty(value = "令牌")
    private String token;

    @ApiModelProperty(value = "账单月份")
    private String month;

    @ApiModelProperty(value = "公司ID")
    private Long merchantId;


    @ApiModelProperty(value = "结算单号")
    private String settleId;


    @ApiModelProperty(value = "云集商品编码")
    private String skuNo;

    @ApiModelProperty(value = "只看差异")
    private String checkDifference;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getSettleId() {
        return settleId;
    }

    public void setSettleId(String settleId) {
        this.settleId = settleId;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public String getCheckDifference() {
        return checkDifference;
    }

    public void setCheckDifference(String checkDifference) {
        this.checkDifference = checkDifference;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
