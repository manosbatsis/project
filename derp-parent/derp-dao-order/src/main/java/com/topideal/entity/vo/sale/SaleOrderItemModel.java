package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
/**
 * 销售订单商品
 * @author lian_
 *
 */
public class SaleOrderItemModel extends PageModel implements Serializable{

     //销售总金额
     private BigDecimal amount;
     //销售订单ID
     private Long orderId;
     //商品ID
     private Long goodsId;
     //销售单价
     private BigDecimal price;
     //销售数量
     private Integer num;
     //id
     private Long id;
     //毛重
     private Double grossWeight;
     //净重
     private Double netWeight;
     //品牌类型
     private String brandName;
     //合同号
     private String contractNo;
     //箱号
     private String boxNo;
     // 商品编码
  	private String goodsCode;
  	// 商品名称
  	private String goodsName;
  	// 商品货号
  	private String goodsNo;
  	//属性说明
    private String unitName;
    // 单位
    private Long unit;
    // 条形码
  	private String barcode;
    //理货单位(00-托盘，01-箱，02-件
    private String tallyingUnit;
    //修改时间
    private Timestamp modifyDate;
    //创建时间
    private Timestamp createDate;
    //备注
    private String remark;

	// 工厂编码
  	private String factoryNo;

  	//标准条码
  	private String commbarcode;

    /**
     * 总毛重
     */
    private Double grossWeightSum;
    /**
     * 总净重
     */
    private Double netWeightSum;

    //申报单价
    private BigDecimal declarePrice;
    //序号
    private Integer seq;
    /**
     * 成分占比
     */
    private String component;
    /**
     * 箱数
     */
    private Integer boxNum;
    /**
     * 托盘号
     */
    private String torrNo;
    /**
     * 销售单价（含税）
     */
    private BigDecimal taxPrice;
    /**
     * 销售总金额（含税）
     */
    private BigDecimal taxAmount;
    /**
     * 税额
     */
    private BigDecimal tax;
    /**
     * 税率
     */
    private Double taxRate;

    public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getTallyingUnit() {
		return tallyingUnit;
	}
	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}
	/*unitName get 方法 */
    public String getUnitName(){
        return unitName;
    }
    /*unitName set 方法 */
    public void setUnitName(String  unitName){
        this.unitName=unitName;
    }
    /*boxNo get 方法 */
    public String getBoxNo(){
        return boxNo;
    }
    /*boxNo set 方法 */
    public void setBoxNo(String  boxNo){
        this.boxNo=boxNo;
    }
    /*contractNo get 方法 */
    public String getContractNo(){
        return contractNo;
    }
    /*contractNo set 方法 */
    public void setContractNo(String  contractNo){
        this.contractNo=contractNo;
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
    /*brandName get 方法 */
    public String getBrandName(){
        return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
        this.brandName=brandName;
    }
    /*amount get 方法 */
    public BigDecimal getAmount(){
        return amount;
    }
    /*amount set 方法 */
    public void setAmount(BigDecimal  amount){
        this.amount=amount;
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
    /*price get 方法 */
    public BigDecimal getPrice(){
        return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
        this.price=price;
    }
    /*num get 方法 */
    public Integer getNum(){
        return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
        this.num=num;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
    /*unit get 方法 */
    public Long getUnit(){
        return unit;
    }
    /*unit set 方法 */
    public void setUnit(Long  unit){
        this.unit=unit;
    }
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getFactoryNo() {
		return factoryNo;
	}
	public void setFactoryNo(String factoryNo) {
		this.factoryNo = factoryNo;
	}
    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    public Double getGrossWeightSum() {
        return grossWeightSum;
    }

    public void setGrossWeightSum(Double grossWeightSum) {
        this.grossWeightSum = grossWeightSum;
    }

    public Double getNetWeightSum() {
        return netWeightSum;
    }

    public void setNetWeightSum(Double netWeightSum) {
        this.netWeightSum = netWeightSum;
    }

    public BigDecimal getDeclarePrice() {
        return declarePrice;
    }

    public void setDeclarePrice(BigDecimal declarePrice) {
        this.declarePrice = declarePrice;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Integer getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(Integer boxNum) {
        this.boxNum = boxNum;
    }

    public String getTorrNo() {
        return torrNo;
    }

    public void setTorrNo(String torrNo) {
        this.torrNo = torrNo;
    }

    public BigDecimal getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(BigDecimal taxPrice) {
        this.taxPrice = taxPrice;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }
}

