package com.topideal.entity.vo.purchase;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class PurchaseLinkInfoModel extends PageModel implements Serializable{

    /**
    * 自增ID
    */
    private Long id;
    /**
    * 公司ID
    */
    private Long merchantId;
    /**
    * 公司名
    */
    private String merchantName;
    /**
    * 链路ID
    */
    private Long tradeLinkId;
    /**
    * 归属时间
    */
    private Timestamp infoAuditDate;
    /**
    * 币种
    */
    private String infoCurrency;
    /**
    * 业务模式 4.购销-实销实结
    */
    private String infoBusinessModel;
    /**
    * 装货港
    */
    private String infoLoadPort;
    /**
    * 卸货港
    */
    private String infoUnloadPort;
    /**
    * 装船时间
    */
    private Timestamp infoShipDate;
    /**
    * 运输方式   1-空运 2-海运 3-陆运 4-港到仓拖车 5-中欧铁路 6-其他
    */
    private String infoTransport;
    /**
    * 承运商
    */
    private String infoCarrier;
    /**
    * 车次
    */
    private String infoTrainNumber;
    /**
    * 标准箱TEU
    */
    private String infoStandardCaseTeu;
    /**
    * 托数
    */
    private Integer infoTorrNum;
    /**
    * LBX单号
    */
    private String infoLbxNo;
    /**
    * 提单号
    */
    private String infoLadingBill;
    /**
    * 提单毛重
    */
    private Double infoGrossWeight;
    /**
    * 运输方式
    */
    private String conMeansOfTransportation;
    /**
    * 始发地（港）
    */
    private String conLoadingCnPort;
    /**
    * 始发地（港）英文
    */
    private String conLoadingEnPort;
    /**
    * 目的地（港）
    */
    private String conDestinationdCn;
    /**
    * 目的地（港）英
    */
    private String conDestinationdEn;
    /**
    * 交货日期
    */
    private Timestamp conDeliveryDate;
    /**
    * 付款方式1-一次结清 2-分批付款 3- 其他
    */
    private String conPaymentMethod;
    /**
    * 付款方式文案
    */
    private String conPaymentMethodText;
    /**
    * 付款方式文案 英文
    */
    private String conPaymentMethodTextEn;
    /**
    * 特别约定英文
    */
    private String conSpecialAgreementEn;
    /**
    * 特别约定
    */
    private String conSpecialAgreement;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * 创建人用户名
    */
    private String createName;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改人
    */
    private Long modifier;
    /**
    * 修改人用户名
    */
    private String modifierUsername;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 采购订单ID
    */
    private Long purchaseOrderId;
    /**
    * 采购订单编码
    */
    private String purchaseOrderCode;
    /**
    * 仓库1名称
    */
    private String startPointDepotName;
    /**
    * 仓库1ID
    */
    private Long startPointDepotId;
    /**
    * po号1
    */
    private String startPointPoNo;
    /**
    * 仓库2名称
    */
    private String oneDepotName;
    /**
    * 仓库2ID
    */
    private Long oneDepotId;
    /**
    * po号2
    */
    private String onePoNo;
    /**
    * 仓库3名称
    */
    private String twoDepotName;
    /**
    * 仓库3ID
    */
    private Long twoDepotId;
    /**
    * po号3
    */
    private String twoPoNo;
    /**
    * 仓库4名称
    */
    private String threeDepotName;
    /**
    * 仓库4ID
    */
    private Long threeDepotId;
    /**
    * po号4
    */
    private String threePoNo;
    /**
    * 仓库5名称
    */
    private String fourDepotName;
    /**
    * 仓库5ID
    */
    private Long fourDepotId;
    /**
    * po号5
    */
    private String fourPoNo;
    /**
    * 修改商品信息
    */
    private String goodsInfo;

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
    /*merchantName get 方法 */
    public String getMerchantName(){
    return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
    this.merchantName=merchantName;
    }
    /*tradeLinkId get 方法 */
    public Long getTradeLinkId(){
    return tradeLinkId;
    }
    /*tradeLinkId set 方法 */
    public void setTradeLinkId(Long  tradeLinkId){
    this.tradeLinkId=tradeLinkId;
    }
    /*infoAuditDate get 方法 */
    public Timestamp getInfoAuditDate(){
    return infoAuditDate;
    }
    /*infoAuditDate set 方法 */
    public void setInfoAuditDate(Timestamp  infoAuditDate){
    this.infoAuditDate=infoAuditDate;
    }
    /*infoCurrency get 方法 */
    public String getInfoCurrency(){
    return infoCurrency;
    }
    /*infoCurrency set 方法 */
    public void setInfoCurrency(String  infoCurrency){
    this.infoCurrency=infoCurrency;
    }
    /*infoBusinessModel get 方法 */
    public String getInfoBusinessModel(){
    return infoBusinessModel;
    }
    /*infoBusinessModel set 方法 */
    public void setInfoBusinessModel(String  infoBusinessModel){
    this.infoBusinessModel=infoBusinessModel;
    }
    /*infoLoadPort get 方法 */
    public String getInfoLoadPort(){
    return infoLoadPort;
    }
    /*infoLoadPort set 方法 */
    public void setInfoLoadPort(String  infoLoadPort){
    this.infoLoadPort=infoLoadPort;
    }
    /*infoUnloadPort get 方法 */
    public String getInfoUnloadPort(){
    return infoUnloadPort;
    }
    /*infoUnloadPort set 方法 */
    public void setInfoUnloadPort(String  infoUnloadPort){
    this.infoUnloadPort=infoUnloadPort;
    }
    /*infoShipDate get 方法 */
    public Timestamp getInfoShipDate(){
    return infoShipDate;
    }
    /*infoShipDate set 方法 */
    public void setInfoShipDate(Timestamp  infoShipDate){
    this.infoShipDate=infoShipDate;
    }
    /*infoTransport get 方法 */
    public String getInfoTransport(){
    return infoTransport;
    }
    /*infoTransport set 方法 */
    public void setInfoTransport(String  infoTransport){
    this.infoTransport=infoTransport;
    }
    /*infoCarrier get 方法 */
    public String getInfoCarrier(){
    return infoCarrier;
    }
    /*infoCarrier set 方法 */
    public void setInfoCarrier(String  infoCarrier){
    this.infoCarrier=infoCarrier;
    }
    /*infoTrainNumber get 方法 */
    public String getInfoTrainNumber(){
    return infoTrainNumber;
    }
    /*infoTrainNumber set 方法 */
    public void setInfoTrainNumber(String  infoTrainNumber){
    this.infoTrainNumber=infoTrainNumber;
    }
    /*infoStandardCaseTeu get 方法 */
    public String getInfoStandardCaseTeu(){
    return infoStandardCaseTeu;
    }
    /*infoStandardCaseTeu set 方法 */
    public void setInfoStandardCaseTeu(String  infoStandardCaseTeu){
    this.infoStandardCaseTeu=infoStandardCaseTeu;
    }
    /*infoTorrNum get 方法 */
    public Integer getInfoTorrNum(){
    return infoTorrNum;
    }
    /*infoTorrNum set 方法 */
    public void setInfoTorrNum(Integer  infoTorrNum){
    this.infoTorrNum=infoTorrNum;
    }
    /*infoLbxNo get 方法 */
    public String getInfoLbxNo(){
    return infoLbxNo;
    }
    /*infoLbxNo set 方法 */
    public void setInfoLbxNo(String  infoLbxNo){
    this.infoLbxNo=infoLbxNo;
    }
    /*infoLadingBill get 方法 */
    public String getInfoLadingBill(){
    return infoLadingBill;
    }
    /*infoLadingBill set 方法 */
    public void setInfoLadingBill(String  infoLadingBill){
    this.infoLadingBill=infoLadingBill;
    }
    /*infoGrossWeight get 方法 */
    public Double getInfoGrossWeight(){
    return infoGrossWeight;
    }
    /*infoGrossWeight set 方法 */
    public void setInfoGrossWeight(Double  infoGrossWeight){
    this.infoGrossWeight=infoGrossWeight;
    }
    /*conMeansOfTransportation get 方法 */
    public String getConMeansOfTransportation(){
    return conMeansOfTransportation;
    }
    /*conMeansOfTransportation set 方法 */
    public void setConMeansOfTransportation(String  conMeansOfTransportation){
    this.conMeansOfTransportation=conMeansOfTransportation;
    }
    /*conLoadingCnPort get 方法 */
    public String getConLoadingCnPort(){
    return conLoadingCnPort;
    }
    /*conLoadingCnPort set 方法 */
    public void setConLoadingCnPort(String  conLoadingCnPort){
    this.conLoadingCnPort=conLoadingCnPort;
    }
    /*conLoadingEnPort get 方法 */
    public String getConLoadingEnPort(){
    return conLoadingEnPort;
    }
    /*conLoadingEnPort set 方法 */
    public void setConLoadingEnPort(String  conLoadingEnPort){
    this.conLoadingEnPort=conLoadingEnPort;
    }
    /*conDestinationdCn get 方法 */
    public String getConDestinationdCn(){
    return conDestinationdCn;
    }
    /*conDestinationdCn set 方法 */
    public void setConDestinationdCn(String  conDestinationdCn){
    this.conDestinationdCn=conDestinationdCn;
    }
    /*conDestinationdEn get 方法 */
    public String getConDestinationdEn(){
    return conDestinationdEn;
    }
    /*conDestinationdEn set 方法 */
    public void setConDestinationdEn(String  conDestinationdEn){
    this.conDestinationdEn=conDestinationdEn;
    }
    /*conDeliveryDate get 方法 */
    public Timestamp getConDeliveryDate(){
    return conDeliveryDate;
    }
    /*conDeliveryDate set 方法 */
    public void setConDeliveryDate(Timestamp  conDeliveryDate){
    this.conDeliveryDate=conDeliveryDate;
    }
    /*conPaymentMethod get 方法 */
    public String getConPaymentMethod(){
    return conPaymentMethod;
    }
    /*conPaymentMethod set 方法 */
    public void setConPaymentMethod(String  conPaymentMethod){
    this.conPaymentMethod=conPaymentMethod;
    }
    /*conPaymentMethodText get 方法 */
    public String getConPaymentMethodText(){
    return conPaymentMethodText;
    }
    /*conPaymentMethodText set 方法 */
    public void setConPaymentMethodText(String  conPaymentMethodText){
    this.conPaymentMethodText=conPaymentMethodText;
    }
    /*conPaymentMethodTextEn get 方法 */
    public String getConPaymentMethodTextEn(){
    return conPaymentMethodTextEn;
    }
    /*conPaymentMethodTextEn set 方法 */
    public void setConPaymentMethodTextEn(String  conPaymentMethodTextEn){
    this.conPaymentMethodTextEn=conPaymentMethodTextEn;
    }
    /*conSpecialAgreementEn get 方法 */
    public String getConSpecialAgreementEn(){
    return conSpecialAgreementEn;
    }
    /*conSpecialAgreementEn set 方法 */
    public void setConSpecialAgreementEn(String  conSpecialAgreementEn){
    this.conSpecialAgreementEn=conSpecialAgreementEn;
    }
    /*conSpecialAgreement get 方法 */
    public String getConSpecialAgreement(){
    return conSpecialAgreement;
    }
    /*conSpecialAgreement set 方法 */
    public void setConSpecialAgreement(String  conSpecialAgreement){
    this.conSpecialAgreement=conSpecialAgreement;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*createName get 方法 */
    public String getCreateName(){
    return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
    this.createName=createName;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*modifier get 方法 */
    public Long getModifier(){
    return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
    this.modifier=modifier;
    }
    /*modifierUsername get 方法 */
    public String getModifierUsername(){
    return modifierUsername;
    }
    /*modifierUsername set 方法 */
    public void setModifierUsername(String  modifierUsername){
    this.modifierUsername=modifierUsername;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
    /*purchaseOrderId get 方法 */
    public Long getPurchaseOrderId(){
    return purchaseOrderId;
    }
    /*purchaseOrderId set 方法 */
    public void setPurchaseOrderId(Long  purchaseOrderId){
    this.purchaseOrderId=purchaseOrderId;
    }
    /*purchaseOrderCode get 方法 */
    public String getPurchaseOrderCode(){
    return purchaseOrderCode;
    }
    /*purchaseOrderCode set 方法 */
    public void setPurchaseOrderCode(String  purchaseOrderCode){
    this.purchaseOrderCode=purchaseOrderCode;
    }
    /*startPointDepotName get 方法 */
    public String getStartPointDepotName(){
    return startPointDepotName;
    }
    /*startPointDepotName set 方法 */
    public void setStartPointDepotName(String  startPointDepotName){
    this.startPointDepotName=startPointDepotName;
    }
    /*startPointDepotId get 方法 */
    public Long getStartPointDepotId(){
    return startPointDepotId;
    }
    /*startPointDepotId set 方法 */
    public void setStartPointDepotId(Long  startPointDepotId){
    this.startPointDepotId=startPointDepotId;
    }
    /*startPointPoNo get 方法 */
    public String getStartPointPoNo(){
    return startPointPoNo;
    }
    /*startPointPoNo set 方法 */
    public void setStartPointPoNo(String  startPointPoNo){
    this.startPointPoNo=startPointPoNo;
    }
    /*oneDepotName get 方法 */
    public String getOneDepotName(){
    return oneDepotName;
    }
    /*oneDepotName set 方法 */
    public void setOneDepotName(String  oneDepotName){
    this.oneDepotName=oneDepotName;
    }
    /*oneDepotId get 方法 */
    public Long getOneDepotId(){
    return oneDepotId;
    }
    /*oneDepotId set 方法 */
    public void setOneDepotId(Long  oneDepotId){
    this.oneDepotId=oneDepotId;
    }
    /*onePoNo get 方法 */
    public String getOnePoNo(){
    return onePoNo;
    }
    /*onePoNo set 方法 */
    public void setOnePoNo(String  onePoNo){
    this.onePoNo=onePoNo;
    }
    /*twoDepotName get 方法 */
    public String getTwoDepotName(){
    return twoDepotName;
    }
    /*twoDepotName set 方法 */
    public void setTwoDepotName(String  twoDepotName){
    this.twoDepotName=twoDepotName;
    }
    /*twoDepotId get 方法 */
    public Long getTwoDepotId(){
    return twoDepotId;
    }
    /*twoDepotId set 方法 */
    public void setTwoDepotId(Long  twoDepotId){
    this.twoDepotId=twoDepotId;
    }
    /*twoPoNo get 方法 */
    public String getTwoPoNo(){
    return twoPoNo;
    }
    /*twoPoNo set 方法 */
    public void setTwoPoNo(String  twoPoNo){
    this.twoPoNo=twoPoNo;
    }
    /*threeDepotName get 方法 */
    public String getThreeDepotName(){
    return threeDepotName;
    }
    /*threeDepotName set 方法 */
    public void setThreeDepotName(String  threeDepotName){
    this.threeDepotName=threeDepotName;
    }
    /*threeDepotId get 方法 */
    public Long getThreeDepotId(){
    return threeDepotId;
    }
    /*threeDepotId set 方法 */
    public void setThreeDepotId(Long  threeDepotId){
    this.threeDepotId=threeDepotId;
    }
    /*threePoNo get 方法 */
    public String getThreePoNo(){
    return threePoNo;
    }
    /*threePoNo set 方法 */
    public void setThreePoNo(String  threePoNo){
    this.threePoNo=threePoNo;
    }
    /*fourDepotName get 方法 */
    public String getFourDepotName(){
    return fourDepotName;
    }
    /*fourDepotName set 方法 */
    public void setFourDepotName(String  fourDepotName){
    this.fourDepotName=fourDepotName;
    }
    /*fourDepotId get 方法 */
    public Long getFourDepotId(){
    return fourDepotId;
    }
    /*fourDepotId set 方法 */
    public void setFourDepotId(Long  fourDepotId){
    this.fourDepotId=fourDepotId;
    }
    /*fourPoNo get 方法 */
    public String getFourPoNo(){
    return fourPoNo;
    }
    /*fourPoNo set 方法 */
    public void setFourPoNo(String  fourPoNo){
    this.fourPoNo=fourPoNo;
    }

    public String getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo;
    }
}
