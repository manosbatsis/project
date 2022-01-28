package com.topideal.order.webapi.purchase.form;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@ApiModel
public class ProjectQuotaWarningItemForm extends PageForm implements Serializable{

    @ApiModelProperty(value = "票据",required = true)
    private String token ;
    /**
    * 预警ID
    */
    @ApiModelProperty("预警ID")
    private Long waringId;

    @ApiModelProperty("明细类型 1-累计采购冻结金额 2-累计销售已回款金额 3.累计采购已付金额")
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
