package com.topideal.json.inventory.j06;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 商品收发明细Json
 **/
public class InventoryDetailJson implements Serializable {

    private String merchantId; //商家ID
    private String merchantName;//商家名称
    private String topidealCode;//商家编码
    private String depotId;//仓库ID
    private String depotName;//仓库名称
    private String depotCode;//仓库编码
    private String depotType;//仓库类型
    private String orderNo;//订单号
    private String businessNo;//业务订单号
    private String source;//单据1采购  2 调拨 3 销售 4库存调整单5类型调整
    private String sourceDate;//单据时间
    private String ownMonth;//归属月份 yyyy-MM
    private String divergenceDate;//出入时间  yyyy-MM-dd HH:mm:ss
    private String storePlatformName;// 电商平编码 非必填
    private String shopCode;// 店铺编码 非必填
    //来源类型  1采购入库  2 销售入库
    private String sourceType;


    private List<InventoryGoodsDetailJson> goodsList; //商品集合

    private String backTopic;//回调主题
    private String backTags;//回调标签
    private Map<String, Object> customParam;//自定义回调参数

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getTopidealCode() {
        return topidealCode;
    }

    public void setTopidealCode(String topidealCode) {
        this.topidealCode = topidealCode;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public void setDepotCode(String depotCode) {
        this.depotCode = depotCode;
    }

    public String getDepotType() {
        return depotType;
    }

    public void setDepotType(String depotType) {
        this.depotType = depotType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceDate() {
        return sourceDate;
    }

    public void setSourceDate(String sourceDate) {
        this.sourceDate = sourceDate;
    }

    public String getOwnMonth() {
        return ownMonth;
    }

    public void setOwnMonth(String ownMonth) {
        this.ownMonth = ownMonth;
    }

    public String getDivergenceDate() {
        return divergenceDate;
    }

    public void setDivergenceDate(String divergenceDate) {
        this.divergenceDate = divergenceDate;
    }

    public String getStorePlatformName() {
        return storePlatformName;
    }

    public void setStorePlatformName(String storePlatformName) {
        this.storePlatformName = storePlatformName;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }
    public List<InventoryGoodsDetailJson> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<InventoryGoodsDetailJson> goodsList) {
        this.goodsList = goodsList;
    }

    public String getBackTopic() {
        return backTopic;
    }

    public void setBackTopic(String backTopic) {
        this.backTopic = backTopic;
    }

    public String getBackTags() {
        return backTags;
    }

    public void setBackTags(String backTags) {
        this.backTags = backTags;
    }

    public Map<String, Object> getCustomParam() {
        return customParam;
    }

    public void setCustomParam(Map<String, Object> customParam) {
        this.customParam = customParam;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
}
