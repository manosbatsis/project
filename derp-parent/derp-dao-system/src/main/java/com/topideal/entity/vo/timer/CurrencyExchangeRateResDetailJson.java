package com.topideal.entity.vo.timer;

import java.io.Serializable;

/**
 * @Description: 获取汇率接口汇率明细实体
 * @Date: 2020/04/27 14:26
 **/
public class CurrencyExchangeRateResDetailJson implements Serializable {

    //数据来源
    private String dataSource;

    //本币 必填
    private String localCurrency;

    //外币 必填
    private String foreignCurrency;

    //买入价
    private Double buyPrice;

    //卖出价
    private Double sellPrice;

    //中间价 必填
    private Double middlePrice;

    //银行汇率发布时间 必填
    private String rateTime;

    //抓取时间 必填
    private String crawlTime;

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

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Double getMiddlePrice() {
        return middlePrice;
    }

    public void setMiddlePrice(Double middlePrice) {
        this.middlePrice = middlePrice;
    }

    public String getRateTime() {
        return rateTime;
    }

    public void setRateTime(String rateTime) {
        this.rateTime = rateTime;
    }

    public String getCrawlTime() {
        return crawlTime;
    }

    public void setCrawlTime(String crawlTime) {
        this.crawlTime = crawlTime;
    }
}
