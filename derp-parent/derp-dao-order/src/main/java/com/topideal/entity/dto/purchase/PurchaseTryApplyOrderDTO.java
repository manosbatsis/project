package com.topideal.entity.dto.purchase;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

public class PurchaseTryApplyOrderDTO extends PageModel implements Serializable{

    /**
     * ID
     */
    private Long id;
    /**
     * 商家ID
     */
    private Long merchantId;
    /**
     * 商家名称
     */
    private String merchantName;
    /**
     * 表单名称 立项：品牌立项申请表；试单：品牌试单采购申请表
     */
    private String formName;
    /**
     * 申请人编码
     */
    private String creater;
    /**
     * 申请人名称
     */
    private String createrName;
    /**
     * 业务员编码
     */
    private String businessManager;
    /**
     * 业务员名称
     */
    private String businessManagerName;
    /**
     * 业务部门
     */
    private String businessDept;
    /**
     * 业务部门
     */
    private String businessDeptName;
    /**
     * 归档编辑号
     */
    private String effectiveCode;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 预计年度采购金额（万元人民币）
     */
    private String annualPurPlanAmount;
    /**
     * 立项额度（万元人民币）
     */
    private String proAccLimit;
    /**
     * 交货方式 0：FOB，1：CIF，2：CIP，3：FCA，4：EXW，5：其他，6：DAP，7：CFR
     */
    private String deliveryType;
    private String deliveryTypeLabel;
    /**
     * 其他交货方式
     */
    private String otherDelType;
    private String otherDelTypeLabel;
    /**
     * 我司签约主体
     */
    private String ourContSignComy;
    /**
     * 供应商编码
     */
    private String counterpartContSignComy;
    /**
     * 供应商类型 0：品牌商，1：一级授权商，2：其他
     */
    private String supplierType;
    private String supplierTypeLabel;
    /**
     * 其他供应商类型
     */
    private String otherSupplier;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 商品类型 0：母婴类，1：美妆个护，2：保健品，3：日化家清，4：普通食品，5：数码家电，6：宠物食品，7：其他
     */
    private String comtyType;
    private String comtyTypeLabel;
    /**
     * 其他商品类型
     */
    private String otherComty;
    /**
     * 供应商描述
     */
    private String counterpartDesc;
    /**
     * 供应商商品描述
     */
    private String supProdDesc;
    /**
     * 市场营销费用、补贴、奖励等约定
     */
    private String otherMonAppoint;
    /**
     * 退货条款
     */
    private String returnGoodsApp;
    /**
     * 采购类型 0：进口，1：出口，2：内贸
     */
    private String purchaseType;
    private String purchaseTypeLabel;
    /**
     * 流水编号
     */
    private String oaBillCode;
    /**
     * 审批状态 0：审批驳回；1：审批中；2：审批通过
     */
    private String appState;
    private String appStateLabel;
    /**
     * 业务类型 0：跨境进口；1：跨境出口；2：一般贸易；3：内贸
     */
    private String businessMode;
    private String businessModeLabel;

    /**
     * 数据ID
     */
    private String dataId;
    /**
     * 创建时间
     */
    private Timestamp createDate;
    /**
     * 修改时间
     */
    private Timestamp modifyDate;

    private String purchaseMethod;

    /**
     * 供应商id
     */
    private Long supplierId;
    /**
     * 供应商名称
     */
    private String supplierName;

    @ApiModelProperty(value="是否查询所有状态, true:是, false：否")
    private Boolean allStatusFlag;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getBusinessManager() {
        return businessManager;
    }

    public void setBusinessManager(String businessManager) {
        this.businessManager = businessManager;
    }

    public String getBusinessDept() {
        return businessDept;
    }

    public void setBusinessDept(String businessDept) {
        this.businessDept = businessDept;
    }

    public String getEffectiveCode() {
        return effectiveCode;
    }

    public void setEffectiveCode(String effectiveCode) {
        this.effectiveCode = effectiveCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAnnualPurPlanAmount() {
        return annualPurPlanAmount;
    }

    public void setAnnualPurPlanAmount(String annualPurPlanAmount) {
        this.annualPurPlanAmount = annualPurPlanAmount;
    }

    public String getProAccLimit() {
        return proAccLimit;
    }

    public void setProAccLimit(String proAccLimit) {
        this.proAccLimit = proAccLimit;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
        this.deliveryTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseFrameContract_deliveryTypeList, deliveryType);
    }

