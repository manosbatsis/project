package com.topideal.order.webapi.common.dto;

import com.topideal.entity.dto.common.SdPurchaseConfigDTO;
import com.topideal.entity.vo.common.SdPurchaseConfigItemModel;
import com.topideal.entity.vo.common.SdTypeConfigModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class SdPurchaseConfigResponseDTO {
    @ApiModelProperty(value = "采购SD配置信息")
    SdPurchaseConfigDTO detail ;

    @ApiModelProperty(value = "表体信息")
    List<SdPurchaseConfigItemModel> itemList;

    @ApiModelProperty(value = "单比例集合信息")
    List<SdTypeConfigModel> singleList;

    @ApiModelProperty(value = "多比例集合信息")
    List<SdTypeConfigModel> multiList;

    public SdPurchaseConfigDTO getDetail() {
        return detail;
    }

    public void setDetail(SdPurchaseConfigDTO detail) {
        this.detail = detail;
    }

    public List<SdPurchaseConfigItemModel> getItemList() {
        return itemList;
    }

    public void setItemList(List<SdPurchaseConfigItemModel> itemList) {
        this.itemList = itemList;
    }

    public List<SdTypeConfigModel> getSingleList() {
        return singleList;
    }

    public void setSingleList(List<SdTypeConfigModel> singleList) {
        this.singleList = singleList;
    }

    public List<SdTypeConfigModel> getMultiList() {
        return multiList;
    }

    public void setMultiList(List<SdTypeConfigModel> multiList) {
        this.multiList = multiList;
    }
}
