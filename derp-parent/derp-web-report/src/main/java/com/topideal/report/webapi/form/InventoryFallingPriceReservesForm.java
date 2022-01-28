package com.topideal.report.webapi.form;
import java.math.BigDecimal;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class InventoryFallingPriceReservesForm extends PageForm{

	@ApiModelProperty(value = "令牌")
	private String token;

	@ApiModelProperty(value = "仓库ids 多个用,号隔开")
    private String depotIds ;

	@ApiModelProperty(value = "事业部id 多个用,号隔开")
	private String buIds;

	@ApiModelProperty(value = "商品货号")
    private String goodsNo;

	@ApiModelProperty(value = "跌价准备比例")
    private BigDecimal depreciationReserveProportion;    

	@ApiModelProperty(value = "效期区间1:0＜X≤1/10 ; 2: 1/10＜X≤1/5 ; 3: 1/5＜X≤1/3 ; 4: 1/3＜X≤1/2 ; 5: 1/2＜X≤2/3 ; 6: 2/3以上 ; 7:过期品(为负) ; 8: 残次品")
    private String effectiveInterval;
	
	@ApiModelProperty(value = "效期区间集合")
    private String effectiveIntervals;
    
	@ApiModelProperty(value = "报表月份")
    private String reportMonth;
	
	@ApiModelProperty(value = "id集合(多个用逗号隔开)")
    private String ids;
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getBuIds() {
		return buIds;
	}

	public void setBuIds(String buIds) {
		this.buIds = buIds;
	}

	public String getDepotIds() {
		return depotIds;
	}

	public void setDepotIds(String depotIds) {
		this.depotIds = depotIds;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public BigDecimal getDepreciationReserveProportion() {
		return depreciationReserveProportion;
	}

	public void setDepreciationReserveProportion(BigDecimal depreciationReserveProportion) {
		this.depreciationReserveProportion = depreciationReserveProportion;
	}

	public String getEffectiveInterval() {
		return effectiveInterval;
	}

	public void setEffectiveInterval(String effectiveInterval) {
		this.effectiveInterval = effectiveInterval;
	}

	public String getEffectiveIntervals() {
		return effectiveIntervals;
	}

	public void setEffectiveIntervals(String effectiveIntervals) {
		this.effectiveIntervals = effectiveIntervals;
	}

	public String getReportMonth() {
		return reportMonth;
	}

	public void setReportMonth(String reportMonth) {
		this.reportMonth = reportMonth;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}
