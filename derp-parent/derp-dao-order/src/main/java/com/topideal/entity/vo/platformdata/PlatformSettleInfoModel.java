package com.topideal.entity.vo.platformdata;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/6 14:48
 * @Description: 爬虫可以结算单文件
 */
public class PlatformSettleInfoModel extends PageModel implements Serializable {

    private Long id;

    private String shopCode;

    private String shopName;

    private String gelCode;

    private String settleNo;

    private Timestamp settleDate;

    private String hasReceived;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getGelCode() {
        return gelCode;
    }

    public void setGelCode(String gelCode) {
        this.gelCode = gelCode;
    }

    public String getSettleNo() {
        return settleNo;
    }

    public void setSettleNo(String settleNo) {
        this.settleNo = settleNo;
    }

    public Timestamp getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(Timestamp settleDate) {
        this.settleDate = settleDate;
    }

    public String getHasReceived() {
        return hasReceived;
    }

    public void setHasReceived(String hasReceived) {
        this.hasReceived = hasReceived;
    }
}
