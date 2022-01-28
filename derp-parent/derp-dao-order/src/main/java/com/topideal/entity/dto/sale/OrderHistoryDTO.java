package com.topideal.entity.dto.sale;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.vo.sale.WayBillModel;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class OrderHistoryDTO extends PageModel implements Serializable{
    //订购人电话
    private String makerTel;
    //申报时间
    private Timestamp declareDate;
    //进口模式10：BBC;20：BC;30：保留; 40：CC
    private String importMode;
    private String importModeLabel;
    //收件人电话
    private String receiverTel;
    //订单号(自生成)
    private String code;
    //外部单号
    private String externalCode;
    //交易时间
    private Timestamp tradingDate;
    //货款
    private BigDecimal goodsAmount;
    //仓库ID
    private Long depotId;
    //税费
    private BigDecimal taxes;
    //收件人省份
    private String receiverProvince;
    //备注
    private String remark;
    //申报方案ID
    private Long declarePlan;
    //收件人城市
    private String receiverCity;
    //收货地址
    private String receiverAddress;
    //商家名称
    private String merchantName;
    //电商平台名称
    private String storePlatformName;
    private String storePlatformNameLabel;
    //收件人国家
    private String receiverCountry;
    //商家ID
    private Long merchantId;
    //发货时间
    private Timestamp deliverDate;
    //id
    private Long id;
    //订购人
    private String makerName;
    //审核时间
    private Timestamp auditDate;
    //创建时间
    private Timestamp createDate;
    //仓库名称
    private String depotName;
    // 订单来源  1:跨境宝推送, 2:导入,3:第e仓 4: 订单100
    private Integer orderSource;
    private String orderSourceLabel;
    //放行时间
    private Timestamp releaseDate;
    //收件人名字
    private String receiverName;
    //收件人区
    private String receiverArea;
    //订购人注册号
    private String makerRegisterNo;
    //国检状态 11：已报国检12：国检放行13：国检审核驳回14：国检抽检15：国检抽检未过
    private String inspectStatus;
    private String inspectStatusLabel;
    //运费  两位小数
    private BigDecimal wayFrtFee;
    //物流企业名称
    private String logisticsName;
    private String logisticsNameLabel;
    //保税   两位小数
    private BigDecimal wayIndFee;
    //创建人
    private Long creater;
    //运单号
    private String wayBillNo;
    //单据状态：1:待申报 2.待放行,3:待发货,4:已发货,5:已取消
    private String status;
    private String statusLabel;
    //海关状态 21：已报海关22：海关单证放行23：报海关失败24：海关查验/转人工/挂起等
    //25：海关单证审核不通过26：海关已接受申报，待货物运抵后处理41：海关货物查扣42：海关货物放行
    private String customsStatus;
    private String customsStatusLabel;
    //订单实付金额
    private BigDecimal payment;
    //表体数据
    private List<OrderItemDTO> itemList;
    //运单信息
    private WayBillModel wayBill;
    //优惠减免金额
    private BigDecimal discount;
    //制单时间
    private Timestamp makingTime;
    //修改时间
    private Timestamp modifyDate;
    // 店铺名称
    private String shopName;
    // 客户id
    private Long customerId;
    // 店铺编码
    private String shopCode;
     // 客户名称
     private String customerName;
    //包裹重量
    private Double weight;
    
    //扩展字段
    private String batchNo;//批次
    private Timestamp productionDate;//生效日期
    private Timestamp overdueDate;//失效日期
    
	// 交易开始时间
	private String tradingStartDate;
	// 交易结束时间
	private String tradingEndDate; 	 	
	private String deliverStartDate;// 发货时间开始
	private String deliverEndDate;//发货时间结束

   //总佣金
   private BigDecimal saleCom;

   //币种
   private String currency;
   private String currencyLabel;

   //平台订单号
   private String exOrderId;

    //店铺类型值编码
    private String shopTypeCode;
    private String shopTypeCodeLabel;
    
    //商品名称
    private String goodsName;
    //商品货号
    private String goodsNo;
    //条形码
   private String barcode;
   //商品编码
   private String goodsCode;
   //商品ID
   private Long goodsId;
   //理货单位 00-托盘 01-箱 02-件
   private String tallyingUnit ;

    /**
     * 是否生成暂估费用 0-未生成 1-已生成
     */
    private String isGenerate;
    /**
	 * 内贸实付金额（不含税）
	 */
	private BigDecimal tradePayment;
	/**
	 * 内贸税额
	 */
	private BigDecimal tradePaymentTax;

    public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Timestamp getMakingTime() {
		return makingTime;
	}
	public void setMakingTime(Timestamp makingTime) {
		this.makingTime = makingTime;
	}
	/*discount get 方法 */
    public BigDecimal getDiscount(){
        return discount;
    }
    /*discount set 方法 */
    public void setDiscount(BigDecimal  discount){
        this.discount=discount;
    }

    /*payment get 方法 */
    public BigDecimal getPayment() {
		return payment;
	}
    /*payment set 方法 */
	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}
	/*receiverAddress get 方法 */
   public String getReceiverAddress() {
		return receiverAddress;
	}
   /*receiverAddress set 方法 */
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	/*makerTel get 方法 */
   public String getMakerTel(){
       return makerTel;
   }
   /*makerTel set 方法 */
   public void setMakerTel(String  makerTel){
       this.makerTel=makerTel;
   }
   /*declareDate get 方法 */
   public Timestamp getDeclareDate(){
       return declareDate;
   }
   /*declareDate set 方法 */
   public void setDeclareDate(Timestamp  declareDate){
       this.declareDate=declareDate;
   }
   /*importMode get 方法 */
   public String getImportMode(){
       return importMode;
   }
   /*importMode set 方法 */
   public void setImportMode(String  importMode){
       this.importMode=importMode;
		if(StringUtils.isNotBlank(importMode)){
			this.importModeLabel=DERP_ORDER.getLabelByKey(DERP_ORDER.order_importModeList, importMode);
		}
   }
   /*receiverTel get 方法 */
   public String getReceiverTel(){
       return receiverTel;
   }
   /*receiverTel set 方法 */
   public void setReceiverTel(String  receiverTel){
       this.receiverTel=receiverTel;
   }
   /*code get 方法 */
   public String getCode(){
       return code;
   }
   /*code set 方法 */
   public void setCode(String  code){
       this.code=code;
   }
   /*externalCode get 方法 */
   public String getExternalCode(){
       return externalCode;
   }
   /*externalCode set 方法 */
   public void setExternalCode(String  externalCode){
       this.externalCode=externalCode;
   }
   /*tradingDate get 方法 */
   public Timestamp getTradingDate(){
       return tradingDate;
   }
   /*tradingDate set 方法 */
   public void setTradingDate(Timestamp  tradingDate){
       this.tradingDate=tradingDate;
   }
   /*goodsAmount get 方法 */
   public BigDecimal getGoodsAmount(){
       return goodsAmount;
   }
   /*goodsAmount set 方法 */
   public void setGoodsAmount(BigDecimal  goodsAmount){
       this.goodsAmount=goodsAmount;
   }
   /*depotId get 方法 */
   public Long getDepotId(){
       return depotId;
   }
   /*depotId set 方法 */
   public void setDepotId(Long  depotId){
       this.depotId=depotId;
   }
   /*taxes get 方法 */
   public BigDecimal getTaxes(){
       return taxes;
   }
   /*taxes set 方法 */
   public void setTaxes(BigDecimal  taxes){
       this.taxes=taxes;
   }
   /*receiverProvince get 方法 */
   public String getReceiverProvince(){
       return receiverProvince;
   }
   /*receiverProvince set 方法 */
   public void setReceiverProvince(String  receiverProvince){
       this.receiverProvince=receiverProvince;
   }
   /*remark get 方法 */
   public String getRemark(){
       return remark;
   }
   /*remark set 方法 */
   public void setRemark(String  remark){
       this.remark=remark;
   }
   /*declarePlan get 方法 */
   public Long getDeclarePlan(){
       return declarePlan;
   }
   /*declarePlan set 方法 */
   public void setDeclarePlan(Long  declarePlan){
       this.declarePlan=declarePlan;
   }
   /*receiverCity get 方法 */
   public String getReceiverCity(){
       return receiverCity;
   }
   /*receiverCity set 方法 */
   public void setReceiverCity(String  receiverCity){
       this.receiverCity=receiverCity;
   }
   /*merchantName get 方法 */
   public String getMerchantName(){
       return merchantName;
   }
   /*merchantName set 方法 */
   public void setMerchantName(String  merchantName){
       this.merchantName=merchantName;
   }
   /*storePlatformName get 方法 */
   public String getStorePlatformName(){
       return storePlatformName;
   }
   /*storePlatformName set 方法 */
   public void setStorePlatformName(String  storePlatformName){
       this.storePlatformName=storePlatformName;
       if(StringUtils.isNotBlank(storePlatformName)){
    	   this.storePlatformNameLabel = DERP.getLabelByKey(DERP.storePlatformList, storePlatformName);
       }
   }
   /*receiverCountry get 方法 */
   public String getReceiverCountry(){
       return receiverCountry;
   }
   /*receiverCountry set 方法 */
   public void setReceiverCountry(String  receiverCountry){
       this.receiverCountry=receiverCountry;
   }
   /*merchantId get 方法 */
   public Long getMerchantId(){
       return merchantId;
   }
   /*merchantId set 方法 */
   public void setMerchantId(Long  merchantId){
       this.merchantId=merchantId;
   }
   /*deliverDate get 方法 */
   public Timestamp getDeliverDate(){
       return deliverDate;
   }
   /*deliverDate set 方法 */
   public void setDeliverDate(Timestamp  deliverDate){
       this.deliverDate=deliverDate;
   }
   /*id get 方法 */
   public Long getId(){
       return id;
   }
   /*id set 方法 */
   public void setId(Long  id){
       this.id=id;
   }
   /*makerName get 方法 */
   public String getMakerName(){
       return makerName;
   }
   /*makerName set 方法 */
   public void setMakerName(String  makerName){
       this.makerName=makerName;
   }
   /*auditDate get 方法 */
   public Timestamp getAuditDate(){
       return auditDate;
   }
   /*auditDate set 方法 */
   public void setAuditDate(Timestamp  auditDate){
       this.auditDate=auditDate;
   }
   /*createDate get 方法 */
   public Timestamp getCreateDate(){
       return createDate;
   }
   /*createDate set 方法 */
   public void setCreateDate(Timestamp  createDate){
       this.createDate=createDate;
   }
   /*depotName get 方法 */
   public String getDepotName(){
       return depotName;
   }
   /*depotName set 方法 */
   public void setDepotName(String  depotName){
       this.depotName=depotName;
   }
   /*orderSource get 方法 */
   public Integer getOrderSource(){
       return orderSource;
   }
   /*orderSource set 方法 */
   public void setOrderSource(Integer  orderSource){
       this.orderSource=orderSource;
       if(orderSource!=null){
    	   this.orderSourceLabel = DERP.getLabelByKey(DERP.dataSourceList, String.valueOf(orderSource));
       }
   }
   /*releaseDate get 方法 */
   public Timestamp getReleaseDate(){
       return releaseDate;
   }
   /*releaseDate set 方法 */
   public void setReleaseDate(Timestamp  releaseDate){
       this.releaseDate=releaseDate;
   }
   /*receiverName get 方法 */
   public String getReceiverName(){
       return receiverName;
   }
   /*receiverName set 方法 */
   public void setReceiverName(String  receiverName){
       this.receiverName=receiverName;
   }
   /*receiverArea get 方法 */
   public String getReceiverArea(){
       return receiverArea;
   }
   /*receiverArea set 方法 */
   public void setReceiverArea(String  receiverArea){
       this.receiverArea=receiverArea;
   }
   /*makerRegisterNo get 方法 */
   public String getMakerRegisterNo(){
       return makerRegisterNo;
   }
   /*makerRegisterNo set 方法 */
   public void setMakerRegisterNo(String  makerRegisterNo){
       this.makerRegisterNo=makerRegisterNo;
   }
   /*inspectStatus get 方法 */
   public String getInspectStatus(){
       return inspectStatus;
   }
   /*inspectStatus set 方法 */
   public void setInspectStatus(String  inspectStatus){
       this.inspectStatus=inspectStatus;
		if(StringUtils.isNotBlank(inspectStatus)){
			this.inspectStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.order_inspectStatusList, inspectStatus);
		}
   }
   /*wayFrtFee get 方法 */
   public BigDecimal getWayFrtFee(){
       return wayFrtFee;
   }
   /*wayFrtFee set 方法 */
   public void setWayFrtFee(BigDecimal  wayFrtFee){
       this.wayFrtFee=wayFrtFee;
   }
   /*logisticsName get 方法 */
   public String getLogisticsName(){
       return logisticsName;
   }
   /*logisticsName set 方法 */
   public void setLogisticsName(String  logisticsName){
       this.logisticsName=logisticsName;
       if(StringUtils.isNotBlank(logisticsName)){
           this.logisticsNameLabel = DERP.getLabelByKey(DERP.logisticsNameList, logisticsName);
       }
   }
   /*wayIndFee get 方法 */
   public BigDecimal getWayIndFee(){
       return wayIndFee;
   }
   /*wayIndFee set 方法 */
   public void setWayIndFee(BigDecimal  wayIndFee){
       this.wayIndFee=wayIndFee;
   }
   /*creater get 方法 */
   public Long getCreater(){
       return creater;
   }
   /*creater set 方法 */
   public void setCreater(Long  creater){
       this.creater=creater;
   }
   /*wayBillNo get 方法 */
   public String getWayBillNo(){
       return wayBillNo;
   }
   /*wayBillNo set 方法 */
   public void setWayBillNo(String  wayBillNo){
       this.wayBillNo=wayBillNo;
   }
   /*status get 方法 */
   public String getStatus(){
       return status;
   }
   /*status set 方法 */
   public void setStatus(String  status){
       this.status=status;
		if(StringUtils.isNotBlank(status)){
			this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.order_statusList, status);
		}
   }
   /*customsStatus get 方法 */
   public String getCustomsStatus(){
       return customsStatus;
   }
   /*customsStatus set 方法 */
   public void setCustomsStatus(String  customsStatus){
       this.customsStatus=customsStatus;
		if(StringUtils.isNotBlank(customsStatus)){
			this.customsStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.order_customsStatusList, customsStatus);
		}
   }
	public List<OrderItemDTO> getItemList() {
		return itemList;
	}
	public void setItemList(List<OrderItemDTO> itemList) {
		this.itemList = itemList;
	}
	public WayBillModel getWayBill() {
		return wayBill;
	}
	public void setWayBill(WayBillModel wayBill) {
		this.wayBill = wayBill;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public Timestamp getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(Timestamp productionDate) {
		this.productionDate = productionDate;
	}
	public Timestamp getOverdueDate() {
		return overdueDate;
	}
	public void setOverdueDate(Timestamp overdueDate) {
		this.overdueDate = overdueDate;
	}
	public String getTradingStartDate() {
		return tradingStartDate;
	}
	public void setTradingStartDate(String tradingStartDate) {
		this.tradingStartDate = tradingStartDate;
	}
	public String getTradingEndDate() {
		return tradingEndDate;
	}
	public void setTradingEndDate(String tradingEndDate) {
		this.tradingEndDate = tradingEndDate;
	}
	public String getDeliverStartDate() {
		return deliverStartDate;
	}
	public void setDeliverStartDate(String deliverStartDate) {
		this.deliverStartDate = deliverStartDate;
	}
	public String getDeliverEndDate() {
		return deliverEndDate;
	}
	public void setDeliverEndDate(String deliverEndDate) {
		this.deliverEndDate = deliverEndDate;
	}

   public BigDecimal getSaleCom() {
       return saleCom;
   }

   public void setSaleCom(BigDecimal saleCom) {
       this.saleCom = saleCom;
   }

   public String getCurrency() {
       return currency;
   }

   public void setCurrency(String currency) {
       this.currency = currency;
	   if(currency != null) {
			this.currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency) ;
	   }
   }

   public String getCurrencyLabel() {
	return currencyLabel;
}
public void setCurrencyLabel(String currencyLabel) {
	this.currencyLabel = currencyLabel;
}
public String getExOrderId() {
       return exOrderId;
   }

   public void setExOrderId(String exOrderId) {
       this.exOrderId = exOrderId;
   }
   // start
	public String getImportModeLabel() {
		return importModeLabel;
	}
	public void setImportModeLabel(String importModeLabel) {
		this.importModeLabel = importModeLabel;
	}

	public String getOrderSourceLabel() {
		return orderSourceLabel;
	}
	public void setOrderSourceLabel(String orderSourceLabel) {
		this.orderSourceLabel = orderSourceLabel;
	}
	public String getInspectStatusLabel() {
		return inspectStatusLabel;
	}
	public void setInspectStatusLabel(String inspectStatusLabel) {
		this.inspectStatusLabel = inspectStatusLabel;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	public String getCustomsStatusLabel() {
		return customsStatusLabel;
	}
	public void setCustomsStatusLabel(String customsStatusLabel) {
		this.customsStatusLabel = customsStatusLabel;
	}
	public String getStorePlatformNameLabel() {
		return storePlatformNameLabel;
	}
	public void setStorePlatformNameLabel(String storePlatformNameLabel) {
		this.storePlatformNameLabel = storePlatformNameLabel;
	}
	public String getShopTypeCode() {
		return shopTypeCode;
	}
	public void setShopTypeCode(String shopTypeCode) {
		this.shopTypeCode = shopTypeCode;
		this.shopTypeCodeLabel = DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, shopTypeCode);
	}
	public String getShopTypeCodeLabel() {
		return shopTypeCodeLabel;
	}
	public void setShopTypeCodeLabel(String shopTypeCodeLabel) {
		this.shopTypeCodeLabel = shopTypeCodeLabel;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

    public String getLogisticsNameLabel() {
        return logisticsNameLabel;
    }

    public void setLogisticsNameLabel(String logisticsNameLabel) {
        this.logisticsNameLabel = logisticsNameLabel;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getTallyingUnit() {
        return tallyingUnit;
    }

    public void setTallyingUnit(String tallyingUnit) {
        this.tallyingUnit = tallyingUnit;
    }

    public String getIsGenerate() {
        return isGenerate;
    }

    public void setIsGenerate(String isGenerate) {
        this.isGenerate = isGenerate;
    }
	public BigDecimal getTradePayment() {
		return tradePayment;
	}
	public void setTradePayment(BigDecimal tradePayment) {
		this.tradePayment = tradePayment;
	}
	public BigDecimal getTradePaymentTax() {
		return tradePaymentTax;
	}
	public void setTradePaymentTax(BigDecimal tradePaymentTax) {
		this.tradePaymentTax = tradePaymentTax;
	}
    
    
}