    public String getOtherDelType() {
        return otherDelType;
    }

    public void setOtherDelType(String otherDelType) {
        this.otherDelType = otherDelType;
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
        this.supplierTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseFrameContract_supplierTypeList, supplierType);
    }

    public String getOtherSupplier() {
        return otherSupplier;
    }

    public void setOtherSupplier(String otherSupplier) {
        this.otherSupplier = otherSupplier;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getComtyType() {
        return comtyType;
    }

    public void setComtyType(String comtyType) {
        this.comtyType = comtyType;
        this.comtyTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseFrameContract_comtyTypeList, comtyType);
    }

    public String getOtherComty() {
        return otherComty;
    }

    public void setOtherComty(String otherComty) {
        this.otherComty = otherComty;
    }

    public String getCounterpartDesc() {
        return counterpartDesc;
    }

    public void setCounterpartDesc(String counterpartDesc) {
        this.counterpartDesc = counterpartDesc;
    }

    public String getSupProdDesc() {
        return supProdDesc;
    }

    public void setSupProdDesc(String supProdDesc) {
        this.supProdDesc = supProdDesc;
    }

    public String getOtherMonAppoint() {
        return otherMonAppoint;
    }

    public void setOtherMonAppoint(String otherMonAppoint) {
        this.otherMonAppoint = otherMonAppoint;
    }

    public String getReturnGoodsApp() {
        return returnGoodsApp;
    }

    public void setReturnGoodsApp(String returnGoodsApp) {
        this.returnGoodsApp = returnGoodsApp;
    }

    public String getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
        this.purchaseTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseFrameContract_purchaseTypeList, purchaseType);
    }

    public String getOaBillCode() {
        return oaBillCode;
    }

    public void setOaBillCode(String oaBillCode) {
        this.oaBillCode = oaBillCode;
    }

    public String getAppState() {
        return appState;
    }

    public void setAppState(String appState) {
        this.appState = appState;
        this.appStateLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseTryOrder_appStateList, appState);
    }

    public String getBusinessMode() {
        return businessMode;
    }

    public void setBusinessMode(String businessMode) {
        this.businessMode = businessMode;
        this.businessModeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseTryOrder_bussinessModeList, businessMode);
    }

    public String getDeliveryTypeLabel() {
        return deliveryTypeLabel;
    }

    public void setDeliveryTypeLabel(String deliveryTypeLabel) {
        this.deliveryTypeLabel = deliveryTypeLabel;
    }

    public String getOtherDelTypeLabel() {
        return otherDelTypeLabel;
    }

    public void setOtherDelTypeLabel(String otherDelTypeLabel) {
        this.otherDelTypeLabel = otherDelTypeLabel;
    }

    public String getSupplierTypeLabel() {
        return supplierTypeLabel;
    }

    public void setSupplierTypeLabel(String supplierTypeLabel) {
        this.supplierTypeLabel = supplierTypeLabel;
    }

    public String getComtyTypeLabel() {
        return comtyTypeLabel;
    }

    public void setComtyTypeLabel(String comtyTypeLabel) {
        this.comtyTypeLabel = comtyTypeLabel;
    }

    public String getPurchaseTypeLabel() {
        return purchaseTypeLabel;
    }

    public void setPurchaseTypeLabel(String purchaseTypeLabel) {
        this.purchaseTypeLabel = purchaseTypeLabel;
    }

    public String getAppStateLabel() {
        return appStateLabel;
    }

    public void setAppStateLabel(String appStateLabel) {
        this.appStateLabel = appStateLabel;
    }

    public String getBusinessModeLabel() {
        return businessModeLabel;
    }

    public void setBusinessModeLabel(String businessModeLabel) {
        this.businessModeLabel = businessModeLabel;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getBusinessDeptName() {
        return businessDeptName;
    }

    public void setBusinessDeptName(String businessDeptName) {
        this.businessDeptName = businessDeptName;
    }

    public String getPurchaseMethod() {
        return purchaseMethod;
    }

    public void setPurchaseMethod(String purchaseMethod) {
        this.purchaseMethod = purchaseMethod;
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

    public String getBusinessManagerName() {
        return businessManagerName;
    }

    public void setBusinessManagerName(String businessManagerName) {
        this.businessManagerName = businessManagerName;
    }

    public Boolean getAllStatusFlag() {
        return allStatusFlag;
    }

    public void setAllStatusFlag(Boolean allStatusFlag) {
        this.allStatusFlag = allStatusFlag;
    }
}
