package com.topideal.order.webapi.common.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PlatformSettlementConfigForm extends PageForm {

    @ApiModelProperty(value = "令牌", required = true)
    private String token;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "id集合")
    private String ids;

    @ApiModelProperty(value = "平台名称")
    private String storePlatformCode;

    @ApiModelProperty(value = "平台费用名称")
    private String name;

    @ApiModelProperty(value = "本级费项名称")
    private Long settlementConfigId;

    @ApiModelProperty(value = "NC收支费项")
    private Long ncPaymentId;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "旧名称")
    private String oldName;

    @ApiModelProperty(value = "电商平台名称")
    private String storePlatformName;

    @ApiModelProperty(value = "费项配置表名称")
    private String settlementConfigName;

    @ApiModelProperty(value = "NC收支费项名称")
    private String ncPaymentName;

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

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getStorePlatformCode() {
        return storePlatformCode;
    }

    public void setStorePlatformCode(String storePlatformCode) {
        this.storePlatformCode = storePlatformCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSettlementConfigId() {
        return settlementConfigId;
    }

    public void setSettlementConfigId(Long settlementConfigId) {
        this.settlementConfigId = settlementConfigId;
    }

    public Long getNcPaymentId() {
        return ncPaymentId;
    }

    public void setNcPaymentId(Long ncPaymentId) {
        this.ncPaymentId = ncPaymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getStorePlatformName() {
        return storePlatformName;
    }

    public void setStorePlatformName(String storePlatformName) {
        this.storePlatformName = storePlatformName;
    }

    public String getSettlementConfigName() {
        return settlementConfigName;
    }

    public void setSettlementConfigName(String settlementConfigName) {
        this.settlementConfigName = settlementConfigName;
    }

    public String getNcPaymentName() {
        return ncPaymentName;
    }

    public void setNcPaymentName(String ncPaymentName) {
        this.ncPaymentName = ncPaymentName;
    }
}
