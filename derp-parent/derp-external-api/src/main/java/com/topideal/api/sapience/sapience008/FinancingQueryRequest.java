package com.topideal.api.sapience.sapience008;

/**
 * 融资申请基础信息获取请求json
 **/
public class FinancingQueryRequest {

    //方法名
    private String method;
    //来源系统
    private String sourceCode;
    //客户名称
    private String debtorName;
    //客户编码
    private String debtorCode;


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getDebtorName() {
        return debtorName;
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    public String getDebtorCode() {
        return debtorCode;
    }

    public void setDebtorCode(String debtorCode) {
        this.debtorCode = debtorCode;
    }
}
