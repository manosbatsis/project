package com.topideal.entity.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 盘点结果详情表
 */
@ApiModel
public class TakesStockResultItemDTO extends PageModel implements Serializable {

    @ApiModelProperty(value = "商品货号")
    private String goodsNo;
    @ApiModelProperty(value = "失效日期")
    private Date overdueDate;
    @ApiModelProperty(value = "批次号")
    private String batchNo;
    @ApiModelProperty(value = "是否坏品")
    private String isDamage;
    @ApiModelProperty(value = "是否坏品（中文）")
    private String isDamageLabel;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    @ApiModelProperty(value = "盘盈数量")
    private Integer surplusNum;
    @ApiModelProperty(value = "调整类型")
    private String type;
    @ApiModelProperty(value = "调整类型（中文）")
    private String typeLabel;

    @ApiModelProperty(value = "生成日期")
    private Date productionDate;
    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    @ApiModelProperty(value = "盘亏数量")
    private Integer deficientNum;
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "商品编码")
    private String goodsCode;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品条形码")
    private String barcode;
    @ApiModelProperty(value = "盘点结果id")
    private Long takesStockResultId;
    @ApiModelProperty(value = "总调整数量")
    private Integer adjustTotal;
    @ApiModelProperty(value = "系统数量")
    private Integer systemNum;
    @ApiModelProperty(value = "差异数量")
    private Integer differencesNum;
    @ApiModelProperty(value = "实盘数量")
    private Integer realQty;
    @ApiModelProperty(value = "原因描述")
    private String reasonDes;
    @ApiModelProperty(value = "盘点原因代码")
    private String reasonCode;
    @ApiModelProperty(value = "盘点原因中文")
    private String reasonCodeLabel;

    @ApiModelProperty(value = "理货单位")
    private String tallyUnit;
    @ApiModelProperty(value = "理货单位（中文）")
    private String tallyUnitLabel;

    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    @ApiModelProperty(value = "标准条码")
    private String commbarcode;

    @ApiModelProperty(value = "结算单价")
    private BigDecimal settlementPrice;
    @ApiModelProperty(value = "事业部名称")
    private String buName;

    /**
     *  事业部id
     */
    @ApiModelProperty(value = "事业部id")
    private Long buId;

    @ApiModelProperty(value = "事业部库位类型ID")
    private Long stockLocationTypeId;

    @ApiModelProperty(value = "库位类型")
    private String stockLocationTypeName;

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getTallyUnit() {
        return tallyUnit;
    }

    public void setTallyUnit(String tallyUnit) {
        this.tallyUnit = tallyUnit;
        if (StringUtils.isNotBlank(tallyUnit)) {
            this.tallyUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyUnit);
        }
    }

    public String getReasonDes() {
        return reasonDes;
    }

    public void setReasonDes(String reasonDes) {
        this.reasonDes = reasonDes;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
        if (StringUtils.isNotBlank(reasonCode)) {
            this.reasonCodeLabel = DERP_STORAGE.getLabelByKey(DERP_STORAGE.takesStockResultItem_reasonCodeList, reasonCode);
        }
    }

    /*differencesNum get 方法 */
    public Integer getDifferencesNum() {
        return differencesNum;
    }

    /*differencesNum set 方法 */
    public void setDifferencesNum(Integer differencesNum) {
        this.differencesNum = differencesNum;
    }

    /*systemNum get 方法 */
    public Integer getSystemNum() {
        return systemNum;
    }

    /*systemNum set 方法 */
    public void setSystemNum(Integer systemNum) {
        this.systemNum = systemNum;
    }

    /*realQty get 方法 */
    public Integer getRealQty() {
        return realQty;
    }

    /*realQty set 方法 */
    public void setRealQty(Integer realQty) {
        this.realQty = realQty;
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

    /*batchNo get 方法 */
    public String getBatchNo() {
        return batchNo;
    }

    /*batchNo set 方法 */
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    /*isDamage get 方法 */
    public String getIsDamage() {
        return isDamage;
    }

    /*isDamage set 方法 */
    public void setIsDamage(String isDamage) {
        this.isDamage = isDamage;
        this.isDamageLabel = DERP.getLabelByKey(DERP_STORAGE.takesStockResult_isDamageList, isDamage);
    }

    /*goodsId get 方法 */
    public Long getGoodsId() {
        return goodsId;
    }

    /*goodsId set 方法 */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    /*surplusNum get 方法 */
    public Integer getSurplusNum() {
        return surplusNum;
    }

    /*surplusNum set 方法 */
    public void setSurplusNum(Integer surplusNum) {
        this.surplusNum = surplusNum;
    }

    /*type get 方法 */
    public String getType() {
        return type;
    }

    /*type set 方法 */
    public void setType(String type) {
        this.type = type;
        if (StringUtils.isNotBlank(type)) {
            this.typeLabel = DERP_STORAGE.getLabelByKey(DERP_STORAGE.takesStockResultItem_typeList, type);
        }
    }

    /*productionDate get 方法 */
    public Date getProductionDate() {
        return productionDate;
    }

    /*productionDate set 方法 */
    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    /*createTime get 方法 */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /*createTime set 方法 */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    /*deficientNum get 方法 */
    public Integer getDeficientNum() {
        return deficientNum;
    }

    /*deficientNum set 方法 */
    public void setDeficientNum(Integer deficientNum) {
        this.deficientNum = deficientNum;
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

    /*takesStockResultId get 方法 */
    public Long getTakesStockResultId() {
        return takesStockResultId;
    }

    /*takesStockResultId set 方法 */
    public void setTakesStockResultId(Long takesStockResultId) {
        this.takesStockResultId = takesStockResultId;
    }

    /*adjustTotal get 方法 */
    public Integer getAdjustTotal() {
        return adjustTotal;
    }

    /*adjustTotal set 方法 */
    public void setAdjustTotal(Integer adjustTotal) {
        this.adjustTotal = adjustTotal;
    }

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    public BigDecimal getSettlementPrice() {
        return settlementPrice;
    }

    public void setSettlementPrice(BigDecimal settlementPrice) {
        this.settlementPrice = settlementPrice;
    }

    public String getIsDamageLabel() {
        return isDamageLabel;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public String getReasonCodeLabel() {
        return reasonCodeLabel;
    }

    public String getTallyUnitLabel() {
        return tallyUnitLabel;
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

    public void setIsDamageLabel(String isDamageLabel) {
        this.isDamageLabel = isDamageLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public void setReasonCodeLabel(String reasonCodeLabel) {
        this.reasonCodeLabel = reasonCodeLabel;
    }

    public void setTallyUnitLabel(String tallyUnitLabel) {
        this.tallyUnitLabel = tallyUnitLabel;
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

