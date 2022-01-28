package com.topideal.entity.dto.sale;

import java.sql.Timestamp;

import com.topideal.common.constant.DERP_SYS;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 待审核订单vo
 * @Author: Chen Yiluan
 * @Date: 2019/12/04 19:58
 **/
@ApiModel
public class PendingCheckOrderVo {

	@ApiModelProperty(value="订单ID")
    private Long id;

    //订单编码
	@ApiModelProperty(value="订单编码")
    private String code;

    //出仓仓库
	@ApiModelProperty(value="出仓仓库")
    private String depotName;

    //创建时间
	@ApiModelProperty(value="创建时间")
    private Timestamp createDate;

    //订单类型
	@ApiModelProperty(value="订单类型")
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

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
        this.orderTypeLabel = DERP_SYS.getLabelByKey(DERP_SYS.saleOrderTypeList, orderType);
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
