package com.topideal.api.nc.nc08;
import java.util.Date;

/**
 * 结算账单红冲/作废接口
 */
public class ReceiveHcInvalidRoot {

    private String confirmBillId;
    private String bconfirmBillId;
    private String sourceCode;
    private String state;
    private String cancelTime;
    private String remark;
    public void setConfirmBillId(String confirmBillId) {
         this.confirmBillId = confirmBillId;
     }
     public String getConfirmBillId() {
         return confirmBillId;
     }

    public void setBconfirmBillId(String bconfirmBillId) {
         this.bconfirmBillId = bconfirmBillId;
     }
     public String getBconfirmBillId() {
         return bconfirmBillId;
     }

    public void setSourceCode(String sourceCode) {
         this.sourceCode = sourceCode;
     }
     public String getSourceCode() {
         return sourceCode;
     }

    public void setState(String state) {
         this.state = state;
     }
     public String getState() {
         return state;
     }

    public void setCancelTime(String cancelTime) {
         this.cancelTime = cancelTime;
     }
     public String getCancelTime() {
         return cancelTime;
     }

    public void setRemark(String remark) {
         this.remark = remark;
     }
     public String getRemark() {
         return remark;
     }

}