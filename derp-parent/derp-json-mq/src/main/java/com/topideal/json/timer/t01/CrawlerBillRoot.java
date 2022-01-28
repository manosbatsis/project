package com.topideal.json.timer.t01;
import java.math.BigDecimal;
import java.sql.Timestamp;
/**
 * 爬虫账单
 * @author lian_
 *
 */
public class CrawlerBillRoot{

     //档期名称
     private String scheduleName;
     //id
     private String hid;
     //处理类型
     private String processingType;
     //供应商编号
     private String customerCode;
     //1、云集  2、唯品
     private String type;
     //
     private String normName;
     //分类
     private String categoryName;
     //采购订单创建日期
     private String poTime;
     //用户编号
     private String userCode;
     //采购含税金额(不含税)
     private BigDecimal billAmount;
     //PO号
     private String poNo;
     //商家ID
     private Long merchantId;
     //采购单价(不含税)
     private BigDecimal billPrice;
     //id
     private Long id;
     //商品名称
     private String goodsName;
     //销售订单编号
     private String saleOrderCode;
     //商品货号
     private String goodsNo;
     //贸易方式
     private String fobType;
     //商家编码
     private String merchantCode;
     //品牌名称
     private String brandName;
     //采购含税金额(含税)
     private BigDecimal totalBillAmount;
     //商助
     private String merchandiser;
     //
     private String dataSign;
     //售后数量
     private Integer totalReturnQty;
     //
     private String start;
     //
     private String length;
     //协议号
     private String contractNumber;
     //发货商品数量
     private Integer totalSalesQty;
     //账单编码
     private String billCode;
     //
     private String lotNumber;
     //UPC 条码
     private String upcNo;
     //客户名称
     private String customerName;
     //是否已使用 0: 未使用 1：已使用
     private String isUsed;
     //采购单价(含税)
     private BigDecimal billTaxPrice;
     //税率
     private BigDecimal taxRate;
     //档期编号
     private String scheduleNo;
     //原采购单号
     private String originPo;
     //币种
     private String currencyCode;
     //商家名称
     private String merchantName;
     //客户id
     private Long customerId;
     //卓志编码
     private String topidealCode;
     //代理商家id
     private Long proxyId;
     //创建时间
     private String createDate;
     //原账单id
     private Long oldId;
     //账单时间
     private String billDate;
     
     /*customerId get 方法 */
     public Long getCustomerId(){
         return customerId;
     }
     /*customerId set 方法 */
     public void setCustomerId(Long  customerId){
         this.customerId=customerId;
     }
     /*merchantName get 方法 */
     public String getMerchantName(){
         return merchantName;
     }
     /*merchantName set 方法 */
     public void setMerchantName(String  merchantName){
         this.merchantName=merchantName;
     }

