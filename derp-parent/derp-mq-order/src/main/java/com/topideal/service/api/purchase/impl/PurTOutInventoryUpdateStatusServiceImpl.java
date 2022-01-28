package com.topideal.service.api.purchase.impl;

import com.alibaba.fastjson.JSONObject;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.PurchaseReturnItemDao;
import com.topideal.dao.purchase.PurchaseReturnOdepotDao;
import com.topideal.dao.purchase.PurchaseReturnOdepotItemDao;
import com.topideal.dao.purchase.PurchaseReturnOrderDao;
import com.topideal.entity.vo.purchase.PurchaseReturnItemModel;
import com.topideal.entity.vo.purchase.PurchaseReturnOdepotItemModel;
import com.topideal.entity.vo.purchase.PurchaseReturnOdepotModel;
import com.topideal.entity.vo.purchase.PurchaseReturnOrderModel;
import com.topideal.json.api.v1_4.OrderStatusUpdateJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.ExchangeRateMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.ExchangeRateMongo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.api.purchase.PurTOutInventoryUpdateStatusService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PurTOutInventoryUpdateStatusServiceImpl implements PurTOutInventoryUpdateStatusService {

    @Autowired
    private PurchaseReturnOdepotDao purchaseReturnOdepotDao ;
    @Autowired
    private PurchaseReturnOrderDao purchaseReturnOrderDao ;
    @Autowired
    private PurchaseReturnOdepotItemDao purchaseReturnOdepotItemDao ;
    @Autowired
    private ExchangeRateMongoDao exchangeRateMongoDao;
    @Autowired
    private MerchantInfoMongoDao merchantMongoDao;
    // 仓库
    @Autowired
    private DepotInfoMongoDao depotInfoMongoDao;
    // MQ
    @Autowired
    private RMQProducer rocketMQProducer;
    @Autowired
    private PurchaseReturnItemDao purchaseReturnItemDao ;

    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_12203110001, model = DERP_LOG_POINT.POINT_12203110001_Label, keyword = "orderId")
    public boolean updatePurTOutInventoryStatus(String json, String keys, String topics, String tags) throws Exception {

        OrderStatusUpdateJson jsonObj = JSONObject.parseObject(json, OrderStatusUpdateJson.class) ;

        if(StringUtils.isBlank(jsonObj.getOrderId())
            || StringUtils.isBlank(jsonObj.getUpdateTime()) ){
            throw new DerpException("报文异常：订单ID或出库时间为空") ;
        }

        String orderId = jsonObj.getOrderId() ;
        PurchaseReturnOrderModel queryModel = new PurchaseReturnOrderModel() ;
        queryModel.setCode(orderId);
        PurchaseReturnOrderModel purchaseReturnOrderModel = purchaseReturnOrderDao.searchByModel(queryModel);

        if(purchaseReturnOrderModel == null){
            throw new DerpException("采购退货订单不存在。订单号：" + jsonObj.getOrderId()) ;
        }

        PurchaseReturnOdepotModel odepotModel = new PurchaseReturnOdepotModel() ;
        odepotModel.setPurchaseReturnId(purchaseReturnOrderModel.getId());
        odepotModel = purchaseReturnOdepotDao.searchByModel(odepotModel) ;
        if(odepotModel == null){
            throw new DerpException("采购退货出库订单不存在") ;
        }

        if(!DERP_ORDER.PURCHASERETURNORDER_STATUS_015.equals(odepotModel.getStatus())){
            throw new DerpException("采购退货出库订单不为：待出库") ;
        }
        
        PurchaseReturnOdepotItemModel queryItem = new PurchaseReturnOdepotItemModel() ;
        queryItem.setOdepotOrderId(odepotModel.getId());
        List<PurchaseReturnOdepotItemModel> itemList = purchaseReturnOdepotItemDao.list(queryItem);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("merchantId", purchaseReturnOrderModel.getMerchantId()) ;
        MerchantInfoMongo merchant = merchantMongoDao.findOne(params);

        params = new HashMap<String, Object>();
        params.put("depotId", purchaseReturnOrderModel.getOutDepotId());// 根据仓库id
        DepotInfoMongo mongo = depotInfoMongoDao.findOne(params);

        String deliverDateStr = jsonObj.getUpdateTime();
        Timestamp deliverDate = TimeUtils.parse(deliverDateStr, "yyyy-MM-dd HH:mm:ss");

        PurchaseReturnOdepotModel updateModel = new PurchaseReturnOdepotModel() ;
        updateModel.setId(odepotModel.getId());
        updateModel.setStatus(DERP_ORDER.PURCHASERETURNORDER_STATUS_027);
        updateModel.setReturnOutDate(deliverDate);
        updateModel.setModifyDate(TimeUtils.getNow());

        purchaseReturnOdepotDao.modify(updateModel) ;

        // 库存发货时间
        String deliverDateMq = TimeUtils.format(deliverDate, "yyyy-MM-dd HH:mm:ss");

        // 扣减销售出库库存量
        InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String now1 = sdf.format(new Date());
        invetAddOrSubtractRootJson.setMerchantId(odepotModel.getMerchantId().toString());
        invetAddOrSubtractRootJson.setMerchantName(odepotModel.getMerchantName());
        invetAddOrSubtractRootJson.setTopidealCode(merchant.getTopidealCode());// 卓志编码
        // 事业部
        invetAddOrSubtractRootJson.setBuId(String.valueOf(odepotModel.getBuId()));
        invetAddOrSubtractRootJson.setBuName(odepotModel.getBuName());
        invetAddOrSubtractRootJson.setDepotId(mongo.getDepotId().toString());
        invetAddOrSubtractRootJson.setDepotName(mongo.getName());
        invetAddOrSubtractRootJson.setDepotCode(mongo.getCode());
        invetAddOrSubtractRootJson.setDepotType(mongo.getType());
        invetAddOrSubtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
        invetAddOrSubtractRootJson.setOrderNo(odepotModel.getCode());
        invetAddOrSubtractRootJson.setBusinessNo(purchaseReturnOrderModel.getCode());
        invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0018);
        invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0030);
        invetAddOrSubtractRootJson.setSourceDate(now1);
        invetAddOrSubtractRootJson.setDivergenceDate(deliverDateMq);
        // 获取当前年月
        invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.formatMonth(deliverDate));
        List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
        for (PurchaseReturnOdepotItemModel item : itemList) {
            InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
            invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());
            invetAddOrSubtractGoodsListJson.setGoodsId(item.getGoodsId().toString());
            invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());
            invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
            invetAddOrSubtractGoodsListJson.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);// 商品分类 （0 好品 1坏品） 字符串
            invetAddOrSubtractGoodsListJson.setNum(item.getNum());
            invetAddOrSubtractGoodsListJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);// 字符串 0 调减 1调增
            invetAddOrSubtractGoodsListJson.setBatchNo(item.getBatchNo());
            invetAddOrSubtractGoodsListJson.setStockLocationTypeId(String.valueOf(purchaseReturnOrderModel.getStockLocationTypeId()));
            invetAddOrSubtractGoodsListJson.setStockLocationTypeName(purchaseReturnOrderModel.getStockLocationTypeName());
            if (item.getProductionDate() != null) {
                invetAddOrSubtractGoodsListJson.setProductionDate(sdf3.format(item.getProductionDate()));
            }
            // 0 过期 1 未过期
            if (item.getOverdueDate() != null) {
                Date overdueDateTimestamp = item.getOverdueDate();
                invetAddOrSubtractGoodsListJson.setOverdueDate(sdf3.format(overdueDateTimestamp));
                String isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());// 判断是否过期是否过期（0是 1否）
                invetAddOrSubtractGoodsListJson.setIsExpire(isExpire);// 是否过期（0是 1否）
            } else {
                invetAddOrSubtractGoodsListJson.setIsExpire("1");
            }
            invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
        }
        invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);

        // 回调mq
        invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.PURCHASE_RETURN_ODEPOT_PUSH_BACK.getTopic());
        invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.PURCHASE_RETURN_ODEPOT_PUSH_BACK.getTags());
        Map<String, Object> customParam = new HashMap<String, Object>();
        customParam.put("code", odepotModel.getCode()); // 订单code
        invetAddOrSubtractRootJson.setCustomParam(customParam);

        String accountCurrency = null;
        Double rate = null;
        if (merchant != null) {
            accountCurrency = merchant.getAccountCurrency();

            if (org.apache.commons.lang.StringUtils.isNotBlank(accountCurrency)) {

                if (accountCurrency.equals(purchaseReturnOrderModel.getCurrency())) {
                    rate = 1.00;
                } else {
                    Map<String, Object> queryRateMap = new HashMap<String, Object>();
                    queryRateMap.put("origCurrencyCode", purchaseReturnOrderModel.getCurrency());
                    queryRateMap.put("tgtCurrencyCode", accountCurrency);
                    queryRateMap.put("effectiveDate", TimeUtils.format(deliverDate, "yyyy-MM-dd"));

                    ExchangeRateMongo rateMongo = exchangeRateMongoDao.findOne(queryRateMap);

                    if (rateMongo != null) {
                        rate = rateMongo.getRate();
                    }
                }
            }

        }

        // 修改采购退货订单状态为出库中、本位币、汇率
        PurchaseReturnOrderModel updateReturnModel = new PurchaseReturnOrderModel();
        updateReturnModel.setId(purchaseReturnOrderModel.getId());
        updateReturnModel.setStatus(DERP_ORDER.PURCHASERETURNORDER_STATUS_027);
        updateReturnModel.setTgtCurrency(accountCurrency);
        updateReturnModel.setModifyDate(TimeUtils.getNow());
        if (rate != null) {
            updateReturnModel.setRate(new BigDecimal(rate));
        }

        purchaseReturnOrderDao.modify(updateReturnModel);

        PurchaseReturnItemModel queryItemModel = new PurchaseReturnItemModel();
        queryItemModel.setOrderId(purchaseReturnOrderModel.getId());

        List<PurchaseReturnItemModel> returnItemList = purchaseReturnItemDao.list(queryItemModel);

        for (PurchaseReturnItemModel purchaseReturnItemModel : returnItemList) {
            if (rate == null) {
                continue;
            }

            // 设置本位单价、金额
            BigDecimal returnAmount = purchaseReturnItemModel.getReturnAmount();
            Integer returnNum = purchaseReturnItemModel.getReturnNum();

            BigDecimal tgtAmount = returnAmount.multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP);

            BigDecimal tgtPrice = tgtAmount.divide(new BigDecimal(returnNum), 8, BigDecimal.ROUND_HALF_UP);

            purchaseReturnItemModel.setTgtReturnAmount(tgtAmount);
            purchaseReturnItemModel.setTgtReturnPrice(tgtPrice);
            purchaseReturnItemModel.setModifyDate(TimeUtils.getNow());

            purchaseReturnItemDao.modify(purchaseReturnItemModel);
        }

        // 推送加减库存
        rocketMQProducer.send(net.sf.json.JSONObject.fromObject(invetAddOrSubtractRootJson).toString(),
                MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
                MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

        return true;
    }
}
