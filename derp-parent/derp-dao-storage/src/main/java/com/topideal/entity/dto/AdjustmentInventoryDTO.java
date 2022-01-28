package com.topideal.entity.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 库存调整Dto
 * @Date: 2019/12/30 09:56
 **/
@ApiModel
public class AdjustmentInventoryDTO extends PageModel implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "盘点仓库名称") 
    private String depotName;
    @ApiModelProperty(value = "创建人id")  
    private Long createUserId;
    @ApiModelProperty(value = "库存调整单号")  
    private String code;
    @ApiModelProperty(value = "盘点仓库id")  
    private Long depotId;
    @ApiModelProperty(value = "商家名称")  
    private String merchantName;
    @ApiModelProperty(value = "来源单据号")  
    private String sourceCode;
    @ApiModelProperty(value = "创建人名称")  
    private String createUsername;
    @ApiModelProperty(value = "商家id")  
    private Long merchantId;
    @ApiModelProperty(value = "创建时间") 
    private Timestamp createDate;
    @ApiModelProperty(value = "修改时间")  
    private Timestamp modifyDate;
    @ApiModelProperty(value = "业务类别")  
    private String model;
    @ApiModelProperty(value = "业务类别（中文）")  
    private String modelLabel;
    @ApiModelProperty(value = "调整时间")  
    private Timestamp adjustmentTime;
    @ApiModelProperty(value = "状态")  
    private String status;
    @ApiModelProperty(value = "状态（中文）")
    private String statusLabel;

    @ApiModelProperty(value = "月份")  
    private String months;
    @ApiModelProperty(value = "单据所属日期") 
    private Timestamp sourceTime;
    @ApiModelProperty(value = "备注") 
    private String remark;

    @ApiModelProperty(value = "po号") 
    private String poNo;

    @ApiModelProperty(value = "单据来源（01:接口回传 02:手工录入）") 
    private String source;
    @ApiModelProperty(value = "单据来源（中文）") 
    private String sourceLabel;

    @ApiModelProperty(value = "币种") 
    private String currency;
    @ApiModelProperty(value = "币种（中文）")
    private String currencyLabel;

    @ApiModelProperty(value = "商品名称") 
    private String goodsName;

    @ApiModelProperty(value = "表体")  
    private List<AdjustmentInventoryItemDTO> itemList;

    @ApiModelProperty(value = "单据所属开始日期",hidden = true) 
    private String sourceStartTime;

    @ApiModelProperty(value = "单据所属结束日期",hidden = true) 
    private String sourceEndTime;

    @ApiModelProperty(value = "确认人id")
    private Long confirmUserId;
    
    @ApiModelProperty(value = "确认人名称")
    private String confirmUsername;
  
    @ApiModelProperty(value = "确认时间")
    private Timestamp confirmTime;

    @ApiModelProperty(value = "单据id集合，多个用逗号隔开")
    private String ids;

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getCreateUsername() {
        return createUsername;
    }

    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
        if (StringUtils.isNotBlank(model)) {
            this.modelLabel = DERP_STORAGE.getLabelByKey(DERP_STORAGE.adjustmentInventory_modelList, model);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getAdjustmentTime() {
        return adjustmentTime;
    }

    public void setAdjustmentTime(Timestamp adjustmentTime) {
        this.adjustmentTime = adjustmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        if (StringUtils.isNotBlank(status)) {
            this.statusLabel = DERP_STORAGE.getLabelByKey(DERP_STORAGE.adjustmentInventory_statusList, status);
        }
    }

    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    public Timestamp getSourceTime() {
        return sourceTime;
    }

    public void setSourceTime(Timestamp sourceTime) {
        this.sourceTime = sourceTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public List<AdjustmentInventoryItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<AdjustmentInventoryItemDTO> itemList) {
        this.itemList = itemList;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
        if (StringUtils.isNotBlank(source)) {
            this.sourceLabel = DERP_STORAGE.getLabelByKey(DERP_STORAGE.adjustmentInventory_sourceList, source);
        }
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
        if (StringUtils.isNotBlank(currency)) {
            this.currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency);
        }
    }

    public String getModelLabel() {
        return modelLabel;
    }

    public void setModelLabel(String modelLabel) {
        this.modelLabel = modelLabel;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getSourceLabel() {
        return sourceLabel;
    }

    public void setSourceLabel(String sourceLabel) {
        this.sourceLabel = sourceLabel;
    }

    public String getCurrencyLabel() {
        return currencyLabel;
    }

    public void setCurrencyLabel(String currencyLabel) {
        this.currencyLabel = currencyLabel;
    }

    public String getSourceStartTime() {
        return sourceStartTime;
    }

    public void setSourceStartTime(String sourceStartTime) {
        this.sourceStartTime = sourceStartTime;
    }

    public String getSourceEndTime() {
        return sourceEndTime;
    }

    public void setSourceEndTime(String sourceEndTime) {
        this.sourceEndTime = sourceEndTime;
    }

    public Long getConfirmUserId() {
        return confirmUserId;
    }

    public void setConfirmUserId(Long confirmUserId) {
        this.confirmUserId = confirmUserId;
    }

    public String getConfirmUsername() {
        return confirmUsername;
    }

    public void setConfirmUsername(String confirmUsername) {
        this.confirmUsername = confirmUsername;
    }

    public Timestamp getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Timestamp confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
