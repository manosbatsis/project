package com.topideal.entity.dto.purchase;

import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@ApiModel
public class PurchaseWriteOffItemDTO extends PageModel implements Serializable {

    @ApiModelProperty(value="商品id")
    private Long goodsId;

    @ApiModelProperty(value="商品货号")
    private String goodsNo;

    @ApiModelProperty(value="入库单id")
    private Long warehouseId;

    @ApiModelProperty(value="理货单位")
    private String tallyingUnit;

    @ApiModelProperty(value="批次号")
    private String batchNo;

    @ApiModelProperty(value="失效时间")
    private Date overdueDate;

    @ApiModelProperty(value="生产日期")
    private Date productionDate;

    @ApiModelProperty(value="数量")
    private Integer num;

    @ApiModelProperty(value="坏货数量", hidden = true)
    private Integer wornNum;

    @ApiModelProperty(value="过期数量", hidden = true)
    private Integer expireNum;

    @ApiModelProperty(value="正常数量", hidden = true)
    private Integer normalNum;

    @ApiModelProperty(value="是否过期（0 过期 1 未过期）")
    private String isExpire;

    @ApiModelProperty(value="好坏品 0 正常品  1 残次品")
    private String type;

    @ApiModelProperty(value = "仓库id")
    private Long depotId;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getTallyingUnit() {
        return tallyingUnit;
    }

    public void setTallyingUnit(String tallyingUnit) {
        this.tallyingUnit = tallyingUnit;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Date getOverdueDate() {
        return overdueDate;
    }

    public void setOverdueDate(Date overdueDate) {
        this.overdueDate = overdueDate;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public Integer getWornNum() {
        return wornNum;
    }

    public void setWornNum(Integer wornNum) {
        this.wornNum = wornNum;
    }

    public Integer getExpireNum() {
        return expireNum;
    }

    public void setExpireNum(Integer expireNum) {
        this.expireNum = expireNum;
    }

    public Integer getNormalNum() {
        return normalNum;
    }

    public void setNormalNum(Integer normalNum) {
        this.normalNum = normalNum;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getIsExpire() {
        return isExpire;
    }

    public void setIsExpire(String isExpire) {
        this.isExpire = isExpire;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
