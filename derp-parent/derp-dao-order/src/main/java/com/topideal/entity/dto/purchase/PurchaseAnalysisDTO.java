package com.topideal.entity.dto.purchase;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 采购入库分析表
 * 
 * @author lian_
 */
@ApiModel
public class PurchaseAnalysisDTO extends PageModel implements Serializable {

	// 商品货号
	@ApiModelProperty(value = "商品货号")
	private String goodsNo;
	// 供应商名称
	@ApiModelProperty(value = "供应商名称")
	private String supplierName;
	// 供应商ID
	@ApiModelProperty(value = "供应商ID")
	private Long supplierId;
	// 采购订单ID
	@ApiModelProperty(value = "采购订单ID")
	private Long orderId;
	// 商品ID
	@ApiModelProperty(value = "商品ID")
	private Long goodsId;
	// 入库数量
	@ApiModelProperty(value = "入库数量")
	private Integer warehouseNum;
	// 采购入库编码
	@ApiModelProperty(value = "采购入库编码")
	private String warehouseCode;
	// 采购入库单ID
	@ApiModelProperty(value = "采购入库单ID")
	private Long warehouseId;
	// 创建人
	@ApiModelProperty(value = "创建人")
	private Long creater;
	// 采购订单编号
	@ApiModelProperty(value = "采购订单编号")
	private String orderCode;
	// id
	@ApiModelProperty(value = "差异记录id")
	private Long id;
	// 采购数量
	@ApiModelProperty(value = "采购数量")
	private Integer purchaseNum;
	// 商品名称
	@ApiModelProperty(value = "商品名称")
	private String goodsName;
	// 创建时间
	@ApiModelProperty(value = "创建时间")
	private Timestamp createDate;
	// 完结入库时间
	@ApiModelProperty(value = "完结入库时间")
	private Timestamp endDate;
	// 是否完结入库(1-是,0-否)
	@ApiModelProperty(value = "是否完结入库(1-是,0-否)")
	private String isEnd;
	@ApiModelProperty(value = "是否完结入库中文(1-是,0-否)")
	private String isEndLabel ;
	// 失效日期
	@ApiModelProperty(value = "失效日期")
	private Date overdueDate;
	// 批次号
	@ApiModelProperty(value = "批次号")
	private String batchNo;
	// 入库是否组合品(1-是，0-否)
	@ApiModelProperty(value = "入库是否组合品(1-是，0-否)")
	private String isGroup;
	@ApiModelProperty(value = "入库是否组合品中文(1-是，0-否)")
	private String isGroupLabel;
	// 勾稽关联时间
	@ApiModelProperty(value = "勾稽关联时间")
	private Timestamp relDate;
	// 生产日期
	@ApiModelProperty(value = "生产日期")
	private Date productionDate;
	// 商家ID
	@ApiModelProperty(value = "商家ID")
	private Long merchantId;
	// 商家名称
	@ApiModelProperty(value = "商家名称")
	private String merchantName;
    //修改时间
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
    //条形码
	@ApiModelProperty(value = "条形码")
    private String barcode;
    //理货单位 00-托盘 01-箱 02-件
	@ApiModelProperty(value = "理货单位 00-托盘 01-箱 02-件")
    private String tallyingUnit;
	@ApiModelProperty(value = "理货单位中文 00-托盘 01-箱 02-件")
    private String tallyingUnitLabel ;

	// 拓展字段
	// 差异
	@ApiModelProperty(value = "差异数量")
	private Integer differenceNum;
	// 采购创建时间
	@ApiModelProperty(value = "采购创建时间")
	private Timestamp purchaseCreateDate;
	// 采购po号
	@ApiModelProperty(value = "采购po号")
	private String poNo;
	// 预计到港时间
	@ApiModelProperty(value = "预计到港时间")
	private String arriveDate;
	// 最后入仓时间
	@ApiModelProperty(value = "预计到港时间")
	private String warehouseDate;
	// 在途数量
	@ApiModelProperty(value = "在途数量")
	private Integer onPassageNum;

	/**
	 * 事业部名称
	 */
	@ApiModelProperty(value = "事业部名称")
	private String buName;
	/**
	 *  事业部id
	 */
	@ApiModelProperty(value = "事业部id")
	private Long buId;
	@ApiModelProperty(hidden = true)
	private List<Long> buIds;

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
		if(tallyingUnit != null) {
			this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
		}
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getWarehouseDate() {
		return warehouseDate;
	}

