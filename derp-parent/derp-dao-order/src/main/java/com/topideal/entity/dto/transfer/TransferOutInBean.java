package com.topideal.entity.dto.transfer;


import java.io.Serializable;
import java.util.List;

import com.topideal.common.constant.DERP;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;

/**
 * 调出调入流水bean
 * */
@ApiModel
public class TransferOutInBean extends PageModel implements Serializable{

	@ApiModelProperty(value = "调拨单id", required = false)
	private Long id;//调拨单id
	@ApiModelProperty(value = "调拨订单号", required = false)
	private String code;//调拨订单号
	@ApiModelProperty(value = "调出仓库名称", required = false)
	private String outDepotName;//调出仓库名称
	@ApiModelProperty(value = "调入仓库名称", required = false)
	private String inDepotName;//调入仓库名称
	@ApiModelProperty(value = "调入商品id", required = false)
	private Long inGoodsId;//调入商品id
	@ApiModelProperty(value = "调入商品货号", required = false)
	private String inGoodsNo;//调入商品货号
	@ApiModelProperty(value = "调入商品名称", required = false)
	private String inGoodsName;//调入商品名称
	@ApiModelProperty(value = "调入商品id", required = false)
	private Long outGoodsId;//调入商品id
	@ApiModelProperty(value = "调出商品货号", required = false)
	private String outGoodsNo;//调出商品货号
	@ApiModelProperty(value = "调出商品名称", required = false)
	private String outGoodsName;//调出商品名称
	@ApiModelProperty(value = "调出数量", required = false)
	private Integer transferNum;//调出数量
	@ApiModelProperty(value = "调出单位", required = false)
	private String tallyingUnit;//调出单位 00-托盘 01-箱 02-件
	@ApiModelProperty(value = "调出单位中文", required = false)
	private String tallyingUnitLabel;//调出单位 00-托盘 01-箱 02-件
	@ApiModelProperty(value = "调入数量", required = false)
	private Integer orderInTransferNum;//调入数量
	@ApiModelProperty(value = "调入单位", required = false)
	private String inTallyingUnit;//调入单位 00-托盘 01-箱 02-件
	@ApiModelProperty(value = "调入单位", required = false)
	private String inTallyingUnitLabel;//调入单位 00-托盘 01-箱 02-件
	@ApiModelProperty(value = "调拨出库单号", required = false)
	private String outDepotCode;//调拨出库单号
	@ApiModelProperty(value = "出库单调出商品货号", required = false)
	private String outGoodsNoItem;//出库单调出商品货号
	@ApiModelProperty(value = "出库单调出商品名称", required = false)
	private String outGoodsNameItem;//出库单调出商品名称
	@ApiModelProperty(value = "调拨出库批次号", required = false)
	private String outTransferBatchNo;//调拨出库批次号
	@ApiModelProperty(value = "调拨出库数量", required = false)
	private Integer outTransferNum;//调拨出库数量
	@ApiModelProperty(value = "调拨坏品数量", required = false)
	private Integer outWornNum;//调拨坏品数量
	@ApiModelProperty(value = "调拨过期数量", required = false)
	private Integer outExpireNum;//调拨过期数量
	@ApiModelProperty(value = "调拨出库数量", required = false)
	private Integer outTransferNumAll;//调拨出库数量
	@ApiModelProperty(value = "调拨入库单号", required = false)
	private String inDepotCode;//调拨入库单号
	@ApiModelProperty(value = "入库单调入商品货号", required = false)
	private String inGoodsNoItem;//入库单调入商品货号
	@ApiModelProperty(value = "入库单调入商品名称", required = false)
	private String inGoodsNameItem;//入库单调入商品名称
	@ApiModelProperty(value = "调拨入库批次号", required = false)
	private String inTransferBatchNo;//调拨入库批次号
	@ApiModelProperty(value = "调拨入库数量", required = false)
	private Integer inTransferNum;//调拨入库数量
	@ApiModelProperty(value = "坏品数量", required = false)
	private Integer wornNum;//坏品数量
	@ApiModelProperty(value = "过期数量", required = false)
	private Integer expireNum;//过期数量
	@ApiModelProperty(value = "调入数量", required = false)
	private Integer inTransferNumAll;//调入数量
	@ApiModelProperty(value = "商家id", required = false)
	private Long merchantId;//商家id
	@ApiModelProperty(value = "调出仓库id", required = false)
	private Long outDepotId;//调出仓库id
	@ApiModelProperty(value = "调入仓库id", required = false)
	private Long inDepotId;//调入仓库id
	@ApiModelProperty(value = "调拨订单时间", required = false)
	private String createDate;//调拨订单时间
	@ApiModelProperty(value = "调拨订单开始时间", required = false)
	private String createDateStart;//调拨订单开始时间
	@ApiModelProperty(value = "调拨订单结束时间", required = false)
	private String createDateEnd;//调拨订单结束时间
	@ApiModelProperty(value = "事业部名称", required = false)
	private String buName;//事业部名称
	@ApiModelProperty(value = "事业部id", required = false)
	private Long buId; //事业部id
	//登录用户关联的事业部集合
	@ApiModelProperty(hidden = true)
	private List<Long> userBuList;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public Long getInGoodsId() {
		return inGoodsId;
	}
	public void setInGoodsId(Long inGoodsId) {
		this.inGoodsId = inGoodsId;
	}
	public Long getOutGoodsId() {
		return outGoodsId;
	}
	public void setOutGoodsId(Long outGoodsId) {
		this.outGoodsId = outGoodsId;
	}
	public String getOutDepotName() {
		return outDepotName;
	}
	public void setOutDepotName(String outDepotName) {
		this.outDepotName = outDepotName;
	}
	public String getInDepotName() {
		return inDepotName;
	}
	public void setInDepotName(String inDepotName) {
		this.inDepotName = inDepotName;
	}
	public String getInGoodsNo() {
		return inGoodsNo;
	}
	public void setInGoodsNo(String inGoodsNo) {
		this.inGoodsNo = inGoodsNo;
	}
	public String getInGoodsName() {
		return inGoodsName;
	}
	public void setInGoodsName(String inGoodsName) {
		this.inGoodsName = inGoodsName;
	}
	public String getOutGoodsNo() {
		return outGoodsNo;
	}
	public void setOutGoodsNo(String outGoodsNo) {
		this.outGoodsNo = outGoodsNo;
	}
	public String getOutGoodsName() {
		return outGoodsName;
	}
	public void setOutGoodsName(String outGoodsName) {
		this.outGoodsName = outGoodsName;
	}
	public Integer getTransferNum() {
		return transferNum;
	}
	public void setTransferNum(Integer transferNum) {
		this.transferNum = transferNum;
	}
	
