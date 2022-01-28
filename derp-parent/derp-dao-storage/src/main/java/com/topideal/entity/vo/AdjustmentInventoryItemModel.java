package com.topideal.entity.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 库存调整详情表
 *
 * @author lian_
 */
public class AdjustmentInventoryItemModel extends PageModel implements Serializable {

    //商品货号
    private String goodsNo;
    //失效日期
    private Date overdueDate;
    //是否坏品
    private String isDamage;
    //商品id
    private Long goodsId;
    //原始批次号
    private String oldBatchNo;
    //调整类型
    private String type;
    //库存调整id
    private Long tAdjustmentInventoryId;
    //生成日期
    private Date productionDate;
    //创建时间
    private Timestamp createDate;
    private Timestamp modifyDate;
    //id
    private Long id;
    //商品编码
    private String goodsCode;
    //商品名称
    private String goodsName;
    //商品条形码
    private String barcode;
    //总调整数量
    private Integer adjustTotal;
    //理货单位(00-托盘，01-箱，02-件)
    private String tallyingUnit;

    // 扩展字段
//     private String isExpire;//是否失效 用于  库存调整单审核时商品是否过期

    //po号
    private String poNo;

    //标准条码
    private String commbarcode;

    //po单时间
    private Timestamp poDate;

    //结算单价
    private BigDecimal settlementPrice;

    //事业部名称
    private String buName;

    /**
     *  事业部id
     */
    private Long buId;

    /**
     * 事业部库位类型ID
     */
    private Long stockLocationTypeId;
    /**
     * 库位类型
     */
    private String stockLocationTypeName;


    public String getTallyingUnit() {
        return tallyingUnit;
    }

    public void setTallyingUnit(String tallyingUnit) {
        this.tallyingUnit = tallyingUnit;
    }

    /*goodsNo get 方法 */
    public String getGoodsNo() {
        return goodsNo;
    }

    /*goodsNo set 方法 */
    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    /*overdueDate get 方法 */
    public Date getOverdueDate() {
        return overdueDate;
    }

    /*overdueDate set 方法 */
    public void setOverdueDate(Date overdueDate) {
        this.overdueDate = overdueDate;
    }

    /*isDamage get 方法 */
    public String getIsDamage() {
        return isDamage;
    }

    /*isDamage set 方法 */
    public void setIsDamage(String isDamage) {
        this.isDamage = isDamage;
    }

    /*goodsId get 方法 */
    public Long getGoodsId() {
        return goodsId;
    }

    /*goodsId set 方法 */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    /*oldBatchNo get 方法 */
    public String getOldBatchNo() {
        return oldBatchNo;
    }

    /*oldBatchNo set 方法 */
    public void setOldBatchNo(String oldBatchNo) {
        this.oldBatchNo = oldBatchNo;
    }

    /*type get 方法 */
    public String getType() {
        return type;
    }

    /*type set 方法 */
    public void setType(String type) {
        this.type = type;
    }

    /*tAdjustmentInventoryId get 方法 */
    public Long getTAdjustmentInventoryId() {
        return tAdjustmentInventoryId;
    }

    /*tAdjustmentInventoryId set 方法 */
    public void setTAdjustmentInventoryId(Long tAdjustmentInventoryId) {
        this.tAdjustmentInventoryId = tAdjustmentInventoryId;
    }

    /*productionDate get 方法 */
    public Date getProductionDate() {
        return productionDate;
    }

    /*productionDate set 方法 */
    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    /*id get 方法 */
    public Long getId() {
        return id;
    }

    /*id set 方法 */
    public void setId(Long id) {
        this.id = id;
    }

    /*goodsCode get 方法 */
    public String getGoodsCode() {
        return goodsCode;
    }

    /*goodsCode set 方法 */
    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    /*goodsName get 方法 */
    public String getGoodsName() {
        return goodsName;
    }

    /*goodsName set 方法 */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    /*barcode get 方法 */
    public String getBarcode() {
        return barcode;
    }

    /*barcode set 方法 */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /*adjustTotal get 方法 */
    public Integer getAdjustTotal() {
        return adjustTotal;
    }

    /*adjustTotal set 方法 */
    public void setAdjustTotal(Integer adjustTotal) {
        this.adjustTotal = adjustTotal;
    }

    public Long gettAdjustmentInventoryId() {
        return tAdjustmentInventoryId;
    }

    public void settAdjustmentInventoryId(Long tAdjustmentInventoryId) {
        this.tAdjustmentInventoryId = tAdjustmentInventoryId;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    public Timestamp getPoDate() {
        return poDate;
    }

    public void setPoDate(Timestamp poDate) {
        this.poDate = poDate;
    }

    public BigDecimal getSettlementPrice() {
        return settlementPrice;
    }

    public void setSettlementPrice(BigDecimal settlementPrice) {
        this.settlementPrice = settlementPrice;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public Long getStockLocationTypeId() {
        return stockLocationTypeId;
    }

    public void setStockLocationTypeId(Long stockLocationTypeId) {
        this.stockLocationTypeId = stockLocationTypeId;
    }

    public String getStockLocationTypeName() {
        return stockLocationTypeName;
    }

    public void setStockLocationTypeName(String stockLocationTypeName) {
        this.stockLocationTypeName = stockLocationTypeName;
    }
}

