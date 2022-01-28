package com.topideal.inventory.webapi.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
@ApiModel
public class InventorySummaryForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "令牌",required = true)
    private String token;

    @ApiModelProperty(value="商家ID")
    private Long merchantId;

    @ApiModelProperty(value="仓库ID")
    private Long depotId;

    @ApiModelProperty(value="商品货号")
    private String goodsNo;

    @ApiModelProperty(value="当前月份")
    private String  currentMonth;  //当前月


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

	public String getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(String currentMonth) {
		this.currentMonth = currentMonth;
	}


}
