package com.topideal.webapi.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class MerchantDepotBuForm{

    @ApiModelProperty(value = "令牌",required = true)
    private String token;

    @ApiModelProperty(value = "公司id")
    private String merchantId;

    @ApiModelProperty(value = "仓库1 id")
    private String depotId1;

    @ApiModelProperty(value = "仓库2 id")
    private String depotId2;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getDepotId1() {
        return depotId1;
    }

    public void setDepotId1(String depotId1) {
        this.depotId1 = depotId1;
    }

    public String getDepotId2() {
        return depotId2;
    }

    public void setDepotId2(String depotId2) {
        this.depotId2 = depotId2;
    }
}
