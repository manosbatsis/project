package com.topideal.api.nc.nc07;
import java.math.BigDecimal;
import java.util.List;

/**
 * 财务 NC 4.7 结算账单接收接口 实体类
 * @author gy
 */
public class ReceiveBillJsonRoot {

	/**
	 * 结算账单号
	 */
    private String confirmBillId;
    
    /**
     * 来源系统编码
     */
    private String sourceCode;
    
    /**
     * 结算类型。 1=应收，2=应付，3=预收，4=预付
     */
    private String type;
    
    /**
     * 业务系统对应法人主体卓志编码
     */
    private String corCcode;
    
    /**
     * 客商卓志编码
     */
    private String cusCcode;
    
    /**
     * 结算年月，6位
     */
    private String yearMonth;
    
    /**
     * 结算账单审批时间，格式：yyyy-MM-dd HH:mm:ss
     */
    private String created;
    
    /**
     * 结算总金额，保留2位小数（含税）
     */
    private BigDecimal totalAmount;
    
    /**
     * 结算税额，保留2位小数
     */
    private BigDecimal taxAmount;
    
    /**
     * 主币种，默认CNY
     */
    private String currency;
    
    /**
     * 客户确认账单标识。 0待确认  1已确认
     */
    private String checked;
    
    /**
     * 是否已开票标识。 0未开票   1已开票
     */
    private String invoiced;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 账单明细
     */
    private List<Details> details;
    
    
    public void setConfirmBillId(String confirmBillId) {
         this.confirmBillId = confirmBillId;
     }
     public String getConfirmBillId() {
         return confirmBillId;
     }

    public void setSourceCode(String sourceCode) {
         this.sourceCode = sourceCode;
     }
     public String getSourceCode() {
         return sourceCode;
     }

    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setCorCcode(String corCcode) {
         this.corCcode = corCcode;
     }
     public String getCorCcode() {
         return corCcode;
     }

    public void setCusCcode(String cusCcode) {
         this.cusCcode = cusCcode;
     }
     public String getCusCcode() {
         return cusCcode;
     }

    public void setYearMonth(String yearMonth) {
         this.yearMonth = yearMonth;
     }
     public String getYearMonth() {
         return yearMonth;
     }

    public void setCreated(String created) {
         this.created = created;
     }
     public String getCreated() {
         return created;
     }

    public void setTotalAmount(BigDecimal totalAmount) {
         this.totalAmount = totalAmount;
     }
     public BigDecimal getTotalAmount() {
         return totalAmount;
     }

    public void setTaxAmount(BigDecimal taxAmount) {
         this.taxAmount = taxAmount;
     }
     public BigDecimal getTaxAmount() {
         return taxAmount;
     }

    public void setCurrency(String currency) {
         this.currency = currency;
     }
     public String getCurrency() {
         return currency;
     }

    public void setChecked(String checked) {
         this.checked = checked;
     }
     public String getChecked() {
         return checked;
     }

    public void setInvoiced(String invoiced) {
         this.invoiced = invoiced;
     }
     public String getInvoiced() {
         return invoiced;
     }

    public void setRemark(String remark) {
         this.remark = remark;
     }
     public String getRemark() {
         return remark;
     }

    public void setDetails(List<Details> details) {
         this.details = details;
     }
     public List<Details> getDetails() {
         return details;
     }

}