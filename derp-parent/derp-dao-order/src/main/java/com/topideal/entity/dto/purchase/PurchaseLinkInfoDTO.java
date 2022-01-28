package com.topideal.entity.dto.purchase;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@ApiModel
public class PurchaseLinkInfoDTO extends PageModel implements Serializable{

    /**
    * 自增ID
    */
	@ApiModelProperty(value="采购链路明细ID")
    private Long id;
    /**
    * 公司ID
    */
	@ApiModelProperty(value="公司ID")
    private Long merchantId;
    /**
    * 公司名
    */
	@ApiModelProperty(value="公司名")
    private String merchantName;
    /**
    * 链路ID
    */
	@ApiModelProperty(value="配置链路ID")
    private Long tradeLinkId;
    /**
    * 归属时间
    */
	@ApiModelProperty(value="归属时间")
    private Timestamp infoAuditDate;
    /**
    * 币种
    */
	@ApiModelProperty(value="币种")
    private String infoCurrency;
	@ApiModelProperty(value="币种中文")
    private String infoCurrencyLabel ;
    /**
    * 业务模式 4.购销-实销实结
    */
	@ApiModelProperty(value="业务模式 4.购销-实销实结")
    private String infoBusinessModel;
	@ApiModelProperty(value="业务模式中文 4.购销-实销实结")
    private String infoBusinessModelLabel;
    /**
    * 装货港
    */
	@ApiModelProperty(value="装货港")
    private String infoLoadPort;
    /**
    * 卸货港
    */
	@ApiModelProperty(value="卸货港")
    private String infoUnloadPort;
    /**
    * 装船时间
    */
	@ApiModelProperty(value="装船时间")
    private Timestamp infoShipDate;
    /**
    * 运输方式   1-空运 2-海运 3-陆运 4-港到仓拖车 5-中欧铁路 6-其他
    */
	@ApiModelProperty(value="运输方式   1-空运 2-海运 3-陆运 4-港到仓拖车 5-中欧铁路 6-其他")
    private String infoTransport;
    /**
    * 承运商
    */
	@ApiModelProperty(value="承运商")
    private String infoCarrier;
    /**
    * 车次
    */
	@ApiModelProperty(value="车次")
    private String infoTrainNumber;
    /**
    * 标准箱TEU
    */
	@ApiModelProperty(value="标准箱TEU")
    private String infoStandardCaseTeu;
    /**
    * 托数
    */
	@ApiModelProperty(value="托数")
    private Integer infoTorrNum;
    /**
    * LBX单号
    */
	@ApiModelProperty(value="LBX单号")
    private String infoLbxNo;
    /**
    * 提单号
    */
	@ApiModelProperty(value="提单号")
    private String infoLadingBill;
    /**
    * 提单毛重
    */
	@ApiModelProperty(value="提单毛重")
    private Double infoGrossWeight;
    /**
    * 运输方式
    */
	@ApiModelProperty(value="运输方式")
    private String conMeansOfTransportation;
    /**
    * 始发地（港）
    */
	@ApiModelProperty(value="始发地（港）")
    private String conLoadingCnPort;
    /**
    * 始发地（港）英文
    */
	@ApiModelProperty(value="始发地（港）英文")
    private String conLoadingEnPort;
    /**
    * 目的地（港）
    */
	@ApiModelProperty(value="目的地（港）")
    private String conDestinationdCn;
    /**
    * 目的地（港）英
    */
	@ApiModelProperty(value="目的地（港）英")
    private String conDestinationdEn;
    /**
    * 交货日期
    */
	@ApiModelProperty(value="交货日期")
    private Timestamp conDeliveryDate;
    /**
    * 付款方式1-一次结清 2-分批付款 3- 其他
    */
	@ApiModelProperty(value="付款方式1-一次结清 2-分批付款 3- 其他")
    private String conPaymentMethod;
    /**
    * 付款方式文案
    */
	@ApiModelProperty(value="付款方式文案")
    private String conPaymentMethodText;
    /**
    * 付款方式文案 英文
    */
	@ApiModelProperty(value="付款方式文案 英文")
    private String conPaymentMethodTextEn;
    /**
    * 特别约定英文
    */
	@ApiModelProperty(value="特别约定英文")
    private String conSpecialAgreementEn;
    /**
    * 特别约定
    */
	@ApiModelProperty(value="特别约定")
    private String conSpecialAgreement;
    /**
    * 创建人
    */
	@ApiModelProperty(value="创建人")
    private Long creater;
    /**
    * 创建人用户名
    */
	@ApiModelProperty(value="创建人用户名")
    private String createName;
    /**
    * 创建时间
    */
	@ApiModelProperty(value="创建时间")
    private Timestamp createDate;
    /**
    * 修改人
    */
	@ApiModelProperty(value="修改人")
    private Long modifier;
    /**
    * 修改人用户名
    */
	@ApiModelProperty(value="修改人用户名")
    private String modifierUsername;
    /**
    * 修改时间
    */
	@ApiModelProperty(value="修改时间")
    private Timestamp modifyDate;
    /**
    * 采购订单ID
    */
	@ApiModelProperty(value="采购订单ID")
    private Long purchaseOrderId;
    /**
    * 采购订单编码
    */
	@ApiModelProperty(value="采购订单编码")
    private String purchaseOrderCode;
    /**
    * 仓库1名称
    */
	@ApiModelProperty(value="起点仓库名称")
    private String startPointDepotName;
    /**
    * 仓库1ID
    */
	@ApiModelProperty(value="起点仓库ID")
    private Long startPointDepotId;
    /**
    * po号1
    */
	@ApiModelProperty(value="起点PO号")
    private String startPointPoNo;
	@ApiModelProperty(value="起点公司ID")
    private Long startPointMerchantId ;
	@ApiModelProperty(value="起点公司名")
    private String startPointMerchantName ;
	@ApiModelProperty(value="起点事业部ID")
    private Long startPointBuId ;
	@ApiModelProperty(value="起点事业部名")
    private String startPointBuName ;
	@ApiModelProperty(value="起点加价比例")
    private BigDecimal startPointPremiumRate;
    
