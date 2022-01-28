package com.topideal.entity.dto.sale;

import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@ApiModel
public class OrderReturnIdepotItemDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "主键ID")
    private Long id;

	@ApiModelProperty(value = "电商订单退货入库ID")
    private Long oreturnIdepotId;

	@ApiModelProperty(value = "退入商品id")
    private Long inGoodsId;

	@ApiModelProperty(value = "退入商品编码")
    private String inGoodsCode;

	@ApiModelProperty(value = "退入商品货号")
    private String inGoodsNo;

	@ApiModelProperty(value = "退入商品名称")
    private String inGoodsName;

	@ApiModelProperty(value = "销售单价")
    private BigDecimal price;

	@ApiModelProperty(value = "销售金额")
    private BigDecimal decTotal;

	@ApiModelProperty(value = "退货正常品数量")
    private Integer returnNum;

	@ApiModelProperty(value = "退货残品数量")
    private Integer badGoodsNum;

	@ApiModelProperty(value = "条形码")
    private String barcode;

	@ApiModelProperty(value = "标准条码")
    private String commbarcode;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value = "批次号")
     private String batchNo;

	@ApiModelProperty(value = "生产日期")
     private Date productionDate;

	@ApiModelProperty(value = "失效日期")
     private Date overdueDate;

	@ApiModelProperty(value = "电商订单退货商品批次详情")
     private List<OrderReturnIdepotBatchDTO> batchList;

	@ApiModelProperty(value = "事业部名称")
    private String buName;

	@ApiModelProperty(value = "事业部id")
    private Long buId;

    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;

    /**
	 * 内贸商品退款金额（不含税）
	 */
    @ApiModelProperty(value = "内贸商品退款金额（不含税）")
	private BigDecimal tradeRefundAmount;
	/**
	 * 内贸商品退款税额
	 */
    @ApiModelProperty(value = "内贸商品退款税额")
	private BigDecimal tradeRefundTax;

    /**
     * 库位类型id
     */
    @ApiModelProperty(value = "库位类型id")
    private Long stockLocationTypeId;
    /**
     * 库位类型
     */
    @ApiModelProperty(value = "库位类型")
    private String stockLocationTypeName;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*oreturnIdepotId get 方法 */
    public Long getOreturnIdepotId(){
    return oreturnIdepotId;
    }
    /*oreturnIdepotId set 方法 */
    public void setOreturnIdepotId(Long  oreturnIdepotId){
    this.oreturnIdepotId=oreturnIdepotId;
    }
    /*inGoodsId get 方法 */
    public Long getInGoodsId(){
    return inGoodsId;
    }
    /*inGoodsId set 方法 */
    public void setInGoodsId(Long  inGoodsId){
    this.inGoodsId=inGoodsId;
    }
    /*inGoodsCode get 方法 */
    public String getInGoodsCode(){
    return inGoodsCode;
    }
    /*inGoodsCode set 方法 */
    public void setInGoodsCode(String  inGoodsCode){
    this.inGoodsCode=inGoodsCode;
    }
    /*inGoodsNo get 方法 */
    public String getInGoodsNo(){
    return inGoodsNo;
    }
    /*inGoodsNo set 方法 */
    public void setInGoodsNo(String  inGoodsNo){
    this.inGoodsNo=inGoodsNo;
    }
    /*inGoodsName get 方法 */
    public String getInGoodsName(){
    return inGoodsName;
    }
    /*inGoodsName set 方法 */
    public void setInGoodsName(String  inGoodsName){
    this.inGoodsName=inGoodsName;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
    return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
    this.price=price;
    }
    /*returnNum get 方法 */
    public Integer getReturnNum(){
    return returnNum;
    }
    /*returnNum set 方法 */
    public void setReturnNum(Integer  returnNum){
    this.returnNum=returnNum;
    }
    /*badGoodsNum get 方法 */
    public Integer getBadGoodsNum(){
    return badGoodsNum;
    }
    /*badGoodsNum set 方法 */
    public void setBadGoodsNum(Integer  badGoodsNum){
    this.badGoodsNum=badGoodsNum;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
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

    public BigDecimal getDecTotal() {
        return decTotal;
    }

    public void setDecTotal(BigDecimal decTotal) {
        this.decTotal = decTotal;
    }
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public Date getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}
	public Date getOverdueDate() {
		return overdueDate;
	}
	public void setOverdueDate(Date overdueDate) {
		this.overdueDate = overdueDate;
	}
	public List<OrderReturnIdepotBatchDTO> getBatchList() {
		return batchList;
	}
	public void setBatchList(List<OrderReturnIdepotBatchDTO> batchList) {
		this.batchList = batchList;
	}

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }
    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

	public BigDecimal getTradeRefundAmount() {
		return tradeRefundAmount;
	}
	public void setTradeRefundAmount(BigDecimal tradeRefundAmount) {
		this.tradeRefundAmount = tradeRefundAmount;
	}
	public BigDecimal getTradeRefundTax() {
		return tradeRefundTax;
	}
	public void setTradeRefundTax(BigDecimal tradeRefundTax) {
		this.tradeRefundTax = tradeRefundTax;
	}

    public Long getStockLocationTypeId() {
        return stockLocationTypeId;
    }

    public void setStockLocationTypeId(Long stockLocationTypeId) {
        this.stockLocationTypeId = stockLocationTypeId;
    }

    public String getStockLocationTypeName() {
        return stockLocationTypeName;
    }

    public void setStockLocationTypeName(String stockLocationTypeName) {
        this.stockLocationTypeName = stockLocationTypeName;
    }
}
