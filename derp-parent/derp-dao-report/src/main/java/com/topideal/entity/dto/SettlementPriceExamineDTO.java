package com.topideal.entity.dto;

import java.math.BigDecimal;

public class SettlementPriceExamineDTO extends SettlementPriceDTO{

	/**
	 *  调整前价格
	 */
	private BigDecimal historyPrice ;
	
	/**
	 * 调价原因
	 */
	private String adjustPriceResult ;

	public BigDecimal getHistoryPrice() {
		return historyPrice;
	}

	public void setHistoryPrice(BigDecimal historyPrice) {
		this.historyPrice = historyPrice;
	}

	public String getAdjustPriceResult() {
		return adjustPriceResult;
	}

	public void setAdjustPriceResult(String adjustPriceResult) {
		this.adjustPriceResult = adjustPriceResult;
	}
	
	
}
