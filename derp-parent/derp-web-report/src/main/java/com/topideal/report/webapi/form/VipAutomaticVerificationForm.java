package com.topideal.report.webapi.form;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
public class VipAutomaticVerificationForm extends PageForm {

    @ApiModelProperty(value = "令牌")
    private String token;

    @ApiModelProperty(value = "月份")
    private String month;

    @ApiModelProperty(value = "公司id")
    private Long merchantId;

    @ApiModelProperty(value = "po号")
    private String poNo;

    @ApiModelProperty(value = "爬虫账单号")
    private String crawlerNo;

    @ApiModelProperty(value = "校验结果：1-已对平、0-未对平")
    private String verificationResult;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getCrawlerNo() {
        return crawlerNo;
    }

    public void setCrawlerNo(String crawlerNo) {
        this.crawlerNo = crawlerNo;
    }

    public String getVerificationResult() {
        return verificationResult;
    }

    public void setVerificationResult(String verificationResult) {
        this.verificationResult = verificationResult;
    }
}
