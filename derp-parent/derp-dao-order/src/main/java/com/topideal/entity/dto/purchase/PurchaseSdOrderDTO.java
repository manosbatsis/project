package com.topideal.entity.dto.purchase;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.vo.purchase.PurchaseSdOrderSditemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

@ApiModel
public class PurchaseSdOrderDTO extends PageModel implements Serializable {

    /**
     * id
     */
    @ApiModelProperty("采购SD单ID")
    private Long id;
    /**
     * 采购订单编号
     */
    @ApiModelProperty("采购订单编号")
    private String purchaseCode;
    /**
     * PO号
     */
    @ApiModelProperty("PO号")
    private String poNo;
    /**
     * 供应商ID
     */
    @ApiModelProperty("供应商ID")
    private Long supplierId;
    /**
     * 供应商名称
     */
    @ApiModelProperty("供应商名称")
    private String supplierName;
    /**
     * 入仓时间
     */
    @ApiModelProperty("入仓时间")
    private Timestamp inboundDate;
    /**
     * 商家ID
     */
    @ApiModelProperty("商家ID")
    private Long merchantId;
    /**
     * 商家名称
     */
    @ApiModelProperty("商家名称")
    private String merchantName;
    /**
     * 汇率
     */
    @ApiModelProperty("汇率")
    private Double rate;
    /**
     * 币种
     */
    @ApiModelProperty("币种")
    private String currency;
    @ApiModelProperty("币种中文")
    private String currencyLabel;
    /**
     * 事业部名称
     */
    @ApiModelProperty("事业部名称")
    private String buName;
    /**
     * 事业部id
     */
    @ApiModelProperty("事业部id")
    private Long buId;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long creater;
    /**
     * 创建人用户名
     */
    @ApiModelProperty("创建人用户名")
    private String createName;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Timestamp createDate;
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Timestamp modifyDate;
    /**
     * 采购SD配置ID
     */
    @ApiModelProperty("采购SD配置ID")
    private Long sdPurchaseConfigId;
    /**
     * 本位币
     */
    @ApiModelProperty("本位币")
    private String tgtCurrency;
    @ApiModelProperty("本位币中文")
    private String tgtCurrencyLabel;
    //是否已同步宝信（0-否，1-是）
    @ApiModelProperty("是否已同步宝信（0-否，1-是）")
    private String isSyn;
    //采购SD编号
    @ApiModelProperty("采购SD编号")
    private String code;
    //1-采购SD，2-采购退SD，3-调整SD
    @ApiModelProperty("采购SD类型 1-采购SD，2-采购退SD，3-调整SD")
    private String type;
    @ApiModelProperty("采购SD类型中文 1-采购SD，2-采购退SD，3-调整SD")
    private String typeLabel;
    //仓库ID
    @ApiModelProperty("仓库ID")
    private Long depotId;
    //仓库名称
    @ApiModelProperty("仓库名称")
    private String depotName;
    /**
     * SD金额
     */
    @ApiModelProperty("SD金额")
    private BigDecimal sdAmount;
    /**
     * SD类型
     */
    @ApiModelProperty("SD类型")
    private String sdTypeName;
    /**
     * SD简称
     */
    @ApiModelProperty("SD简称")
    private String sdSimpleName;

    //备注
    @ApiModelProperty("备注")
    private String remarks;

    //状态 006-已删除
    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("商品信息")
    private List<PurchaseSdOrderSditemModel> itemList ;

    /*id get 方法 */
    public Long getId() {
        return id;
    }

    /*id set 方法 */
    public void setId(Long id) {
        this.id = id;
    }

    /*purchaseCode get 方法 */
    public String getPurchaseCode() {
        return purchaseCode;
    }

    /*purchaseCode set 方法 */
    public void setPurchaseCode(String purchaseCode) {
        this.purchaseCode = purchaseCode;
    }

    /*poNo get 方法 */
    public String getPoNo() {
        return poNo;
    }

    /*poNo set 方法 */
    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    /*supplierId get 方法 */
    public Long getSupplierId() {
        return supplierId;
    }

    /*supplierId set 方法 */
    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    /*supplierName get 方法 */
    public String getSupplierName() {
        return supplierName;
    }

    /*supplierName set 方法 */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    /*inboundDate get 方法 */
    public Timestamp getInboundDate() {
        return inboundDate;
    }

    /*inboundDate set 方法 */
    public void setInboundDate(Timestamp inboundDate) {
        this.inboundDate = inboundDate;
    }

    /*merchantId get 方法 */
    public Long getMerchantId() {
        return merchantId;
    }

    /*merchantId set 方法 */
    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    /*merchantName get 方法 */
    public String getMerchantName() {
        return merchantName;
    }

    /*merchantName set 方法 */
    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    /*rate get 方法 */
    public Double getRate() {
        return rate;
    }

    /*rate set 方法 */
    public void setRate(Double rate) {
        this.rate = rate;
    }

    /*currency get 方法 */
    public String getCurrency() {
        return currency;
    }

    /*currency set 方法 */
    public void setCurrency(String currency) {
        this.currency = currency;
        this.currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency);
    }

    /*buName get 方法 */
    public String getBuName() {
        return buName;
    }

    /*buName set 方法 */
    public void setBuName(String buName) {
        this.buName = buName;
    }

    /*buId get 方法 */
    public Long getBuId() {
        return buId;
    }

    /*buId set 方法 */
    public void setBuId(Long buId) {
        this.buId = buId;
    }

    /*creater get 方法 */
    public Long getCreater() {
        return creater;
    }

    /*creater set 方法 */
    public void setCreater(Long creater) {
        this.creater = creater;
    }

    /*createName get 方法 */
    public String getCreateName() {
        return createName;
    }

    /*createName set 方法 */
    public void setCreateName(String createName) {
        this.createName = createName;
    }

    /*createDate get 方法 */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /*createDate set 方法 */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Long getSdPurchaseConfigId() {
        return sdPurchaseConfigId;
    }

    public void setSdPurchaseConfigId(Long sdPurchaseConfigId) {
        this.sdPurchaseConfigId = sdPurchaseConfigId;
    }

    public String getCurrencyLabel() {
        return currencyLabel;
    }

    public void setCurrencyLabel(String currencyLabel) {
        this.currencyLabel = currencyLabel;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getTgtCurrency() {
        return tgtCurrency;
    }

    public void setTgtCurrency(String tgtCurrency) {
        this.tgtCurrency = tgtCurrency;
        this.tgtCurrencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, tgtCurrency);
    }

    public String getIsSyn() {
        return isSyn;
    }

    public void setIsSyn(String isSyn) {
        this.isSyn = isSyn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        this.typeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseSdOrder_typeList, type);
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public BigDecimal getSdAmount() {
        return sdAmount;
    }

    public void setSdAmount(BigDecimal sdAmount) {
        this.sdAmount = sdAmount;
    }

    public String getSdTypeName() {
        return sdTypeName;
    }

    public void setSdTypeName(String sdTypeName) {
        this.sdTypeName = sdTypeName;
    }

    public String getSdSimpleName() {
        return sdSimpleName;
    }

    public void setSdSimpleName(String sdSimpleName) {
        this.sdSimpleName = sdSimpleName;
    }

    public String getTgtCurrencyLabel() {
        return tgtCurrencyLabel;
    }

    public void setTgtCurrencyLabel(String tgtCurrencyLabel) {
        this.tgtCurrencyLabel = tgtCurrencyLabel;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PurchaseSdOrderSditemModel> getItemList() {
        return itemList;
    }

    public void setItemList(List<PurchaseSdOrderSditemModel> itemList) {
        this.itemList = itemList;
    }
}
