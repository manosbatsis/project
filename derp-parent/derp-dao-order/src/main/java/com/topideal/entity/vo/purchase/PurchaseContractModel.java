package com.topideal.entity.vo.purchase;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.math.BigDecimal;

public class PurchaseContractModel extends PageModel implements Serializable{

    /**
    * 自增ID
    */
    private Long id;
    /**
    * 采购订单ID
    */
    private Long orderId;
    /**
    * 采购合同编号
    */
    private String purchaseContractNo;
    /**
    * 采购订单号(采购单-PO号字段)
    */
    private String purchaseOrderNo;
    /**
    * 公司信息-英文名称
    */
    private String buyerEnName;
    /**
    * 公司信息-中文名称
    */
    private String buyerCnName;
    /**
    * 供应商信息-英文名
    */
    private String sellerEnName;
    /**
    * 供应商信息-全称
    */
    private String sellerCnName;
    /**
    * 运输方式
    */
    private String meansOfTransportation;
    /**
    * 始发地（港）
    */
    private String loadingCnPort;
    /**
    * 始发地（港）英文
    */
    private String loadingEnPort;
    /**
    * 目的地（港）
    */
    private String destinationdCn;
    /**
    * 目的地（港）英
    */
    private String destinationdEn;
    /**
    * 交货日期
    */
    private Timestamp deliveryDate;
    /**
    * 付款方式1-一次结清 2-分批付款 3- 其他
    */
    private String paymentMethod;
    /**
    * 付款方式文案
    */
    private String paymentMethodText;
    /**
    * 特别约定
    */
    private String specialAgreement;
    /**
    * 账号
    */
    private String accountNo;
    /**
    * 采购币种
    */
    private String currency;
    /**
    * 账户
    */
    private String beneficiaryName;
    /**
    * 开户行
    */
    private String beneficiaryBankName;
    /**
    * 地址
    */
    private String bankAddress;
    /**
    * Swift Code
    */
    private String swiftCode;
    /**
    * 甲方授权代表签字（英）
    */
    private String buyerAuthorizedSignatureEn;
    /**
    * 甲方授权代表签字
    */
    private String buyerAuthorizedSignature;
    /**
    * 乙方授权代表签字（英）
    */
    private String sellerAuthorizedSignatureEn;
    /**
    * 乙方授权代表签字
    */
    private String sellerAuthorizedSignature;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    //特别约定英文
    private String specialAgreementEn;

    //付款方式文案
    private String paymentMethodTextEn;

    /**
     * 发货地址（英）
     */
    private String deliveryAddressEn;
    /**
     * 发货地址
     */
    private String deliveryAddress;
    /**
     * 提货地址(英)
     */
    private String pickingUpAddressEn;
    /**
     * 提货地址
     */
    private String pickingUpAddress;

