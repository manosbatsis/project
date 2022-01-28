package com.topideal.entity.dto.purchase;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel
public class PurchaseSdOrderPageDTO extends PageModel implements Serializable {

    /**
     * id
     */
    @ApiModelProperty("sd单 id")
    private Long id;
    @ApiModelProperty("采购SD单编码")
    private String code;

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
    @ApiModelProperty("本位币种")
    private String tgtCurrency;
    @ApiModelProperty("本位币种中文")
    private String tgtCurrencyLabel;

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
     * 采购SD单ID
     */
    @ApiModelProperty("采购SD单ID")
    private Long orderId;
    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Long goodsId;
    /**
     * 商品货号
     */
    @ApiModelProperty("商品货号")
    private String goodsNo;
    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;
    /**
     * 条形码
     */
    @ApiModelProperty("条形码")
    private String barcode;
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
    /**
     * SD采购单价
     */
    @ApiModelProperty("SD采购单价")
    private BigDecimal sdPrice;
    /**
     * SD本位币单价
     */
    @ApiModelProperty("SD本位币单价")
    private BigDecimal sdTgtPrice;
    /**
     * SD采购总金额
     */
    @ApiModelProperty("SD采购总金额")
    private BigDecimal sdAmount;
    /**
     * SD本位币总价
     */
    @ApiModelProperty("SD本位币总价")
    private BigDecimal sdTgtAmount;
    /**
     * 采购单价
     */
    @ApiModelProperty("采购单价")
    private BigDecimal price;
    /**
     * 本位币单价
     */
    @ApiModelProperty("本位币单价")
    private BigDecimal tgtPrice;
    /**
     * 采购总金额
     */
    @ApiModelProperty("采购总金额")
    private BigDecimal amount;
    /**
     * 本位币总价
     */
    @ApiModelProperty("本位币总价")
    private BigDecimal tgtAmount;

    @ApiModelProperty("总数量")
    private Integer num;

    @ApiModelProperty(hidden = true)
    private String poNos;

    @ApiModelProperty(hidden = true)
    private String purchaseCodes;

    @ApiModelProperty("入库时间")
    private String inboundDateStr;
    @ApiModelProperty(hidden = true)
    private String inboundStartDateStr;
    @ApiModelProperty(hidden = true)
    private String inboundEndDateStr;

    @ApiModelProperty("能否编辑")
    private String editAble;

    @ApiModelProperty("SD单类型")
    private String type;
    @ApiModelProperty("SD单类型中文")
    private String typeLabel;

    @ApiModelProperty("总数量")
    private Integer totalNum;

    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("采购SD单id,多个以‘，’隔开")
    private String ids;

    @ApiModelProperty("仓库名称")
    private String depotName;

    @ApiModelProperty("仓库Id")
    private Long depotId;


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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
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

    public BigDecimal getSdPrice() {
        return sdPrice;
    }

    public void setSdPrice(BigDecimal sdPrice) {
        this.sdPrice = sdPrice;
    }

    public BigDecimal getSdTgtPrice() {
        return sdTgtPrice;
    }

    public void setSdTgtPrice(BigDecimal sdTgtPrice) {
        this.sdTgtPrice = sdTgtPrice;
    }

    public BigDecimal getSdAmount() {
        return sdAmount;
    }

    public void setSdAmount(BigDecimal sdAmount) {
        this.sdAmount = sdAmount;
    }

    public BigDecimal getSdTgtAmount() {
        return sdTgtAmount;
    }

    public void setSdTgtAmount(BigDecimal sdTgtAmount) {
        this.sdTgtAmount = sdTgtAmount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTgtPrice() {
        return tgtPrice;
    }

    public void setTgtPrice(BigDecimal tgtPrice) {
        this.tgtPrice = tgtPrice;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getPoNos() {
        return poNos;
    }

    public void setPoNos(String poNos) {
        this.poNos = poNos;
    }

    public String getPurchaseCodes() {
        return purchaseCodes;
    }

    public void setPurchaseCodes(String purchaseCodes) {
        this.purchaseCodes = purchaseCodes;
    }

    public String getInboundDateStr() {
        return inboundDateStr;
    }

    public void setInboundDateStr(String inboundDateStr) {
        this.inboundDateStr = inboundDateStr;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTgtAmount() {
        return tgtAmount;
    }

    public void setTgtAmount(BigDecimal tgtAmount) {
        this.tgtAmount = tgtAmount;
    }

    public String getCurrencyLabel() {
        return currencyLabel;
    }

    public void setCurrencyLabel(String currencyLabel) {
        this.currencyLabel = currencyLabel;
    }

    public String getEditAble() {
        return editAble;
    }

    public void setEditAble(String editAble) {
        this.editAble = editAble;
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

    public String getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public String getTgtCurrencyLabel() {
        return tgtCurrencyLabel;
    }

    public void setTgtCurrencyLabel(String tgtCurrencyLabel) {
        this.tgtCurrencyLabel = tgtCurrencyLabel;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public String getInboundStartDateStr() {
        return inboundStartDateStr;
    }

    public void setInboundStartDateStr(String inboundStartDateStr) {
        this.inboundStartDateStr = inboundStartDateStr;
    }

    public String getInboundEndDateStr() {
        return inboundEndDateStr;
    }

    public void setInboundEndDateStr(String inboundEndDateStr) {
        this.inboundEndDateStr = inboundEndDateStr;
    }


}
