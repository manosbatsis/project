package com.topideal.inventory.webapi.form;

import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 商品收发明细搜索参数
 */
@ApiModel
public class InventoryDetailsForm extends PageForm implements Serializable {
    
    @ApiModelProperty(value = "令牌",required = true)
    private String token;

    @ApiModelProperty(value = "商家名称",required = false)
    private String merchantName;

    @ApiModelProperty(value = "仓库id",required = false)
    private Long depotId;

    @ApiModelProperty(value = "事业部id",required = false)
    private Long buId;

    @ApiModelProperty(value = "商品货号",required = false)
    private String goodsNo;

    @ApiModelProperty(value = "开始时间",required = false)
    private String startTime;

    @ApiModelProperty(value = "结算时间",required = false)
    private String endTime;

    @ApiModelProperty(value = "批次号",required = false)
    private String batchNo;

    @ApiModelProperty(value = "条形码",required = false)
    private String barcode;

    @ApiModelProperty(value = "属性说明",required = false)
    private String orderNo;

    @ApiModelProperty(value = "业务单号",required = false)
    private String businessNo;

    @ApiModelProperty(value = "单据状态",required = false)
    private String thingStatus;

    @ApiModelProperty(value = "标准条码",required = false)
    private String commbarcode;

    @ApiModelProperty(value = "时间段 1-近12个月数据 2-12个月以前数据",required = false)
    private String orderTimeRange;

    @ApiModelProperty(value = "操作类型  0 调减 1调增据",required = false)
    private String operateType;

    private String operateTypeLabel;

    @ApiModelProperty(value = "id, 多个以英文逗号隔开",required = false)
    private String ids;


    public String getToken() {
        return token;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public Long getDepotId() {
        return depotId;
    }

    public Long getBuId() {
        return buId;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public String getThingStatus() {
        return thingStatus;
    }

    public String getCommbarcode() {
        return commbarcode;
    }

    public String getOrderTimeRange() {
        return orderTimeRange;
    }

    public String getOperateType() {
        return operateType;
    }

    public String getOperateTypeLabel() {
        return operateTypeLabel;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public void setThingStatus(String thingStatus) {
        this.thingStatus = thingStatus;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    public void setOrderTimeRange(String orderTimeRange) {
        this.orderTimeRange = orderTimeRange;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
        this.operateTypeLabel= DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.inventory_operateTypeList, operateType);
    }

    public void setOperateTypeLabel(String operateTypeLabel) {
        this.operateTypeLabel = operateTypeLabel;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
