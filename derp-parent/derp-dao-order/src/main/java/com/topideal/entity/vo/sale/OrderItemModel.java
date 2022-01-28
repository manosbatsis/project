package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 电商订单表体
 * @author lian_
 *
 */
public class OrderItemModel extends PageModel implements Serializable{

     //电商订单ID
     private Long orderId;
     //商品ID
     private Long goodsId;
     //结算单价
     private BigDecimal price;
     //销售/结算数量
     private Integer num;
     //id
     private Long id;
     //结算总金额
     private BigDecimal decTotal;
     //商品名称
     private String goodsName;
      //商品编码
     private String goodsCode;
      //条形码
     private String barcode;
   //属性说明
     private String goodsNo;
     //成本价（与商家结算价）
     private BigDecimal deliveryPrice;
     //修改时间
     private Timestamp modifyDate;
     // 创建时间
     private Timestamp createDate;
     //批次号
     private String batchNo;
     //商品优惠金额
     private BigDecimal goodsDiscount;

     //标准条码
     private String commbarcode;

     //佣金
     private BigDecimal saleCom;

    //销售单价
    private BigDecimal originalPrice;

    //销售总金额
    private BigDecimal originalDecTotal;

    //事业部名称
    private String buName;

    //事业部id
    private Long buId;

    //商品税费
    private BigDecimal tax;
    //商品skuid
    private String skuId;
    /**
     * 分摊运费，2位小数
     */
    private BigDecimal wayFrtFee;

    /**
     * 批次号
     */
    private String orderBatchNo;
    /**
     * 生产日期
     */
    private Date productionDate;
    /**
     * 失效日期
     */
    private Date overdueDate;
    /**
	 * 内贸商品结算金额（不含税）
	 */
	private BigDecimal tradeDecTotal;
	/**
	 * 内贸商品结算税额
	 */
	private BigDecimal tradeDecTax;

    /**
     * 事业部库位类型ID
     */
    private Long stockLocationTypeId;
    /**
     * 库位类型
     */
    private String stockLocationTypeName;

     public BigDecimal getGoodsDiscount() {
		return goodsDiscount;
	}
	public void setGoodsDiscount(BigDecimal goodsDiscount) {
		this.goodsDiscount = goodsDiscount;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public BigDecimal getDeliveryPrice() {
		return deliveryPrice;
	}
	public void setDeliveryPrice(BigDecimal deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}
	/*barcode get 方法 */
     public String getBarcode(){
         return barcode;
     }
     /*barcode set 方法 */
     public void setBarcode(String  barcode){
         this.barcode=barcode;
     }
     /*goodsCode get 方法 */
     public String getGoodsCode(){
         return goodsCode;
     }
     /*goodsCode set 方法 */
     public void setGoodsCode(String  goodsCode){
         this.goodsCode=goodsCode;
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
    /*decTotal get 方法 */
    public BigDecimal getDecTotal(){
        return decTotal;
    }
    /*decTotal set 方法 */
    public void setDecTotal(BigDecimal  decTotal){
        this.decTotal=decTotal;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
        return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
        this.goodsName=goodsName;
    }
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    public BigDecimal getSaleCom() {
        return saleCom;
    }

    public void setSaleCom(BigDecimal saleCom) {
        this.saleCom = saleCom;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getOriginalDecTotal() {
        return originalDecTotal;
    }

    public void setOriginalDecTotal(BigDecimal originalDecTotal) {
        this.originalDecTotal = originalDecTotal;
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

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public BigDecimal getWayFrtFee() {
        return wayFrtFee;
    }

    public void setWayFrtFee(BigDecimal wayFrtFee) {
        this.wayFrtFee = wayFrtFee;
    }

    public String getOrderBatchNo() {
        return orderBatchNo;
    }

    public void setOrderBatchNo(String orderBatchNo) {
        this.orderBatchNo = orderBatchNo;
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
	public BigDecimal getTradeDecTotal() {
		return tradeDecTotal;
	}
	public void setTradeDecTotal(BigDecimal tradeDecTotal) {
		this.tradeDecTotal = tradeDecTotal;
	}
	public BigDecimal getTradeDecTax() {
		return tradeDecTax;
	}
	public void setTradeDecTax(BigDecimal tradeDecTax) {
		this.tradeDecTax = tradeDecTax;
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

