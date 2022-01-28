package com.topideal.entity.dto.purchase;

import com.topideal.mongo.entity.AttachmentInfoMongo;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/28 9:32
 * @Description:
 */
public class PurchaseSaveOAAuditDTO implements Serializable {

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "事业部id")
    private Long buId;

    @ApiModelProperty(value = "事业部名称")
    private String buName;

    @ApiModelProperty(value = "我司签约主体ID")
    private Long merchantId;
    @ApiModelProperty(value = "我司签约主体名称")
    private String merchantName;

    @ApiModelProperty(value = "id集合,逗号分割")
    private String ids;

    @ApiModelProperty(value = "是否为框架下的订单")
    private Boolean frameContractFlag;

    @ApiModelProperty(value = "框架合同号")
    private String frameContractNo;

    @ApiModelProperty(value = "采购试单申请编号")
    private String tryApplyCode;

    @ApiModelProperty(value = "申请人")
    private String creater;

    @ApiModelProperty(value = "业务员编码")
    private String businessManager;

    @ApiModelProperty(value = "业务员名称")
    private String businessManagerName;

    @ApiModelProperty(value = "框架数据ID")
    private String frameContractDataId;

    @ApiModelProperty(value = "试单数据ID")
    private String tryApplyDataId;

    @ApiModelProperty(value = "PO号")
    private String poNo;

    @ApiModelProperty(value = "合同模板")
    private String contractVersion;

    @ApiModelProperty(value = "我司签约主体")
    private String ourContSignComy;

    @ApiModelProperty(value = "框架合同供应商编码")
    private String counterpartContSignComy;

    @ApiModelProperty(value = "采购单供应商Id")
    private Long supplierId;

    @ApiModelProperty(value = "采购单供应商名称")
    private String supplierName;

    // 0：品牌商，1：一级授权商，2：其他
    @ApiModelProperty(value = "供应商类型")
    private String supplierType;

    @ApiModelProperty(value = "其他供应商类型")
    private String otherSupplierType;

    @ApiModelProperty(value = "协议开始日期")
    private String contractStartTime;
    @ApiModelProperty(value = "协议结束日期")
    private String contractEndTime;

    @ApiModelProperty(value = "采购类型 0：进口，1：出口，2：内贸")
    private String purchaseType;

    /**
     * 商品类型 0：母婴类，1：美妆个护，2：保健品，3：日化家清，4：普通食品，5：数码家电，6：宠物食品，7：其他
     */
    @ApiModelProperty(value = "商品类型")
    private String comtyType;

    /**
     * 其他商品
     */
    @ApiModelProperty(value = "其他商品类型")
    private String otherComty;

    @ApiModelProperty(value = "业务对应的财务经理")
    private String financeManager;

    @ApiModelProperty(value = "采购订单编号集合, 逗号分割")
    private String codes;

    @ApiModelProperty(value = "采购方式")
    private String purchaseMethod;

    @ApiModelProperty(value = "交货方式 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW")
    private String tradeTerms;

    @ApiModelProperty(value = "其它交货方式")
    private String otherTradeTerms;

    @ApiModelProperty(value = "合作模式")
    private String cooperationModel;
    @ApiModelProperty(value = "其他合作模式")
    private String otherCooperationModel;

    @ApiModelProperty(value = "采购可用额度")
    private BigDecimal purchaseQuota;

    @ApiModelProperty(value = "SKU数量(个)")
    private Long skuNum;

    @ApiModelProperty(value = "采购总数量（件）")
    private Long purchaseTotalNum;

    @ApiModelProperty(value = "采购币种")
    private String purchaseCurreny;

    @ApiModelProperty(value = "采购总金额")
    private BigDecimal purchaseAmount;

    @ApiModelProperty(value = "预计毛利率")
    private BigDecimal esGrossMargin;

    @ApiModelProperty(value = "采购折算人民币金额")
    private BigDecimal purchaseRmbAmount;

    @ApiModelProperty(value = "新品单次采购金额")
    private BigDecimal newProductSingleAmount;

    @ApiModelProperty(value = "补货单次采购金额")
    private BigDecimal supplyProductSingleAmount;

    @ApiModelProperty(value = "客户ID")
    private Long customerId;
    @ApiModelProperty(value = "客户编码")
    private String customerCode;
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户预付款")
    private Boolean hasCustomerPrepayment;

    @ApiModelProperty(value = "客户预付款币种")
    private String preCustomerPaymentCurreny;

    @ApiModelProperty(value = "客户预付款金额")
    private BigDecimal preCustomerPaymentAmount;

    @ApiModelProperty(value = "结算方式")
    private String settlementModel;

    @ApiModelProperty(value = "结算条款")
    private String settlementProvision;

    @ApiModelProperty(value = "结算账期")
    private String settlementDate;

    @ApiModelProperty(value = "是否有预付款")
    private Boolean hasPrepayment;

    @ApiModelProperty(value = "预付款币种")
    private String prePaymentCurrency;

    @ApiModelProperty(value = "预付款金额")
    private BigDecimal prePaymentAmount;

    @ApiModelProperty(value = "约定银行变更条款")
    private String bankChangeProvision;

    @ApiModelProperty(value = "供应商描述")
    private String supplierDesc;

    @ApiModelProperty(value = "供应商商品描述")
    private String supplierMerchandiseDesc;

    @ApiModelProperty(value = "用印顺序")
    private String sealOrder;

    @ApiModelProperty(value = "用印类型")
    private String sealType;

    @ApiModelProperty(value = "盖章文件上传")
    private List<AttachmentInfoMongo> contractList;

    @ApiModelProperty(value = "其他辅助性文件上传")
    private List<AttachmentInfoMongo> commonFileList;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getOtherSupplierType() {
        return otherSupplierType;
    }

    public void setOtherSupplierType(String otherSupplierType) {
        this.otherSupplierType = otherSupplierType;
    }

    public Boolean getFrameContractFlag() {
        return frameContractFlag;
    }

    public void setFrameContractFlag(Boolean frameContractFlag) {
        this.frameContractFlag = frameContractFlag;
    }

    public String getFrameContractNo() {
        return frameContractNo;
    }

    public void setFrameContractNo(String frameContractNo) {
        this.frameContractNo = frameContractNo;
    }

    public String getTryApplyCode() {
        return tryApplyCode;
    }

    public void setTryApplyCode(String tryApplyCode) {
        this.tryApplyCode = tryApplyCode;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getContractVersion() {
        return contractVersion;
    }

    public void setContractVersion(String contractVersion) {
        this.contractVersion = contractVersion;
    }

    public String getOurContSignComy() {
        return ourContSignComy;
    }

    public void setOurContSignComy(String ourContSignComy) {
        this.ourContSignComy = ourContSignComy;
    }

    public String getCounterpartContSignComy() {
        return counterpartContSignComy;
    }

    public void setCounterpartContSignComy(String counterpartContSignComy) {
        this.counterpartContSignComy = counterpartContSignComy;
    }

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    public String getContractStartTime() {
        return contractStartTime;
    }

    public void setContractStartTime(String contractStartTime) {
        this.contractStartTime = contractStartTime;
    }

    public String getContractEndTime() {
        return contractEndTime;
    }

    public void setContractEndTime(String contractEndTime) {
        this.contractEndTime = contractEndTime;
    }

    public String getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
    }

    public String getComtyType() {
        return comtyType;
    }

    public void setComtyType(String comtyType) {
        this.comtyType = comtyType;
    }

    public String getOtherComty() {
        return otherComty;
    }

    public void setOtherComty(String otherComty) {
        this.otherComty = otherComty;
    }

    public String getFinanceManager() {
        return financeManager;
    }

    public void setFinanceManager(String financeManager) {
        this.financeManager = financeManager;
    }

    public String getCodes() {
        return codes;
    }

    public void setCodes(String codes) {
        this.codes = codes;
    }

    public String getPurchaseMethod() {
        return purchaseMethod;
    }

    public void setPurchaseMethod(String purchaseMethod) {
        this.purchaseMethod = purchaseMethod;
    }

    public String getTradeTerms() {
        return tradeTerms;
    }

    public void setTradeTerms(String tradeTerms) {
        this.tradeTerms = tradeTerms;
    }

    public String getOtherTradeTerms() {
        return otherTradeTerms;
    }

    public void setOtherTradeTerms(String otherTradeTerms) {
        this.otherTradeTerms = otherTradeTerms;
    }

    public String getCooperationModel() {
        return cooperationModel;
    }

    public void setCooperationModel(String cooperationModel) {
        this.cooperationModel = cooperationModel;
    }

    public String getOtherCooperationModel() {
        return otherCooperationModel;
    }

    public void setOtherCooperationModel(String otherCooperationModel) {
        this.otherCooperationModel = otherCooperationModel;
    }

    public BigDecimal getPurchaseQuota() {
        return purchaseQuota;
    }

    public void setPurchaseQuota(BigDecimal purchaseQuota) {
        this.purchaseQuota = purchaseQuota;
    }

    public Long getSkuNum() {
        return skuNum;
    }

    public void setSkuNum(Long skuNum) {
        this.skuNum = skuNum;
    }

    public Long getPurchaseTotalNum() {
        return purchaseTotalNum;
    }

    public void setPurchaseTotalNum(Long purchaseTotalNum) {
        this.purchaseTotalNum = purchaseTotalNum;
    }

    public String getPurchaseCurreny() {
        return purchaseCurreny;
    }

    public void setPurchaseCurreny(String purchaseCurreny) {
        this.purchaseCurreny = purchaseCurreny;
    }

    public BigDecimal getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(BigDecimal purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public BigDecimal getEsGrossMargin() {
        return esGrossMargin;
    }

    public void setEsGrossMargin(BigDecimal esGrossMargin) {
        this.esGrossMargin = esGrossMargin;
    }

    public BigDecimal getPurchaseRmbAmount() {
        return purchaseRmbAmount;
    }

    public void setPurchaseRmbAmount(BigDecimal purchaseRmbAmount) {
        this.purchaseRmbAmount = purchaseRmbAmount;
    }

    public BigDecimal getNewProductSingleAmount() {
        return newProductSingleAmount;
    }

    public void setNewProductSingleAmount(BigDecimal newProductSingleAmount) {
        this.newProductSingleAmount = newProductSingleAmount;
    }

    public BigDecimal getSupplyProductSingleAmount() {
        return supplyProductSingleAmount;
    }

    public void setSupplyProductSingleAmount(BigDecimal supplyProductSingleAmount) {
        this.supplyProductSingleAmount = supplyProductSingleAmount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Boolean getHasCustomerPrepayment() {
        return hasCustomerPrepayment;
    }

    public void setHasCustomerPrepayment(Boolean hasCustomerPrepayment) {
        this.hasCustomerPrepayment = hasCustomerPrepayment;
    }

    public String getPreCustomerPaymentCurreny() {
        return preCustomerPaymentCurreny;
    }

    public void setPreCustomerPaymentCurreny(String preCustomerPaymentCurreny) {
        this.preCustomerPaymentCurreny = preCustomerPaymentCurreny;
    }

    public BigDecimal getPreCustomerPaymentAmount() {
        return preCustomerPaymentAmount;
    }

    public void setPreCustomerPaymentAmount(BigDecimal preCustomerPaymentAmount) {
        this.preCustomerPaymentAmount = preCustomerPaymentAmount;
    }

    public String getSettlementModel() {
        return settlementModel;
    }

    public void setSettlementModel(String settlementModel) {
        this.settlementModel = settlementModel;
    }

    public String getSettlementProvision() {
        return settlementProvision;
    }

    public void setSettlementProvision(String settlementProvision) {
        this.settlementProvision = settlementProvision;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public Boolean getHasPrepayment() {
        return hasPrepayment;
    }

    public void setHasPrepayment(Boolean hasPrepayment) {
        this.hasPrepayment = hasPrepayment;
    }

    public String getPrePaymentCurrency() {
        return prePaymentCurrency;
    }

    public void setPrePaymentCurrency(String prePaymentCurrency) {
        this.prePaymentCurrency = prePaymentCurrency;
    }

    public BigDecimal getPrePaymentAmount() {
        return prePaymentAmount;
    }

    public void setPrePaymentAmount(BigDecimal prePaymentAmount) {
        this.prePaymentAmount = prePaymentAmount;
    }

    public String getBankChangeProvision() {
        return bankChangeProvision;
    }

    public void setBankChangeProvision(String bankChangeProvision) {
        this.bankChangeProvision = bankChangeProvision;
    }

    public String getSupplierDesc() {
        return supplierDesc;
    }

    public void setSupplierDesc(String supplierDesc) {
        this.supplierDesc = supplierDesc;
    }

    public String getSupplierMerchandiseDesc() {
        return supplierMerchandiseDesc;
    }

    public void setSupplierMerchandiseDesc(String supplierMerchandiseDesc) {
        this.supplierMerchandiseDesc = supplierMerchandiseDesc;
    }

    public String getSealOrder() {
        return sealOrder;
    }

    public void setSealOrder(String sealOrder) {
        this.sealOrder = sealOrder;
    }

    public String getSealType() {
        return sealType;
    }

    public void setSealType(String sealType) {
        this.sealType = sealType;
    }

    public List<AttachmentInfoMongo> getContractList() {
        return contractList;
    }

    public void setContractList(List<AttachmentInfoMongo> contractList) {
        this.contractList = contractList;
    }

    public List<AttachmentInfoMongo> getCommonFileList() {
        return commonFileList;
    }

    public void setCommonFileList(List<AttachmentInfoMongo> commonFileList) {
        this.commonFileList = commonFileList;
    }

    public String getBusinessManager() {
        return businessManager;
    }

    public void setBusinessManager(String businessManager) {
        this.businessManager = businessManager;
    }

    public String getBusinessManagerName() {
        return businessManagerName;
    }

    public void setBusinessManagerName(String businessManagerName) {
        this.businessManagerName = businessManagerName;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
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

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFrameContractDataId() {
        return frameContractDataId;
    }

    public void setFrameContractDataId(String frameContractDataId) {
        this.frameContractDataId = frameContractDataId;
    }

    public String getTryApplyDataId() {
        return tryApplyDataId;
    }

    public void setTryApplyDataId(String tryApplyDataId) {
        this.tryApplyDataId = tryApplyDataId;
    }
}
