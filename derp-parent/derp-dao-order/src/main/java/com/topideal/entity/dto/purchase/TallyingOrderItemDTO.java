package com.topideal.entity.dto.purchase;

import com.topideal.entity.vo.purchase.TallyingOrderItemModel;

public class TallyingOrderItemDTO extends TallyingOrderItemModel{

	private String depotName ;
	
	private String code ;
	
	public TallyingOrderItemDTO() {
		super() ;
	}
	
	public TallyingOrderItemDTO(TallyingOrderItemModel item){
		super() ;
		this.setBarcode(item.getBarcode());
		this.setGoodsName(item.getGoodsName());
		this.setGoodsNo(item.getGoodsNo());
		this.setLackNum(item.getLackNum());
		this.setNormalNum(item.getNormalNum());
		this.setMultiNum(item.getMultiNum());
		this.setPurchaseNum(item.getPurchaseNum());
		this.setTallyingNum(item.getTallyingNum());
		this.setTallyingUnit(item.getTallyingUnit());
	}

	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
