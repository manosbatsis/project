package com.topideal.entity.dto.api;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/2 19:03
 * @Description:
 */
public class TocSettleBillDTO implements Serializable {

    //平台编码
    private String platformCode;

    //商家编码
    private String sellerCode;

    //结算币种
    private String currency;

    //结算单号
    private String settleNo;

    //店铺编码
    private String storeCode;

    //结算时间
    private String settleTime;

    private String id;

    private List<TocSettleBillDataDTO> data;

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getSellerCode() {
        return sellerCode;
    }

    public void setSellerCode(String sellerCode) {
        this.sellerCode = sellerCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSettleNo() {
        return settleNo;
    }

    public void setSettleNo(String settleNo) {
        this.settleNo = settleNo;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getSettleTime() {
        return settleTime;
    }

    public void setSettleTime(String settleTime) {
        this.settleTime = settleTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TocSettleBillDataDTO> getData() {
        return data;
    }

    public void setData(List<TocSettleBillDataDTO> data) {
        this.data = data;
    }
}
