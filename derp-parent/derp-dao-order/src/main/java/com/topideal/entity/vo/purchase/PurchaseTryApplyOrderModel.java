package com.topideal.entity.vo.purchase;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

public class PurchaseTryApplyOrderModel extends PageModel implements Serializable{

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
     * 业务部门名称
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
    /**
    * 其他交货方式
    */
    private String otherDelType;
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
    /**
    * 流水编号
    */
    private String oaBillCode;
    /**
    * 审批状态 0：审批驳回；1：审批中；2：审批通过
    */
    private String appState;
    /**
    * 业务类型 0：跨境进口；1：跨境出口；2：一般贸易；3：内贸
    */
    private String businessMode;

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

    /**
     * 供应商id
     */
    private Long supplierId;
    /**
     * 供应商名称
     */
    private String supplierName;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
    return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
    this.merchantId=merchantId;
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

    /*creater get 方法 */
    public String getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(String  creater){
    this.creater=creater;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    /*businessManager get 方法 */
    public String getBusinessManager(){
    return businessManager;
    }
    /*businessManager set 方法 */
    public void setBusinessManager(String  businessManager){
    this.businessManager=businessManager;
    }

    public String getBusinessManagerName() {
        return businessManagerName;
    }

    public void setBusinessManagerName(String businessManagerName) {
        this.businessManagerName = businessManagerName;
    }

    /*businessDept get 方法 */
    public String getBusinessDept(){
    return businessDept;
    }
    /*businessDept set 方法 */
    public void setBusinessDept(String  businessDept){
    this.businessDept=businessDept;
    }
    /*effectiveCode get 方法 */
    public String getEffectiveCode(){
    return effectiveCode;
    }
    /*effectiveCode set 方法 */
    public void setEffectiveCode(String  effectiveCode){
    this.effectiveCode=effectiveCode;
    }
    /*projectName get 方法 */
    public String getProjectName(){
    return projectName;
    }
    /*projectName set 方法 */
    public void setProjectName(String  projectName){
    this.projectName=projectName;
    }
    /*annualPurPlanAmount get 方法 */
    public String getAnnualPurPlanAmount(){
    return annualPurPlanAmount;
    }
    /*annualPurPlanAmount set 方法 */
    public void setAnnualPurPlanAmount(String  annualPurPlanAmount){
    this.annualPurPlanAmount=annualPurPlanAmount;
    }
    /*proAccLimit get 方法 */
    public String getProAccLimit(){
    return proAccLimit;
    }
    /*proAccLimit set 方法 */
    public void setProAccLimit(String  proAccLimit){
    this.proAccLimit=proAccLimit;
    }
    /*deliveryType get 方法 */
    public String getDeliveryType(){
    return deliveryType;
    }
    /*deliveryType set 方法 */
    public void setDeliveryType(String  deliveryType){
    this.deliveryType=deliveryType;
    }
    /*otherDelType get 方法 */
    public String getOtherDelType(){
    return otherDelType;
    }
    /*otherDelType set 方法 */
    public void setOtherDelType(String  otherDelType){
    this.otherDelType=otherDelType;
    }
    /*ourContSignComy get 方法 */
    public String getOurContSignComy(){
    return ourContSignComy;
    }
    /*ourContSignComy set 方法 */
    public void setOurContSignComy(String  ourContSignComy){
    this.ourContSignComy=ourContSignComy;
    }
    /*counterpartContSignComy get 方法 */
    public String getCounterpartContSignComy(){
    return counterpartContSignComy;
    }
    /*counterpartContSignComy set 方法 */
    public void setCounterpartContSignComy(String  counterpartContSignComy){
    this.counterpartContSignComy=counterpartContSignComy;
    }
    /*supplierType get 方法 */
    public String getSupplierType(){
    return supplierType;
    }
    /*supplierType set 方法 */
    public void setSupplierType(String  supplierType){
    this.supplierType=supplierType;
    }
    /*otherSupplier get 方法 */
    public String getOtherSupplier(){
    return otherSupplier;
    }
    /*otherSupplier set 方法 */
    public void setOtherSupplier(String  otherSupplier){
    this.otherSupplier=otherSupplier;
    }
    /*brandName get 方法 */
    public String getBrandName(){
    return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
    this.brandName=brandName;
    }
    /*comtyType get 方法 */
    public String getComtyType(){
    return comtyType;
    }
    /*comtyType set 方法 */
    public void setComtyType(String  comtyType){
    this.comtyType=comtyType;
    }
    /*otherComty get 方法 */
    public String getOtherComty(){
    return otherComty;
    }
    /*otherComty set 方法 */
    public void setOtherComty(String  otherComty){
    this.otherComty=otherComty;
    }
    /*counterpartDesc get 方法 */
    public String getCounterpartDesc(){
    return counterpartDesc;
    }
    /*counterpartDesc set 方法 */
    public void setCounterpartDesc(String  counterpartDesc){
    this.counterpartDesc=counterpartDesc;
    }
    /*supProdDesc get 方法 */
    public String getSupProdDesc(){
    return supProdDesc;
    }
    /*supProdDesc set 方法 */
    public void setSupProdDesc(String  supProdDesc){
    this.supProdDesc=supProdDesc;
    }
    /*otherMonAppoint get 方法 */
    public String getOtherMonAppoint(){
    return otherMonAppoint;
    }
    /*otherMonAppoint set 方法 */
    public void setOtherMonAppoint(String  otherMonAppoint){
    this.otherMonAppoint=otherMonAppoint;
    }
    /*returnGoodsApp get 方法 */
    public String getReturnGoodsApp(){
    return returnGoodsApp;
    }
    /*returnGoodsApp set 方法 */
    public void setReturnGoodsApp(String  returnGoodsApp){
    this.returnGoodsApp=returnGoodsApp;
    }
    /*purchaseType get 方法 */
    public String getPurchaseType(){
    return purchaseType;
    }
    /*purchaseType set 方法 */
    public void setPurchaseType(String  purchaseType){
    this.purchaseType=purchaseType;
    }
    /*oaBillCode get 方法 */
    public String getOaBillCode(){
    return oaBillCode;
    }
    /*oaBillCode set 方法 */
    public void setOaBillCode(String  oaBillCode){
    this.oaBillCode=oaBillCode;
    }
    /*appState get 方法 */
    public String getAppState(){
    return appState;
    }
    /*appState set 方法 */
    public void setAppState(String  appState){
    this.appState=appState;
    }
    /*businessMode get 方法 */
    public String getBusinessMode(){
    return businessMode;
    }
    /*businessMode set 方法 */
    public void setBusinessMode(String  businessMode){
    this.businessMode=businessMode;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }

    public String getBusinessDeptName() {
        return businessDeptName;
    }

    public void setBusinessDeptName(String businessDeptName) {
        this.businessDeptName = businessDeptName;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
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
}
