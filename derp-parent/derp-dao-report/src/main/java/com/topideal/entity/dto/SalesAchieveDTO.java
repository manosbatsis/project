package com.topideal.entity.dto;

import java.math.BigDecimal;

/**
 * @Author: Wilson Lau
 * @Date: 2021/9/14 9:55
 * @Description:
 */
public class SalesAchieveDTO {

    /**
     * 月目标金额
     */
    private BigDecimal monthTargetAmount;

    /**
     * 月达成金额
     */
    private BigDecimal monthAchieveAmount;

    /**
     * 月完成度
     */
    private BigDecimal monthCompletionPercentage;


    private String month;

    public SalesAchieveDTO() {
    }

    public SalesAchieveDTO(BigDecimal monthTargetAmount, BigDecimal monthAchieveAmount, BigDecimal monthCompletionPercentage){
        this.monthTargetAmount = monthTargetAmount;
        this.monthAchieveAmount = monthAchieveAmount;
        this.monthCompletionPercentage = monthCompletionPercentage;
    }

    public BigDecimal getMonthTargetAmount() {
        return monthTargetAmount;
    }

    public void setMonthTargetAmount(BigDecimal monthTargetAmount) {
        this.monthTargetAmount = monthTargetAmount;
    }

    public BigDecimal getMonthAchieveAmount() {
        return monthAchieveAmount;
    }

    public void setMonthAchieveAmount(BigDecimal monthAchieveAmount) {
        this.monthAchieveAmount = monthAchieveAmount;
    }

    public BigDecimal getMonthCompletionPercentage() {
        return monthCompletionPercentage;
    }

    public void setMonthCompletionPercentage(BigDecimal monthCompletionPercentage) {
        this.monthCompletionPercentage = monthCompletionPercentage;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
