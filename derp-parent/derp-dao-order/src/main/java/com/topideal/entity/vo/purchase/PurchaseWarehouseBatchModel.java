package com.topideal.entity.vo.purchase;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 采购入库单商品批次详情
 * 
 * @author lian_
 *
 */
public class PurchaseWarehouseBatchModel extends PageModel implements Serializable {

	// 采购入库商品列表ID
	private Long itemId;
	// 失效时间
	private Date overdueDate;
	// 批次号
	private String batchNo;
	// 生产日期
	private Date productionDate;
	// 商品ID
	private Long goodsId;
	// 正常数量
	private Integer normalNum;
	// 创建人
	private Long creater;
	// id
	private Long id;
	// 坏货数量
	private Integer wornNum;
	// 过期数量
	private Integer expireNum;
	// 创建时间
	private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

	// 拓展字段
	// 商品货号
	private String goodsNo;
	// 商品名称
	private String goodsName;
	// 毛重
	private Double grossWeight;
	// 净重
	private Double netWeight;
	// 体积
	private Double volume;
	// 长
	private Double length;
	// 宽
	private Double width;
	// 高
	private Double height;
	//正常数量
	private Integer purchaseNum;
	// 生产日期
	private String productionDateStr;
	// 失效时间
	private String overdueDateStr;
	// 理货单位
	private String tallyingUnit;
	// 条形码
	private String barcode;
	// 实收数量
	private Integer tallyingNum;

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Integer getTallyingNum() {
		return tallyingNum;
	}

	public void setTallyingNum(Integer tallyingNum) {
		this.tallyingNum = tallyingNum;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}

	public String getProductionDateStr() {
		return productionDateStr;
	}

	public void setProductionDateStr(String productionDateStr) {
		this.productionDateStr = productionDateStr;
	}

	public String getOverdueDateStr() {
		return overdueDateStr;
	}

	public void setOverdueDateStr(String overdueDateStr) {
		this.overdueDateStr = overdueDateStr;
	}

	public Integer getPurchaseNum() {
		return purchaseNum;
	}

	public void setPurchaseNum(Integer purchaseNum) {
		this.purchaseNum = purchaseNum;
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

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	/* itemId get 方法 */
	public Long getItemId() {
		return itemId;
	}

	/* itemId set 方法 */
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	/* overdueDate get 方法 */
	public Date getOverdueDate() {
		return overdueDate;
	}

	/* overdueDate set 方法 */
	public void setOverdueDate(Date overdueDate) {
		this.overdueDate = overdueDate;
	}

	/* batchNo get 方法 */
	public String getBatchNo() {
		return batchNo;
	}

	/* batchNo set 方法 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	/* productionDate get 方法 */
	public Date getProductionDate() {
		return productionDate;
	}

	/* productionDate set 方法 */
	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
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

	/* wornNum get 方法 */
	public Integer getWornNum() {
		return wornNum;
	}

	/* wornNum set 方法 */
	public void setWornNum(Integer wornNum) {
		this.wornNum = wornNum;
	}

	/* expireNum get 方法 */
	public Integer getExpireNum() {
		return expireNum;
	}

	/* expireNum set 方法 */
	public void setExpireNum(Integer expireNum) {
		this.expireNum = expireNum;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

}
