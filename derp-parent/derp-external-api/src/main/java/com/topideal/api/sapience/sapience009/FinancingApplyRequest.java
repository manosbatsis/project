package com.topideal.api.sapience.sapience009;

import java.math.BigDecimal;
import java.util.List;

/**
 * 发起融资申请推送接口请求json
 **/
public class FinancingApplyRequest {

    //方法名
    private String method;
    //来源系统
    private String sourceCode;
    //客户名称
    private String debtorName;
    //客户编码
    private String debtorCode;
    //资金方
    private Integer investorType;
    //业务类型编号
    private String productCode;
    //供应商名称
    private String supplierName;
    //供应商内部编码
    private Integer supplierCode;
    //入库仓库名称
    private String inStockWarehouseName;
    //入库仓库内部编码
    private String inStockWarehouseCode;
    //采购币种
    private String billCurrencyCode;
    //采购折扣金额
    private BigDecimal purchaseDiscountAmount;
    //提货地址
    private String warehouseAddress;
    //融资天数
    private Integer borrowingDays;
    //采购合同号
    private String contractNo;
    //付款条款名称
    private String paymentTermName;
    //贸易方式
    private String tradeMode;
    //备注
    private String loanApplyRemark;
    //预期交货日期
    private List<String> loanApprovalExpdeldates;
    //商品列表
    private List<Goods> goodsList;
    //附件列表
    private List<FileUploadEntity> fileUploadEntityList;

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

    public Integer getInvestorType() {
        return investorType;
    }

    public void setInvestorType(Integer investorType) {
        this.investorType = investorType;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Integer getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(Integer supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getInStockWarehouseName() {
        return inStockWarehouseName;
    }

    public void setInStockWarehouseName(String inStockWarehouseName) {
        this.inStockWarehouseName = inStockWarehouseName;
    }

    public String getInStockWarehouseCode() {
        return inStockWarehouseCode;
    }

    public void setInStockWarehouseCode(String inStockWarehouseCode) {
        this.inStockWarehouseCode = inStockWarehouseCode;
    }

    public String getBillCurrencyCode() {
        return billCurrencyCode;
    }

    public void setBillCurrencyCode(String billCurrencyCode) {
        this.billCurrencyCode = billCurrencyCode;
    }

    public BigDecimal getPurchaseDiscountAmount() {
        return purchaseDiscountAmount;
    }

    public void setPurchaseDiscountAmount(BigDecimal purchaseDiscountAmount) {
        this.purchaseDiscountAmount = purchaseDiscountAmount;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }

    public Integer getBorrowingDays() {
        return borrowingDays;
    }

    public void setBorrowingDays(Integer borrowingDays) {
        this.borrowingDays = borrowingDays;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getPaymentTermName() {
        return paymentTermName;
    }

    public void setPaymentTermName(String paymentTermName) {
        this.paymentTermName = paymentTermName;
    }

    public String getTradeMode() {
        return tradeMode;
    }

    public void setTradeMode(String tradeMode) {
        this.tradeMode = tradeMode;
    }

    public String getLoanApplyRemark() {
        return loanApplyRemark;
    }

    public void setLoanApplyRemark(String loanApplyRemark) {
        this.loanApplyRemark = loanApplyRemark;
    }

    public List<String> getLoanApprovalExpdeldates() {
        return loanApprovalExpdeldates;
    }

    public void setLoanApprovalExpdeldates(List<String> loanApprovalExpdeldates) {
        this.loanApprovalExpdeldates = loanApprovalExpdeldates;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public List<FileUploadEntity> getFileUploadEntityList() {
        return fileUploadEntityList;
    }

    public void setFileUploadEntityList(List<FileUploadEntity> fileUploadEntityList) {
        this.fileUploadEntityList = fileUploadEntityList;
    }
}
