package com.topideal.inventory.webapi.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 实时库存快照表
 * @author lian_
 */
@ApiModel
public class InventoryRealTimeSnapshotForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "令牌",required = true)
    private String token;
     //商品货号
    @ApiModelProperty(value = "商品货号")
     private String goodsNo;
     //批次号
     @ApiModelProperty(value = "批次号")
     private String batchNo;
     //仓库id
     @ApiModelProperty(value = "仓库id")
     private Long depotId;
     //创建时间
     @ApiModelProperty(value = "创建时间")
     private String createDateStr;
     //条形码
     @ApiModelProperty(value = "条形码")
     private String barcode;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}

