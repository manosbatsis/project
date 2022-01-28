package com.topideal.entity.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 类型调整详情表
 *
 * @author lian_
 */
@ApiModel
public class AdjustmentTypeItemDTO extends PageModel implements Serializable {

    @ApiModelProperty(value = "商品货号")
    private String goodsNo;
    
    @ApiModelProperty(value = "失效日期")
    private Date overdueDate;

    @ApiModelProperty(value = "调整前商品类型")
    private String oldGoodType;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "调整前失效日期")
    private Date oldOverdueDate;

    @ApiModelProperty(value = "原批次号")
    private String oldBatchNo;

    @ApiModelProperty(value = "调整前货号")
    private String oldGoodsNo;

    @ApiModelProperty(value = "调整前生产日期")
    private Date oldProductionDate;

    @ApiModelProperty(value = "生产日期")
    private Date productionDate;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "商品编码")
    private String goodsCode;

    @ApiModelProperty(value = "类型调整单id")
    private Long tAdjustmentTypeId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品条形码")
    private String barcode;

    @ApiModelProperty(value = "调整后商品类型")
    private String newGoodType;

    @ApiModelProperty(value = "总调整数量")
    private Integer adjustTotal;

    @ApiModelProperty(value = "原商品条形码")
    private String oldBarcode;

    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    //理货单位 00-托盘 01-箱 02-件
    @ApiModelProperty(value = "理货单位")
    private String tallyingUnit;
    @ApiModelProperty(value = "理货单位名称")
    private String tallyingUnitLabel;

    //调整类型 0 调减 1 调增
    @ApiModelProperty(value = "调整类型 0 调减 1 调增")
    private String type;
    @ApiModelProperty(value = "调整类型名称")
    private String typeLabel;
    @ApiModelProperty(value = "大货理货调整归属")
    private String typeSourceLabel; //大货理货调整归属

    //是否坏品 0 好品 1坏
    @ApiModelProperty(value = "是否坏品 0 好品 1坏")
    private String isDamage;
    @ApiModelProperty(value = "是否坏品（中文）")
    private String isDamageLabel;
    @ApiModelProperty(value = "是否过期")
    private String isExpire;
    @ApiModelProperty(value = "事业部名称")
    private String buName;

    @ApiModelProperty(value = "事业部id")
    private Long buId;

    @ApiModelProperty(value = "库位类型ID")
    private Long stockLocationTypeId;
    /**
     * 库位类型
     */
    @ApiModelProperty(value = "库位类型")
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
        if (StringUtils.isNotBlank(type)) {
            this.typeLabel = DERP_STORAGE.getLabelByKey(DERP_STORAGE.adjustment_typeList, type);
            this.typeSourceLabel = DERP_STORAGE.getLabelByKey(DERP_STORAGE.adjustmentType_typeDHLHList, type);
        }
    }

    public String getIsDamage() {
        return isDamage;
    }

    public void setIsDamage(String isDamage) {
        this.isDamage = isDamage;
        if (StringUtils.isNotBlank(isDamage)) {
            this.isDamageLabel = DERP.getLabelByKey(DERP.isDamageList, isDamage);
        }
    }

    public String getTallyingUnit() {
        return tallyingUnit;
    }

    public void setTallyingUnit(String tallyingUnit) {
        this.tallyingUnit = tallyingUnit;
        if (StringUtils.isNotBlank(tallyingUnit)) {
            this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
        }
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

    public String getTallyingUnitLabel() {
        return tallyingUnitLabel;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public String getIsDamageLabel() {
        return isDamageLabel;
    }

    public String getTypeSourceLabel() {
        return typeSourceLabel;
    }

    public String getIsExpire() {
        return isExpire;
    }

    public void setIsExpire(String isExpire) {
        this.isExpire = isExpire;
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

    public void setTallyingUnitLabel(String tallyingUnitLabel) {
        this.tallyingUnitLabel = tallyingUnitLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public void setTypeSourceLabel(String typeSourceLabel) {
        this.typeSourceLabel = typeSourceLabel;
    }

    public void setIsDamageLabel(String isDamageLabel) {
        this.isDamageLabel = isDamageLabel;
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

