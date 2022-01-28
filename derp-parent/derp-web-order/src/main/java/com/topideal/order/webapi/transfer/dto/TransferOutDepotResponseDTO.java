package com.topideal.order.webapi.transfer.dto;

import com.topideal.entity.dto.transfer.TransferInDepotItemDTO;
import com.topideal.entity.dto.transfer.TransferOrderDTO;
import com.topideal.entity.dto.transfer.TransferOutDepotDTO;
import com.topideal.entity.dto.transfer.TransferOutDepotItemDTO;
import com.topideal.mongo.entity.DepotInfoMongo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 调拨出库返回值 dto
 **/
@ApiModel
public class TransferOutDepotResponseDTO {

    @ApiModelProperty(value = "调拨出库单表头")
    TransferOutDepotDTO transferOutDepotDTO;
    @ApiModelProperty(value = "对应调拨订单")
    TransferOrderDTO transferOrderDTO;
    @ApiModelProperty(value = "出仓仓库")
    DepotInfoMongo outDepotModel;
    @ApiModelProperty(value = "调拨出库单表体")
    List<TransferOutDepotItemDTO> itemList;

    public TransferOutDepotDTO getTransferOutDepotDTO() {
        return transferOutDepotDTO;
    }

    public void setTransferOutDepotDTO(TransferOutDepotDTO transferOutDepotDTO) {
        this.transferOutDepotDTO = transferOutDepotDTO;
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

    public List<TransferOutDepotItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<TransferOutDepotItemDTO> itemList) {
        this.itemList = itemList;
    }
}
