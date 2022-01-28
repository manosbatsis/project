package com.topideal.entity.dto.purchase;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 采购入库商品列表
 *
 * @author lian_
 *
 */
public class PurchaseWarehouseItemDTO extends PageModel implements Serializable {

	// 采购入库单ID
	private Long warehouseId;
	// 多货数量
	private Integer multiNum;
	// 缺少数量
	private Integer lackNum;
	// 商品ID
	private Long goodsId;
	// 正常数量
	private Integer normalNum;
	// 创建人
	private Long creater;
	// id
	private Long id;
	// 采购单数
	private Integer purchaseNum;
	// 理货数量
	private Integer tallyingNum;
	// 创建时间
	private Timestamp createDate;
	// 商品名称
	private String goodsName;
	// 条形码
	private String barcode;
    //属性说明
    private String createName;
    // 商品货号
 	private String goodsNo;
 	//毛重
    private Double grossWeight;
    //净重
    private Double netWeight;
    //长
    private Double length;
    // 体积
    private Double volume;
    //宽
    private Double width;
    // 高
    private Double height;
    //理货单位(00-托盘，01-箱，02-件)
    private String tallyingUnit;
    //修改时间
    private Timestamp modifyDate;

	// 拓展字段
	// 采购入库单
	private String warehouseCode;
	//采购订单表体Id
	private Long purchaseItemId;


	// 正常数
	private Integer transferNum;
	// 坏品数
	private Integer wornNum;
	// 过期数
	private Integer expireNum;

	private BigDecimal purchasePrice;
	// 失效时间
	private String overdueDate;
	// 批次号
	private String batchNo;
	// 生产日期
	private String productionDate;

	public Long getPurchaseItemId() {
		return purchaseItemId;
	}

	public void setPurchaseItemId(Long purchaseItemId) {
		this.purchaseItemId = purchaseItemId;
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
	/*height get 方法 */
    public Double getHeight(){
        return height;
    }
    /*height set 方法 */
    public void setHeight(Double  height){
        this.height=height;
    }
	/*width get 方法 */
    public Double getWidth(){
        return width;
    }
    /*width set 方法 */
    public void setWidth(Double  width){
        this.width=width;
    }
    /*length get 方法 */
    public Double getLength(){
        return length;
    }
    /*length set 方法 */
    public void setLength(Double  length){
        this.length=length;
    }
    /*volume get 方法 */
    public Double getVolume(){
        return volume;
    }
    /*volume set 方法 */
    public void setVolume(Double  volume){
        this.volume=volume;
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
	   /*createName get 方法 */
    public String getCreateName(){
        return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
        this.createName=createName;
    }
	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	/* warehouseId get 方法 */
	public Long getWarehouseId() {
		return warehouseId;
	}

	/* warehouseId set 方法 */
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	/* multiNum get 方法 */
	public Integer getMultiNum() {
		return multiNum;
	}

	/* multiNum set 方法 */
	public void setMultiNum(Integer multiNum) {
		this.multiNum = multiNum;
	}

	/* lackNum get 方法 */
	public Integer getLackNum() {
		return lackNum;
	}

	/* lackNum set 方法 */
	public void setLackNum(Integer lackNum) {
		this.lackNum = lackNum;
	}

	/* goodsId get 方法 */
	public Long getGoodsId() {
		return goodsId;
	}

	/* goodsId set 方法 */
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	/* normalNum get 方法 */
	public Integer getNormalNum() {
		return normalNum;
	}

	/* normalNum set 方法 */
	public void setNormalNum(Integer normalNum) {
		this.normalNum = normalNum;
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

	/* purchaseNum get 方法 */
	public Integer getPurchaseNum() {
		return purchaseNum;
	}

	/* purchaseNum set 方法 */
	public void setPurchaseNum(Integer purchaseNum) {
		this.purchaseNum = purchaseNum;
	}

	/* tallyingNum get 方法 */
	public Integer getTallyingNum() {
		return tallyingNum;
	}

	/* tallyingNum set 方法 */
	public void setTallyingNum(Integer tallyingNum) {
		this.tallyingNum = tallyingNum;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Integer getTransferNum() {
		return transferNum;
	}

	public void setTransferNum(Integer transferNum) {
		this.transferNum = transferNum;
	}

	public Integer getWornNum() {
		return wornNum;
	}

	public void setWornNum(Integer wornNum) {
		this.wornNum = wornNum;
	}

	public Integer getExpireNum() {
		return expireNum;
	}

	public void setExpireNum(Integer expireNum) {
		this.expireNum = expireNum;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getOverdueDate() {
		return overdueDate;
	}

	public void setOverdueDate(String overdueDate) {
		this.overdueDate = overdueDate;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}
}