    /**
    * 仓库2名称
    */
	@ApiModelProperty(value="客户1仓库名称")
    private String oneDepotName;
    /**
    * 仓库2ID
    */
	@ApiModelProperty(value="客户1仓库ID")
    private Long oneDepotId;
    /**
    * po号2
    */
	@ApiModelProperty(value="客户1po号")
    private String onePoNo;
	@ApiModelProperty(value="客户1ID")
    private Long oneCustomerId ;
	@ApiModelProperty(value="客户1名称")
    private String oneCustomerName ;
	@ApiModelProperty(value="客户1事业部ID")
    private Long oneBuId ;
	@ApiModelProperty(value="客户1事业部名称")
    private String oneBuName ;
	@ApiModelProperty(value="客户1加价比例")
    private BigDecimal onePremiumRate;
    /**
    * 仓库3名称
    */
	@ApiModelProperty(value="客户2仓库名称")
    private String twoDepotName;
    /**
    * 仓库3ID
    */
	@ApiModelProperty(value="客户2仓库ID")
    private Long twoDepotId;
    /**
    * po号3
    */
	@ApiModelProperty(value="客户2po号")
    private String twoPoNo;
	@ApiModelProperty(value="客户2ID")
    private Long twoCustomerId ;
	@ApiModelProperty(value="客户2名称")
    private String twoCustomerName ;
	@ApiModelProperty(value="客户2事业部ID")
    private Long twoBuId ;
	@ApiModelProperty(value="客户2事业部名称")
    private String twoBuName ;
	@ApiModelProperty(value="客户2加价比例")
    private BigDecimal twoPremiumRate;
    /**
    * 仓库4名称
    */
	@ApiModelProperty(value="客户3仓库名称")
    private String threeDepotName;
    /**
    * 仓库4ID
    */
    @ApiModelProperty(value="客户3仓库ID")
    private Long threeDepotId;
    /**
    * po号4
    */
    @ApiModelProperty(value="客户3po号")
    private String threePoNo;
    @ApiModelProperty(value="客户3ID")
    private Long threeCustomerId ;
    @ApiModelProperty(value="客户3名称")
    private String threeCustomerName ;
    @ApiModelProperty(value="客户3事业部ID")
    private Long threeBuId ;
    @ApiModelProperty(value="客户3事业部名称")
    private String threeBuName ;
    @ApiModelProperty(value="客户3加价比例")
    private BigDecimal threePremiumRate;
    /**
    * 仓库5名称
    */
    @ApiModelProperty(value="客户3仓库名称")
    private String fourDepotName;
    /**
    * 仓库5ID
    */
    @ApiModelProperty(value="客户4仓库ID")
    private Long fourDepotId;
    /**
    * po号5
    */
    @ApiModelProperty(value="客户4po号")
    private String fourPoNo;
    @ApiModelProperty(value="客户4ID")
    private Long fourCustomerId ;
    @ApiModelProperty(value="客户4名称")
    private String fourCustomerName ;
    @ApiModelProperty(value="客户4事业部ID")
    private Long fourBuId ;
    @ApiModelProperty(value="客户4事业部名称")
    private String fourBuName ;
    @ApiModelProperty(value="客户4加价比例")
    private BigDecimal fourPremiumRate;
    @ApiModelProperty(value="客户1类型(1内部,2外部)")
    private String oneType ;
    @ApiModelProperty(value="客户2类型(1内部,2外部)")
    private String twoType ;
    @ApiModelProperty(value="客户3类型(1内部,2外部)")
    private String threeType ;
    @ApiModelProperty(value="客户4类型(1内部,2外部)")
    private String fourType ;
    @ApiModelProperty(value="客户1ID类型(1客户id,2公司id)")
    private String oneIdValueType ;
    @ApiModelProperty(value="客户2ID类型(1客户id,2公司id)")
    private String twoIdValueType ;
    @ApiModelProperty(value="客户3ID类型(1客户id,2公司id)")
    private String threeIdValueType ;
    @ApiModelProperty(value="客户4ID类型(1客户id,2公司id)")
    private String fourIdValueType ;
    @ApiModelProperty(value="仓库类型")
    private String depotType ;
    @ApiModelProperty(value="修改商品信息")
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
    this.infoCurrencyLabel=DERP.getLabelByKey(DERP.currencyCodeList, infoCurrency) ;
    }
    /*infoBusinessModel get 方法 */
    public String getInfoBusinessModel(){
    return infoBusinessModel;
    }
    /*infoBusinessModel set 方法 */
    public void setInfoBusinessModel(String  infoBusinessModel){
    this.infoBusinessModel=infoBusinessModel;
    this.infoBusinessModelLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleOrder_businessModelList, infoBusinessModel) ;
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
	public String getInfoCurrencyLabel() {
		return infoCurrencyLabel;
	}
	public void setInfoCurrencyLabel(String infoCurrencyLabel) {
		this.infoCurrencyLabel = infoCurrencyLabel;
	}
	public String getInfoBusinessModelLabel() {
		return infoBusinessModelLabel;
	}
	public void setInfoBusinessModelLabel(String infoBusinessModelLabel) {
		this.infoBusinessModelLabel = infoBusinessModelLabel;
	}
	public Long getStartPointMerchantId() {
		return startPointMerchantId;
	}
	public void setStartPointMerchantId(Long startPointMerchantId) {
		this.startPointMerchantId = startPointMerchantId;
	}
	public String getStartPointMerchantName() {
		return startPointMerchantName;
	}
	public void setStartPointMerchantName(String startPointMerchantName) {
		this.startPointMerchantName = startPointMerchantName;
	}
	public Long getStartPointBuId() {
		return startPointBuId;
	}
	public void setStartPointBuId(Long startPointBuId) {
		this.startPointBuId = startPointBuId;
	}
	public String getStartPointBuName() {
		return startPointBuName;
	}
	public void setStartPointBuName(String startPointBuName) {
		this.startPointBuName = startPointBuName;
	}
	public BigDecimal getStartPointPremiumRate() {
		return startPointPremiumRate;
	}
	public void setStartPointPremiumRate(BigDecimal startPointPremiumRate) {
		this.startPointPremiumRate = startPointPremiumRate;
	}
	public Long getOneCustomerId() {
		return oneCustomerId;
	}
	public void setOneCustomerId(Long oneCustomerId) {
		this.oneCustomerId = oneCustomerId;
	}
	public String getOneCustomerName() {
		return oneCustomerName;
	}
	public void setOneCustomerName(String oneCustomerName) {
		this.oneCustomerName = oneCustomerName;
	}
	public Long getOneBuId() {
		return oneBuId;
	}
	public void setOneBuId(Long oneBuId) {
		this.oneBuId = oneBuId;
	}
	public String getOneBuName() {
		return oneBuName;
	}
	public void setOneBuName(String oneBuName) {
		this.oneBuName = oneBuName;
	}
	public BigDecimal getOnePremiumRate() {
		return onePremiumRate;
	}
	public void setOnePremiumRate(BigDecimal onePremiumRate) {
		this.onePremiumRate = onePremiumRate;
	}
	public Long getTwoCustomerId() {
		return twoCustomerId;
	}
	public void setTwoCustomerId(Long twoCustomerId) {
		this.twoCustomerId = twoCustomerId;
	}
	public String getTwoCustomerName() {
		return twoCustomerName;
	}
	public void setTwoCustomerName(String twoCustomerName) {
		this.twoCustomerName = twoCustomerName;
	}
	public Long getTwoBuId() {
		return twoBuId;
	}
	public void setTwoBuId(Long twoBuId) {
		this.twoBuId = twoBuId;
	}
	public String getTwoBuName() {
		return twoBuName;
	}
	public void setTwoBuName(String twoBuName) {
		this.twoBuName = twoBuName;
	}
	public BigDecimal getTwoPremiumRate() {
		return twoPremiumRate;
	}
	public void setTwoPremiumRate(BigDecimal twoPremiumRate) {
		this.twoPremiumRate = twoPremiumRate;
	}
	public Long getThreeCustomerId() {
		return threeCustomerId;
	}
	public void setThreeCustomerId(Long threeCustomerId) {
		this.threeCustomerId = threeCustomerId;
	}
	public String getThreeCustomerName() {
		return threeCustomerName;
	}
	public void setThreeCustomerName(String threeCustomerName) {
		this.threeCustomerName = threeCustomerName;
	}
	public Long getThreeBuId() {
		return threeBuId;
	}
	public void setThreeBuId(Long threeBuId) {
		this.threeBuId = threeBuId;
	}
	public String getThreeBuName() {
		return threeBuName;
	}
	public void setThreeBuName(String threeBuName) {
		this.threeBuName = threeBuName;
	}
	public BigDecimal getThreePremiumRate() {
		return threePremiumRate;
	}
	public void setThreePremiumRate(BigDecimal threePremiumRate) {
		this.threePremiumRate = threePremiumRate;
	}
	public Long getFourCustomerId() {
		return fourCustomerId;
	}
	public void setFourCustomerId(Long fourCustomerId) {
		this.fourCustomerId = fourCustomerId;
	}
	public String getFourCustomerName() {
		return fourCustomerName;
	}
	public void setFourCustomerName(String fourCustomerName) {
		this.fourCustomerName = fourCustomerName;
	}
	public Long getFourBuId() {
		return fourBuId;
	}
	public void setFourBuId(Long fourBuId) {
		this.fourBuId = fourBuId;
	}
	public String getFourBuName() {
		return fourBuName;
	}
	public void setFourBuName(String fourBuName) {
		this.fourBuName = fourBuName;
	}
	public BigDecimal getFourPremiumRate() {
		return fourPremiumRate;
	}
	public void setFourPremiumRate(BigDecimal fourPremiumRate) {
		this.fourPremiumRate = fourPremiumRate;
	}
	public String getOneType() {
		return oneType;
	}
	public void setOneType(String oneType) {
		this.oneType = oneType;
	}
	public String getTwoType() {
		return twoType;
	}
	public void setTwoType(String twoType) {
		this.twoType = twoType;
	}
	public String getThreeType() {
		return threeType;
	}
	public void setThreeType(String threeType) {
		this.threeType = threeType;
	}
	public String getFourType() {
		return fourType;
	}
	public void setFourType(String fourType) {
		this.fourType = fourType;
	}
	public String getOneIdValueType() {
		return oneIdValueType;
	}
	public void setOneIdValueType(String oneIdValueType) {
		this.oneIdValueType = oneIdValueType;
	}
	public String getTwoIdValueType() {
		return twoIdValueType;
	}
	public void setTwoIdValueType(String twoIdValueType) {
		this.twoIdValueType = twoIdValueType;
	}
	public String getThreeIdValueType() {
		return threeIdValueType;
	}
	public void setThreeIdValueType(String threeIdValueType) {
		this.threeIdValueType = threeIdValueType;
	}
	public String getFourIdValueType() {
		return fourIdValueType;
	}
	public void setFourIdValueType(String fourIdValueType) {
		this.fourIdValueType = fourIdValueType;
	}
	public String getDepotType() {
		return depotType;
	}
	public void setDepotType(String depotType) {
		this.depotType = depotType;
	}

    public String getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo;
    }
}
