package com.topideal.entity.vo.purchase;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

public class PurchaseFrameContractModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 商家编码ID
    */
    private Long merchantId;
    /**
     * 商家编码名称
     */
    private String merchantName;
    /**
    * 合同协议号
    */
    private String contractNo;
    /**
    * 原协议编号
    */
    private String contractOldNo;
    /**
    * 立项/试单编号
    */
    private String lxsdNo;
    /**
    * 合同名称
    */
    private String contractName;
    /**
    * 合同模板
    */
    private String contractVersion;
    /**
    * 申请人编码
    */
    private String creater;
    /**
     * 申请人名称
     */
    private String createrName;
    /**
    * 申请日期(YYYY-MM-DD)
    */
    private String applicationDate;
    /**
    * 员工编码
    */
    private String businessManager;
    /**
     * 员工名称
     */
    private String businessManagerName;
    /**
    * 部门编码
    */
    private String businessDept;

    /**
     * 部门名称
     */
    private String businessDeptName;
    /**
    * 合同类型 0：新签，1：续签 2：补充 3：终止
    */
    private String contractType;
    /**
    * 归档编辑号
    */
    private String effectiveCode;
    /**
    * 我司签约主体
    */
    private String ourContSignComy;
    /**
    * 我司联系人
    */
    private String ourContacter;
    /**
    * 我司联系电话
    */
    private String ourContactTel;
    /**
    * 供应商编码
    */
    private String counterpartContSignComy;
    /**
    * 供应商类型 0：品牌商，1：一级授权商，2：其他
    */
    private String supplierType;
    /**
    * 其他供应商
    */
    private String otherSupplier;
    /**
    * 供应商联系人
    */
    private String counterpartConter;
    /**
    * 供应商联系电话
    */
    private String counterpartContTel;
    /**
    * 供应商地址
    */
    private String counterpartAdd;
    /**
    * 协议开始日期YYYY-MM-DD
    */
    private String contractStartTime;
    /**
    * 协议结束日期YYYY-MM-DD
    */
    private String contractEndTime;
    /**
    * 采购类型 0：进口，1：出口，2：内贸
    */
    private String purchaseType;
    /**
    * 资金来源 0：自有资金，1：卓普信资金
    */
    private String capitalType;
    /**
    * 商品类型 0：母婴类，1：美妆个护，2：保健品，3：日化家清，4：普通食品，5：数码家电，6：宠物食品，7：其他
    */
    private String comtyType;
    /**
    * 其他商品
    */
    private String otherComty;
    /**
    * 商品来源 0：品牌方，1：工厂采购，2：一级经销商，3：海外超市，4：其他
    */
    private String comtySource;
    /**
    * 其他商品来源
    */
    private String otherComtySource;
    /**
    * 合同状态 0：生效，1：补充生效 2：期满终止 3：提前终止
    */
    private String contractState;
    /**
    * 预计年度采购金额(万元人民币)
    */
    private String annualPurPlanAmount;
    /**
    * 立项额度（万元人民币）
    */
    private String proAccLimit;
    /**
    * 是否已取得授权 0：是，1：否
    */
    private String empJudge;
    /**
    * 起订量要求
    */
    private String orderNumReq;
    /**
    * 下采购订单的方式
    */
    private String orderType;
    /**
    * 分销范围
    */
    private String saleRange;
    /**
    * 交货方式 0：FOB，1：CIF，2：CIP，3：FCA，4：EXW，5：其他
    */
    private String deliveryType;
    /**
    * 其他交货方式
    */
    private String otherDelType;
    /**
    * 供应商对产品销售定价要求（如有）
    */
    private String priceReq;
    /**
    * 供应商对销售目标的要求（如有）
    */
    private String saleAimReq;
    /**
    * 市场营销费用、补贴、奖励等约定
    */
    private String otherMonAppoint;
    /**
    * 运营需求（如有）
    */
    private String operationNeeds;
    /**
    * 仓储运输要求
    */
    private String storageTranReq;
    /**
    * 收货及验收要求
    */
    private String acceptanceReq;
    /**
    * 短缺残损与滞销约定
    */
    private String damagedAppoint;
    /**
    * 退货条款
    */
    private String returnGoodsApp;
    /**
    * 法律管辖仲裁地
    */
    private String argueSolvePlace;
    /**
    * 终止约定（如有）
    */
    private String endAppoint;
    /**
    * 特殊约定（如违约金、违约责任）（如有）
    */
    private String specialAppoint;
    /**
    * 结算方式 0：先货后款，1：先款后货，2：实销实结
    */
    private String settleMent;
    /**
    * 结算币种
    */
    private String settleCury;
    /**
    * 其他结算币种
    */
    private String otherSettleCury;
    /**
    * 结算条款
    */
    private String settleAppoint;
    /**
    * 结算账期（自然日）
    */
    private String settleAccPeriod;
    /**
    * 预付款 0：有，1：无
    */
    private String advanceChargeJudge;
    /**
    * 约定银行账号变更条款
    */
    private String bankAccChaAppoint;
    /**
    * 预付款币种
    */
    private String advanceChargeCury;
    /**
    * 其他预付款币种
    */
    private String otherAdvanceChargeCury;
    /**
    * 预付款金额（万元人民币）
    */
    private String advanceCharge;
    /**
    * 供应商描述
    */
    private String counterpartDesc;
    /**
    * 供应商商品描述
    */
    private String supProdDesc;
    /**
    * 数据ID,后续引用到PO执行合同的框架合同编号主键ID
    */
    private String dataId;
    /**
    * 业务对应的财务经理  0：龚小香；1：董欢
    */
    private String financeManager;
    /**
    * 用印顺序 0：我方先盖章；1：对方先盖章；2：无需盖章
    */
    private String sealOrder;
    /**
    * 用印类型 0：传统物理盖章；1：线上电子签章
    */
    private String sealType;
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

    /*contractNo get 方法 */
    public String getContractNo(){
    return contractNo;
    }
    /*contractNo set 方法 */
    public void setContractNo(String  contractNo){
    this.contractNo=contractNo;
    }
    /*contractOldNo get 方法 */
    public String getContractOldNo(){
    return contractOldNo;
    }
    /*contractOldNo set 方法 */
    public void setContractOldNo(String  contractOldNo){
    this.contractOldNo=contractOldNo;
    }
    /*lxsdNo get 方法 */
    public String getLxsdNo(){
    return lxsdNo;
    }
    /*lxsdNo set 方法 */
    public void setLxsdNo(String  lxsdNo){
    this.lxsdNo=lxsdNo;
    }
    /*contractName get 方法 */
    public String getContractName(){
    return contractName;
    }
    /*contractName set 方法 */
    public void setContractName(String  contractName){
    this.contractName=contractName;
    }
    /*contractVersion get 方法 */
    public String getContractVersion(){
    return contractVersion;
    }
    /*contractVersion set 方法 */
    public void setContractVersion(String  contractVersion){
    this.contractVersion=contractVersion;
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

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
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
    /*contractType get 方法 */
    public String getContractType(){
    return contractType;
    }
    /*contractType set 方法 */
    public void setContractType(String  contractType){
    this.contractType=contractType;
    }
    /*effectiveCode get 方法 */
    public String getEffectiveCode(){
    return effectiveCode;
    }
    /*effectiveCode set 方法 */
    public void setEffectiveCode(String  effectiveCode){
    this.effectiveCode=effectiveCode;
    }
    /*ourContSignComy get 方法 */
    public String getOurContSignComy(){
    return ourContSignComy;
    }
    /*ourContSignComy set 方法 */
    public void setOurContSignComy(String  ourContSignComy){
    this.ourContSignComy=ourContSignComy;
    }
    /*ourContacter get 方法 */
    public String getOurContacter(){
    return ourContacter;
    }
    /*ourContacter set 方法 */
    public void setOurContacter(String  ourContacter){
    this.ourContacter=ourContacter;
    }
    /*ourContactTel get 方法 */
    public String getOurContactTel(){
    return ourContactTel;
    }
    /*ourContactTel set 方法 */
    public void setOurContactTel(String  ourContactTel){
    this.ourContactTel=ourContactTel;
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
    /*counterpartConter get 方法 */
    public String getCounterpartConter(){
    return counterpartConter;
    }
    /*counterpartConter set 方法 */
    public void setCounterpartConter(String  counterpartConter){
    this.counterpartConter=counterpartConter;
    }
    /*counterpartContTel get 方法 */
    public String getCounterpartContTel(){
    return counterpartContTel;
    }
    /*counterpartContTel set 方法 */
    public void setCounterpartContTel(String  counterpartContTel){
    this.counterpartContTel=counterpartContTel;
    }
    /*counterpartAdd get 方法 */
    public String getCounterpartAdd(){
    return counterpartAdd;
    }
    /*counterpartAdd set 方法 */
    public void setCounterpartAdd(String  counterpartAdd){
    this.counterpartAdd=counterpartAdd;
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

    /*purchaseType get 方法 */
    public String getPurchaseType(){
    return purchaseType;
    }
    /*purchaseType set 方法 */
    public void setPurchaseType(String  purchaseType){
    this.purchaseType=purchaseType;
    }
    /*capitalType get 方法 */
    public String getCapitalType(){
    return capitalType;
    }
    /*capitalType set 方法 */
    public void setCapitalType(String  capitalType){
    this.capitalType=capitalType;
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
    /*comtySource get 方法 */
    public String getComtySource(){
    return comtySource;
    }
    /*comtySource set 方法 */
    public void setComtySource(String  comtySource){
    this.comtySource=comtySource;
    }
    /*otherComtySource get 方法 */
    public String getOtherComtySource(){
    return otherComtySource;
    }
    /*otherComtySource set 方法 */
    public void setOtherComtySource(String  otherComtySource){
    this.otherComtySource=otherComtySource;
    }
    /*contractState get 方法 */
    public String getContractState(){
    return contractState;
    }
    /*contractState set 方法 */
    public void setContractState(String  contractState){
    this.contractState=contractState;
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
    /*empJudge get 方法 */
    public String getEmpJudge(){
    return empJudge;
    }
    /*empJudge set 方法 */
    public void setEmpJudge(String  empJudge){
    this.empJudge=empJudge;
    }
    /*orderNumReq get 方法 */
    public String getOrderNumReq(){
    return orderNumReq;
    }
    /*orderNumReq set 方法 */
    public void setOrderNumReq(String  orderNumReq){
    this.orderNumReq=orderNumReq;
    }
    /*orderType get 方法 */
    public String getOrderType(){
    return orderType;
    }
    /*orderType set 方法 */
    public void setOrderType(String  orderType){
    this.orderType=orderType;
    }
    /*saleRange get 方法 */
    public String getSaleRange(){
    return saleRange;
    }
    /*saleRange set 方法 */
    public void setSaleRange(String  saleRange){
    this.saleRange=saleRange;
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
    /*priceReq get 方法 */
    public String getPriceReq(){
    return priceReq;
    }
    /*priceReq set 方法 */
    public void setPriceReq(String  priceReq){
    this.priceReq=priceReq;
    }
    /*saleAimReq get 方法 */
    public String getSaleAimReq(){
    return saleAimReq;
    }
    /*saleAimReq set 方法 */
    public void setSaleAimReq(String  saleAimReq){
    this.saleAimReq=saleAimReq;
    }
    /*otherMonAppoint get 方法 */
    public String getOtherMonAppoint(){
    return otherMonAppoint;
    }
    /*otherMonAppoint set 方法 */
    public void setOtherMonAppoint(String  otherMonAppoint){
    this.otherMonAppoint=otherMonAppoint;
    }
    /*operationNeeds get 方法 */
    public String getOperationNeeds(){
    return operationNeeds;
    }
    /*operationNeeds set 方法 */
    public void setOperationNeeds(String  operationNeeds){
    this.operationNeeds=operationNeeds;
    }
    /*storageTranReq get 方法 */
    public String getStorageTranReq(){
    return storageTranReq;
    }
    /*storageTranReq set 方法 */
    public void setStorageTranReq(String  storageTranReq){
    this.storageTranReq=storageTranReq;
    }
    /*acceptanceReq get 方法 */
    public String getAcceptanceReq(){
    return acceptanceReq;
    }
    /*acceptanceReq set 方法 */
    public void setAcceptanceReq(String  acceptanceReq){
    this.acceptanceReq=acceptanceReq;
    }
    /*damagedAppoint get 方法 */
    public String getDamagedAppoint(){
    return damagedAppoint;
    }
    /*damagedAppoint set 方法 */
    public void setDamagedAppoint(String  damagedAppoint){
    this.damagedAppoint=damagedAppoint;
    }
    /*returnGoodsApp get 方法 */
    public String getReturnGoodsApp(){
    return returnGoodsApp;
    }
    /*returnGoodsApp set 方法 */
    public void setReturnGoodsApp(String  returnGoodsApp){
    this.returnGoodsApp=returnGoodsApp;
    }
    /*argueSolvePlace get 方法 */
    public String getArgueSolvePlace(){
    return argueSolvePlace;
    }
    /*argueSolvePlace set 方法 */
    public void setArgueSolvePlace(String  argueSolvePlace){
    this.argueSolvePlace=argueSolvePlace;
    }
    /*endAppoint get 方法 */
    public String getEndAppoint(){
    return endAppoint;
    }
    /*endAppoint set 方法 */
    public void setEndAppoint(String  endAppoint){
    this.endAppoint=endAppoint;
    }
    /*specialAppoint get 方法 */
    public String getSpecialAppoint(){
    return specialAppoint;
    }
    /*specialAppoint set 方法 */
    public void setSpecialAppoint(String  specialAppoint){
    this.specialAppoint=specialAppoint;
    }
    /*settleMent get 方法 */
    public String getSettleMent(){
    return settleMent;
    }
    /*settleMent set 方法 */
    public void setSettleMent(String  settleMent){
    this.settleMent=settleMent;
    }
    /*settleCury get 方法 */
    public String getSettleCury(){
    return settleCury;
    }
    /*settleCury set 方法 */
    public void setSettleCury(String  settleCury){
    this.settleCury=settleCury;
    }
    /*otherSettleCury get 方法 */
    public String getOtherSettleCury(){
    return otherSettleCury;
    }
    /*otherSettleCury set 方法 */
    public void setOtherSettleCury(String  otherSettleCury){
    this.otherSettleCury=otherSettleCury;
    }
    /*settleAppoint get 方法 */
    public String getSettleAppoint(){
    return settleAppoint;
    }
    /*settleAppoint set 方法 */
    public void setSettleAppoint(String  settleAppoint){
    this.settleAppoint=settleAppoint;
    }
    /*settleAccPeriod get 方法 */
    public String getSettleAccPeriod(){
    return settleAccPeriod;
    }
    /*settleAccPeriod set 方法 */
    public void setSettleAccPeriod(String  settleAccPeriod){
    this.settleAccPeriod=settleAccPeriod;
    }
    /*advanceChargeJudge get 方法 */
    public String getAdvanceChargeJudge(){
    return advanceChargeJudge;
    }
    /*advanceChargeJudge set 方法 */
    public void setAdvanceChargeJudge(String  advanceChargeJudge){
    this.advanceChargeJudge=advanceChargeJudge;
    }
    /*bankAccChaAppoint get 方法 */
    public String getBankAccChaAppoint(){
    return bankAccChaAppoint;
    }
    /*bankAccChaAppoint set 方法 */
    public void setBankAccChaAppoint(String  bankAccChaAppoint){
    this.bankAccChaAppoint=bankAccChaAppoint;
    }
    /*advanceChargeCury get 方法 */
    public String getAdvanceChargeCury(){
    return advanceChargeCury;
    }
    /*advanceChargeCury set 方法 */
    public void setAdvanceChargeCury(String  advanceChargeCury){
    this.advanceChargeCury=advanceChargeCury;
    }
    /*otherAdvanceChargeCury get 方法 */
    public String getOtherAdvanceChargeCury(){
    return otherAdvanceChargeCury;
    }
    /*otherAdvanceChargeCury set 方法 */
    public void setOtherAdvanceChargeCury(String  otherAdvanceChargeCury){
    this.otherAdvanceChargeCury=otherAdvanceChargeCury;
    }
    /*advanceCharge get 方法 */
    public String getAdvanceCharge(){
    return advanceCharge;
    }
    /*advanceCharge set 方法 */
    public void setAdvanceCharge(String  advanceCharge){
    this.advanceCharge=advanceCharge;
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
    /*dataId get 方法 */
    public String getDataId(){
    return dataId;
    }
    /*dataId set 方法 */
    public void setDataId(String  dataId){
    this.dataId=dataId;
    }
    /*financeManager get 方法 */
    public String getFinanceManager(){
    return financeManager;
    }
    /*financeManager set 方法 */
    public void setFinanceManager(String  financeManager){
    this.financeManager=financeManager;
    }
    /*sealOrder get 方法 */
    public String getSealOrder(){
    return sealOrder;
    }
    /*sealOrder set 方法 */
    public void setSealOrder(String  sealOrder){
    this.sealOrder=sealOrder;
    }
    /*sealType get 方法 */
    public String getSealType(){
    return sealType;
    }
    /*sealType set 方法 */
    public void setSealType(String  sealType){
    this.sealType=sealType;
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
