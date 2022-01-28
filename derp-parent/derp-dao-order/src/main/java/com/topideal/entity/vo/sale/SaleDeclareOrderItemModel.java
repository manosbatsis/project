package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
@ApiModel
public class SaleDeclareOrderItemModel extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "销售预申报单ID")
    private Long declareOrderId;

	@ApiModelProperty(value = "销售订单ID")
    private Long saleOrderId;

	@ApiModelProperty(value = "销售订单编号")
    private String saleOrderCode;

	@ApiModelProperty(value = "po单号")
    private String poNo;

	@ApiModelProperty(value = "商品id")
    private Long goodsId;

	@ApiModelProperty(value = "商品编码")
    private String goodsCode;

	@ApiModelProperty(value = "商品名称")
    private String goodsName;

	@ApiModelProperty(value = "商品货号")
    private String goodsNo;

	@ApiModelProperty(value = "条形码")
    private String barcode;

	@ApiModelProperty(value = "标准条码")
    private String commbarcode;

	@ApiModelProperty(value = "申报数量")
    private Integer num;

	@ApiModelProperty(value = "销售单价")
    private BigDecimal salePrice;

	@ApiModelProperty(value = "申报单价")
    private BigDecimal price;

	@ApiModelProperty(value = "申报总金额")
    private BigDecimal amount;

	@ApiModelProperty(value = "品牌类型")
    private String brandName;

	@ApiModelProperty(value = "毛重")
    private Double grossWeight;

	@ApiModelProperty(value = "净重")
    private Double netWeight;

	@ApiModelProperty(value = "总毛重")
    private Double grossWeightSum;

	@ApiModelProperty(value = "总净重")
    private Double netWeightSum;

	@ApiModelProperty(value = "箱数")
    private Integer boxNum;

	@ApiModelProperty(value = "箱号")
    private String boxNo;

	@ApiModelProperty(value = "托盘号")
    private String torrNo;

	@ApiModelProperty(value = "批次号")
    private String batchNo;

	@ApiModelProperty(value = "成分占比")
    private String constituentRatio;

	@ApiModelProperty(value = "序号")
    private Integer seq;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

    @ApiModelProperty(value = "创建人")
    private Long creater;

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
    /*declareOrderId get 方法 */
    public Long getDeclareOrderId(){
    return declareOrderId;
    }
    /*declareOrderId set 方法 */
    public void setDeclareOrderId(Long  declareOrderId){
    this.declareOrderId=declareOrderId;
    }
    /*saleOrderId get 方法 */
    public Long getSaleOrderId(){
    return saleOrderId;
    }
    /*saleOrderId set 方法 */
    public void setSaleOrderId(Long  saleOrderId){
    this.saleOrderId=saleOrderId;
    }
    /*saleOrderCode get 方法 */
    public String getSaleOrderCode(){
    return saleOrderCode;
    }
    /*saleOrderCode set 方法 */
    public void setSaleOrderCode(String  saleOrderCode){
    this.saleOrderCode=saleOrderCode;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
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
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*salePrice get 方法 */
    public BigDecimal getSalePrice(){
    return salePrice;
    }
    /*salePrice set 方法 */
    public void setSalePrice(BigDecimal  salePrice){
    this.salePrice=salePrice;
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
    /*brandName get 方法 */
    public String getBrandName(){
    return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
    this.brandName=brandName;
    }
    /*grossWeight get 方法 */
    public Double getGrossWeight(){
    return grossWeight;
    }
    /*grossWeight set 方法 */
    public void setGrossWeight(Double  grossWeight){
    this.grossWeight=grossWeight;
    }
    /*netWeight get 方法 */
    public Double getNetWeight(){
    return netWeight;
    }
    /*netWeight set 方法 */
    public void setNetWeight(Double  netWeight){
    this.netWeight=netWeight;
    }
    /*grossWeightSum get 方法 */
    public Double getGrossWeightSum(){
    return grossWeightSum;
    }
    /*grossWeightSum set 方法 */
    public void setGrossWeightSum(Double  grossWeightSum){
    this.grossWeightSum=grossWeightSum;
    }
    /*netWeightSum get 方法 */
    public Double getNetWeightSum(){
    return netWeightSum;
    }
    /*netWeightSum set 方法 */
    public void setNetWeightSum(Double  netWeightSum){
    this.netWeightSum=netWeightSum;
    }
    /*boxNum get 方法 */
    public Integer getBoxNum(){
    return boxNum;
    }
    /*boxNum set 方法 */
    public void setBoxNum(Integer  boxNum){
    this.boxNum=boxNum;
    }
    /*boxNo get 方法 */
    public String getBoxNo(){
    return boxNo;
    }
    /*boxNo set 方法 */
    public void setBoxNo(String  boxNo){
    this.boxNo=boxNo;
    }
    /*torrNo get 方法 */
    public String getTorrNo(){
    return torrNo;
    }
    /*torrNo set 方法 */
    public void setTorrNo(String  torrNo){
    this.torrNo=torrNo;
    }
    /*batchNo get 方法 */
    public String getBatchNo(){
    return batchNo;
    }
    /*batchNo set 方法 */
    public void setBatchNo(String  batchNo){
    this.batchNo=batchNo;
    }
    /*constituentRatio get 方法 */
    public String getConstituentRatio(){
    return constituentRatio;
    }
    /*constituentRatio set 方法 */
    public void setConstituentRatio(String  constituentRatio){
    this.constituentRatio=constituentRatio;
    }
    /*seq get 方法 */
    public Integer getSeq(){
    return seq;
    }
    /*seq set 方法 */
    public void setSeq(Integer  seq){
    this.seq=seq;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }

    public Long getSaleItemId() {
        return saleItemId;
    }

    public void setSaleItemId(Long saleItemId) {
        this.saleItemId = saleItemId;
    }
}
