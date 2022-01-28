package com.topideal.entity.dto.bill;

import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class TobTemporaryReceiveBillCostItemMonthlyDTO extends PageModel implements Serializable{

    /**
    * 记录ID
    */
    @ApiModelProperty(value = "记录ID")
    private Long id;
    /**
    * 月结月份 YYYY-MM
    */
    @ApiModelProperty(value = "月结月份")
    private String month;
    /**
    * 入账月份 YYYY-MM
    */
    @ApiModelProperty(value = "入账月份")
    private String creditMonth;
    /**
    * 事业部ID
    */
    @ApiModelProperty(value = "事业部ID")
    private Long buId;
    /**
    * 事业部名称
    */
    @ApiModelProperty(value = "事业部名称")
    private String buName;
    /**
    * 商家ID
    */
    @ApiModelProperty(value = "公司ID")
    private Long merchantId;
    /**
    * 商家名称
    */
    @ApiModelProperty(value = "公司名称")
    private String merchantName;
    /**
    * 客户id
    */
    @ApiModelProperty(value = "客户id")
    private Long customerId;
    /**
    * 客户名称
    */
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    /**
    * 上架单号
    */
    @ApiModelProperty(value = "上架单号")
    private String shelfCode;
    /**
     * 销售SD单号
     */
    @ApiModelProperty(value = "销售SD单号")
    private String sdCode;
    /**
    * 销售币种
    */
    @ApiModelProperty(value = "销售币种")
    private String currency;
    /**
    * po号
    */
    @ApiModelProperty(value = "po号")
    private String poNo;
    /**
    * 上架时间
    */
    @ApiModelProperty(value = "上架时间")
    private Timestamp shelfDate;
    /**
    * 商品id
    */
    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    /**
    * 商品名称
    */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
    * 商品货号
    */
    @ApiModelProperty(value = "商品货号")
    private String goodsNo;
    /**
    * 母品牌
    */
    @ApiModelProperty(value = "母品牌")
    private String parentBrandName;
    /**
    * 母品牌id
    */
    @ApiModelProperty(value = "母品牌id")
    private Long parentBrandId;
    /**
    * 母品牌编码
    */
    @ApiModelProperty(value = "母品牌编码")
    private String parentBrandCode;
    /**
    * 销售单价
    */
    @ApiModelProperty(value = "销售单价")
    private BigDecimal price;
    /**
    * 上架好品量
    */
    @ApiModelProperty(value = "上架好品量")
    private Integer shelfNum;
    /**
    * 核销应收金额
    */
    @ApiModelProperty(value = "核销应收金额")
    private BigDecimal verifiedAmount;
    /**
    * 暂估返利金额
    */
    @ApiModelProperty(value = "暂估返利金额")
    private BigDecimal rebateAmount;
    /**
    * 销售SD类型id
    */
    @ApiModelProperty(value = "销售SD类型id")
    private Long sdTypeId;
    /**
    * 销售SD类型名称
    */
    @ApiModelProperty(value = "销售SD类型名称")
    private String sdTypeName;
    /**
    * sd比例
    */
    @ApiModelProperty(value = "sd比例")
    private Double sdRatio;
    /**
    * 关联应收单id
    */
    @ApiModelProperty(value = "关联应收单id")
    private Long receiveId;
    /**
    * 应收账单号
    */
    @ApiModelProperty(value = "应收账单号")
    private String receiveCode;
    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    /**
     * 暂估返利明细id
     */
    @ApiModelProperty(value = "暂估返利明细id")
    private Long tempItemId;

    @ApiModelProperty(value = "关联暂估明细id，多个以英文逗号隔开", hidden = true)
    private String tempItemIds;

    @ApiModelProperty(value = "月结月份最后一天日期", hidden = true)
    private String lastDay;

    @ApiModelProperty(value = "日期区间", hidden = true)
    private String type;

    @ApiModelProperty(value = "未核销应收金额")
    private BigDecimal nonVerifiedAmount;

    @ApiModelProperty(value = "当前登录用户绑定的事业部集合")
    private List<Long> buList;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*creditMonth get 方法 */
    public String getCreditMonth(){
    return creditMonth;
    }
    /*creditMonth set 方法 */
    public void setCreditMonth(String  creditMonth){
    this.creditMonth=creditMonth;
    }
    /*buId get 方法 */
    public Long getBuId(){
    return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    this.buId=buId;
    }
    /*buName get 方法 */
    public String getBuName(){
    return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
    this.buName=buName;
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
    /*customerId get 方法 */
    public Long getCustomerId(){
    return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
    this.customerId=customerId;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
    return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
    this.customerName=customerName;
    }
    /*shelfCode get 方法 */
    public String getShelfCode(){
    return shelfCode;
    }
    /*shelfCode set 方法 */
    public void setShelfCode(String  shelfCode){
    this.shelfCode=shelfCode;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*shelfDate get 方法 */
    public Timestamp getShelfDate(){
    return shelfDate;
    }
    /*shelfDate set 方法 */
    public void setShelfDate(Timestamp  shelfDate){
    this.shelfDate=shelfDate;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*parentBrandName get 方法 */
    public String getParentBrandName(){
    return parentBrandName;
    }
    /*parentBrandName set 方法 */
    public void setParentBrandName(String  parentBrandName){
    this.parentBrandName=parentBrandName;
    }
    /*parentBrandId get 方法 */
    public Long getParentBrandId(){
    return parentBrandId;
    }
    /*parentBrandId set 方法 */
    public void setParentBrandId(Long  parentBrandId){
    this.parentBrandId=parentBrandId;
    }
    /*parentBrandCode get 方法 */
    public String getParentBrandCode(){
    return parentBrandCode;
    }
    /*parentBrandCode set 方法 */
    public void setParentBrandCode(String  parentBrandCode){
    this.parentBrandCode=parentBrandCode;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
    return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
    this.price=price;
    }
    /*shelfNum get 方法 */
    public Integer getShelfNum(){
    return shelfNum;
    }
    /*shelfNum set 方法 */
    public void setShelfNum(Integer  shelfNum){
    this.shelfNum=shelfNum;
    }
    /*verifiedAmount get 方法 */
    public BigDecimal getVerifiedAmount(){
    return verifiedAmount;
    }
    /*verifiedAmount set 方法 */
    public void setVerifiedAmount(BigDecimal  verifiedAmount){
    this.verifiedAmount=verifiedAmount;
    }
    /*rebateAmount get 方法 */
    public BigDecimal getRebateAmount(){
    return rebateAmount;
    }
    /*rebateAmount set 方法 */
    public void setRebateAmount(BigDecimal  rebateAmount){
    this.rebateAmount=rebateAmount;
    }
    /*sdTypeId get 方法 */
    public Long getSdTypeId(){
    return sdTypeId;
    }
    /*sdTypeId set 方法 */
    public void setSdTypeId(Long  sdTypeId){
    this.sdTypeId=sdTypeId;
    }
    /*sdTypeName get 方法 */
    public String getSdTypeName(){
    return sdTypeName;
    }
    /*sdTypeName set 方法 */
    public void setSdTypeName(String  sdTypeName){
    this.sdTypeName=sdTypeName;
    }
    /*sdRatio get 方法 */
    public Double getSdRatio(){
    return sdRatio;
    }
    /*sdRatio set 方法 */
    public void setSdRatio(Double  sdRatio){
    this.sdRatio=sdRatio;
    }
    /*receiveId get 方法 */
    public Long getReceiveId(){
    return receiveId;
    }
    /*receiveId set 方法 */
    public void setReceiveId(Long  receiveId){
    this.receiveId=receiveId;
    }
    /*receiveCode get 方法 */
    public String getReceiveCode(){
    return receiveCode;
    }
    /*receiveCode set 方法 */
    public void setReceiveCode(String  receiveCode){
    this.receiveCode=receiveCode;
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

    public String getSdCode() {
        return sdCode;
    }

    public void setSdCode(String sdCode) {
        this.sdCode = sdCode;
    }

    public Long getTempItemId() {
        return tempItemId;
    }

    public void setTempItemId(Long tempItemId) {
        this.tempItemId = tempItemId;
    }

    public String getLastDay() {
        return lastDay;
    }

    public void setLastDay(String lastDay) {
        this.lastDay = lastDay;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTempItemIds() {
        return tempItemIds;
    }

    public void setTempItemIds(String tempItemIds) {
        this.tempItemIds = tempItemIds;
    }

    public BigDecimal getNonVerifiedAmount() {
        return nonVerifiedAmount;
    }

    public void setNonVerifiedAmount(BigDecimal nonVerifiedAmount) {
        this.nonVerifiedAmount = nonVerifiedAmount;
    }

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }
}