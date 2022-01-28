package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.math.BigDecimal;

import com.topideal.common.system.ibatis.PageModel;

public class CrawlerOutTempModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 数据来源 1-云集 2-唯品
    */
    private String sourceType;
    /**
    * 数据类型 1-库存量 2-可核销量 3-出入库分配明细
    */
    private String dataType;
    /**
    * 商家id
    */
    private Long merchantId;
    /**
    * 客户id
    */
    private Long customerId;
    /**
    * 仓库id
    */
    private Long depotId;
    /**
    * 经分销自增长账单id
    */
    private Long billId;
    /**
    * 唯品/云集账单号
    */
    private String billCode;
    /**
    * po号
    */
    private String poNo;
    /**
    * 平台销售单号
    */
    private String saleCode;
    /**
    * 经分销货号
    */
    private String goodsNo;
    /**
     * 商品id
     */
     private Long goodsId;
    /**
    * 库存余量
    */
    private Integer surplusNum;
    /**
    * 分配数量
    */
    private Integer fpNum;

    //处理类型归类:XSC-销售出库 GJC-国检出库 PKC-盘亏出库 BFC-报废 PYR-盘盈入库 KTR-客退入库
    private String billType;

    //爬虫商品编码
    private String skuNo;

    //可核销量
    private Integer verifiNum;

    //币种
    private String currencyCode;

    //单价
    private BigDecimal price;
    //金额
    private BigDecimal amount;
    //事业部id
    private Long buId;
    //事业部名称
    private String buName;
    //爬虫登陆用户名
    private String userCode;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*sourceType get 方法 */
    public String getSourceType(){
    return sourceType;
    }
    /*sourceType set 方法 */
    public void setSourceType(String  sourceType){
    this.sourceType=sourceType;
    }
    /*dataType get 方法 */
    public String getDataType(){
    return dataType;
    }
    /*dataType set 方法 */
    public void setDataType(String  dataType){
    this.dataType=dataType;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
    return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
    this.merchantId=merchantId;
    }
    /*customerId get 方法 */
    public Long getCustomerId(){
    return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
    this.customerId=customerId;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
    return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
    this.depotId=depotId;
    }
    /*billId get 方法 */
    public Long getBillId(){
    return billId;
    }
    /*billId set 方法 */
    public void setBillId(Long  billId){
    this.billId=billId;
    }
    /*billCode get 方法 */
    public String getBillCode(){
    return billCode;
    }
    /*billCode set 方法 */
    public void setBillCode(String  billCode){
    this.billCode=billCode;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*saleCode get 方法 */
    public String getSaleCode(){
    return saleCode;
    }
    /*saleCode set 方法 */
    public void setSaleCode(String  saleCode){
    this.saleCode=saleCode;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    
    public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	/*surplusNum get 方法 */
    public Integer getSurplusNum(){
    return surplusNum;
    }
    /*surplusNum set 方法 */
    public void setSurplusNum(Integer  surplusNum){
    this.surplusNum=surplusNum;
    }
   
	/*fpNum get 方法 */
    public Integer getFpNum(){
    return fpNum;
    }
    /*fpNum set 方法 */
    public void setFpNum(Integer  fpNum){
    this.fpNum=fpNum;
    }


    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public Integer getVerifiNum() {
        return verifiNum;
    }

    public void setVerifiNum(Integer verifiNum) {
        this.verifiNum = verifiNum;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

}
