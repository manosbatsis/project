package com.topideal.entity.dto.sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class BuMoveInventoryItemDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "事业部移库单ID")
    private Long moveId;

	@ApiModelProperty(value = "商品ID")
    private Long goodsId;

	@ApiModelProperty(value = "商品编码")
    private String goodsCode;

	@ApiModelProperty(value = "商品货号")
    private String goodsNo;

	@ApiModelProperty(value = "商品名称")
    private String goodsName;

	@ApiModelProperty(value = "条形码")
    private String barcode;

	@ApiModelProperty(value = "标准条码")
    private String commbarcode;

	@ApiModelProperty(value = "销售数量")
    private Integer num;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value = "移库单价")
    private BigDecimal price;

	@ApiModelProperty(value = "移库金额")
    private BigDecimal amount;

	@ApiModelProperty(value = "协议单价")
    private BigDecimal agreementPrice;

    @ApiModelProperty(value = "汇率")
    private Double rate;


    private Long originalGoodsId;

    @ApiModelProperty(value = "库存类型  0 正常品  1 坏品")
    private String type;
    @ApiModelProperty(value = "库存类型  0 正常品  1 坏品")
    private String typeLabel;

    /**
     * 移入库位类型id
     */
    @ApiModelProperty(value = "移入库位类型id")
    private Long inStockLocationTypeId;
    /**
     * 移入库位类型
     */
    @ApiModelProperty(value = "移入库位类型")
    private String inStockLocationTypeName;
    /**
     * 移出库位类型id
     */
    @ApiModelProperty(value = "移出库位类型id")
    private Long outStockLocationTypeId;
    /**
     * 移出库位类型
     */
    @ApiModelProperty(value = "移出库位类型")
    private String outStockLocationTypeName;

    /*id get 方法 */
    public Long getId() {
        return id;
    }

    /*id set 方法 */
    public void setId(Long id) {
        this.id = id;
    }

    /*moveId get 方法 */
    public Long getMoveId(){
    return moveId;
    }
    /*moveId set 方法 */
    public void setMoveId(Long  moveId){
    this.moveId=moveId;
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
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
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

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
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

    public BigDecimal getAgreementPrice() {
        return agreementPrice;
    }

    public void setAgreementPrice(BigDecimal agreementPrice) {
        this.agreementPrice = agreementPrice;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Long getOriginalGoodsId() {
        return originalGoodsId;
    }

    public void setOriginalGoodsId(Long originalGoodsId) {
        this.originalGoodsId = originalGoodsId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        this.typeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.buMoveInventoryItem_typeList, type);
    }

	public String getTypeLabel() {
		return typeLabel;
	}

	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}

    public Long getInStockLocationTypeId() {
        return inStockLocationTypeId;
    }

    public void setInStockLocationTypeId(Long inStockLocationTypeId) {
        this.inStockLocationTypeId = inStockLocationTypeId;
    }

    public String getInStockLocationTypeName() {
        return inStockLocationTypeName;
    }

    public void setInStockLocationTypeName(String inStockLocationTypeName) {
        this.inStockLocationTypeName = inStockLocationTypeName;
    }

    public Long getOutStockLocationTypeId() {
        return outStockLocationTypeId;
    }

    public void setOutStockLocationTypeId(Long outStockLocationTypeId) {
        this.outStockLocationTypeId = outStockLocationTypeId;
    }

    public String getOutStockLocationTypeName() {
        return outStockLocationTypeName;
    }

    public void setOutStockLocationTypeName(String outStockLocationTypeName) {
        this.outStockLocationTypeName = outStockLocationTypeName;
    }
}
