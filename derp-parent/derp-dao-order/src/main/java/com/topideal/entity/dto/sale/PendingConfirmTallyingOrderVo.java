package com.topideal.entity.dto.sale;

import java.sql.Timestamp;

import com.topideal.common.constant.DERP_SYS;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 待确认理货单vo
 * @Author: Chen Yiluan
 * @Date: 2019/12/04 19:18
 **/

@ApiModel
public class PendingConfirmTallyingOrderVo {

	@ApiModelProperty(value = "订单ID", required = false)
    private Long id;

    //订单编码
	@ApiModelProperty(value = "订单编码", required = false)
    private String code;

    //仓库
	@ApiModelProperty(value = "仓库名称", required = false)
    private String depotName;

	@ApiModelProperty(value = "理货时间", required = false)
    private Timestamp tallyingDate;

    //订单类型 1-采购  2-调拨 3-销售退理货
	@ApiModelProperty(value = "订单类型 1-采购  2-调拨 3-销售退理货", required = false)
    private String orderType;
    private String orderTypeLabel;

    @ApiModelProperty(value = "事业部")
    private String buName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public Timestamp getTallyingDate() {
        return tallyingDate;
    }

    public void setTallyingDate(Timestamp tallyingDate) {
        this.tallyingDate = tallyingDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
        this.orderTypeLabel = DERP_SYS.getLabelByKey(DERP_SYS.tallyOrderType, orderType);
    }

    public String getOrderTypeLabel() {
        return orderTypeLabel;
    }

    public void setOrderTypeLabel(String orderTypeLabel) {
        this.orderTypeLabel = orderTypeLabel;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }
}
