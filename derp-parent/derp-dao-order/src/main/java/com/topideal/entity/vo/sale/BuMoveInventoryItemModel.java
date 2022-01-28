package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class BuMoveInventoryItemModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 事业部移库单ID
    */
    private Long moveId;
    /**
    * 商品ID
    */
    private Long goodsId;
    /**
    * 商品编码
    */
    private String goodsCode;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 条形码
    */
    private String barcode;
    /**
     * 标准条码
     */
    private String commbarcode;
    /**
    * 销售数量
    */
    private Integer num;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    //移库单价
    private BigDecimal price;
    //移库金额
    private BigDecimal amount;
    //协议单价
    private BigDecimal agreementPrice;
    //汇率
    private Double rate;

    //库存类型  0 正常品  1 坏品
    private String type;
    /**
     * 移入库位类型id
     */
    private Long inStockLocationTypeId;
    /**
     * 移入库位类型
     */
    private String inStockLocationTypeName;
    /**
     * 移出库位类型id
     */
    private Long outStockLocationTypeId;
    /**
     * 移出库位类型
     */
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


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
