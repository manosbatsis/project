package com.topideal.entity.dto.sale;

import com.topideal.common.constant.DERP;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class SaleOrderItemDTO extends PageModel implements Serializable{

    @ApiModelProperty(value = "销售总金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "销售订单ID")
    private Long orderId;
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "销售单价")
    private BigDecimal price;
    @ApiModelProperty(value = "销售数量")
    private Integer num;
    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "毛重")
    private Double grossWeight;
    @ApiModelProperty(value = "净重")
    private Double netWeight;
    @ApiModelProperty(value = "品牌类型")
    private String brandName;
    @ApiModelProperty(value = "合同号")
    private String contractNo;
    @ApiModelProperty(value = "箱号")
    private String boxNo;
    @ApiModelProperty(value = "商品编码")
 	private String goodsCode;
 	@ApiModelProperty(value = "商品名称")
 	private String goodsName;
 	@ApiModelProperty(value = "商品货号")
 	private String goodsNo;
 	@ApiModelProperty(value = "属性说明")
   private String unitName;
   @ApiModelProperty(value = "单位")
   private Long unit;
   @ApiModelProperty(value = "条形码")
 	private String barcode;
   @ApiModelProperty(value = "理货单位(00-托盘，01-箱，02-件")
   private String tallyingUnit;
   @ApiModelProperty(value = "理货单位(中文")
   private String tallyingUnitLabel;
   @ApiModelProperty(value = "修改时间")
   private Timestamp modifyDate;
   @ApiModelProperty(value = "创建时间")
   private Timestamp createDate;
   @ApiModelProperty(value = "备注")
   private String remark;

	@ApiModelProperty(value = "工厂编码")
 	private String factoryNo;
 	@ApiModelProperty(value = "销售订单编码")
 	private String orderCode;

 	@ApiModelProperty(value = "标准条码")
 	private String commbarcode;

 	@ApiModelProperty(value = "总毛重")
    private Double grossWeightSum;

    @ApiModelProperty(value = "总净重")
    private Double netWeightSum;
    @ApiModelProperty(value = "申报单价")
    private BigDecimal declarePrice;
    @ApiModelProperty(value = "序号")
    private Integer seq;
    @ApiModelProperty(value = "备案价格")
    private BigDecimal filingPrice;
    @ApiModelProperty(value = "po单号")
    private String poNo;
    @ApiModelProperty(value = "标准品牌名")
    private String commBrandParentName;

    @ApiModelProperty(value = "成分占比")
    private String component;

    @ApiModelProperty(value = "箱数")
    private Integer boxNum;

    @ApiModelProperty(value = "托盘号")
    private String torrNo;

    @ApiModelProperty(value = "销售单价（含税）")
    private BigDecimal taxPrice;

    @ApiModelProperty(value = "销售总金额（含税）")
    private BigDecimal taxAmount;

    @ApiModelProperty(value = "税额")
    private BigDecimal tax;

    @ApiModelProperty(value = "税率")
    private Double taxRate;

    @ApiModelProperty(value = "待出库量")
    private Integer stayOutNum;

    @ApiModelProperty(value = "上架量")
    private Integer shelfNum;

    @ApiModelProperty(value = "上架总金额")
    private BigDecimal shelfAmount;

    @ApiModelProperty(value = "销售订单id集合")
    private List<Long> orderIds;

    /*filingPrice get 方法 */
    public BigDecimal getFilingPrice(){
        return filingPrice;
    }
    /*filingPrice set 方法 */
    public void setFilingPrice(BigDecimal  filingPrice){
        this.filingPrice=filingPrice;
    }

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
		if(StringUtils.isNotBlank(tallyingUnit)){
			this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
		}
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
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}


   public String getCommbarcode() {
       return commbarcode;
   }

   public void setCommbarcode(String commbarcode) {
       this.commbarcode = commbarcode;
   }
	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}
	public void setTallyingUnitLabel(String tallyingUnitLabel) {
		this.tallyingUnitLabel = tallyingUnitLabel;
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

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getCommBrandParentName() {
        return commBrandParentName;
    }

    public void setCommBrandParentName(String commBrandParentName) {
        this.commBrandParentName = commBrandParentName;
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
	public Integer getStayOutNum() {
		return stayOutNum;
	}
	public void setStayOutNum(Integer stayOutNum) {
		this.stayOutNum = stayOutNum;
	}

    public Integer getShelfNum() {
        return shelfNum;
    }

    public void setShelfNum(Integer shelfNum) {
        this.shelfNum = shelfNum;
    }

    public BigDecimal getShelfAmount() {
        return shelfAmount;
    }

    public void setShelfAmount(BigDecimal shelfAmount) {
        this.shelfAmount = shelfAmount;
    }
	public List<Long> getOrderIds() {
		return orderIds;
	}
	public void setOrderIds(List<Long> orderIds) {
		this.orderIds = orderIds;
	}
}

