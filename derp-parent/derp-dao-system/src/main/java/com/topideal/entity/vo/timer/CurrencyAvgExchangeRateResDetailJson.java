package com.topideal.entity.vo.timer;

import java.io.Serializable;

/**
 * @Description: 获取平均汇率接口汇率明细实体
 * @Date: 2020/04/27 14:26
 **/
public class CurrencyAvgExchangeRateResDetailJson implements Serializable {

    //数据来源
    private String dataSource;

    //本币 必填
    private String localCurrency;

    //外币 必填
    private String foreignCurrency;

    //买入价月均价
    private Double averageBuyPrice;

    //卖出价月均价
    private Double averageSellPrice;

    //中间价月均价
    private Double averageMiddlePrice;

    //买入价累计均价
    private Double cumAverageBuyPrice;

    //卖出价累计均价
    private Double cumAverageSellPrice;

    //中间价累计均价
    private Double cumAverageMiddlePrice;

    //汇率年份
    private String rateYear;

    //汇率月份
    private String rateMonth;

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getLocalCurrency() {
        return localCurrency;
    }

    public void setLocalCurrency(String localCurrency) {
        this.localCurrency = localCurrency;
    }

    public String getForeignCurrency() {
        return foreignCurrency;
    }

    public void setForeignCurrency(String foreignCurrency) {
        this.foreignCurrency = foreignCurrency;
    }

    public Double getAverageBuyPrice() {
        return averageBuyPrice;
    }

    public void setAverageBuyPrice(Double averageBuyPrice) {
        this.averageBuyPrice = averageBuyPrice;
    }

    public Double getAverageSellPrice() {
        return averageSellPrice;
    }

    public void setAverageSellPrice(Double averageSellPrice) {
        this.averageSellPrice = averageSellPrice;
    }

    public Double getAverageMiddlePrice() {
        return averageMiddlePrice;
    }

    public void setAverageMiddlePrice(Double averageMiddlePrice) {
        this.averageMiddlePrice = averageMiddlePrice;
    }

    public Double getCumAverageBuyPrice() {
        return cumAverageBuyPrice;
    }

    public void setCumAverageBuyPrice(Double cumAverageBuyPrice) {
        this.cumAverageBuyPrice = cumAverageBuyPrice;
    }

    public Double getCumAverageSellPrice() {
        return cumAverageSellPrice;
    }

    public void setCumAverageSellPrice(Double cumAverageSellPrice) {
        this.cumAverageSellPrice = cumAverageSellPrice;
    }

    public Double getCumAverageMiddlePrice() {
        return cumAverageMiddlePrice;
    }

    public void setCumAverageMiddlePrice(Double cumAverageMiddlePrice) {
        this.cumAverageMiddlePrice = cumAverageMiddlePrice;
    }

    public String getRateYear() {
        return rateYear;
    }

    public void setRateYear(String rateYear) {
        this.rateYear = rateYear;
    }

    public String getRateMonth() {
        return rateMonth;
    }

    public void setRateMonth(String rateMonth) {
        this.rateMonth = rateMonth;
    }
}
