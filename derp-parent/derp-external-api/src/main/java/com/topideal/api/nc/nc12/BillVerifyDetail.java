package com.topideal.api.nc.nc12;

import java.math.BigDecimal;

/**
 * 核销记录查询接口返回明细
 */
public class BillVerifyDetail {

    //唯一主键id
    private Integer ncId;

    //账单号【结算账单号】
    private String zdh;

    //对应核销单号【结算账单核销对方单据号】
    private String billNo2;

    //处理日期【核销日期】
    private String busiDate;

    //核销记录状态： 1=正常， 2=作废
    private String state;

    //核销明细处理人
    private String verifyDetailCreator;

    //借方处理原币金额【应收】
    private BigDecimal moneyde;

    //贷方处理原币金额【应付】
    private BigDecimal moneycr;


    public Integer getNcId() {
        return ncId;
    }

    public void setNcId(Integer ncId) {
        this.ncId = ncId;
    }

    public String getZdh() {
        return zdh;
    }

    public void setZdh(String zdh) {
        this.zdh = zdh;
    }

    public String getBillNo2() {
        return billNo2;
    }

    public void setBillNo2(String billNo2) {
        this.billNo2 = billNo2;
    }

    public String getBusiDate() {
        return busiDate;
    }

    public void setBusiDate(String busiDate) {
        this.busiDate = busiDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getVerifyDetailCreator() {
        return verifyDetailCreator;
    }

    public void setVerifyDetailCreator(String verifyDetailCreator) {
        this.verifyDetailCreator = verifyDetailCreator;
    }

    public BigDecimal getMoneyde() {
        return moneyde;
    }

    public void setMoneyde(BigDecimal moneyde) {
        this.moneyde = moneyde;
    }

    public BigDecimal getMoneycr() {
        return moneycr;
    }

    public void setMoneycr(BigDecimal moneycr) {
        this.moneycr = moneycr;
    }
}