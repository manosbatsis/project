package com.topideal.api.sapience.sapience011;

/**
 * 融资申请单状态获取接口请求json
 **/
public class FinancingStatusPushRequest {

    //方法名
    private String method;
    //来源系统
    private String sourceCode;
    //融资申请单号
    private String loanApplyNo;

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

    public String getLoanApplyNo() {
        return loanApplyNo;
    }

    public void setLoanApplyNo(String loanApplyNo) {
        this.loanApplyNo = loanApplyNo;
    }
}
