package com.topideal.webService.oa.dto;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/27 20:16
 * @Description:
 */
public class WorkflowRequestIdResponse {

    private String resultCode;

    private String message;

    private String requestId;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
