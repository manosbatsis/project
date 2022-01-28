package com.topideal.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.enums.PushYwmsTypeEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.XmlConverUtil;
import com.topideal.dao.InventoryRealTimeSnapshotDao;
import com.topideal.entity.vo.InventoryRealTimeSnapshotModel;
import com.topideal.http.HttpClientUtil;
import com.topideal.json.pushapi.ywms.inventory.push.Criteria;
import com.topideal.json.pushapi.ywms.inventory.push.CriteriaList;
import com.topideal.json.pushapi.ywms.inventory.push.InventoryPushRoot;
import com.topideal.json.pushapi.ywms.inventory.push.Request;
import com.topideal.json.pushapi.ywms.inventory.pushback.InventoryPushBackRoot;
import com.topideal.json.pushapi.ywms.inventory.pushback.Item;
import com.topideal.json.pushapi.ywms.inventory.pushback.Items;
import com.topideal.json.pushapi.ywms.inventory.pushback.Response;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.InventoryYwmsTimeSnapshotService;
import com.topideal.tools.QimenSignUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * 众邦云实时库存快照 实现类
 **/
@Service
public class InventoryYwmsTimeSnapshotServiceImpl implements InventoryYwmsTimeSnapshotService {

    /* 打印日志 */
    private static final Logger logger = LoggerFactory.getLogger(InventoryYwmsTimeSnapshotServiceImpl.class);

