package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class CutomerQuotaWarningItemForm extends PageForm {

    @ApiModelProperty(value = "令牌",required = true)
    private String token;

    @ApiModelProperty(value = "预警ID")
    private Long waringId;

    @ApiModelProperty(value = "明细类型 1-销售在途 2-待确认 3-待开票 4-待回款 ")
    private String type;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getWaringId() {
        return waringId;
    }

    public void setWaringId(Long waringId) {
        this.waringId = waringId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
