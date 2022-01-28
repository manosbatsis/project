package com.topideal.webapi.form;

import com.topideal.entity.dto.main.ReminderEmailRelDTO;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

public class ReminderEmailConfigForm implements Serializable {
    @ApiModelProperty(value = "令牌",required = true)
    private String token;
    @ApiModelProperty(value = "id 修改必填 新增非必填")
    private Long id;
    @ApiModelProperty(value = "商家ID")
    private Long merchantId;
    @ApiModelProperty(value = "事业部ID")
    private Long buId;
    @ApiModelProperty(value = "业务模块类型 1:应收 2:采购 3:销售")
    private String businessModuleType;
    @ApiModelProperty(value="邮件提醒表体明细")
    private List<ReminderEmailRelDTO> items;

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

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getBusinessModuleType() {
        return businessModuleType;
    }

    public void setBusinessModuleType(String businessModuleType) {
        this.businessModuleType = businessModuleType;
    }

    public List<ReminderEmailRelDTO> getItems() {
        return items;
    }

    public void setItems(List<ReminderEmailRelDTO> items) {
        this.items = items;
    }
}
