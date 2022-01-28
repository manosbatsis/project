package com.topideal.order.webapi.transfer.dto;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.transfer.TransferOrderDTO;
import com.topideal.entity.dto.transfer.TransferOrderItemDTO;
import com.topideal.entity.vo.common.PackingDetailsModel;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.PackTypeMogo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 调拨订单返回值 dto
 */
@ApiModel
public class TransferOrderResponseDTO {

	@ApiModelProperty(value = "调拨订单信息")
	TransferOrderDTO transferOrder;

	@ApiModelProperty(value = "运单信息")
	List<TransferOrderItemDTO> transferOrderItemDTOList;

	@ApiModelProperty(value = "调出仓库信息")
	DepotInfoMongo outDepotInfo;

	@ApiModelProperty(value = "数据来源")
	String pageSource;

	@ApiModelProperty(value = "包装方式")
	List<PackTypeMogo> packTypeList;

	@ApiModelProperty(value = "当前用户")
	User user;

	@ApiModelProperty(value = "调入仓库进出接口指令")
	String inDepotIsInOutInstruction;

	@ApiModelProperty(value = "调出仓库进出接口指令")
	String outDepotIsInOutInstruction;

	@ApiModelProperty(value="装箱明细")
	private List<PackingDetailsModel> packingList ;

	public TransferOrderDTO getTransferOrder() {
		return transferOrder;
	}

	public void setTransferOrder(TransferOrderDTO transferOrder) {
		this.transferOrder = transferOrder;
	}

	public List<TransferOrderItemDTO> getTransferOrderItemDTOList() {
		return transferOrderItemDTOList;
	}

	public void setTransferOrderItemDTOList(List<TransferOrderItemDTO> transferOrderItemDTOList) {
		this.transferOrderItemDTOList = transferOrderItemDTOList;
	}

	public DepotInfoMongo getOutDepotInfo() {
		return outDepotInfo;
	}

	public void setOutDepotInfo(DepotInfoMongo outDepotInfo) {
		this.outDepotInfo = outDepotInfo;
	}

	public String getPageSource() {
		return pageSource;
	}

	public void setPageSource(String pageSource) {
		this.pageSource = pageSource;
	}

	public List<PackTypeMogo> getPackTypeList() {
		return packTypeList;
	}

	public void setPackTypeList(List<PackTypeMogo> packTypeList) {
		this.packTypeList = packTypeList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getInDepotIsInOutInstruction() {
		return inDepotIsInOutInstruction;
	}

	public void setInDepotIsInOutInstruction(String inDepotIsInOutInstruction) {
		this.inDepotIsInOutInstruction = inDepotIsInOutInstruction;
	}

	public String getOutDepotIsInOutInstruction() {
		return outDepotIsInOutInstruction;
	}

	public void setOutDepotIsInOutInstruction(String outDepotIsInOutInstruction) {
		this.outDepotIsInOutInstruction = outDepotIsInOutInstruction;
	}

	public List<PackingDetailsModel> getPackingList() {
		return packingList;
	}

	public void setPackingList(List<PackingDetailsModel> packingList) {
		this.packingList = packingList;
	}
}
