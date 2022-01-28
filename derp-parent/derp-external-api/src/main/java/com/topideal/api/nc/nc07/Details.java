package com.topideal.api.nc.nc07;
import java.math.BigDecimal;

/**
 * 财务 NC 4.7 结算账单接收接口 明细类
 * @author gy
 */
public class Details {

	/**
	 * 明细行唯一标识 （更新发票接口的标识须与此标识一致）
	 */
    private String sindex;
    
    /**
     * 收支项目编码
     */
    private String inExpCode;
    
    /**
     * 币种，默认CNY
     */
    private String currency;
    
    /**
     * 含税原币金额，保留2位小数
     */
    private BigDecimal amount;
    
    /**
     * 税率
     */
    private String rate;
    
    /**
     * 税额，保留两2位小数
     */
    private BigDecimal tax;
    
    /**
     * 科目编码
     */
    private String accCode;
    
    /**
     * 摘要
     */
    private String abstracts;
    
    /**
     * 发票号
     */
    private String invoiceCode;
    
    /**
     * 发票日期，格式：yyyy-MM-dd
     */
    private String invoiceDate;
    
    /**
     * 部门编码
     */
    private String detpCode;
    
    /**
     * 业务员工号
     */
    private String salesmenCode;
    
    /**
     * 渠道编码
     */
    private String channelCode;
    
    /**
     * 业务模式编码
     */
    private String businessCode;
    
    /**
     * 销售模式编码
     */
    private String saleCode;
    
    /**
     * 品牌编码
     */
    private String brandCode;
    
    /**
     * 平台编码
     */
    private String platCode;
    
    /**
     * 店铺编码
     */
    private String shopCode;
    
    /**
     * 业务系统单号
     */
    private String systemOrder;
    
    /**
     * 外部平台单号
     */
    private String externalOrder;
    
    public void setSindex(String sindex) {
         this.sindex = sindex;
     }
     public String getSindex() {
         return sindex;
     }

    public void setInExpCode(String inExpCode) {
         this.inExpCode = inExpCode;
     }
     public String getInExpCode() {
         return inExpCode;
     }

    public void setCurrency(String currency) {
         this.currency = currency;
     }
     public String getCurrency() {
         return currency;
     }

    public void setAmount(BigDecimal amount) {
         this.amount = amount;
     }
     public BigDecimal getAmount() {
         return amount;
     }

    public void setRate(String rate) {
         this.rate = rate;
     }
     public String getRate() {
         return rate;
     }

    public void setTax(BigDecimal tax) {
         this.tax = tax;
     }
     public BigDecimal getTax() {
         return tax;
     }

    public void setAccCode(String accCode) {
         this.accCode = accCode;
     }
     public String getAccCode() {
         return accCode;
     }

    public void setAbstracts(String abstracts) {
         this.abstracts = abstracts;
     }
     public String getAbstracts() {
         return abstracts;
     }

    public void setInvoiceCode(String invoiceCode) {
         this.invoiceCode = invoiceCode;
     }
     public String getInvoiceCode() {
         return invoiceCode;
     }

    public void setInvoiceDate(String invoiceDate) {
         this.invoiceDate = invoiceDate;
     }
     public String getInvoiceDate() {
         return invoiceDate;
     }

    public void setDetpCode(String detpCode) {
         this.detpCode = detpCode;
     }
     public String getDetpCode() {
         return detpCode;
     }

    public void setSalesmenCode(String salesmenCode) {
         this.salesmenCode = salesmenCode;
     }
     public String getSalesmenCode() {
         return salesmenCode;
     }

    public void setChannelCode(String channelCode) {
         this.channelCode = channelCode;
     }
     public String getChannelCode() {
         return channelCode;
     }

    public void setBusinessCode(String businessCode) {
         this.businessCode = businessCode;
     }
     public String getBusinessCode() {
         return businessCode;
     }

    public void setSaleCode(String saleCode) {
         this.saleCode = saleCode;
     }
     public String getSaleCode() {
         return saleCode;
     }

    public void setBrandCode(String brandCode) {
         this.brandCode = brandCode;
     }
     public String getBrandCode() {
         return brandCode;
     }

    public void setPlatCode(String platCode) {
         this.platCode = platCode;
     }
     public String getPlatCode() {
         return platCode;
     }

    public void setShopCode(String shopCode) {
         this.shopCode = shopCode;
     }
     public String getShopCode() {
         return shopCode;
     }

    public void setSystemOrder(String systemOrder) {
         this.systemOrder = systemOrder;
     }
     public String getSystemOrder() {
         return systemOrder;
     }

    public void setExternalOrder(String externalOrder) {
         this.externalOrder = externalOrder;
     }
     public String getExternalOrder() {
         return externalOrder;
     }

}