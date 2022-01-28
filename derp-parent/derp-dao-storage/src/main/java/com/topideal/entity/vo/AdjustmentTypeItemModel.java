package com.topideal.entity.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 类型调整详情表
 *
 * @author lian_
 */
public class AdjustmentTypeItemModel extends PageModel implements Serializable {

    //商品货号
    private String goodsNo;
    //失效日期
    private Date overdueDate;
    //调整前商品类型
    private String oldGoodType;
    //商品id
    private Long goodsId;
    //调整前失效日期
    private Date oldOverdueDate;
    //原批次号
    private String oldBatchNo;
    //调整前货号
    private String oldGoodsNo;
    //调整前生成日期
    private Date oldProductionDate;
    //生成日期
    private Date productionDate;
    //创建时间
    private Timestamp createDate;
    //id
    private Long id;
    //商品编码
    private String goodsCode;
    //类型调整id
    private Long tAdjustmentTypeId;
    //商品名称
    private String goodsName;
    //商品条形码
    private String barcode;
    //调整后商品类型
    private String newGoodType;
    //总调整数量
    private Integer adjustTotal;
    //原商品条形码
    private String oldBarcode;
    //理货单位 00-托盘 01-箱 02-件
    private String tallyingUnit;
    /**
     * 调整类型 0 调减 1 调增
     */
    private String type;
    /**
     * 是否坏品 0 好品 1坏
     */
    private String isDamage;
    /**
     * 修改时间
     */
    private Timestamp modifyDate;

    //事业部名称
    private String buName;

    /**
     * 事业部id
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

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsDamage() {
        return isDamage;
    }

    public void setIsDamage(String isDamage) {
        this.isDamage = isDamage;
    }

    public String getTallyingUnit() {
        return tallyingUnit;
    }

    public void setTallyingUnit(String tallyingUnit) {
        this.tallyingUnit = tallyingUnit;
    }

    public Long gettAdjustmentTypeId() {
        return tAdjustmentTypeId;
    }

    public void settAdjustmentTypeId(Long tAdjustmentTypeId) {
        this.tAdjustmentTypeId = tAdjustmentTypeId;
    }

    public String getOldBarcode() {
        return oldBarcode;
    }

    public void setOldBarcode(String oldBarcode) {
        this.oldBarcode = oldBarcode;
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

    /*oldGoodType get 方法 */
    public String getOldGoodType() {
        return oldGoodType;
    }

    /*oldGoodType set 方法 */
    public void setOldGoodType(String oldGoodType) {
        this.oldGoodType = oldGoodType;
    }

    /*goodsId get 方法 */
    public Long getGoodsId() {
        return goodsId;
    }

    /*goodsId set 方法 */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    /*oldOverdueDate get 方法 */
    public Date getOldOverdueDate() {
        return oldOverdueDate;
    }

    /*oldOverdueDate set 方法 */
    public void setOldOverdueDate(Date oldOverdueDate) {
        this.oldOverdueDate = oldOverdueDate;
    }

    /*oldBatchNo get 方法 */
    public String getOldBatchNo() {
        return oldBatchNo;
    }

    /*oldBatchNo set 方法 */
    public void setOldBatchNo(String oldBatchNo) {
        this.oldBatchNo = oldBatchNo;
    }

    /*oldGoodsNo get 方法 */
    public String getOldGoodsNo() {
        return oldGoodsNo;
    }

    /*oldGoodsNo set 方法 */
    public void setOldGoodsNo(String oldGoodsNo) {
        this.oldGoodsNo = oldGoodsNo;
    }

    /*oldProductionDate get 方法 */
    public Date getOldProductionDate() {
        return oldProductionDate;
    }

    /*oldProductionDate set 方法 */
    public void setOldProductionDate(Date oldProductionDate) {
        this.oldProductionDate = oldProductionDate;
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

    /*tAdjustmentTypeId get 方法 */
    public Long getTAdjustmentTypeId() {
        return tAdjustmentTypeId;
    }

    /*tAdjustmentTypeId set 方法 */
    public void setTAdjustmentTypeId(Long tAdjustmentTypeId) {
        this.tAdjustmentTypeId = tAdjustmentTypeId;
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

    /*newGoodType get 方法 */
    public String getNewGoodType() {
        return newGoodType;
    }

    /*newGoodType set 方法 */
    public void setNewGoodType(String newGoodType) {
        this.newGoodType = newGoodType;
    }

    /*adjustTotal get 方法 */
    public Integer getAdjustTotal() {
        return adjustTotal;
    }

    /*adjustTotal set 方法 */
    public void setAdjustTotal(Integer adjustTotal) {
        this.adjustTotal = adjustTotal;
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

