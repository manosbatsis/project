package com.topideal.json.storage.j02;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 唯品红冲库存调整商品集合实体
 * @Author: Chen Yiluan
 * @Date: 2019/07/16 17:38
 **/
public class AdjustmentInventoryVipGoodsListJson implements Serializable {

    private String goodsId;// 商品ID

    private String goodsName;// 商品名称：账单上的商品名称

    private String goodsNo;// 商品货号：唯品账单对应到本系统的商品货号

    private String barcode;// 商品条码：唯品账单对应到本系统的商品条码

    private String type;// 调整类型 //0 调减 1 调增

    private int adjustTotal;// 总调整数量：唯品账单上单SKU的红冲总量

    private String commbarcode; //标准条码

    private BigDecimal settlementPrice; // 结算单价
    
    

    public BigDecimal getSettlementPrice() {
		return settlementPrice;
	}

	public void setSettlementPrice(BigDecimal settlementPrice) {
		this.settlementPrice = settlementPrice;
	}

	public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAdjustTotal() {
        return adjustTotal;
    }

    public void setAdjustTotal(int adjustTotal) {
        this.adjustTotal = adjustTotal;
    }

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }
}
