package com.topideal.order.webapi.bill.form;

import com.topideal.entity.dto.bill.AdvanceBillDataDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 校验业务单据是否是同客户+同事业部+同币种
 */
@ApiModel
public class AdvanceBillCheckForm  implements Serializable {

    @ApiModelProperty(value = "token", required = false)
    private String token;

    @ApiModelProperty(value = "业务单据List",required = false)
    private List<AdvanceBillDataDTO> list;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<AdvanceBillDataDTO> getList() {
        return list;
    }

    public void setList(List<AdvanceBillDataDTO> list) {
        this.list = list;
    }
}
