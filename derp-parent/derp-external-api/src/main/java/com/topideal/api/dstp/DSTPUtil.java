package com.topideal.api.dstp;

import com.supplychain.gateway.api.DefaultGatewayClient;
import com.supplychain.gateway.api.GatewayClient;
import com.supplychain.gateway.api.GatewayResponse;
import com.supplychain.gateway.api.goods.GoodsSynchronizeRequest;
import com.supplychain.gateway.api.internal.util.JacksonUtils;
import com.supplychain.gateway.api.orderv2.Purchase.PurchaseOrderRequest;
import com.supplychain.gateway.api.orderv2.sale.SaleOrderToBRequest;
import com.supplychain.gateway.api.orderv2.sale.SaleOrderToCBatchRequest;
import com.topideal.api.dstp.d01.GoodsDstp;
import com.topideal.api.dstp.d01.GoodsItem;
import com.topideal.common.tools.ApolloUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DSTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(DSTPUtil.class);
    /**
     * 根据卓志编码获取Appkey
     * @param topidealCode
     */
    public static String getAppkey(String topidealCode){
        String appkey = "";
        if(topidealCode.equals("0000138")){
            appkey = ApolloUtil.dstpAppkey_0000138;//0000138-宝信
        }else if(topidealCode.equals("1000000204")){
            appkey = ApolloUtil.dstpAppkey_1000000204;//1000000204-健太
        }else if(topidealCode.equals("1000000626")){
            appkey = ApolloUtil.dstpAppkey_1000000626;//1000000626-润佰贸易
        }else if(topidealCode.equals("0000134")){
            appkey = ApolloUtil.dstpAppkey_0000134;//0000134-卓烨贸易
        }
        return appkey;
    }
    /**
     * 根据卓志编码获取appSecret
     * @param topidealCode
     */
    public static String getAppSecret(String topidealCode){
        String appSecret = "";
        if(topidealCode.equals("0000138")){
            appSecret = ApolloUtil.dstpAppsecret_0000138;//0000138-宝信
        }else if(topidealCode.equals("1000000204")){
            appSecret = ApolloUtil.dstpAppsecret_1000000204;//1000000204-健太
        }else if(topidealCode.equals("1000000626")){
            appSecret = ApolloUtil.dstpAppsecret_1000000626;//1000000626-润佰贸易
        }else if(topidealCode.equals("0000134")){
            appSecret = ApolloUtil.dstpAppsecret_0000134;//0000134-卓烨贸易
        }
        return appSecret;
    }
    /**
     * 发送DSTP同步商品
     * @param jsonStr
     * @return
     */
    public static GatewayResponse sendGoodsDSTP(String jsonStr,String topidealCode){
        logger.info("调DSTP接口："+ ApolloUtil.dstpUrl);
        logger.info("报文："+jsonStr);
        String appKey = getAppkey(topidealCode);
        String appSecret = getAppSecret(topidealCode);
        GoodsSynchronizeRequest gatewayRequest = JacksonUtils.readValue(jsonStr, GoodsSynchronizeRequest.class);
        GatewayClient client = new DefaultGatewayClient(ApolloUtil.dstpUrl, appKey, appSecret);
        try {
            GatewayResponse response = client.execute(gatewayRequest);
            logger.info("返回："+response);
            return response;
        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
      return null;
    }
    /**
     * 发送DSTP采购订单
     * @param jsonStr
     * @return
     */
    public static GatewayResponse sendPurchaseDSTP(String jsonStr,String topidealCode){
        logger.info("调DSTP接口："+ ApolloUtil.dstpUrl);
        logger.info("报文："+jsonStr);
        String appKey = getAppkey(topidealCode);
        String appSecret = getAppSecret(topidealCode);
        PurchaseOrderRequest gatewayRequest = JacksonUtils.readValue(jsonStr, PurchaseOrderRequest.class);
        GatewayClient client = new DefaultGatewayClient(ApolloUtil.dstpUrl, appKey, appSecret);
        try {
            GatewayResponse response = client.execute(gatewayRequest);
            logger.info("返回："+response);
            return response;
        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 发送DSTP销售订单
     * @param jsonStr
     * @return
     */
    public static GatewayResponse sendSaleDSTP(String jsonStr,String topidealCode){
        logger.info("调DSTP接口："+ ApolloUtil.dstpUrl);
        logger.info("报文："+jsonStr);
        String appKey = getAppkey(topidealCode);
        String appSecret = getAppSecret(topidealCode);
        SaleOrderToBRequest gatewayRequest = JacksonUtils.readValue(jsonStr, SaleOrderToBRequest.class);
        GatewayClient client = new DefaultGatewayClient(ApolloUtil.dstpUrl, appKey, appSecret);
        try {
            GatewayResponse response = client.execute(gatewayRequest);
            logger.info("返回："+response);
            return response;
        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 发送DSTP电商订单
     * @param jsonStr
     * @return
     */
    public static GatewayResponse sendTocOrderDSTP(String jsonStr,String topidealCode){
        logger.info("调DSTP接口："+ ApolloUtil.dstpUrl);
        logger.info("报文："+jsonStr);
        String appKey = getAppkey(topidealCode);
        String appSecret = getAppSecret(topidealCode);
        SaleOrderToCBatchRequest gatewayRequest = JacksonUtils.readValue(jsonStr, SaleOrderToCBatchRequest.class);
        GatewayClient client = new DefaultGatewayClient(ApolloUtil.dstpUrl, appKey, appSecret);
        try {
            GatewayResponse response = client.execute(gatewayRequest);
            logger.info("返回："+response);
            return response;
        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

        List<GoodsItem> itemList = new ArrayList<>();
        GoodsItem item = new GoodsItem();
        item.setEntGoodsNo("goodsno001");
        item.setRecordPrice("2.30");
        itemList.add(item);

       List<String> picUrl = new ArrayList<>();
        picUrl.add("http://www.baidu.com");
        picUrl.add("http://www.baidu.com");

        GoodsDstp goodsRoot = new GoodsDstp();
        goodsRoot.setCustomerCode("2268376");
        goodsRoot.setBarCode("88889955");
        goodsRoot.setGoodsName("调试商品");
        goodsRoot.setBrand("测试品牌");
        goodsRoot.setBrandEn("AK47");
        goodsRoot.setSpecification("T002");//规格型号
        goodsRoot.setOrgCountry("中国");//原产国
        goodsRoot.setProduceCompany("卓志");//生产厂家
        goodsRoot.setFirstLegalUnit("法定单位1");//第一法定单位
        goodsRoot.setSecondLegalUnit("法定单位2");//第二法定单位
        goodsRoot.setGrossWeight("5.63");//毛重
        goodsRoot.setNetWeight("1.3");//净重
        goodsRoot.setPicUrl(picUrl);//图片
        goodsRoot.setRecordInfo(itemList);
        JSONObject jsonData = JSONObject.fromObject(goodsRoot);

        String topidealCode="";
        sendGoodsDSTP(jsonData.toString(),topidealCode);
    }

}
