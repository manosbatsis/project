package com.topideal.entity.dto.common;

import java.io.Serializable;

/**
 * 美赞臣单据表体商品&库位映射类型（常规品、赠品、样品）
 **/
public class ZPOrderItemDTO implements Serializable {

    private Long goodsId;//库位商品id
    private String goodsNo;//库位商品货号
    private Long originalGoodsId;//原商品id
    private String originalGoodsNo;//原货号
    private String type;//库位类型：1、常规品 2、赠送品 3、sample
    private Integer normalNum;//正常品数量 默认0
    private Integer wornNum;//坏品数量 默认0
    private String poNo;//po号

    //订单表体Id(采购单多价格用，非接口字段)
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
    public String getOriginalGoodsNo() {
        return originalGoodsNo;
    }
    public void setOriginalGoodsNo(String originalGoodsNo) {
        this.originalGoodsNo = originalGoodsNo;
    }
    public Long getOriginalGoodsId() {
        return originalGoodsId;
    }
    public void setOriginalGoodsId(Long originalGoodsId) {
        this.originalGoodsId = originalGoodsId;
    }
    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
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