     /*scheduleName get 方法 */
     public String getScheduleName(){
         return scheduleName;
     }
     /*scheduleName set 方法 */
     public void setScheduleName(String  scheduleName){
         this.scheduleName=scheduleName;
     }
     /*hid get 方法 */
     public String getHid(){
         return hid;
     }
     /*hid set 方法 */
     public void setHid(String  hid){
         this.hid=hid;
     }
     /*processingType get 方法 */
     public String getProcessingType(){
         return processingType;
     }
     /*processingType set 方法 */
     public void setProcessingType(String  processingType){
         this.processingType=processingType;
     }
     /*customerCode get 方法 */
     public String getCustomerCode(){
         return customerCode;
     }
     /*customerCode set 方法 */
     public void setCustomerCode(String  customerCode){
         this.customerCode=customerCode;
     }
     /*type get 方法 */
     public String getType(){
         return type;
     }
     /*type set 方法 */
     public void setType(String  type){
         this.type=type;
     }
     /*normName get 方法 */
     public String getNormName(){
         return normName;
     }
     /*normName set 方法 */
     public void setNormName(String normName){
         this.normName=normName;
     }
     /*categoryName get 方法 */
     public String getCategoryName(){
         return categoryName;
     }
     /*categoryName set 方法 */
     public void setCategoryName(String  categoryName){
         this.categoryName=categoryName;
     }
     /*poTime get 方法 */
     public String getPoTime(){
         return poTime;
     }
     /*poTime set 方法 */
     public void setPoTime(String  poTime){
         this.poTime=poTime;
     }
     /*userCode get 方法 */
     public String getUserCode(){
         return userCode;
     }
     /*userCode set 方法 */
     public void setUserCode(String  userCode){
         this.userCode=userCode;
     }
     /*billAmount get 方法 */
     public BigDecimal getBillAmount(){
         return billAmount;
     }
     /*billAmount set 方法 */
     public void setBillAmount(BigDecimal  billAmount){
         this.billAmount=billAmount;
     }
     /*poNo get 方法 */
     public String getPoNo(){
         return poNo;
     }
     /*poNo set 方法 */
     public void setPoNo(String  poNo){
         this.poNo=poNo;
     }
     /*merchantId get 方法 */
     public Long getMerchantId(){
         return merchantId;
     }
     /*merchantId set 方法 */
     public void setMerchantId(Long  merchantId){
         this.merchantId=merchantId;
     }
     /*billPrice get 方法 */
     public BigDecimal getBillPrice(){
         return billPrice;
     }
     /*billPrice set 方法 */
     public void setBillPrice(BigDecimal  billPrice){
         this.billPrice=billPrice;
     }
     /*id get 方法 */
     public Long getId(){
         return id;
     }
     /*id set 方法 */
     public void setId(Long  id){
         this.id=id;
     }
     /*goodsName get 方法 */
     public String getGoodsName(){
         return goodsName;
     }
     /*goodsName set 方法 */
     public void setGoodsName(String  goodsName){
         this.goodsName=goodsName;
     }
     /*saleOrderCode get 方法 */
     public String getSaleOrderCode(){
         return saleOrderCode;
     }
     /*saleOrderCode set 方法 */
     public void setSaleOrderCode(String  saleOrderCode){
         this.saleOrderCode=saleOrderCode;
     }
     /*goodsNo get 方法 */
     public String getGoodsNo(){
         return goodsNo;
     }
     /*goodsNo set 方法 */
     public void setGoodsNo(String  goodsNo){
         this.goodsNo=goodsNo;
     }
     /*fobType get 方法 */
     public String getFobType(){
         return fobType;
     }
     /*fobType set 方法 */
     public void setFobType(String  fobType){
         this.fobType=fobType;
     }
     /*merchantCode get 方法 */
     public String getMerchantCode(){
         return merchantCode;
     }
     /*merchantCode set 方法 */
     public void setMerchantCode(String  merchantCode){
         this.merchantCode=merchantCode;
     }
     /*brandName get 方法 */
     public String getBrandName(){
         return brandName;
     }
     /*brandName set 方法 */
     public void setBrandName(String  brandName){
         this.brandName=brandName;
     }
     /*totalBillAmount get 方法 */
     public BigDecimal getTotalBillAmount(){
         return totalBillAmount;
     }
     /*totalBillAmount set 方法 */
     public void setTotalBillAmount(BigDecimal  totalBillAmount){
         this.totalBillAmount=totalBillAmount;
     }
     /*dataSign get 方法 */
     public String getDataSign(){
         return dataSign;
     }
     /*dataSign set 方法 */
     public void setDataSign(String  dataSign){
         this.dataSign=dataSign;
     }
     /*totalReturnQty get 方法 */
     public Integer getTotalReturnQty(){
         return totalReturnQty;
     }
     /*totalReturnQty set 方法 */
     public void setTotalReturnQty(Integer  totalReturnQty){
         this.totalReturnQty=totalReturnQty;
     }
     /*merchandiser get 方法 */
     public String getMerchandiser(){
         return merchandiser;
     }
     /*merchandiser set 方法 */
     public void setMerchandiser(String  merchandiser){
         this.merchandiser=merchandiser;
     }
     /*start get 方法 */
     public String getStart(){
         return start;
     }
     /*start set 方法 */
     public void setStart(String  start){
         this.start=start;
     }
     /*length get 方法 */
     public String getLength(){
         return length;
     }
     /*length set 方法 */
     public void setLength(String  length){
         this.length=length;
     }
     /*contractNumber get 方法 */
     public String getContractNumber(){
         return contractNumber;
     }
     /*contractNumber set 方法 */
     public void setContractNumber(String  contractNumber){
         this.contractNumber=contractNumber;
     }
     /*totalSalesQty get 方法 */
     public Integer getTotalSalesQty(){
         return totalSalesQty;
     }
     /*totalSalesQty set 方法 */
     public void setTotalSalesQty(Integer  totalSalesQty){
         this.totalSalesQty=totalSalesQty;
     }
     /*billCode get 方法 */
     public String getBillCode(){
         return billCode;
     }
     /*billCode set 方法 */
     public void setBillCode(String  billCode){
         this.billCode=billCode;
     }
     /*lotNumber get 方法 */
     public String getLotNumber(){
         return lotNumber;
     }
     /*lotNumber set 方法 */
     public void setLotNumber(String  lotNumber){
         this.lotNumber=lotNumber;
     }
     /*upcNo get 方法 */
     public String getUpcNo(){
         return upcNo;
     }
     /*upcNo set 方法 */
     public void setUpcNo(String  upcNo){
         this.upcNo=upcNo;
     }
     /*customerName get 方法 */
     public String getCustomerName(){
         return customerName;
     }
     /*customerName set 方法 */
     public void setCustomerName(String  customerName){
         this.customerName=customerName;
     }
     /*isUsed get 方法 */
     public String getIsUsed(){
         return isUsed;
     }
     /*isUsed set 方法 */
     public void setIsUsed(String  isUsed){
         this.isUsed=isUsed;
     }
     /*billTaxPrice get 方法 */
     public BigDecimal getBillTaxPrice(){
         return billTaxPrice;
     }
     /*billTaxPrice set 方法 */
     public void setBillTaxPrice(BigDecimal  billTaxPrice){
         this.billTaxPrice=billTaxPrice;
     }
     /*taxRate get 方法 */
     public BigDecimal getTaxRate(){
         return taxRate;
     }
     /*taxRate set 方法 */
     public void setTaxRate(BigDecimal  taxRate){
         this.taxRate=taxRate;
     }
     /*scheduleNo get 方法 */
     public String getScheduleNo(){
         return scheduleNo;
     }
     /*scheduleNo set 方法 */
     public void setScheduleNo(String  scheduleNo){
         this.scheduleNo=scheduleNo;
     }
     /*originPo get 方法 */
     public String getOriginPo(){
         return originPo;
     }
     /*originPo set 方法 */
     public void setOriginPo(String  originPo){
         this.originPo=originPo;
     }
     /*currencyCode get 方法 */
     public String getCurrencyCode(){
         return currencyCode;
     }
     /*currencyCode set 方法 */
     public void setCurrencyCode(String  currencyCode){
         this.currencyCode=currencyCode;
     }
	public String getTopidealCode() {
		return topidealCode;
	}
	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}
	public Long getProxyId() {
		return proxyId;
	}
	public void setProxyId(Long proxyId) {
		this.proxyId = proxyId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public Long getOldId() {
		return oldId;
	}
	public void setOldId(Long oldId) {
		this.oldId = oldId;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
     
}