	public void setWarehouseDate(String warehouseDate) {
		this.warehouseDate = warehouseDate;
	}

	public String getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(String arriveDate) {
		this.arriveDate = arriveDate;
	}

	/* merchantId get 方法 */
	public Long getMerchantId() {
		return merchantId;
	}

	/* merchantId set 方法 */
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	/* merchantName get 方法 */
	public String getMerchantName() {
		return merchantName;
	}

	/* merchantName set 方法 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Integer getOnPassageNum() {
		return onPassageNum;
	}

	public void setOnPassageNum(Integer onPassageNum) {
		this.onPassageNum = onPassageNum;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Timestamp getPurchaseCreateDate() {
		return purchaseCreateDate;
	}

	public void setPurchaseCreateDate(Timestamp purchaseCreateDate) {
		this.purchaseCreateDate = purchaseCreateDate;
	}

	/* productionDate get 方法 */
	public Date getProductionDate() {
		return productionDate;
	}

	/* productionDate set 方法 */
	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	/* relDate get 方法 */
	public Timestamp getRelDate() {
		return relDate;
	}

	/* relDate set 方法 */
	public void setRelDate(Timestamp relDate) {
		this.relDate = relDate;
	}

	/* isGroup get 方法 */
	public String getIsGroup() {
		return isGroup;
	}

	/* isGroup set 方法 */
	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
		if(isGroup != null) {
			this.isGroupLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseAnalysis_isGroupList, isGroup);
		}
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

	/* isEnd get 方法 */
	public String getIsEnd() {
		return isEnd;
	}

	/* isEnd set 方法 */
	public void setIsEnd(String isEnd) {
		this.isEnd = isEnd;
		if(isEnd != null) {
			this.isEndLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseAnalysis_isEndList, isEnd) ;
		}
	}

	/* endDate get 方法 */
	public Timestamp getEndDate() {
		return endDate;
	}

	/* endDate set 方法 */
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	/* differenceNum get 方法 */
	public Integer getDifferenceNum() {
		return differenceNum;
	}

	/* differenceNum set 方法 */
	public void setDifferenceNum(Integer differenceNum) {
		this.differenceNum = differenceNum;
	}

	/* goodsNo get 方法 */
	public String getGoodsNo() {
		return goodsNo;
	}

	/* goodsNo set 方法 */
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	/* supplierName get 方法 */
	public String getSupplierName() {
		return supplierName;
	}

	/* supplierName set 方法 */
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	/* supplierId get 方法 */
	public Long getSupplierId() {
		return supplierId;
	}

	/* supplierId set 方法 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	/* orderId get 方法 */
	public Long getOrderId() {
		return orderId;
	}

	/* orderId set 方法 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	/* goodsId get 方法 */
	public Long getGoodsId() {
		return goodsId;
	}

	/* goodsId set 方法 */
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	/* warehouseNum get 方法 */
	public Integer getWarehouseNum() {
		return warehouseNum;
	}

	/* warehouseNum set 方法 */
	public void setWarehouseNum(Integer warehouseNum) {
		this.warehouseNum = warehouseNum;
	}

	/* warehouseCode get 方法 */
	public String getWarehouseCode() {
		return warehouseCode;
	}

	/* warehouseCode set 方法 */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	/* warehouseId get 方法 */
	public Long getWarehouseId() {
		return warehouseId;
	}

	/* warehouseId set 方法 */
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	/* creater get 方法 */
	public Long getCreater() {
		return creater;
	}

	/* creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}

	/* orderCode get 方法 */
	public String getOrderCode() {
		return orderCode;
	}

	/* orderCode set 方法 */
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
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

	/* goodsName get 方法 */
	public String getGoodsName() {
		return goodsName;
	}

	/* goodsName set 方法 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getIsEndLabel() {
		return isEndLabel;
	}

	public void setIsEndLabel(String isEndLabel) {
		this.isEndLabel = isEndLabel;
	}

	public String getIsGroupLabel() {
		return isGroupLabel;
	}

	public void setIsGroupLabel(String isGroupLabel) {
		this.isGroupLabel = isGroupLabel;
	}

	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}

	public void setTallyingUnitLabel(String tallyingUnitLabel) {
		this.tallyingUnitLabel = tallyingUnitLabel;
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

	public List<Long> getBuIds() {
		return buIds;
	}

	public void setBuIds(List<Long> buIds) {
		this.buIds = buIds;
	}
	
	
}
