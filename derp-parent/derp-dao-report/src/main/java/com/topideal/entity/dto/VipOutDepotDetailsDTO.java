package com.topideal.entity.dto;

import com.topideal.entity.vo.reporting.VipOutDepotDetailsModel;

import java.util.List;

public class VipOutDepotDetailsDTO extends VipOutDepotDetailsModel{

	/**
	 * 账单类型
	 */
	private String billType ;
	/**事业部id*/
	private Long buId;
	/**客户ID*/
	private Long customerId;

	private List<Long> buList;

	public VipOutDepotDetailsDTO() {}

	public VipOutDepotDetailsDTO(VipOutDepotDetailsModel model) {
		super() ;
		this.setCommbarcode(model.getCommbarcode());
		this.setCreateDate(model.getCreateDate());
		this.setDepotId(model.getDepotId());
		this.setDepotName(model.getDepotName());
		this.setGoodsId(model.getGoodsId());
		this.setGoodsName(model.getGoodsName());
		this.setGoodsNo(model.getGoodsNo());
		this.setId(model.getId());
		this.setList(model.getList());
		this.setMerchantId(model.getMerchantId());
		this.setMerchantName(model.getMerchantName());
		this.setModifyDate(model.getModifyDate());
		this.setOutDepotDate(model.getOutDepotDate());
		this.setOutDepotNum(model.getOutDepotNum());
		this.setPoNo(model.getPoNo());
		this.setSaleOrder(model.getSaleOrder());
		this.setSaleOutOrder(model.getSaleOutOrder());
		this.setVipBillCode(model.getVipBillCode());
		this.setPoDate(model.getPoDate());
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public List<Long> getBuList() {
		return buList;
	}

	public void setBuList(List<Long> buList) {
		this.buList = buList;
	}
}