    private List<PurchaseOrderItemModel> itemList ;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*orderId get 方法 */
    public Long getOrderId(){
    return orderId;
    }
    /*orderId set 方法 */
    public void setOrderId(Long  orderId){
    this.orderId=orderId;
    }
    /*purchaseContractNo get 方法 */
    public String getPurchaseContractNo(){
    return purchaseContractNo;
    }
    /*purchaseContractNo set 方法 */
    public void setPurchaseContractNo(String  purchaseContractNo){
    this.purchaseContractNo=purchaseContractNo;
    }
    /*purchaseOrderNo get 方法 */
    public String getPurchaseOrderNo(){
    return purchaseOrderNo;
    }
    /*purchaseOrderNo set 方法 */
    public void setPurchaseOrderNo(String  purchaseOrderNo){
    this.purchaseOrderNo=purchaseOrderNo;
    }
    /*buyerEnName get 方法 */
    public String getBuyerEnName(){
    return buyerEnName;
    }
    /*buyerEnName set 方法 */
    public void setBuyerEnName(String  buyerEnName){
    this.buyerEnName=buyerEnName;
    }
    /*buyerCnName get 方法 */
    public String getBuyerCnName(){
    return buyerCnName;
    }
    /*buyerCnName set 方法 */
    public void setBuyerCnName(String  buyerCnName){
    this.buyerCnName=buyerCnName;
    }
    /*sellerEnName get 方法 */
    public String getSellerEnName(){
    return sellerEnName;
    }
    /*sellerEnName set 方法 */
    public void setSellerEnName(String  sellerEnName){
    this.sellerEnName=sellerEnName;
    }
    /*sellerCnName get 方法 */
    public String getSellerCnName(){
    return sellerCnName;
    }
    /*sellerCnName set 方法 */
    public void setSellerCnName(String  sellerCnName){
    this.sellerCnName=sellerCnName;
    }
    /*meansOfTransportation get 方法 */
    public String getMeansOfTransportation(){
    return meansOfTransportation;
    }
    /*meansOfTransportation set 方法 */
    public void setMeansOfTransportation(String  meansOfTransportation){
    this.meansOfTransportation=meansOfTransportation;
    }
    /*loadingCnPort get 方法 */
    public String getLoadingCnPort(){
    return loadingCnPort;
    }
    /*loadingCnPort set 方法 */
    public void setLoadingCnPort(String  loadingCnPort){
    this.loadingCnPort=loadingCnPort;
    }
    /*loadingEnPort get 方法 */
    public String getLoadingEnPort(){
    return loadingEnPort;
    }
    /*loadingEnPort set 方法 */
    public void setLoadingEnPort(String  loadingEnPort){
    this.loadingEnPort=loadingEnPort;
    }
    /*destinationdCn get 方法 */
    public String getDestinationdCn(){
    return destinationdCn;
    }
    /*destinationdCn set 方法 */
    public void setDestinationdCn(String  destinationdCn){
    this.destinationdCn=destinationdCn;
    }
    /*destinationdEn get 方法 */
    public String getDestinationdEn(){
    return destinationdEn;
    }
    /*destinationdEn set 方法 */
    public void setDestinationdEn(String  destinationdEn){
    this.destinationdEn=destinationdEn;
    }
    /*deliveryDate get 方法 */
    public Timestamp getDeliveryDate(){
    return deliveryDate;
    }
    /*deliveryDate set 方法 */
    public void setDeliveryDate(Timestamp  deliveryDate){
    this.deliveryDate=deliveryDate;
    }
    /*paymentMethod get 方法 */
    public String getPaymentMethod(){
    return paymentMethod;
    }
    /*paymentMethod set 方法 */
    public void setPaymentMethod(String  paymentMethod){
    this.paymentMethod=paymentMethod;
    }
    /*paymentMethodText get 方法 */
    public String getPaymentMethodText(){
    return paymentMethodText;
    }
    /*paymentMethodText set 方法 */
    public void setPaymentMethodText(String  paymentMethodText){
    this.paymentMethodText=paymentMethodText;
    }
    /*specialAgreement get 方法 */
    public String getSpecialAgreement(){
    return specialAgreement;
    }
    /*specialAgreement set 方法 */
    public void setSpecialAgreement(String  specialAgreement){
    this.specialAgreement=specialAgreement;
    }
    /*accountNo get 方法 */
    public String getAccountNo(){
    return accountNo;
    }
    /*accountNo set 方法 */
    public void setAccountNo(String  accountNo){
    this.accountNo=accountNo;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*beneficiaryName get 方法 */
    public String getBeneficiaryName(){
    return beneficiaryName;
    }
    /*beneficiaryName set 方法 */
    public void setBeneficiaryName(String  beneficiaryName){
    this.beneficiaryName=beneficiaryName;
    }
    /*beneficiaryBankName get 方法 */
    public String getBeneficiaryBankName(){
    return beneficiaryBankName;
    }
    /*beneficiaryBankName set 方法 */
    public void setBeneficiaryBankName(String  beneficiaryBankName){
    this.beneficiaryBankName=beneficiaryBankName;
    }
    /*bankAddress get 方法 */
    public String getBankAddress(){
    return bankAddress;
    }
    /*bankAddress set 方法 */
    public void setBankAddress(String  bankAddress){
    this.bankAddress=bankAddress;
    }
    /*swiftCode get 方法 */
    public String getSwiftCode(){
    return swiftCode;
    }
    /*swiftCode set 方法 */
    public void setSwiftCode(String  swiftCode){
    this.swiftCode=swiftCode;
    }
    /*buyerAuthorizedSignatureEn get 方法 */
    public String getBuyerAuthorizedSignatureEn(){
    return buyerAuthorizedSignatureEn;
    }
    /*buyerAuthorizedSignatureEn set 方法 */
    public void setBuyerAuthorizedSignatureEn(String  buyerAuthorizedSignatureEn){
    this.buyerAuthorizedSignatureEn=buyerAuthorizedSignatureEn;
    }
    /*buyerAuthorizedSignature get 方法 */
    public String getBuyerAuthorizedSignature(){
    return buyerAuthorizedSignature;
    }
    /*buyerAuthorizedSignature set 方法 */
    public void setBuyerAuthorizedSignature(String  buyerAuthorizedSignature){
    this.buyerAuthorizedSignature=buyerAuthorizedSignature;
    }
    /*sellerAuthorizedSignatureEn get 方法 */
    public String getSellerAuthorizedSignatureEn(){
    return sellerAuthorizedSignatureEn;
    }
    /*sellerAuthorizedSignatureEn set 方法 */
    public void setSellerAuthorizedSignatureEn(String  sellerAuthorizedSignatureEn){
    this.sellerAuthorizedSignatureEn=sellerAuthorizedSignatureEn;
    }
    /*sellerAuthorizedSignature get 方法 */
    public String getSellerAuthorizedSignature(){
    return sellerAuthorizedSignature;
    }
    /*sellerAuthorizedSignature set 方法 */
    public void setSellerAuthorizedSignature(String  sellerAuthorizedSignature){
    this.sellerAuthorizedSignature=sellerAuthorizedSignature;
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

    public String getSpecialAgreementEn() {
        return specialAgreementEn;
    }

    public void setSpecialAgreementEn(String specialAgreementEn) {
        this.specialAgreementEn = specialAgreementEn;
    }

    public String getPaymentMethodTextEn() {
        return paymentMethodTextEn;
    }

    public void setPaymentMethodTextEn(String paymentMethodTextEn) {
        this.paymentMethodTextEn = paymentMethodTextEn;
    }

    public String getDeliveryAddressEn() {
        return deliveryAddressEn;
    }

    public void setDeliveryAddressEn(String deliveryAddressEn) {
        this.deliveryAddressEn = deliveryAddressEn;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPickingUpAddressEn() {
        return pickingUpAddressEn;
    }

    public void setPickingUpAddressEn(String pickingUpAddressEn) {
        this.pickingUpAddressEn = pickingUpAddressEn;
    }

    public String getPickingUpAddress() {
        return pickingUpAddress;
    }

    public void setPickingUpAddress(String pickingUpAddress) {
        this.pickingUpAddress = pickingUpAddress;
    }
	public List<PurchaseOrderItemModel> getItemList() {
		return itemList;
	}
	public void setItemList(List<PurchaseOrderItemModel> itemList) {
		this.itemList = itemList;
	}

}
