package com.topideal.json.inventory.j05;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品收发明细json
 **/
public class InventoryDetailJson  implements Serializable {

    //批次号
    private String batchNo;
    //数量
    private Integer num;
    //商品货号
    private String goodsNo;
    //商品ID
    private Long goodsId;
    //商品名称
    private String goodsName;
    //单位（ 0 托盘 1箱  2件）
    private String unit;
    //失效日期
    private Date overdueDate;
    //生产日期
    private Date productionDate;
    //条形码
    private String barcode;
    //标准条码
    private String commbarcode;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }
}
