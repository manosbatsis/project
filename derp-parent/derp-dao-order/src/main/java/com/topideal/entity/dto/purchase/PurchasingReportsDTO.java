package com.topideal.entity.dto.purchase;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PurchasingReportsDTO extends PageModel implements Serializable{

    /**
    * 自增序号
    */
	@ApiModelProperty(value="自增序号", required=false)
    private Long id;
    /**
    * 商品编码
    */
	@ApiModelProperty(value="商品编码", required=false)
    private String wareId;
    /**
    * 条形码
    */
	@ApiModelProperty(value="条形码", required=false)
    private String upc;
    /**
    * 商品名称
    */
	@ApiModelProperty(value="商品名称", required=false)
    private String name;
    /**
    * 商家id
    */
	@ApiModelProperty(value="商家id", required=false)
    private Long merchantId;
    /**
    * 商家名称
    */
	@ApiModelProperty(value="商家名称", required=false)
    private String merchantName;
    /**
    * 客户id
    */
	@ApiModelProperty(value="客户id", required=false)
    private Long customerId;
    /**
    * 客户名称
    */
	@ApiModelProperty(value="客户名称", required=false)
    private String customerName;
    /**
    * 平台
    */
	@ApiModelProperty(value="平台", required=false)
    private String platform;
    /**
    * 仓库
    */
	@ApiModelProperty(value="仓库", required=false)
    private String warehouse;
    /**
    * 归属日期
    */
	@ApiModelProperty(value="归属日期", required=false)
    private Date businessDate;
    /**
    * 在途库存
    */
	@ApiModelProperty(value="在途库存", required=false)
    private BigDecimal transitStock;
    /**
    * 库存
    */
	@ApiModelProperty(value="库存", required=false)
    private BigDecimal stock;
    /**
    * 90天日均销量
    */
	@ApiModelProperty(value="90天日均销量", required=false)
    private BigDecimal avg90dayNum;
    /**
    * 预计库存清空天数
    */
	@ApiModelProperty(value="预计库存清空天数", required=false)
    private BigDecimal stockSellDays;
    /**
    * 建议采购数量
    */
	@ApiModelProperty(value="建议采购数量", required=false)
    private BigDecimal purchaseNum;
    /**
    * 补货后预计库存清空天数
    */
	@ApiModelProperty(value="补货后预计库存清空天数", required=false)
    private BigDecimal supplySellDays;
    /**
    * 是否触发补货预警
    */
	@ApiModelProperty(value="是否触发补货预警", required=false)
    private String supplyWarning;
    /**
    * 箱规(箱规为空的时候，设置为1)
    */
	@ApiModelProperty(value="箱规(箱规为空的时候，设置为1)", required=false)
    private Integer cartonSize;
    /**
    * 执行日期
    */
	@ApiModelProperty(value="执行日期", required=false)
    private Date runDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*wareId get 方法 */
    public String getWareId(){
    return wareId;
    }
    /*wareId set 方法 */
    public void setWareId(String  wareId){
    this.wareId=wareId;
    }
    /*upc get 方法 */
    public String getUpc(){
    return upc;
    }
    /*upc set 方法 */
    public void setUpc(String  upc){
    this.upc=upc;
    }
    /*name get 方法 */
    public String getName(){
    return name;
    }
    /*name set 方法 */
    public void setName(String  name){
    this.name=name;
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
    /*platform get 方法 */
    public String getPlatform(){
    return platform;
    }
    /*platform set 方法 */
    public void setPlatform(String  platform){
    this.platform=platform;
    }
    /*warehouse get 方法 */
    public String getWarehouse(){
    return warehouse;
    }
    /*warehouse set 方法 */
    public void setWarehouse(String  warehouse){
    this.warehouse=warehouse;
    }
    /*businessDate get 方法 */
    public Date getBusinessDate(){
    return businessDate;
    }
    /*businessDate set 方法 */
    public void setBusinessDate(Date businessDate){
    this.businessDate=businessDate;
    }
    /*transitStock get 方法 */
    public BigDecimal getTransitStock(){
    return transitStock;
    }
    /*transitStock set 方法 */
    public void setTransitStock(BigDecimal  transitStock){
    this.transitStock=transitStock;
    }
    /*stock get 方法 */
    public BigDecimal getStock(){
    return stock;
    }
    /*stock set 方法 */
    public void setStock(BigDecimal  stock){
    this.stock=stock;
    }
    /*avg90dayNum get 方法 */
    public BigDecimal getAvg90dayNum(){
    return avg90dayNum;
    }
    /*avg90dayNum set 方法 */
    public void setAvg90dayNum(BigDecimal  avg90dayNum){
    this.avg90dayNum=avg90dayNum;
    }
    /*stockSellDays get 方法 */
    public BigDecimal getStockSellDays(){
    return stockSellDays;
    }
    /*stockSellDays set 方法 */
    public void setStockSellDays(BigDecimal  stockSellDays){
    this.stockSellDays=stockSellDays;
    }
    /*purchaseNum get 方法 */
    public BigDecimal getPurchaseNum(){
    return purchaseNum;
    }
    /*purchaseNum set 方法 */
    public void setPurchaseNum(BigDecimal  purchaseNum){
    this.purchaseNum=purchaseNum;
    }
    /*supplySellDays get 方法 */
    public BigDecimal getSupplySellDays(){
    return supplySellDays;
    }
    /*supplySellDays set 方法 */
    public void setSupplySellDays(BigDecimal  supplySellDays){
    this.supplySellDays=supplySellDays;
    }
    /*supplyWarning get 方法 */
    public String getSupplyWarning(){
    return supplyWarning;
    }
    /*supplyWarning set 方法 */
    public void setSupplyWarning(String  supplyWarning){
    this.supplyWarning=supplyWarning;
    }
    /*cartonSize get 方法 */
    public Integer getCartonSize(){
    return cartonSize;
    }
    /*cartonSize set 方法 */
    public void setCartonSize(Integer  cartonSize){
    this.cartonSize=cartonSize;
    }
    /*runDate get 方法 */
    public Date getRunDate(){
    return runDate;
    }
    /*runDate set 方法 */
    public void setRunDate(Date  runDate){
    this.runDate=runDate;
    }

}
