package com.topideal.api.nc.nc12;

/**
 * 核销记录查询接口
 */
public class BillVerifyQueryRoot {

    //来源系统编码
    private String sourceCode;

    //业务系统对应法人主体卓志编码[主数据]
    private String corCcode;

    //账单类型： 1=应收， 2=应付，3=预收，4=预付，5=收款，6=付款，7=暂估应收，8=暂估应付【不填则查询所有类型账单】
    private String type;

    //开始时间，格式：yyyy-MM-dd HH:mm:ss
    private String startTime;

    //结束时间，格式：yyyy-MM-dd HH:mm:ss
    private String endTime;

    //账单号【结算账单号】
    private String zdh;

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getCorCcode() {
        return corCcode;
    }

    public void setCorCcode(String corCcode) {
        this.corCcode = corCcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getZdh() {
        return zdh;
    }

    public void setZdh(String zdh) {
        this.zdh = zdh;
    }
}