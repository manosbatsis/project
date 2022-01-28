package com.topideal.entity.dto.purchase;

import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

@ApiModel
public class PurchaseWriteOffDTO extends PageModel implements Serializable {

    @ApiModelProperty(value="采购订单id")
    private Long id;

    @ApiModelProperty(value="采购订单号")
    private String code;

    @ApiModelProperty(value="红冲原因")
    private String reason;

    @ApiModelProperty(value="采购单是否关联退货单：0-否，1-是")
    private String isRelPurchaseReturn;

    @ApiModelProperty(value="采购入库单明细")
    List<PurchaseWriteOffItemDTO> itemDTOS;


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

    public String getIsRelPurchaseReturn() {
        return isRelPurchaseReturn;
    }

    public void setIsRelPurchaseReturn(String isRelPurchaseReturn) {
        this.isRelPurchaseReturn = isRelPurchaseReturn;
    }

    public List<PurchaseWriteOffItemDTO> getItemDTOS() {
        return itemDTOS;
    }

    public void setItemDTOS(List<PurchaseWriteOffItemDTO> itemDTOS) {
        this.itemDTOS = itemDTOS;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
