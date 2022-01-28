package com.topideal.inventory.webapi.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
/**
 * 批次库存明细快照搜索参数
 */
@ApiModel
public class InventoryBatchSnapshotForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "令牌",required = true)
    private String token;

    @ApiModelProperty(value = "商家名称",required = false)
    private String merchantName;

    @ApiModelProperty(value = "仓库id",required = false)
    private Long depotId;

    @ApiModelProperty(value = "商品货号",required = false)
    private String goodsNo;

    @ApiModelProperty(value = "批次号",required = false)
    private String batchNo;

    @ApiModelProperty(value = "品牌id",required = false)
    private Long brandId;

    @ApiModelProperty(value = "标准条码",required = false)
    private String barcode;

    @ApiModelProperty(value = "快照日期",required = false)
    private String snapshotDate;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
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

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getSnapshotDate() {
        return snapshotDate;
    }

    public void setSnapshotDate(String snapshotDate) {
        this.snapshotDate = snapshotDate;
    }
}
