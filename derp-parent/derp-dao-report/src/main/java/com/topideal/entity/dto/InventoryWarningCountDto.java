package com.topideal.entity.dto;

/**
 * @Description: 效期预警统计VO
 * @Author: Chen Yiluan
 * @Date: 2019/12/05 10:57
 **/
public class InventoryWarningCountDto {

    //效期区间
    private String effectiveInterval;

    //总数量
    private Integer surplusNum;

    //效期区间标识
    private String effectiveIntervalLabel;

    public String getEffectiveInterval() {
        return effectiveInterval;
    }

    public void setEffectiveInterval(String effectiveInterval) {
        this.effectiveInterval = effectiveInterval;
    }

    public Integer getSurplusNum() {
        return surplusNum;
    }

    public void setSurplusNum(Integer surplusNum) {
        this.surplusNum = surplusNum;
    }

    public String getEffectiveIntervalLabel() {
        return effectiveIntervalLabel;
    }

    public void setEffectiveIntervalLabel(String effectiveIntervalLabel) {
        this.effectiveIntervalLabel = effectiveIntervalLabel;
    }
}
