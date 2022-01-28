package com.topideal.entity.dto.common;

import java.io.Serializable;

/**
 * 美赞臣分配表体
 **/
public class ZPTempItemDTO implements Serializable {

    private Long goodsId;//库位商品id
    private String goodsNo;//库位商品货号
    private Long originalGoodsId;//原货号id
    private String originalGoodsNo;//原货号
    private Integer normalNum;//正常品数量
    private Integer wornNum;//坏品数量
    private Integer expireNum;//过期数量
    private String batchNo;//批次号
    private String productionDate;//生产日期
    private String overdueDate;//失效日期
    private String tallyingUnit;//理货单位 00-托盘 01-箱 02-件
    private String poNo;//po号

    //订单表体Id(拆分报文用，非接口字段)
    private Long orderItemId;

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
    public Long getOriginalGoodsId() {
        return originalGoodsId;
    }
    public void setOriginalGoodsId(Long originalGoodsId) {
        this.originalGoodsId = originalGoodsId;
    }
    public String getOriginalGoodsNo() {
        return originalGoodsNo;
    }
    public void setOriginalGoodsNo(String originalGoodsNo) {
        this.originalGoodsNo = originalGoodsNo;
    }
    public Integer getNormalNum() {
        if(normalNum==null){
            return 0;
        }
        return normalNum;
    }
    public void setNormalNum(Integer normalNum) {
        this.normalNum = normalNum;
    }
    public Integer getWornNum() {
        if(wornNum==null){
            return 0;
        }
        return wornNum;
    }
    public void setWornNum(Integer wornNum) {
        this.wornNum = wornNum;
    }
    public Integer getExpireNum() {
        if(expireNum==null){
            return 0;
        }
        return expireNum;
    }
    public void setExpireNum(Integer expireNum) {
        this.expireNum = expireNum;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getProductionDate() {
        return productionDate;
    }
    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }
    public String getOverdueDate() {
        return overdueDate;
    }
    public void setOverdueDate(String overdueDate) {
        this.overdueDate = overdueDate;
    }
    public String getTallyingUnit() {
        return tallyingUnit;
    }
    public void setTallyingUnit(String tallyingUnit) {
        this.tallyingUnit = tallyingUnit;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }
}
