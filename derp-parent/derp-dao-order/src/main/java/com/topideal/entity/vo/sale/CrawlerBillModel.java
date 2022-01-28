package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

public class CrawlerBillModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * PO号
    */
    private String poNo;
    /**
    * 客户名称(经分销)
    */
    private String customerName;
    /**
    * 采购订单创建日期
    */
    private Timestamp poTime;
    /**
    * 唯品/云集sku-对应经销货号/条码/对照表
    */
    private String goodsNo;
    /**
    * 发货商品数量
    */
    private Integer totalSalesQty;
    /**
    * 是否使用 0:未使用 1：已使用，默认值0
    */
    private String isUsed;
    /**
    * 1、云集  2、唯品
    */
    private String type;
    /**
    * 账单编码
    */
    private String billCode;
    /**
    * 账单时间
    */
    private Timestamp billDate;
    /**
    * 00-销售明细、01-库存买断、02库存盘亏、03报废、04库存盘盈、05国检抽样
    */
    private String billType;
    /**
    * 处理类型
    */
    private String processingType;
    /**
    * 采购单价(不含税)
    */
    private BigDecimal billPrice;
    /**
    * 商家编码
    */
    private String merchantCode;
    /**
    * 供应商编号
    */
    private String customerCode;
    /**
    * 品牌名称
    */
    private String brandName;
    /**
    * 档期编号
    */
    private String scheduleNo;
    /**
    * 档期名称
    */
    private String scheduleName;
    /**
    * 销售订单编号
    */
    private String saleOrderCode;
    /**
    * 商助
    */
    private String merchandiser;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 采购含税金额(不含税)
    */
    private BigDecimal billAmount;
    /**
    * 采购含税金额(含税)
    */
    private BigDecimal totalBillAmount;
    /**
    * 币种
    */
    private String currencyCode;
    /**
    * 原采购单号
    */
    private String originPo;
    /**
    * 协议号
    */
    private String contractNumber;
    /**
    * 贸易方式
    */
    private String fobType;
    /**
    * 
    */
    private String hid;
    /**
    * 
    */
    private String start;
    /**
    * 
    */
    private String length;
    /**
    * 
    */
    private String dataSign;
    /**
    * 
    */
    private String lotNumber;
    /**
    * 规格名称
    */
    private String normName;
    /**
    * 分类
    */
    private String categoryName;
    /**
    * UPC 条码
    */
    private String upcNo;
    /**
    * 税率
    */
    private BigDecimal taxRate;
    /**
    * 售后数量
    */
    private Integer totalReturnQty;
    /**
    * 采购单价(含税)
    */
    private BigDecimal billTaxPrice;
    /**
    * 用户编码
    */
    private String userCode;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 客户id
    */
    private Long customerId;
    /**
    * 卓志编码
    */
    private String topidealCode;
    /**
    * 代理商家id
    */
    private Long proxyId;
    /**
    * 原账单id
    */
    private Long oldId;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 自动校验获取状态 ，0/空-未获取、1-已获取
    */
    private String isVerify;
    /**
    * 出库单号
    */
    private String outCode;

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
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
    return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
    this.customerName=customerName;
    }
    /*poTime get 方法 */
    public Timestamp getPoTime(){
    return poTime;
    }
    /*poTime set 方法 */
    public void setPoTime(Timestamp  poTime){
    this.poTime=poTime;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*totalSalesQty get 方法 */
    public Integer getTotalSalesQty(){
    return totalSalesQty;
    }
    /*totalSalesQty set 方法 */
    public void setTotalSalesQty(Integer  totalSalesQty){
    this.totalSalesQty=totalSalesQty;
    }
    /*isUsed get 方法 */
    public String getIsUsed(){
    return isUsed;
    }
    /*isUsed set 方法 */
    public void setIsUsed(String  isUsed){
    this.isUsed=isUsed;
    }
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
    }
    /*billCode get 方法 */
    public String getBillCode(){
    return billCode;
    }
    /*billCode set 方法 */
    public void setBillCode(String  billCode){
    this.billCode=billCode;
    }
    /*billDate get 方法 */
    public Timestamp getBillDate(){
    return billDate;
    }
    /*billDate set 方法 */
    public void setBillDate(Timestamp  billDate){
    this.billDate=billDate;
    }
    /*billType get 方法 */
    public String getBillType(){
    return billType;
    }
    /*billType set 方法 */
    public void setBillType(String  billType){
    this.billType=billType;
    }
    /*processingType get 方法 */
    public String getProcessingType(){
    return processingType;
    }
    /*processingType set 方法 */
    public void setProcessingType(String  processingType){
    this.processingType=processingType;
    }
    /*billPrice get 方法 */
    public BigDecimal getBillPrice(){
    return billPrice;
    }
    /*billPrice set 方法 */
    public void setBillPrice(BigDecimal  billPrice){
    this.billPrice=billPrice;
    }
    /*merchantCode get 方法 */
    public String getMerchantCode(){
    return merchantCode;
    }
    /*merchantCode set 方法 */
    public void setMerchantCode(String  merchantCode){
    this.merchantCode=merchantCode;
    }
    /*customerCode get 方法 */
    public String getCustomerCode(){
    return customerCode;
    }
    /*customerCode set 方法 */
    public void setCustomerCode(String  customerCode){
    this.customerCode=customerCode;
    }
    /*brandName get 方法 */
    public String getBrandName(){
    return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
    this.brandName=brandName;
    }
    /*scheduleNo get 方法 */
    public String getScheduleNo(){
    return scheduleNo;
    }
    /*scheduleNo set 方法 */
    public void setScheduleNo(String  scheduleNo){
    this.scheduleNo=scheduleNo;
    }
    /*scheduleName get 方法 */
    public String getScheduleName(){
    return scheduleName;
    }
    /*scheduleName set 方法 */
    public void setScheduleName(String  scheduleName){
    this.scheduleName=scheduleName;
    }
    /*saleOrderCode get 方法 */
    public String getSaleOrderCode(){
    return saleOrderCode;
    }
    /*saleOrderCode set 方法 */
    public void setSaleOrderCode(String  saleOrderCode){
    this.saleOrderCode=saleOrderCode;
    }
    /*merchandiser get 方法 */
    public String getMerchandiser(){
    return merchandiser;
    }
    /*merchandiser set 方法 */
    public void setMerchandiser(String  merchandiser){
    this.merchandiser=merchandiser;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*billAmount get 方法 */
    public BigDecimal getBillAmount(){
    return billAmount;
    }
    /*billAmount set 方法 */
    public void setBillAmount(BigDecimal  billAmount){
    this.billAmount=billAmount;
    }
    /*totalBillAmount get 方法 */
    public BigDecimal getTotalBillAmount(){
    return totalBillAmount;
    }
    /*totalBillAmount set 方法 */
    public void setTotalBillAmount(BigDecimal  totalBillAmount){
    this.totalBillAmount=totalBillAmount;
    }
    /*currencyCode get 方法 */
    public String getCurrencyCode(){
    return currencyCode;
    }
    /*currencyCode set 方法 */
    public void setCurrencyCode(String  currencyCode){
    this.currencyCode=currencyCode;
    }
    /*originPo get 方法 */
    public String getOriginPo(){
    return originPo;
    }
    /*originPo set 方法 */
    public void setOriginPo(String  originPo){
    this.originPo=originPo;
    }
    /*contractNumber get 方法 */
    public String getContractNumber(){
    return contractNumber;
    }
    /*contractNumber set 方法 */
    public void setContractNumber(String  contractNumber){
    this.contractNumber=contractNumber;
    }
    /*fobType get 方法 */
    public String getFobType(){
    return fobType;
    }
    /*fobType set 方法 */
    public void setFobType(String  fobType){
    this.fobType=fobType;
    }
    /*hid get 方法 */
    public String getHid(){
    return hid;
    }
    /*hid set 方法 */
    public void setHid(String  hid){
    this.hid=hid;
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
    /*dataSign get 方法 */
    public String getDataSign(){
    return dataSign;
    }
    /*dataSign set 方法 */
    public void setDataSign(String  dataSign){
    this.dataSign=dataSign;
    }
    /*lotNumber get 方法 */
    public String getLotNumber(){
    return lotNumber;
    }
    /*lotNumber set 方法 */
    public void setLotNumber(String  lotNumber){
    this.lotNumber=lotNumber;
    }
    /*normName get 方法 */
    public String getNormName(){
    return normName;
    }
    /*normName set 方法 */
    public void setNormName(String  normName){
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
    /*upcNo get 方法 */
    public String getUpcNo(){
    return upcNo;
    }
    /*upcNo set 方法 */
    public void setUpcNo(String  upcNo){
    this.upcNo=upcNo;
    }
    /*taxRate get 方法 */
    public BigDecimal getTaxRate(){
    return taxRate;
    }
    /*taxRate set 方法 */
    public void setTaxRate(BigDecimal  taxRate){
    this.taxRate=taxRate;
    }
    /*totalReturnQty get 方法 */
    public Integer getTotalReturnQty(){
    return totalReturnQty;
    }
    /*totalReturnQty set 方法 */
    public void setTotalReturnQty(Integer  totalReturnQty){
    this.totalReturnQty=totalReturnQty;
    }
    /*billTaxPrice get 方法 */
    public BigDecimal getBillTaxPrice(){
    return billTaxPrice;
    }
    /*billTaxPrice set 方法 */
    public void setBillTaxPrice(BigDecimal  billTaxPrice){
    this.billTaxPrice=billTaxPrice;
    }
    /*userCode get 方法 */
    public String getUserCode(){
    return userCode;
    }
    /*userCode set 方法 */
    public void setUserCode(String  userCode){
    this.userCode=userCode;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
    return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
    this.merchantName=merchantName;
    }
    /*customerId get 方法 */
    public Long getCustomerId(){
    return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
    this.customerId=customerId;
    }
    /*topidealCode get 方法 */
    public String getTopidealCode(){
    return topidealCode;
    }
    /*topidealCode set 方法 */
    public void setTopidealCode(String  topidealCode){
    this.topidealCode=topidealCode;
    }
    /*proxyId get 方法 */
    public Long getProxyId(){
    return proxyId;
    }
    /*proxyId set 方法 */
    public void setProxyId(Long  proxyId){
    this.proxyId=proxyId;
    }
    /*oldId get 方法 */
    public Long getOldId(){
    return oldId;
    }
    /*oldId set 方法 */
    public void setOldId(Long  oldId){
    this.oldId=oldId;
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
    /*isVerify get 方法 */
    public String getIsVerify(){
    return isVerify;
    }
    /*isVerify set 方法 */
    public void setIsVerify(String  isVerify){
    this.isVerify=isVerify;
    }
    /*outCode get 方法 */
    public String getOutCode(){
    return outCode;
    }
    /*outCode set 方法 */
    public void setOutCode(String  outCode){
    this.outCode=outCode;
    }






}