    //实时库存快照
    @Autowired
    private InventoryRealTimeSnapshotDao inventoryRealTimeSnapshotDao;
    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
    @Autowired
    private DepotInfoMongoDao depotInfoMongoDao;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;

    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_13201307700, model = DERP_LOG_POINT.POINT_13201307700_Label)
    public void synsInventoryRealTimeYwms(String json, String keys, String topics, String tags) throws Exception {
        logger.info("=============众邦云实时库存快照开始=================" + json);
        String goodsNos = ApolloUtil.ywmsGoodsNos;

        Integer interval = 100;
        if (StringUtils.isNotBlank(goodsNos)) {
            List<String> goodsNoList = Arrays.asList(goodsNos.split(","));
            int size = goodsNoList.size();
            for (int i = 0; i < Math.ceil(size*0.01); i++) {
                InventoryPushRoot root = new InventoryPushRoot();
                Request request = new Request();
                CriteriaList criteriaList = new CriteriaList();
                List<Criteria> criterias = new ArrayList<>();
                for (int j = i*interval; j < (i+1)*interval && j<size; j++) {
                    Criteria criteria = new Criteria();
                    criteria.setWarehouseCode("GZSXCLOUD");
                    criteria.setOwnerCode(ApolloUtil.ywmsOwnerCode);
                    criteria.setItemCode(goodsNoList.get(j));
                    criterias.add(criteria);
                }
                criteriaList.setCriteria(criterias);
                request.setCriteriaList(criteriaList);
                root.setRequest(request);
                //将null 值转空字符串
                String jsonStr = com.alibaba.fastjson.JSONObject.toJSONString(root, SerializerFeature.WriteMapNullValue,
                        SerializerFeature.WriteNullStringAsEmpty) ;
                String reqXml = XmlConverUtil.JsonToXml(jsonStr);
                //推送众邦云仓
                Map<String, String> jsonObject = new HashMap<>();
                jsonObject.put("method", PushYwmsTypeEnum.SSKC.getMethod());
                jsonObject.put("v", ApolloUtil.ywmsV);
                jsonObject.put("app_key", ApolloUtil.ywmsAppkey);// appkey
                jsonObject.put("customerId", ApolloUtil.ywmsCustomerId);//appSecret
                jsonObject.put("format", ApolloUtil.ywmsFormat);
                jsonObject.put("sign_method", "md5");
                jsonObject.put("timestamp", "");
                String sign = QimenSignUtils.sign(jsonObject, reqXml, ApolloUtil.ywmsSecret);
                //组装URL
                StringBuffer url = new StringBuffer() ;
                url.append(ApolloUtil.ywmsApi).append("?");
                url.append("app_key=").append(ApolloUtil.ywmsAppkey).append("&") ;
                url.append("format=").append("xml").append("&") ;
                url.append("method=").append(PushYwmsTypeEnum.SSKC.getMethod()).append("&") ;
                url.append("v=").append(ApolloUtil.ywmsV).append("&") ;
                url.append("sign=").append(sign).append("&") ;
                url.append("sign_method=").append("md5").append("&") ;
                url.append("customerId=").append(ApolloUtil.ywmsCustomerId).append("&") ;
                url.append("timestamp=") ;
                String resultXml = HttpClientUtil.doPostXml(url.toString(), reqXml, "UTF-8");


                String resultJson = XmlConverUtil.XmlToJson(resultXml);
                InventoryPushBackRoot inventoryPushBackRoot = JSONObject.parseObject(resultJson, InventoryPushBackRoot.class) ;
                Response response = inventoryPushBackRoot.getResponse();

                if(!resultXml.contains("success")) {
                    logger.info("请求众邦云实时库存快照信息响应失败" + response.getMessage());
                    throw new RuntimeException("众邦云实时库存快照信息响应失败" + response.getMessage());
                }

                if (response.getItems() == null) {
                    logger.info("众邦云实时库存快照信息响应为空");
                    throw new RuntimeException("众邦云实时库存快照信息响应为空");
                }
                logger.info("请求众邦云实时库存快照信息响应结果：" + resultJson);
                this.saveInventoryRealTimeYwms(response.getItems());
            }
        }

    }

    /**
     * 保存实时库存快照信息
     */
    public void saveInventoryRealTimeYwms(Items items) throws Exception {
        Timestamp now = TimeUtils.getNow();
        String topidealCode = ApolloUtil.ywmsTopidealCode; //公司主体
        Map<String, Object> merchantParam = new HashMap<>();
        merchantParam.put("topidealCode", topidealCode);
        MerchantInfoMongo merchantInfoMongo = merchantInfoMongoDao.findOne(merchantParam);
        List<Item> itemList = items.getItem();
        //遍历保存实时库存快照信息
        for (Item item : itemList) {
            String itemCode = item.getItemCode(); //商品货号
            String warehouseCode = item.getWarehouseCode(); //仓库编码
            String quantity = item.getQuantity(); //quantity未冻结库存数量
            String lockQuantity = item.getLockQuantity();//lockQuantity冻结库存数量
            String inventoryType = item.getInventoryType();//inventoryType库存类型
            InventoryRealTimeSnapshotModel inventoryModel = new InventoryRealTimeSnapshotModel();

            //查询仓库信息
            Map<String, Object> depotParam = new HashMap<>();
            depotParam.put("code", warehouseCode);
            DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(depotParam);
            if (depotInfoMongo != null) {
                inventoryModel.setDepotId(depotInfoMongo.getDepotId());
                inventoryModel.setDepotName(depotInfoMongo.getName());
            }
            //查询商品信息
            Map<String, Object> merchandiseParam = new HashMap<>();
            merchandiseParam.put("merchantId", merchantInfoMongo.getMerchantId());
            merchandiseParam.put("goodsNo", itemCode);
            MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseParam);
            if (merchandiseInfoMogo != null) {
                inventoryModel.setGoodsId(merchandiseInfoMogo.getMerchandiseId());
                inventoryModel.setGoodsNo(merchandiseInfoMogo.getGoodsNo());
                inventoryModel.setGoodsName(merchandiseInfoMogo.getName());
                inventoryModel.setBarcode(merchandiseInfoMogo.getBarcode());
            }
            inventoryModel.setMerchantId(merchantInfoMongo.getMerchantId());
            inventoryModel.setMerchantName(merchantInfoMongo.getName());
            inventoryModel.setBatchNo(item.getBatchCode());
            if (StringUtils.isNotBlank(item.getProductDate())) {
                inventoryModel.setProductionDate(TimeUtils.strToSqlDate(item.getProductDate()));
            }
            if (StringUtils.isNotBlank(item.getExpireDate())) {
                inventoryModel.setOverdueDate(TimeUtils.strToSqlDate(item.getExpireDate()));
            }
            inventoryModel.setQty(Integer.valueOf(quantity) + Integer.valueOf(lockQuantity));
            inventoryModel.setRealFrozenStockNum(Integer.valueOf(lockQuantity));
            inventoryModel.setLockStockNum(0);
            inventoryModel.setRealStockNum(Integer.valueOf(quantity));
            if (StringUtils.isNotBlank(inventoryType)) {
                if ("ZP".equals(inventoryType)) {
                    inventoryModel.setStockType(DERP_INVENTORY.INVENTORY_TYPE_0);
                } else {
                    inventoryModel.setStockType(DERP_INVENTORY.INVENTORY_TYPE_1);
                }
            }
            inventoryModel.setCreateDate(now);
            inventoryModel.setSnapshotSource(DERP_INVENTORY.REALTIMESNAPSHOT_SNAPSHOTSOURCE_YWMS);
            Long id = inventoryRealTimeSnapshotDao.save(inventoryModel);
            if(id==null){
                logger.error("新增实时库存失败");
                throw new Exception("新增实时库存失败");
            }
        }

    }
}
