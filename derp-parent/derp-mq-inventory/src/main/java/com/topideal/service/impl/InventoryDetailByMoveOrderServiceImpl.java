package com.topideal.service.impl;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InventoryAddSubOrderDao;
import com.topideal.dao.InventoryBatchDao;
import com.topideal.dao.InventoryDetailsDao;
import com.topideal.entity.vo.InventoryAddSubOrderModel;
import com.topideal.entity.vo.InventoryBatchModel;
import com.topideal.entity.vo.InventoryDetailsModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.json.inventory.j06.InventoryDetailJson;
import com.topideal.json.inventory.j06.InventoryGoodsDetailJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantDepotBuRelMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantDepotBuRelMongo;
import com.topideal.service.InventoryDetailByMoveOrderService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 移库单审核生成商品收发明细
 **/
@Service
public class InventoryDetailByMoveOrderServiceImpl implements InventoryDetailByMoveOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryDetailByMoveOrderServiceImpl.class);

    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
    @Autowired
    private DepotInfoMongoDao depotInfoMongoDao;
    @Autowired
    private InventoryDetailsDao inventoryDetailsDao;
    @Autowired
    private RMQProducer rocketMQProducer;
    @Autowired
    private MerchantDepotBuRelMongoDao merchantDepotBuRelMongoDao;
    @Autowired
    private InventoryAddSubOrderDao inventoryAddSubOrderDao;
    @Autowired
    private InventoryBatchDao inventoryBatchDao;

    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_13201307600, model = DERP_LOG_POINT.POINT_13201307600_Label, keyword = "orderNo")
    public boolean saveInventoryDetail(String json, String keys, String topics, String tags) throws Exception {
        LOGGER.info("========== 移库单审核生成商品收发明细===============>" + json);
        JSONObject jsonData = JSONObject.fromObject(json);
        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put("goodsList", InventoryGoodsDetailJson.class);
        InventoryDetailJson inventoryDetailJson = (InventoryDetailJson) JSONObject.toBean(jsonData, InventoryDetailJson.class,
                classMap);
        //查询仓库
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("depotId", Long.valueOf(inventoryDetailJson.getDepotId()));
        DepotInfoMongo depot = depotInfoMongoDao.findOne(paramMap);
        if(depot==null){
            LOGGER.error("depotId："+inventoryDetailJson.getDepotId() + "仓库不存在");
            throw new RuntimeException("depotId："+inventoryDetailJson.getDepotId() + "仓库不存在");
        }
        //报文参数校验
        checkJsonData(inventoryDetailJson, depot);

        //去重校验
        InventoryAddSubOrderModel model=new InventoryAddSubOrderModel();
        model.setOrderNo(inventoryDetailJson.getOrderNo());
        model.setBusinessNo(inventoryDetailJson.getBusinessNo());
        model.setSourceType("3");//来源类型 1-库存扣减 2-库存回滚 3-库存流水
        model.setCreateDate(TimeUtils.getNow());
        try{
            inventoryAddSubOrderDao.save(model);
        }catch(Exception e){
            LOGGER.error("单号已存在,库存扣减失败："+inventoryDetailJson.getOrderNo());
            throw new RuntimeException("单号已存在,库存扣减失败："+inventoryDetailJson.getOrderNo());
        }

        Map<String, Map<String, Object>> goodsBatchMap = new HashMap<>();
        List<InventoryGoodsDetailJson> goodsList = inventoryDetailJson.getGoodsList();
        //保存商品收发明细信息
        for(InventoryGoodsDetailJson goods : goodsList) {
            //查询事业部是否在仓库下存在
            Map<String, Object> buRel = new HashMap<>();
            buRel.put("merchantId", Long.valueOf(inventoryDetailJson.getMerchantId()));
            buRel.put("buId", Long.valueOf(goods.getBuId()));
            buRel.put("depotId", depot.getDepotId());
            MerchantDepotBuRelMongo bu = merchantDepotBuRelMongoDao.findOne(buRel);
            if (bu == null) {
                LOGGER.error("buId：" + goods.getBuId() + "的事业部在仓库下不存在");
                throw new RuntimeException("buId：" + goods.getBuId() + "的事业部在仓库下不存在");
            }

            //仓库批次强校验判断
            /**若仓库为批次效期强校验，或入库强校验时，批次效期必填, 先查询一个库存随机分配一个，若都无则默认一个*/
            if (!DERP_SYS.DEPOTINFO_BATCHVALIDATION_0.equals(depot.getBatchValidation())) {
                if (StringUtils.isBlank(goods.getBatchNo()) ||
                        StringUtils.isBlank(goods.getProductionDate()) ||
                        StringUtils.isBlank(goods.getOverdueDate())) {
                    String batchNo = "B00001";
                    Date productionDate = TimeUtils.getLastYear(new Date());
                    Date overdueDate = new Date();
                    if (goodsBatchMap.containsKey(goods.getGoodsId())) {
                        Map<String, Object> goodsMap = goodsBatchMap.get(goods.getGoodsId());
                        batchNo = (String) goodsMap.get("batchNo");
                        productionDate = (Date) goodsMap.get("productionDate");
                        overdueDate = (Date) goodsMap.get("overdueDate");
                    } else {
                        InventoryBatchModel inventoryBatchModel = new InventoryBatchModel();
                        inventoryBatchModel.setMerchantId(Long.valueOf(inventoryDetailJson.getMerchantId()));
                        inventoryBatchModel.setDepotId(depot.getDepotId());
                        inventoryBatchModel.setGoodsId(Long.valueOf(goods.getGoodsId()));
                        List<InventoryBatchModel> inventoryBatchModels = inventoryBatchDao.list(inventoryBatchModel);

                        Collections.sort(inventoryBatchModels, new Comparator<InventoryBatchModel>() {
                            @Override
                            public int compare(InventoryBatchModel o1, InventoryBatchModel o2) {
                                return o2.getOverdueDate().compareTo(o1.getOverdueDate());
                            }
                        });

                        for (InventoryBatchModel inventoryBatch : inventoryBatchModels) {
                            if (StringUtils.isNotBlank(inventoryBatch.getBatchNo())
                                    && inventoryBatch.getProductionDate() != null && inventoryBatch.getOverdueDate() != null) {
                                batchNo = inventoryBatch.getBatchNo();
                                productionDate = inventoryBatch.getProductionDate();
                                overdueDate = inventoryBatch.getOverdueDate();
                                break;
                            }
                        }
                        Map<String, Object> goodsMap = new HashMap<>();
                        goodsMap.put("batchNo", batchNo);
                        goodsMap.put("productionDate", productionDate);
                        goodsMap.put("overdueDate", overdueDate);
                        goodsBatchMap.put(goods.getGoodsId(), goodsMap);
                    }
                    goods.setBatchNo(batchNo);
                    goods.setProductionDate(TimeUtils.formatDay(productionDate));
                    goods.setOverdueDate(TimeUtils.formatDay(overdueDate));
                }
            }

            InventoryDetailsModel detailsModel = new InventoryDetailsModel();
            detailsModel.setOrderNo(inventoryDetailJson.getOrderNo());
            detailsModel.setBusinessNo(inventoryDetailJson.getBusinessNo());
            detailsModel.setDepotId(Long.valueOf(inventoryDetailJson.getDepotId()));
            detailsModel.setDepotName(inventoryDetailJson.getDepotName());
            detailsModel.setDepotCode(inventoryDetailJson.getDepotCode());
            detailsModel.setDepotType(inventoryDetailJson.getDepotType());
            detailsModel.setStorePlatformName(inventoryDetailJson.getStorePlatformName());
            detailsModel.setShopCode(inventoryDetailJson.getShopCode());
            detailsModel.setMerchantId(Long.valueOf(inventoryDetailJson.getMerchantId()));
            detailsModel.setMerchantName(inventoryDetailJson.getMerchantName());
            detailsModel.setTopidealCode(inventoryDetailJson.getTopidealCode());
            detailsModel.setOwnMonth(inventoryDetailJson.getOwnMonth());
            detailsModel.setDivergenceDate(TimeUtils.parseFullTime(inventoryDetailJson.getDivergenceDate()));
            detailsModel.setSourceDate(TimeUtils.parseFullTime(inventoryDetailJson.getSourceDate()));
            detailsModel.setGoodsId(Long.valueOf(goods.getGoodsId()));
            detailsModel.setGoodsName(goods.getGoodsName());
            detailsModel.setCommbarcode(goods.getCommbarcode());
            detailsModel.setGoodsNo(goods.getGoodsNo());
            detailsModel.setBarcode(goods.getBarcode());
            detailsModel.setBatchNo(goods.getBatchNo());
            if (StringUtils.isNotBlank(goods.getProductionDate())) {
                detailsModel.setProductionDate(TimeUtils.strToSqlDate(goods.getProductionDate()));
            }
            if (StringUtils.isNotBlank(goods.getOverdueDate())) {
                detailsModel.setOverdueDate(TimeUtils.strToSqlDate(goods.getOverdueDate()));
            }
            detailsModel.setType(goods.getType());
            detailsModel.setIsExpire(goods.getIsExpire());
            detailsModel.setUnit(goods.getUnit());
            detailsModel.setNum(goods.getNum());
            detailsModel.setBuId(Long.valueOf(goods.getBuId()));
            detailsModel.setBuName(goods.getBuName());
            detailsModel.setSource(inventoryDetailJson.getSource());
            detailsModel.setSourceType(inventoryDetailJson.getSourceType());
            detailsModel.setOperateType(goods.getOperateType());
            if (StringUtils.isNotBlank(goods.getStockLocationTypeId())) detailsModel.setStockLocationTypeId(Long.valueOf(goods.getStockLocationTypeId()));
            if (StringUtils.isNotBlank(goods.getStockLocationTypeName()))detailsModel.setStockLocationTypeName(goods.getStockLocationTypeName());
			
			 
            if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0031.equals(inventoryDetailJson.getSourceType())) {// 移库单
                if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goods.getOperateType())) {// 减
                    detailsModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0037);// 移库单出
                } else if (DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(goods.getOperateType())) {// 加
                    detailsModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0038);// 移库单入
                }
            } else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0032.equals(inventoryDetailJson.getSourceType())) {// 库位调整单
                if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(goods.getOperateType())) {// 减
                    detailsModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0040);// 库位调整出
                } else if (DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(goods.getOperateType())) {// 加
                    detailsModel.setThingStatus(DERP_INVENTORY.INVENTORY_THINGSTATUS_0039);// 库位调整入
                }
            }
            detailsModel.setThingName(DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.inventory_thingStatusList, detailsModel.getThingStatus()));
            Long orderId = inventoryDetailsDao.save(detailsModel);
            if (orderId == null) {
                LOGGER.error("移库单审核新增商品收发明细失败");
                throw new Exception("移库单审核新增商品收发明细失败");
            }
        }
        //回调通知业务端修改移库单状态
        if(StringUtils.isNotBlank(inventoryDetailJson.getBackTopic())){
            LOGGER.debug("回调通知业务端修改移库单状态开始====="+inventoryDetailJson.getOrderNo());
            BackRootJson backRootJson = new BackRootJson();
            backRootJson.setCode(inventoryDetailJson.getOrderNo());
            Map<String, Object> customParam = inventoryDetailJson.getCustomParam();
            if(customParam==null){
                customParam = new HashMap<String, Object>();
            }
            backRootJson.setCustomParam(customParam);
            JSONObject tootJson = JSONObject.fromObject(backRootJson);
            SendResult sendResult = rocketMQProducer.send(tootJson.toString(),inventoryDetailJson.getBackTopic(),
                    inventoryDetailJson.getBackTags());
            LOGGER.debug(inventoryDetailJson.getOrderNo()+"回调通知业务端修改移库单状态结果====="+sendResult);
        }
        return true;
    }


    /**
     * 业务报文参数校验
     * @param inventoryDetailJson
     * @throws Exception
     */
    private void checkJsonData(InventoryDetailJson inventoryDetailJson, DepotInfoMongo depot) throws Exception {
        //1、校验表头参数
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isBlank(inventoryDetailJson.getMerchantId())) {
            LOGGER.error("商家id为空");
            throw new RuntimeException("商家id为空");
        }
        if (StringUtils.isBlank(inventoryDetailJson.getMerchantName())) {
            LOGGER.error("商家名称为空");
            throw new RuntimeException("商家名称为空");
        }
        if (StringUtils.isBlank(inventoryDetailJson.getTopidealCode())) {
            LOGGER.error("商家编码为空");
            throw new RuntimeException("商家编码为空");
        }
        if (StringUtils.isBlank(inventoryDetailJson.getDepotId())) {
            LOGGER.error("仓库id为空");
            throw new RuntimeException("仓库id为空");
        }
        if (StringUtils.isBlank(inventoryDetailJson.getDepotCode())) {
            LOGGER.error("仓库编码为空");
            throw new RuntimeException("仓库编码为空");
        }

        if (StringUtils.isBlank(inventoryDetailJson.getDepotName())) {
            LOGGER.error("仓库名称为空");
            throw new RuntimeException("仓库名称为空");
        }
        if (StringUtils.isBlank(inventoryDetailJson.getOrderNo())) {
            LOGGER.error("来源单据号为空");
            throw new RuntimeException("来源单据号为空");
        }
        if (StringUtils.isBlank(inventoryDetailJson.getSource())) {
            LOGGER.error("单据为空");
            throw new RuntimeException("单据为空");
        }
        if (StringUtils.isBlank(inventoryDetailJson.getSourceType())) {
            LOGGER.error("来源类型为空");
            throw new RuntimeException("来源类型为空");
        }
        if (StringUtils.isBlank(inventoryDetailJson.getOwnMonth())) {
            LOGGER.error("归属月份为空");
            throw new RuntimeException("归属月份为空");
        }
        if (StringUtils.isBlank(inventoryDetailJson.getDivergenceDate())) {
            LOGGER.error("出入时间为空");
            throw new RuntimeException("出入时间为空");
        }
        if (StringUtils.isBlank(inventoryDetailJson.getSourceDate())) {
            LOGGER.error("单据日期为空");
            throw new RuntimeException("单据日期为空");
        }

        //2、校验出入时间的年月和归属月份是否一致
        String temTime= TimeUtils.formatMonth(format.parse(inventoryDetailJson.getDivergenceDate()));
        if(!temTime.equals(inventoryDetailJson.getOwnMonth())){
            LOGGER.error("出入时间的年月和归属月份的年月不相同");
            throw new RuntimeException("出入时间的年月和归属月份的年月不相同");
        }
        // 3、校验表体商品信息
        List<InventoryGoodsDetailJson> goodsList = inventoryDetailJson.getGoodsList();
        if(goodsList == null || goodsList.size() == 0){
            LOGGER.error("商品信息为空");
            throw new RuntimeException("商品信息为空");
        }
        for(InventoryGoodsDetailJson goods : goodsList) {
            //查询商品
            Map<String, Object> merchandiseMap = new HashMap<String, Object>();
            merchandiseMap.put("merchandiseId", Long.valueOf(goods.getGoodsId()));
            MerchandiseInfoMogo merchandiseMogo = merchandiseInfoMogoDao.findOne(merchandiseMap);
            if (merchandiseMogo==null) {
                LOGGER.debug("根据商品id没有查询到对应的商品,商品货号 :"+goods.getGoodsNo());
                throw new RuntimeException("根据商品id没有查询到对应的商品,商品货号 :"+goods.getGoodsNo()) ;
            }
            // 如果商品标准条码不能为空
            if (StringUtils.isBlank(merchandiseMogo.getCommbarcode())) {
                LOGGER.debug("商品标准条码不能为空,商品货号 :"+goods.getGoodsNo());
                throw new RuntimeException("商品标准条码不能为空,商品货号 :"+goods.getGoodsNo()) ;
            }
            // 把标准条码设置进去
            goods.setCommbarcode(merchandiseMogo.getCommbarcode());
            goods.setBarcode(merchandiseMogo.getBarcode());

            //海外仓   单位必填
            if (DERP_SYS.DEPOTINFO_TYPE_C.equals(inventoryDetailJson.getDepotType())) {
                //单位
                String unit = goods.getUnit();
                if (StringUtils.isBlank(goods.getUnit())) {
                    LOGGER.error("海外仓单位必填");
                    throw new RuntimeException("海外仓单位必填");
                }

                if (!(DERP.INVENTORY_UNIT_0.equals(unit) || DERP.INVENTORY_UNIT_1.equals(unit) || DERP.INVENTORY_UNIT_2.equals(unit))) {
                    throw new RuntimeException("海外仓单位格式必须为 0或者1 或者2");
                }

            } else {// 非海外仓不能给理货单位
                if (StringUtils.isNotBlank(goods.getUnit())) {
                    LOGGER.error("非海外仓理货单必须为空");
                    throw new RuntimeException("非海外仓理货单必须为空");
                }
            }

            //非空校验
            if (StringUtils.isBlank(goods.getGoodsId())) {
                LOGGER.error("商品id为空");
                throw new RuntimeException("商品id为空");
            }
            if (StringUtils.isBlank(goods.getGoodsName())) {
                LOGGER.error("商品名称为空");
                throw new RuntimeException("商品名称为空");
            }
            if (StringUtils.isBlank(goods.getGoodsNo())) {
                LOGGER.error("商品货号为空");
                throw new RuntimeException("商品货号为空");
            }
            if (StringUtils.isBlank(goods.getBarcode())) {
                LOGGER.error("商品条码为空");
                throw new RuntimeException("商品条码为空");
            }
            if(goods.getNum()<=0){
                LOGGER.error("库存数量不能小于等于0");
                throw new RuntimeException("库存数量不能小于等于0");
            }
            if (StringUtils.isNotBlank(goods.getBatchNo())) {
                goods.setBatchNo(goods.getBatchNo().trim());
            }
            if (StringUtils.isBlank(goods.getOperateType())) {
                LOGGER.error("操作类型为空");
                throw new RuntimeException("操作类型为空");
            } else if (StringUtils.isNotBlank(goods.getOperateType()) && !goods.getOperateType().matches("0|1")) {
                LOGGER.error("操作类型operateType字段为无效数字");
                throw new RuntimeException("操作类型operateType字段为无效数字");
            }

            if (StringUtils.isBlank(goods.getBuId())) {
                LOGGER.error("事业部ID为空");
                throw new RuntimeException("事业部ID为空");
            }
            if (StringUtils.isBlank(goods.getBuName())) {
                LOGGER.error("事业部名称为空");
                throw new RuntimeException("事业部名称为空");
            }

            //失效日期不为空，是否过期必填
            if(StringUtils.isNotBlank(goods.getOverdueDate())&&StringUtils.isBlank(goods.getIsExpire())) {
                LOGGER.error("是否过期为空");
                throw new RuntimeException("是否过期为空");
            }else if(StringUtils.isNotBlank(goods.getIsExpire())&&!goods.getIsExpire().matches("0|1")){
                LOGGER.error("是否过期字段为无效数字");
                throw new RuntimeException("是否过期字段为无效数字");
            }else if(StringUtils.isBlank(goods.getIsExpire())){
                //业务端没传，则默认为未过期 0 过期  1 未过期
                goods.setIsExpire(DERP.ISEXPIRE_1);
            }

            if(StringUtils.isNotBlank(goods.getType())&&!goods.getType().matches("0|1")) {
                LOGGER.error("商品分类type字段为无效数字");
                throw new RuntimeException("商品分类type字段为无效数字");
            }else if(StringUtils.isBlank(goods.getType())){
                //业务端没传,则默认好品 0 好品  1 坏品
                goods.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);
            }
        }
    }
}
