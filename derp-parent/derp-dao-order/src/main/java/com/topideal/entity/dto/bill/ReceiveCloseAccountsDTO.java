package com.topideal.entity.dto.bill;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/17 11:03
 * @Description: 应收关账
 */
public class ReceiveCloseAccountsDTO extends PageModel implements Serializable {

    /**
     * id主键
     */
    private Long id;
    /**
     * 公司id
     */
    private Long merchantId;
    /**
     * 公司名称
     */
    private String merchantName;
    /**
     * 事业部id
     */
    private Long buId;
    /**
     * 事业部名称
     */
    private String buName;
    /**
     * 应收月份
     */
    private String month;
    /**
     * 期初时间
     */
    private Timestamp firstDate;
    /**
     * 期末时间
     */
    private Timestamp endDate;
    /**
     * 关账时间
     */
    private Timestamp closeDate;
    /**
     * 关账状态 029-未关账 030-已关账
     */
    private String status;
    private String statusLabel;

    /**
     * 修改时间
     */
    private Timestamp modifyDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Timestamp getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(Timestamp firstDate) {
        this.firstDate = firstDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public Timestamp getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Timestamp closeDate) {
        this.closeDate = closeDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveCloseAccounts_statusList, status);
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }
}
