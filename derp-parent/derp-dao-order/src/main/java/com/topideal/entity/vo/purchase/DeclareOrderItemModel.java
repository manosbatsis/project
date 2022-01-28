package com.topideal.entity.vo.purchase;


import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 预申报单表体
 *
 * @author lian_
 *
 */
public class DeclareOrderItemModel extends PageModel implements Serializable {

	// 采购总金额
	private BigDecimal amount;
	// 批次号
	private String batchNo;
	// 子合同号
	private String contractNo;
	// 商品id
	private Long goodsId;
	// 采购数量
	private Integer num;
	// 成分占比
	private String constituentRatio;
	// 预申报单ID
	private Long declareOrderId;
	// 箱号
	private String boxNo;
	// 申报单价
	private BigDecimal price;
	// 创建人
	private Long creater;
	// id
	private Long id;
	// 创建时间
	private Timestamp createDate;
	// 采购订单号
	private String purchase;
	// 商品货号
	private String goodsNo;
	// 商品名称
	private String goodsName;
	// 商品编码
	private String goodsCode;
	// 净重
	private Double grossWeight;
	// 毛重
	private Double netWeight;
	// 品牌名称
	private String brandName;
	//采购单位(00-托盘，01-箱，02-件)
    private String purchaseUnit;
    //总毛重
    private Double grossWeightSum;
	//总净重
    private Double netWeightSum;
    //托盘号
    private String palletNo;
    //箱数
    private Integer cartons;
    //商品规格型号
    private String goodsSpec;
    //采购单价
    private BigDecimal purchasePrice;

    //拓展字段
    //条形码
    private String barcode;
	//序号
	private Integer seq;
	//采购订单表体Id
	private Long purchaseItemId;
	//采购订单Id
	private Long purchaseId;
	//PO号
	private String poNo;

    public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public String getConstituentRatio() {
		return constituentRatio;
	}
	public void setConstituentRatio(String constituentRatio) {
		this.constituentRatio = constituentRatio;
	}
	public String getGoodsSpec() {
		return goodsSpec;
	}
	public void setGoodsSpec(String goodsSpec) {
		this.goodsSpec = goodsSpec;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	/*cartons get 方法 */
    public Integer getCartons(){
        return cartons;
    }
    /*cartons set 方法 */
    public void setCartons(Integer  cartons){
        this.cartons=cartons;
    }
    /*palletNo get 方法 */
    public String getPalletNo(){
        return palletNo;
    }
    /*palletNo set 方法 */
    public void setPalletNo(String  palletNo){
        this.palletNo=palletNo;
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

	public String getPurchaseUnit() {
		return purchaseUnit;
	}

	public void setPurchaseUnit(String purchaseUnit) {
		this.purchaseUnit = purchaseUnit;
	}

	/* brandName get 方法 */
	public String getBrandName() {
		return brandName;
	}

	/* brandName set 方法 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/* purchase get 方法 */
	public String getPurchase() {
		return purchase;
	}

	/* purchase set 方法 */
	public void setPurchase(String purchase) {
		this.purchase = purchase;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	/* amount get 方法 */
	public BigDecimal getAmount() {
		return amount;
	}

	/* amount set 方法 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/* batchNo get 方法 */
	public String getBatchNo() {
		return batchNo;
	}

	/* batchNo set 方法 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	/* contractNo get 方法 */
	public String getContractNo() {
		return contractNo;
	}

	/* contractNo set 方法 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	/* goodsId get 方法 */
	public Long getGoodsId() {
		return goodsId;
	}

	/* goodsId set 方法 */
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	/* num get 方法 */
	public Integer getNum() {
		return num;
	}

	/* num set 方法 */
	public void setNum(Integer num) {
		this.num = num;
	}

	/* declareOrderId get 方法 */
	public Long getDeclareOrderId() {
		return declareOrderId;
	}

	/* declareOrderId set 方法 */
	public void setDeclareOrderId(Long declareOrderId) {
		this.declareOrderId = declareOrderId;
	}

	/* boxNo get 方法 */
	public String getBoxNo() {
		return boxNo;
	}

	/* boxNo set 方法 */
	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	/* price get 方法 */
	public BigDecimal getPrice() {
		return price;
	}

	/* price set 方法 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/* creater get 方法 */
	public Long getCreater() {
		return creater;
	}

	/* creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Long getPurchaseItemId() {
		return purchaseItemId;
	}

	public void setPurchaseItemId(Long purchaseItemId) {
		this.purchaseItemId = purchaseItemId;
	}

	public Long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
}
