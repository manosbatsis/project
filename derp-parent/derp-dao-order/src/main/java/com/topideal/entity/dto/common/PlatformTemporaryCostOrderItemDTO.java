package com.topideal.entity.dto.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@ApiModel
public class PlatformTemporaryCostOrderItemDTO implements Serializable {

    /**
     * ID
     */
    @ApiModelProperty(value = "id",required = false)
    private Long id;
    /**
     * 暂估费用单ID
     */
    @ApiModelProperty(value = "暂估费用单ID",required = false)
    private Long platformCostId;
    /**
     * 电商订单号
     */
    @ApiModelProperty(value = "电商订单号",required = false)
    private String orderCode;
    /**
     * 外部交易单号
     */
    @ApiModelProperty(value = "外部交易单号",required = false)
    private String externalCode;

    /**
     * 事业部id
     */
    @ApiModelProperty(value = "事业部id",required = false)
    private Long buId;
    /**
     * 事业部名称
     */
    @ApiModelProperty(value = "事业部名称",required = false)
    private String buName;
    /**
     * 母品牌id
     */
    @ApiModelProperty(value = "母品牌id",required = false)
    private Long parentBrandId;
    /**
     * 母品牌编码
     */
    @ApiModelProperty(value = "母品牌编码",required = false)
    private String parentBrandCode;
    /**
     * 母品牌
     */
    @ApiModelProperty(value = "母品牌",required = false)
    private String parentBrandName;
    /**
     * 平台费项id
     */
    @ApiModelProperty(value = "平台费项id",required = false)
    private Long platformSettlementId;
    /**
     * 平台费项名称
     */
    @ApiModelProperty(value = "平台费项名称",required = false)
    private String platformSettlementName;
    /**
     * 结算金额
     */
    @ApiModelProperty(value = "结算金额",required = false)
    private BigDecimal amount;
    /**
     * 费项比例
     */
    @ApiModelProperty(value = "费项比例",required = false)
    private Double ratio;
    /**
     * 费项金额
     */
    @ApiModelProperty(value = "费项金额",required =false )
    private BigDecimal settlementAmount;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间",required = false)
    private Timestamp createDate;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间",required = false)
    private Timestamp modifyDate;

    /**
     * 标准品牌Id
     */
    @ApiModelProperty(value = "标准品牌Id",required = false)
    private Long brandParentId;
    /**
     * 标准品牌名称
     */
    @ApiModelProperty(value = "标准品牌名称",required = false)
    private String brandParentName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlatformCostId() {
        return platformCostId;
    }

    public void setPlatformCostId(Long platformCostId) {
        this.platformCostId = platformCostId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public Long getPlatformSettlementId() {
        return platformSettlementId;
    }

    public void setPlatformSettlementId(Long platformSettlementId) {
        this.platformSettlementId = platformSettlementId;
    }

    public String getPlatformSettlementName() {
        return platformSettlementName;
    }

    public void setPlatformSettlementName(String platformSettlementName) {
        this.platformSettlementName = platformSettlementName;
    }

    public Long getParentBrandId() {
        return parentBrandId;
    }

    public void setParentBrandId(Long parentBrandId) {
        this.parentBrandId = parentBrandId;
    }

    public String getParentBrandCode() {
        return parentBrandCode;
    }

    public void setParentBrandCode(String parentBrandCode) {
        this.parentBrandCode = parentBrandCode;
    }

    public String getParentBrandName() {
        return parentBrandName;
    }

    public void setParentBrandName(String parentBrandName) {
        this.parentBrandName = parentBrandName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public BigDecimal getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(BigDecimal settlementAmount) {
        this.settlementAmount = settlementAmount;
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

    public String getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public Long getBrandParentId() {
        return brandParentId;
    }

    public void setBrandParentId(Long brandParentId) {
        this.brandParentId = brandParentId;
    }

    public String getBrandParentName() {
        return brandParentName;
    }

    public void setBrandParentName(String brandParentName) {
        this.brandParentName = brandParentName;
    }
}
