package com.topideal.entity.vo.purchase;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 理货单商品批次详情
 * 
 * @author lianchenxing
 */
public class TallyingItemBatchModel extends PageModel implements Serializable {

	// 理货单商品详情id
	private Long itemId;
	// 失效日期
	private Timestamp overdueDate;
	// 批次号
	private String batchNo;
	// 生产日期
	private Timestamp productionDate;
	// 商品id
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

	// 拓展字段
	// 商品货号
//	private String goodsNo;
//	// 商品名称
//	private String goodsName;
//	// 毛重
//	private String grossWeight;
//	// 净重
//	private String netWeight;
//	// 体积
//	private String volume;
//	// 长
//	private String length;
//	// 宽
//	private String width;
//	// 高
//	private String height;
//	// 生产日期
//	private String productionDateStr;
//	// 失效日期
//	private String overdueDateStr;
//	// 理货单位
//	private String tallyingUnit;
	
//	public String getTallyingUnit() {
//		return tallyingUnit;
//	}
//
//	public void setTallyingUnit(String tallyingUnit) {
//		this.tallyingUnit = tallyingUnit;
//	}
//
//	public String getProductionDateStr() {
//		return productionDateStr;
//	}
//
//	public void setProductionDateStr(String productionDateStr) {
//		this.productionDateStr = productionDateStr;
//	}
//
//	public String getOverdueDateStr() {
//		return overdueDateStr;
//	}
//
//	public void setOverdueDateStr(String overdueDateStr) {
//		this.overdueDateStr = overdueDateStr;
//	}
//
//	public String getHeight() {
//		return height;
//	}
//
//	public void setHeight(String height) {
//		this.height = height;
//	}
//
//	public String getWidth() {
//		return width;
//	}
//
//	public void setWidth(String width) {
//		this.width = width;
//	}
//
//	public String getGrossWeight() {
//		return grossWeight;
//	}
//
//	public void setGrossWeight(String grossWeight) {
//		this.grossWeight = grossWeight;
//	}
//
//	public String getNetWeight() {
//		return netWeight;
//	}
//
//	public void setNetWeight(String netWeight) {
//		this.netWeight = netWeight;
//	}
//
//	public String getVolume() {
//		return volume;
//	}
//
//	public void setVolume(String volume) {
//		this.volume = volume;
//	}
//
//	public String getLength() {
//		return length;
//	}
//
//	public void setLength(String length) {
//		this.length = length;
//	}
//
//	public String getGoodsName() {
//		return goodsName;
//	}
//
//	public void setGoodsName(String goodsName) {
//		this.goodsName = goodsName;
//	}
//
//	public String getGoodsNo() {
//		return goodsNo;
//	}
//
//	public void setGoodsNo(String goodsNo) {
//		this.goodsNo = goodsNo;
//	}

	/* itemId get 方法 */
	public Long getItemId() {
		return itemId;
	}

	/* itemId set 方法 */
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	/* overdueDate get 方法 */
	public Timestamp getOverdueDate() {
		return overdueDate;
	}

	/* overdueDate set 方法 */
	public void setOverdueDate(Timestamp overdueDate) {
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
	public Timestamp getProductionDate() {
		return productionDate;
	}

	/* productionDate set 方法 */
	public void setProductionDate(Timestamp productionDate) {
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
