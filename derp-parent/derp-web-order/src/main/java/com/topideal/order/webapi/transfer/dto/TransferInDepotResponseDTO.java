package com.topideal.order.webapi.transfer.dto;

import com.topideal.entity.dto.transfer.TransferInDepotDTO;
import com.topideal.entity.dto.transfer.TransferInDepotItemDTO;
import com.topideal.entity.dto.transfer.TransferOrderDTO;
import com.topideal.mongo.entity.DepotInfoMongo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 调拨入库返回值 dto
 **/
@ApiModel
public class TransferInDepotResponseDTO {

    @ApiModelProperty(value = "调拨入库单表头")
    TransferInDepotDTO transferInDepotDTO;
    @ApiModelProperty(value = "对应调拨订单")
    TransferOrderDTO transferOrderDTO;
    @ApiModelProperty(value = "出仓仓库")
    DepotInfoMongo outDepotModel;
    @ApiModelProperty(value = "调拨入库单表体")
    List<TransferInDepotItemDTO> itemList;

    public TransferInDepotDTO getTransferInDepotDTO() {
        return transferInDepotDTO;
    }

    public void setTransferInDepotDTO(TransferInDepotDTO transferInDepotDTO) {
        this.transferInDepotDTO = transferInDepotDTO;
    }

    public TransferOrderDTO getTransferOrderDTO() {
        return transferOrderDTO;
    }

    public void setTransferOrderDTO(TransferOrderDTO transferOrderDTO) {
        this.transferOrderDTO = transferOrderDTO;
    }

    public DepotInfoMongo getOutDepotModel() {
        return outDepotModel;
    }

    public void setOutDepotModel(DepotInfoMongo outDepotModel) {
        this.outDepotModel = outDepotModel;
    }

    public List<TransferInDepotItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<TransferInDepotItemDTO> itemList) {
        this.itemList = itemList;
    }
}
