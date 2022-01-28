package com.topideal.entity.dto.sale;

import com.topideal.common.constant.DERP_SYS;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 待上架销售订单
 * @Author: Chen Yiluan
 * @Date: 2019/12/04 18:21
 **/

@ApiModel
public class PendingShelfSaleOrderVo {

	@ApiModelProperty(value="订单id")
    private Long id;

    //订单编码
	@ApiModelProperty(value="订单编码")
    private String code;

    //出仓仓库
	@ApiModelProperty(value="出仓仓库")
    private String depotName;

    //客户名称
	@ApiModelProperty(value="客户名称")
    private String customerName;

    //销售类型 1-购销 2-代销
	@ApiModelProperty(value="销售类型 1-购销 2-代销")
    private String saleType;
    private String saleTypeLabel;

    //订单状态
	@ApiModelProperty(value="订单状态")
    private String status;

    //订单类型 1-销售订单 2-销售出库单
	@ApiModelProperty(value="订单类型 1-销售订单 2-销售出库单")
    private String orderType;

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
        this.saleTypeLabel = DERP_SYS.getLabelByKey(DERP_SYS.shelfSaleType, saleType);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSaleTypeLabel() {
        return saleTypeLabel;
    }

    public void setSaleTypeLabel(String saleTypeLabel) {
        this.saleTypeLabel = saleTypeLabel;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }
}
