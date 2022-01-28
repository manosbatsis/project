package com.topideal.api.dstp.d01;

import java.math.BigDecimal;

public class GoodsItem {
    private String entGoodsNo;//货号
    private String recordPrice;//备案价格

    public String getEntGoodsNo() {
        return entGoodsNo;
    }

    public void setEntGoodsNo(String entGoodsNo) {
        this.entGoodsNo = entGoodsNo;
    }

    public String getRecordPrice() {
        return recordPrice;
    }

    public void setRecordPrice(String recordPrice) {
        this.recordPrice = recordPrice;
    }
}
