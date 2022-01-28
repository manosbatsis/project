package com.topideal.entity.dto.sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SaleSdOrderItemDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "销售sd单id")
    private Long saleSdOrderId;

	@ApiModelProperty(value = "商品id")
    private Long goodsId;

	@ApiModelProperty(value = "商品货号")
    private String goodsNo;

	@ApiModelProperty(value = "商品名称")
    private String goodsName;

	@ApiModelProperty(value = "商品条码")
    private String barcode;

	@ApiModelProperty(value = "理货单位(00-托盘，01-箱，02-件")
    private String tallyingUnit;

	@ApiModelProperty(value = "标准条码")
    private String commbarcode;

	@ApiModelProperty(value = "数量")
    private Integer num;

	@ApiModelProperty(value = "单价")
    private BigDecimal price;

	@ApiModelProperty(value = "金额")
    private BigDecimal amount;

	@ApiModelProperty(value = "比例")
    private Double sdRatio;

	@ApiModelProperty(value = "SD单价")
    private BigDecimal sdPrice;

	@ApiModelProperty(value = "SD金额")
    private BigDecimal sdAmount;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
	
    @ApiModelProperty(value = "单据类型 1-上架单 2-销售退入库单")
    private String orderType;

    @ApiModelProperty(value = "销售明细id")
    private Long saleItemId;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*saleSdOrderId get 方法 */
    public Long getSaleSdOrderId(){
    return saleSdOrderId;
    }
    /*saleSdOrderId set 方法 */
    public void setSaleSdOrderId(Long  saleSdOrderId){
    this.saleSdOrderId=saleSdOrderId;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*tallyingUnit get 方法 */
    public String getTallyingUnit(){
    return tallyingUnit;
    }
    /*tallyingUnit set 方法 */
    public void setTallyingUnit(String  tallyingUnit){
    this.tallyingUnit=tallyingUnit;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
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
    /*sdRatio get 方法 */
    public Double getSdRatio(){
    return sdRatio;
    }
    /*sdRatio set 方法 */
    public void setSdRatio(Double  sdRatio){
    this.sdRatio=sdRatio;
    }
    /*sdPrice get 方法 */
    public BigDecimal getSdPrice(){
    return sdPrice;
    }
    /*sdPrice set 方法 */
    public void setSdPrice(BigDecimal  sdPrice){
    this.sdPrice=sdPrice;
    }
    /*sdAmount get 方法 */
    public BigDecimal getSdAmount(){
    return sdAmount;
    }
    /*sdAmount set 方法 */
    public void setSdAmount(BigDecimal  sdAmount){
    this.sdAmount=sdAmount;
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
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

    public Long getSaleItemId() {
        return saleItemId;
    }

    public void setSaleItemId(Long saleItemId) {
        this.saleItemId = saleItemId;
    }
}
