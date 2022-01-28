package com.topideal.entity.vo.platformdata;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/6 14:48
 * @Description: 爬虫结算单明细
 */
public class PlatformSettleDetailModel extends PageModel implements Serializable {

    private Long id;

    private Long settleId;

    private String onlineOrderNo;

    private String settlementFee;

    private BigDecimal settlementAmount;

    private BigDecimal rmbAmount;

    private BigDecimal exchangeRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSettleId() {
        return settleId;
    }

    public void setSettleId(Long settleId) {
        this.settleId = settleId;
    }

    public String getOnlineOrderNo() {
        return onlineOrderNo;
    }

    public void setOnlineOrderNo(String onlineOrderNo) {
        this.onlineOrderNo = onlineOrderNo;
    }

    public String getSettlementFee() {
        return settlementFee;
    }

    public void setSettlementFee(String settlementFee) {
        this.settlementFee = settlementFee;
    }

    public BigDecimal getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(BigDecimal settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public BigDecimal getRmbAmount() {
        return rmbAmount;
    }

    public void setRmbAmount(BigDecimal rmbAmount) {
        this.rmbAmount = rmbAmount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
