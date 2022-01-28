package com.topideal.json.pushapi.epass.e08;

/**
 * 出库订单商品信息
 **/
public class OutStoreOrderItemJson {

    private Integer index; //序号
    private String good_id;//商品货号
    private Integer amount;//数量
    private Double unit_price;//单价
    private String batch_no;//仓库批次号
    private String batch_code;//客户批次号
    private String notes;//备注
    private Double gross_weight;  // 毛重
    private Double net_weight;  // 净重
    private String contracts_unit; // 单位

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getGood_id() {
        return good_id;
    }

    public void setGood_id(String good_id) {
        this.good_id = good_id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(Double unit_price) {
        this.unit_price = unit_price;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getBatch_code() {
        return batch_code;
    }

    public void setBatch_code(String batch_code) {
        this.batch_code = batch_code;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getGross_weight() {
        return gross_weight;
    }

    public void setGross_weight(Double gross_weight) {
        this.gross_weight = gross_weight;
    }

    public Double getNet_weight() {
        return net_weight;
    }

    public void setNet_weight(Double net_weight) {
        this.net_weight = net_weight;
    }

    public String getContracts_unit() {
        return contracts_unit;
    }

    public void setContracts_unit(String contracts_unit) {
        this.contracts_unit = contracts_unit;
    }
}
