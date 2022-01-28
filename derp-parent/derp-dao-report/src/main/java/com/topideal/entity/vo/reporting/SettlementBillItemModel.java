package com.topideal.entity.vo.reporting;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class SettlementBillItemModel extends PageModel implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * 结算单id
     */
    private Long billId;
    /**
     * 结算单号
     */
    private String billCode;
    /**
     * 数据类型：1-仓内费用 2-快递费 3-取消订单服务费 4-综合税金
     */
    private String dataType;
    /**
     * 订单号
     */
    private String orderCode;
    /**
     * 费用
     */
    private BigDecimal cost;
    /**
     * 超件附加费(仓内费用)
     */
    private BigDecimal attachCost;
    /**
     * 包材费(仓内费用)
     */
    private BigDecimal packagingCost;
    /**
     * 货号
     */
    private String goodsNo;
    /**
     * 是否红冲 1-是 0-否
     */
    private String hcStatus;
    /**
     * 创建时间
     */
    private Timestamp createDate;
    /**
     * 修改时间
     */
    private Timestamp modifyDate;

    /**
     * 超重附加费
     */
    private BigDecimal overweightCost;
    /**
     * 一线进口清关费
     */
    private BigDecimal clearanceCost;
    /**
     * 入库验收费
     */
    private BigDecimal acceptanceCost;
    /**
     * 仓库自编码
     */
    private String depotCode;
    /**
     * 数量
     */
    private String stockNum;
    /**
     * 计费日期
     */
    private String billingDate;

    /*id get 方法 */
    public Long getId() {
        return id;
    }

    /*id set 方法 */
    public void setId(Long id) {
        this.id = id;
    }

    /*billId get 方法 */
    public Long getBillId() {
        return billId;
    }

    /*billId set 方法 */
    public void setBillId(Long billId) {
        this.billId = billId;
    }

    /*billCode get 方法 */
    public String getBillCode() {
        return billCode;
    }

    /*billCode set 方法 */
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    /*dataType get 方法 */
    public String getDataType() {
        return dataType;
    }

    /*dataType set 方法 */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /*orderCode get 方法 */
    public String getOrderCode() {
        return orderCode;
    }

    /*orderCode set 方法 */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    /*cost get 方法 */
    public BigDecimal getCost() {
        return cost;
    }

    /*cost set 方法 */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /*attachCost get 方法 */
    public BigDecimal getAttachCost() {
        return attachCost;
    }

    /*attachCost set 方法 */
    public void setAttachCost(BigDecimal attachCost) {
        this.attachCost = attachCost;
    }

    /*packagingCost get 方法 */
    public BigDecimal getPackagingCost() {
        return packagingCost;
    }

    /*packagingCost set 方法 */
    public void setPackagingCost(BigDecimal packagingCost) {
        this.packagingCost = packagingCost;
    }

    /*goodsNo get 方法 */
    public String getGoodsNo() {
        return goodsNo;
    }

    /*goodsNo set 方法 */
    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    /*hcStatus get 方法 */
    public String getHcStatus() {
        return hcStatus;
    }

    /*hcStatus set 方法 */
    public void setHcStatus(String hcStatus) {
        this.hcStatus = hcStatus;
    }

    /*createDate get 方法 */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /*createDate set 方法 */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    /*modifyDate get 方法 */
    public Timestamp getModifyDate() {
        return modifyDate;
    }

    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public BigDecimal getOverweightCost() {
        return overweightCost;
    }

    public void setOverweightCost(BigDecimal overweightCost) {
        this.overweightCost = overweightCost;
    }

    public BigDecimal getClearanceCost() {
        return clearanceCost;
    }

    public void setClearanceCost(BigDecimal clearanceCost) {
        this.clearanceCost = clearanceCost;
    }

    public BigDecimal getAcceptanceCost() {
        return acceptanceCost;
    }

    public void setAcceptanceCost(BigDecimal acceptanceCost) {
        this.acceptanceCost = acceptanceCost;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public void setDepotCode(String depotCode) {
        this.depotCode = depotCode;
    }

    public String getStockNum() {
        return stockNum;
    }

    public void setStockNum(String stockNum) {
        this.stockNum = stockNum;
    }

    public String getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(String billingDate) {
        this.billingDate = billingDate;
    }
}
