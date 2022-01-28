package com.topideal.entity.dto.sale;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class BuMoveInventoryExportDTO{
    /**
    * 移库单号
    */
    private String code;
    /**
    * 来源业务单号
    */
    private String businessNo;

    /**
    * 移库状态 017-待移库,018-已移库,027-移库中
    */
    private String status;
    private String statusLabel;

    /**
    * 出库仓库名称
    */
    private String depotName;
    /**
    * 移入事业部名称
    */
    private String inBuName;
    /**
    * 移出事业部名称
    */
    private String outBuName;
    /**
    * 移库日期
    */
    private Timestamp moveDate;
    /**
    * 创建人名称
    */
    private String createName;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
     * 商品货号
     */
    private String goodsNo;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 条形码
     */
    private String barcode;
    /**
     * 销售数量
     */
    private Integer num;
    /**
     * 销售单价
     */
    private BigDecimal price;
    
    /**
     * 来源单据类别 DSDD:电商订单、XSDD：销售订单  KCYKD:库存移库单
     */
    private String orderType;
    /**
     * 来源单据类别 DSDD:电商订单、XSDD：销售订单  KCYKD:库存移库单
     */
    private String orderTypeLabel;
    /**
     * 理货单位 00-托盘 01-箱 02-件
     */
    private String tallyingUnit;
    /**
     * 理货单位 00-托盘 01-箱 02-件
     */
    private String tallyingUnitLabel;
    
    
    /**
     * 移库币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
     */
    private String currency;
    /**
     * 移库币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
     */
    private String currencyLabel;
    /**
     * 审核人
     */
    private String auditName;
    /**
     * 审核时间
     */
    private String auditDate;
    
    /**
     * 协议单据
     */
    private String agreementPrice;
    /**
     * 库存类型
     */
    private String type;
    /**
     * 库存类型
     */
    private String typeLabel;
    
    
    




    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        if(status!=null){
            this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.buMoveInventory_statusList, status);
        }
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getInBuName() {
        return inBuName;
    }

    public void setInBuName(String inBuName) {
        this.inBuName = inBuName;
    }

    public String getOutBuName() {
        return outBuName;
    }

    public void setOutBuName(String outBuName) {
        this.outBuName = outBuName;
    }

    public Timestamp getMoveDate() {
        return moveDate;
    }

    public void setMoveDate(Timestamp moveDate) {
        this.moveDate = moveDate;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
		this.orderTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.buMoveInventory_orderTypeList, orderType);
	}

	public String getOrderTypeLabel() {
		return orderTypeLabel;
	}

	public void setOrderTypeLabel(String orderTypeLabel) {
		this.orderTypeLabel = orderTypeLabel;
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
		this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
	}

	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}

	public void setTallyingUnitLabel(String tallyingUnitLabel) {
		this.tallyingUnitLabel = tallyingUnitLabel;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
		this.currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency);
	}

	public String getCurrencyLabel() {
		return currencyLabel;
	}

	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
	}

	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	public String getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}

	public String getAgreementPrice() {
		return agreementPrice;
	}

	public void setAgreementPrice(String agreementPrice) {
		this.agreementPrice = agreementPrice;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		this.typeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.buMoveInventoryItem_typeList, type);
	}

	public String getTypeLabel() {
		return typeLabel;
	}

	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}
    
    
    
}
