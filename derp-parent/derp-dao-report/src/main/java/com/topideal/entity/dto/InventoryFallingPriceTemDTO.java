package com.topideal.entity.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 生成存货跌价基础字段取数临时实体
 * */
public class InventoryFallingPriceTemDTO {

    //状态-采购在途、在库库存、销售在途、调拨在途
    private String inverntoryStatus;
    //仓库id
    private Long depotId;
    //事业部id
    private Long buId;
    //商品id
    private Long goodsId;
    //批次号
    private String batchNo;
    //生产日期
    private Date productionDate;
    //失效日期
    private Date overdueDate;
    //首次上架日期
    private Date firstShelfDate;
    //库存类型  0 正常品  1 残次品
    private String inverntoryType;
    //总数量
    private Long surplusNum;
    //效期区间1:0＜X≤1/10 ; 2: 1/10＜X≤1/5 ; 3: 1/5＜X≤1/3 ; 4: 1/3＜X≤1/2 ; 5: 1/2＜X≤2/3 ; 6: 2/3以上 ; 7:过期品(为负) ; 8: 残次品',
    private String effectiveInterval;
    //理货单位 00 托盘 01箱  02 件
    private String tallyingUnit;
    // 币种（采购在途用）
    private String currency;
    //单价（采购在途用）
    private BigDecimal price;
    //在途单号
    private String noshelfCode;

    public String getInverntoryStatus() {
        return inverntoryStatus;
    }

    public void setInverntoryStatus(String inverntoryStatus) {
        this.inverntoryStatus = inverntoryStatus;
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public Date getOverdueDate() {
        return overdueDate;
    }

    public void setOverdueDate(Date overdueDate) {
        this.overdueDate = overdueDate;
    }

    public Date getFirstShelfDate() {
        return firstShelfDate;
    }

    public void setFirstShelfDate(Date firstShelfDate) {
        this.firstShelfDate = firstShelfDate;
    }

    public String getInverntoryType() {
        return inverntoryType;
    }

    public void setInverntoryType(String inverntoryType) {
        this.inverntoryType = inverntoryType;
    }

    public Long getSurplusNum() {
        return surplusNum;
    }

    public void setSurplusNum(Long surplusNum) {
        this.surplusNum = surplusNum;
    }

    public String getEffectiveInterval() {
        return effectiveInterval;
    }

    public void setEffectiveInterval(String effectiveInterval) {
        this.effectiveInterval = effectiveInterval;
    }

    public String getTallyingUnit() {
        return tallyingUnit;
    }

    public void setTallyingUnit(String tallyingUnit) {
        this.tallyingUnit = tallyingUnit;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getNoshelfCode() {
        return noshelfCode;
    }

    public void setNoshelfCode(String noshelfCode) {
        this.noshelfCode = noshelfCode;
    }
}
