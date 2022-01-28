package com.topideal.report.webapi.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @Author: Wilson Lau
 * @Date: 2021/8/19 15:54
 */
public class SdInterestPriceForm extends PageForm {

    @ApiModelProperty(value = "令牌")
    private String token;

    @ApiModelProperty(value = "单据id集合，多个用逗号隔开")
    private String ids;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String merchantName;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
     * 标准条码
     */
    @ApiModelProperty(value = "标准条码")
    private String commbarcode;

    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌Id")
    private Long brandId;

    /**
     * 结算币种
     */
    @ApiModelProperty(value = "结算币种")
    private String currency;

    /**
     * 结算价格
     */
    @ApiModelProperty(value = "结算价格")
    private BigDecimal price;

    /**
     * 生效月份
     */
    @ApiModelProperty(value = "生效月份")
    private String effectiveMonth;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createDate;

    /**
     * 事业部id
     */
    @ApiModelProperty(value = "事业部id")
    private Long buId;

    /**
     * 事业部名称
     */
    @ApiModelProperty(value = "事业部名称")
    private String buName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getEffectiveMonth() {
        return effectiveMonth;
    }

    public void setEffectiveMonth(String effectiveMonth) {
        this.effectiveMonth = effectiveMonth;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }
}
