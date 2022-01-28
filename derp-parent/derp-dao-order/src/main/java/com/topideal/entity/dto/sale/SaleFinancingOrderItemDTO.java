package com.topideal.entity.dto.sale;
import com.topideal.common.constant.DERP;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@ApiModel
public class SaleFinancingOrderItemDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "融资订单ID")
    private Long orderId;
   
	@ApiModelProperty(value = "商品ID")
    private Long goodsId;
   
	@ApiModelProperty(value = "商品编码")
    private String goodsCode;
   
	@ApiModelProperty(value = "商品名称")
    private String goodsName;
  
	@ApiModelProperty(value = "商品货号")
    private String goodsNo;
    
	@ApiModelProperty(value = "标准条码")
    private String commbarcode;
  
	@ApiModelProperty(value = "条形码")
    private String barcode;
   
	@ApiModelProperty(value = "销售数量")
    private Integer num;
    
	@ApiModelProperty(value = "单价")
    private BigDecimal price;
   
	@ApiModelProperty(value = "金额")
    private BigDecimal amount;
   
	@ApiModelProperty(value = "理货单位(00-托盘，01-箱，02-件")
    private String tallyingUnit;
	@ApiModelProperty(value = "理货单位(中文)")
	private String tallyingUnitLabel;
  
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
   
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
  
	@ApiModelProperty(value = "原货号")
    private String originalGoodsNo;
   
	@ApiModelProperty(value = "原货号id")
    private Long originalGoodsId;

    @ApiModelProperty(value = "合同号")
    private String contractNo;

    @ApiModelProperty(value = "赎回单价")
    private BigDecimal ransomPrice;

    @ApiModelProperty(value = "赎回金额")
    private BigDecimal ransomAmount;

    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 应还本金
     */
    @ApiModelProperty(value = "应还本金")
    private BigDecimal principal;
    /**
     * 资金占用费
     */
    @ApiModelProperty(value = "资金占用费")
    private BigDecimal occupationFee;
    /**
     * 代理费
     */
    @ApiModelProperty(value = "代理费")
    private BigDecimal agencyFee;
    /**
     * 滞纳金
     */
    @ApiModelProperty(value = "滞纳金")
    private BigDecimal delayFee;
    /**
     * 应还款金额
     */
    @ApiModelProperty(value = "应还款金额")
    private BigDecimal payableFee;
    /**
     * 保证金
     */
    @ApiModelProperty(value = "保证金")
    private BigDecimal margin;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*orderId get 方法 */
    public Long getOrderId(){
    return orderId;
    }
    /*orderId set 方法 */
    public void setOrderId(Long  orderId){
    this.orderId=orderId;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsCode get 方法 */
    public String getGoodsCode(){
    return goodsCode;
    }
    /*goodsCode set 方法 */
    public void setGoodsCode(String  goodsCode){
    this.goodsCode=goodsCode;
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
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
    return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
    this.price=price;
    }
    /*amount get 方法 */
    public BigDecimal getAmount(){
    return amount;
    }
    /*amount set 方法 */
    public void setAmount(BigDecimal  amount){
    this.amount=amount;
    }
    /*tallyingUnit get 方法 */
    public String getTallyingUnit(){
    return tallyingUnit;
    }
    /*tallyingUnit set 方法 */
    public void setTallyingUnit(String  tallyingUnit){
    	this.tallyingUnit=tallyingUnit;
    	this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*originalGoodsNo get 方法 */
    public String getOriginalGoodsNo(){
    return originalGoodsNo;
    }
    /*originalGoodsNo set 方法 */
    public void setOriginalGoodsNo(String  originalGoodsNo){
    this.originalGoodsNo=originalGoodsNo;
    }
    /*originalGoodsId get 方法 */
    public Long getOriginalGoodsId(){
    return originalGoodsId;
    }
    /*originalGoodsId set 方法 */
    public void setOriginalGoodsId(Long  originalGoodsId){
    this.originalGoodsId=originalGoodsId;
    }
	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public BigDecimal getRansomPrice() {
        return ransomPrice;
    }

    public void setRansomPrice(BigDecimal ransomPrice) {
        this.ransomPrice = ransomPrice;
    }

    public BigDecimal getRansomAmount() {
        return ransomAmount;
    }

    public void setRansomAmount(BigDecimal ransomAmount) {
        this.ransomAmount = ransomAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setTallyingUnitLabel(String tallyingUnitLabel) {
        this.tallyingUnitLabel = tallyingUnitLabel;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getOccupationFee() {
        return occupationFee;
    }

    public void setOccupationFee(BigDecimal occupationFee) {
        this.occupationFee = occupationFee;
    }

    public BigDecimal getAgencyFee() {
        return agencyFee;
    }

    public void setAgencyFee(BigDecimal agencyFee) {
        this.agencyFee = agencyFee;
    }

    public BigDecimal getDelayFee() {
        return delayFee;
    }

    public void setDelayFee(BigDecimal delayFee) {
        this.delayFee = delayFee;
    }

    public BigDecimal getPayableFee() {
        return payableFee;
    }

    public void setPayableFee(BigDecimal payableFee) {
        this.payableFee = payableFee;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }
}
