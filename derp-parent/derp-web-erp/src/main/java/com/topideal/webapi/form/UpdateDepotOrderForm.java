package com.topideal.webapi.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 不同单据切仓商品修改請求参数
 */
public class UpdateDepotOrderForm extends PageForm implements Serializable {

	@ApiModelProperty(value = "令牌",required = true)
	private String token;

    @ApiModelProperty(value = "仓库id")
    private Long depotId;

    @ApiModelProperty(value = "单据类型 1-采购 2-销售 3-销售退 4-调拨")
    private String orderType;

    @ApiModelProperty(value = "表体数据")
    List<UpdateDepotOrderItemForm> itemList;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public List<UpdateDepotOrderItemForm> getItemList() {
        return itemList;
    }

    public void setItemList(List<UpdateDepotOrderItemForm> itemList) {
        this.itemList = itemList;
    }

}
