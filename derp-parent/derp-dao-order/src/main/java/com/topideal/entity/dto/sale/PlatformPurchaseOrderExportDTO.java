package com.topideal.entity.dto.sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.vo.sale.PlatformPurchaseOrderItemModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PlatformPurchaseOrderExportDTO extends PageModel implements Serializable{

    /**
    * id
    */
	@ApiModelProperty(value = "主键ID")
    private Long id;
	@ApiModelProperty(value = "主键ID集合")
    private String ids;
	
	@ApiModelProperty(value = "商家ID")
    private Long merchantId;
    
	@ApiModelProperty(value = "商家名称")
    private String merchantName;
   
	@ApiModelProperty(value = "仓库id")
    private Long depotId;
  
	@ApiModelProperty(value = "仓库名称")
    private String platformDepotName;
  
	@ApiModelProperty(value = "客户id")
    private Long customerId;
  
	@ApiModelProperty(value = "客户名称")
    private String customerName;

	@ApiModelProperty(value = "PO号")
    private String poNo;
    
	@ApiModelProperty(value = "下单日期")
    private Timestamp orderTime;
	
	@ApiModelProperty(value = "下单开始时间",hidden=true)
    private String orderTimeStartDate ;
	
	@ApiModelProperty(value = "下单结束时间",hidden=true)
    private String orderTimeEndDate ;

	@ApiModelProperty(value = "入库时间")
    private Timestamp deliverDate;

	@ApiModelProperty(value = "提交时间")
    private Timestamp submitDate;

	@ApiModelProperty(value = "提交人ID")
    private Long submiter;

	@ApiModelProperty(value = "提交人名称")
    private String submitName;

	@ApiModelProperty(value = "提交时间")
    private Timestamp resaleDate;
   
	@ApiModelProperty(value = "提交人ID")
    private Long resaler;
   
	@ApiModelProperty(value = "提交人名称")
    private String resaleName;
  
	@ApiModelProperty(value = "币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String currency;
	
	@ApiModelProperty(value = "币种（中文）")
    private String currencyLabel ;
   
	@ApiModelProperty(value = "金额")
    private BigDecimal amount;
   
	@ApiModelProperty(value = "数量")
    private Integer num;
	
	@ApiModelProperty(value = "sku数量")
    private Integer skusNum ;
   
	@ApiModelProperty(value = "平台状态： 1.待发货确认、2.等待签收、3.等待入库、4.部分收货、5已完成")
    private String platformStatus;
	
	@ApiModelProperty(value = "平台状态（中文）")
    private String platformStatusLabel ;
    
	@ApiModelProperty(value = "单据状态：1:待提交 2.已转销售,3:未转销售")
    private String status;
	
	@ApiModelProperty(value = "单据状态（中文）")
    private String statusLabel;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
  
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value = "表体数据")
    private List<PlatformPurchaseOrderItemModel> itemList ;
   
	@ApiModelProperty(value = "销售订单号")
    private String saleCode;
    
	@ApiModelProperty(value = "订单来源 1. 京东 2.天猫")
    private String orderSource;

	@ApiModelProperty(value = "实收好品数量")
    private Integer receiptOkNum;

	@ApiModelProperty(value = "实收坏品数量")
    private Integer receiptBadNum;

	@ApiModelProperty(value = "平台商品名称")
    private String platformGoodsName;
   
	@ApiModelProperty(value = "平台条码")
    private String platformBarcode;

	@ApiModelProperty(value = "商品数量")
    private Integer itemNum;
  
	@ApiModelProperty(value = "单价")
    private BigDecimal price;
  
	@ApiModelProperty(value = "商品总金额")
    private BigDecimal itemAmount;

	@ApiModelProperty(value = "pr号")
    private String prNo;
	
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
    /*depotId get 方法 */
    public Long getDepotId(){
    return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
    this.depotId=depotId;
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
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*orderTime get 方法 */
    public Timestamp getOrderTime(){
    return orderTime;
    }
    /*orderTime set 方法 */
    public void setOrderTime(Timestamp  orderTime){
    this.orderTime=orderTime;
    }
    /*deliverDate get 方法 */
    public Timestamp getDeliverDate(){
    return deliverDate;
    }
    /*deliverDate set 方法 */
    public void setDeliverDate(Timestamp  deliverDate){
    this.deliverDate=deliverDate;
    }
    /*submitDate get 方法 */
    public Timestamp getSubmitDate(){
    return submitDate;
    }
    /*submitDate set 方法 */
    public void setSubmitDate(Timestamp  submitDate){
    this.submitDate=submitDate;
    }
    /*submiter get 方法 */
    public Long getSubmiter(){
    return submiter;
    }
    /*submiter set 方法 */
    public void setSubmiter(Long  submiter){
    this.submiter=submiter;
    }
    /*submitName get 方法 */
    public String getSubmitName(){
    return submitName;
    }
    /*submitName set 方法 */
    public void setSubmitName(String  submitName){
    this.submitName=submitName;
    }
    /*resaleDate get 方法 */
    public Timestamp getResaleDate(){
    return resaleDate;
    }
    /*resaleDate set 方法 */
    public void setResaleDate(Timestamp  resaleDate){
    this.resaleDate=resaleDate;
    }
    /*resaler get 方法 */
    public Long getResaler(){
    return resaler;
    }
    /*resaler set 方法 */
    public void setResaler(Long  resaler){
    this.resaler=resaler;
    }
    /*resaleName get 方法 */
    public String getResaleName(){
    return resaleName;
    }
    /*resaleName set 方法 */
    public void setResaleName(String  resaleName){
    this.resaleName=resaleName;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    this.currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency) ;
    }
    /*amount get 方法 */
    public BigDecimal getAmount(){
    return amount;
    }
    /*amount set 方法 */
    public void setAmount(BigDecimal  amount){
    this.amount=amount;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*platformStatus get 方法 */
    public String getPlatformStatus(){
    return platformStatus;
    }
    /*platformStatus set 方法 */
    public void setPlatformStatus(String  platformStatus){
    this.platformStatus=platformStatus;
    this.platformStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.platformPurchase_platformStatusList, platformStatus) ;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.platformPurchase_statusList, status) ;
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
	public String getCurrencyLabel() {
		return currencyLabel;
	}
	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
	}
	public String getPlatformStatusLabel() {
		return platformStatusLabel;
	}
	public void setPlatformStatusLabel(String platformStatusLabel) {
		this.platformStatusLabel = platformStatusLabel;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	public String getOrderTimeStartDate() {
		return orderTimeStartDate;
	}
	public void setOrderTimeStartDate(String orderTimeStartDate) {
		this.orderTimeStartDate = orderTimeStartDate;
	}
	public String getOrderTimeEndDate() {
		return orderTimeEndDate;
	}
	public void setOrderTimeEndDate(String orderTimeEndDate) {
		this.orderTimeEndDate = orderTimeEndDate;
	}
	public Integer getSkusNum() {
		return skusNum;
	}
	public void setSkusNum(Integer skusNum) {
		this.skusNum = skusNum;
	}

    public String getSaleCode() {
        return saleCode;
    }

    public void setSaleCode(String saleCode) {
        this.saleCode = saleCode;
    }
	public String getPlatformDepotName() {
		return platformDepotName;
	}
	public void setPlatformDepotName(String platformDepotName) {
		this.platformDepotName = platformDepotName;
	}
	public String getPlatformGoodsName() {
		return platformGoodsName;
	}
	public void setPlatformGoodsName(String platformGoodsName) {
		this.platformGoodsName = platformGoodsName;
	}
	public String getPlatformBarcode() {
		return platformBarcode;
	}
	public void setPlatformBarcode(String platformBarcode) {
		this.platformBarcode = platformBarcode;
	}
	public Integer getItemNum() {
		return itemNum;
	}
	public void setItemNum(Integer itemNum) {
		this.itemNum = itemNum;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getItemAmount() {
		return itemAmount;
	}
	public void setItemAmount(BigDecimal itemAmount) {
		this.itemAmount = itemAmount;
	}
	public Integer getReceiptOkNum() {
		return receiptOkNum;
	}
	public void setReceiptOkNum(Integer receiptOkNum) {
		this.receiptOkNum = receiptOkNum;
	}
	public Integer getReceiptBadNum() {
		return receiptBadNum;
	}
	public void setReceiptBadNum(Integer receiptBadNum) {
		this.receiptBadNum = receiptBadNum;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getPrNo() {
		return prNo;
	}
	public void setPrNo(String prNo) {
		this.prNo = prNo;
	}
    
}
