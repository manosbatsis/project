package com.topideal.order.api.bean.dto;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/2 10:06
 * @Description: Toc暂估 DTO
 */
public class QueryTocTempReceiveBillDTO {

    private String appKey;

    private String sign;

    /**
     * 电商平台编码
     */
    private String storePlatformCode;

    /**
     * 商家编码
     */
    private String merchantCode;

    /**
     * 结算标识
     */
    private String settlementMark;

    /**
     * 开始月份 YYYY-MM
     */
    private String startMonth;

    /**
     * 结束月份 YYYY-MM
     */
    private String endMonth;

    private Integer pageNo;

    private Integer pageSize;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getStorePlatformCode() {
        return storePlatformCode;
    }

    public void setStorePlatformCode(String storePlatformCode) {
        this.storePlatformCode = storePlatformCode;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getSettlementMark() {
        return settlementMark;
    }

    public void setSettlementMark(String settlementMark) {
        this.settlementMark = settlementMark;
    }

    public String getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(String startMonth) {
        this.startMonth = startMonth;
    }

    public String getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(String endMonth) {
        this.endMonth = endMonth;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
