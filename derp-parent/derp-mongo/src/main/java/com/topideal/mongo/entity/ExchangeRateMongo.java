package com.topideal.mongo.entity;

import java.util.Date;
import java.sql.Timestamp;

/**
 * 汇率管理表
 */
public class ExchangeRateMongo {

    /**
     * 原币种编码
     */
    private String origCurrencyCode;
    /**
     * 原币种名称
     */
    private String origCurrencyName;
    /**
     * 目标币种编码
     */
    private String tgtCurrencyCode;
    /**
     * 目标币种名称
     */
    private String tgtCurrencyName;
    /**
     * 汇率
     */
    private Double rate;
    //平均汇率
    private Double avgRate;
    /**
     * 生效年月  格式: yyyy-MM-dd
     */
    private String effectiveDate;

    /**
     * 状态 006删除
     */
    private String status;

    /**
     * 创建人id
     */
    private Long creater;
    /**
     * 创建人名称
     */
    private String createName;

    //数据来源
    private String dataSource;
    //自增长ID
    private Long rateId;

    public String getOrigCurrencyCode() {
        return origCurrencyCode;
    }

    public void setOrigCurrencyCode(String origCurrencyCode) {
        this.origCurrencyCode = origCurrencyCode;
    }

    public String getOrigCurrencyName() {
        return origCurrencyName;
    }

    public void setOrigCurrencyName(String origCurrencyName) {
        this.origCurrencyName = origCurrencyName;
    }

    public String getTgtCurrencyCode() {
        return tgtCurrencyCode;
    }

    public void setTgtCurrencyCode(String tgtCurrencyCode) {
        this.tgtCurrencyCode = tgtCurrencyCode;
    }

    public String getTgtCurrencyName() {
        return tgtCurrencyName;
    }

    public void setTgtCurrencyName(String tgtCurrencyName) {
        this.tgtCurrencyName = tgtCurrencyName;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(Double avgRate) {
        this.avgRate = avgRate;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreater() {
        return creater;
    }

    public void setCreater(Long creater) {
        this.creater = creater;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Long getRateId() {
        return rateId;
    }

    public void setRateId(Long rateId) {
        this.rateId = rateId;
    }
}
