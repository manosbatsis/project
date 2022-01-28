package com.topideal.report.webapi.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel
public class SaleTargetAchievementForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "类型 1-销售类型达成 2-电商平台达成 3-店铺计划达成", required = true)
    private String type;

    @ApiModelProperty("事业部ID")
    private Long buId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("标准条码")
    private String commbarcode;

    @ApiModelProperty("月份")
    private String month;
    @ApiModelProperty("季度")
    private String season ;
    @ApiModelProperty("年份")
    private String year ;
    @ApiModelProperty("/是否同步数据 true-是 ，其他-否")
    private String syn;

    public String getSyn() {
        return syn;
    }

    public void setSyn(String syn) {
        this.syn = syn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
    
}