	public String getTallyingUnit() {
		return tallyingUnit;
	}
	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
		if (StringUtils.isNotBlank(tallyingUnit)) {
			this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
		}
	}
	public Integer getOrderInTransferNum() {
		return orderInTransferNum;
	}
	public void setOrderInTransferNum(Integer orderInTransferNum) {
		this.orderInTransferNum = orderInTransferNum;
	}
	public String getInTallyingUnit() {
		return inTallyingUnit;
	}
	public void setInTallyingUnit(String inTallyingUnit) {
		this.inTallyingUnit = inTallyingUnit;
		if (StringUtils.isNotBlank(inTallyingUnit)) {
			this.inTallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, inTallyingUnit);
		}
	}
	public String getOutDepotCode() {
		return outDepotCode;
	}
	public void setOutDepotCode(String outDepotCode) {
		this.outDepotCode = outDepotCode;
	}
	public String getOutGoodsNoItem() {
		return outGoodsNoItem;
	}
	public void setOutGoodsNoItem(String outGoodsNoItem) {
		this.outGoodsNoItem = outGoodsNoItem;
	}
	public String getOutGoodsNameItem() {
		return outGoodsNameItem;
	}
	public void setOutGoodsNameItem(String outGoodsNameItem) {
		this.outGoodsNameItem = outGoodsNameItem;
	}
	public String getOutTransferBatchNo() {
		return outTransferBatchNo;
	}
	public void setOutTransferBatchNo(String outTransferBatchNo) {
		this.outTransferBatchNo = outTransferBatchNo;
	}
	public Integer getOutTransferNum() {
		return outTransferNum;
	}
	public void setOutTransferNum(Integer outTransferNum) {
		this.outTransferNum = outTransferNum;
	}
	public String getInDepotCode() {
		return inDepotCode;
	}
	public void setInDepotCode(String inDepotCode) {
		this.inDepotCode = inDepotCode;
	}
	public String getInGoodsNoItem() {
		return inGoodsNoItem;
	}
	public void setInGoodsNoItem(String inGoodsNoItem) {
		this.inGoodsNoItem = inGoodsNoItem;
	}
	public String getInGoodsNameItem() {
		return inGoodsNameItem;
	}
	public void setInGoodsNameItem(String inGoodsNameItem) {
		this.inGoodsNameItem = inGoodsNameItem;
	}
	public String getInTransferBatchNo() {
		return inTransferBatchNo;
	}
	public void setInTransferBatchNo(String inTransferBatchNo) {
		this.inTransferBatchNo = inTransferBatchNo;
	}
	public Integer getInTransferNum() {
		return inTransferNum;
	}
	public void setInTransferNum(Integer inTransferNum) {
		this.inTransferNum = inTransferNum;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public Long getOutDepotId() {
		return outDepotId;
	}
	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}
	public Long getInDepotId() {
		return inDepotId;
	}
	public void setInDepotId(Long inDepotId) {
		this.inDepotId = inDepotId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
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
	public Integer getInTransferNumAll() {
		return inTransferNumAll;
	}
	public void setInTransferNumAll(Integer inTransferNumAll) {
		this.inTransferNumAll = inTransferNumAll;
	}

	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}

	public String getInTallyingUnitLabel() {
		return inTallyingUnitLabel;
	}

	public String getCreateDateStart() {
		return createDateStart;
	}

	public void setCreateDateStart(String createDateStart) {
		this.createDateStart = createDateStart;
	}

	public String getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	}

	public Integer getOutWornNum() {
		return outWornNum;
	}

	public void setOutWornNum(Integer outWornNum) {
		this.outWornNum = outWornNum;
	}

	public Integer getOutExpireNum() {
		return outExpireNum;
	}

	public void setOutExpireNum(Integer outExpireNum) {
		this.outExpireNum = outExpireNum;
	}

	public Integer getOutTransferNumAll() {
		return outTransferNumAll;
	}

	public void setOutTransferNumAll(Integer outTransferNumAll) {
		this.outTransferNumAll = outTransferNumAll;
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

	public List<Long> getUserBuList() {
		return userBuList;
	}

	public void setUserBuList(List<Long> userBuList) {
		this.userBuList = userBuList;
	}
}
