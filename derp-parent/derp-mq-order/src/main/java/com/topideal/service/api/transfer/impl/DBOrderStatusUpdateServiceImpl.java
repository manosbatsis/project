package com.topideal.service.api.transfer.impl;

import com.topideal.common.constant.*;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.transfer.*;
import com.topideal.entity.vo.transfer.*;
import com.topideal.json.api.v1_4.OrderStatusUpdateJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.DepotMerchantRelMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.DepotMerchantRelMongo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.api.transfer.DBOrderStatusUpdateService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 调拨模块单据状态更新
 **/
@Service
public class DBOrderStatusUpdateServiceImpl implements DBOrderStatusUpdateService {

    public static final Logger LOGGER= LoggerFactory.getLogger(DBOrderStatusUpdateServiceImpl.class);

    @Autowired
    private TransferOrderDao transferOrderDao;// 调拨单表
    @Autowired
    private TransferOrderItemDao transferOrderItemDao;// 调拨单商品
    @Autowired
    private TransferOutDepotDao transferOutDepotDao;//调拨出库单
    @Autowired
    private TransferOutDepotItemDao transferOutDepotItemDao;
    @Autowired
    private TransferInDepotDao transferInDepotDao;// 调拨入库单
    @Autowired
    private TransferInDepotItemDao transferInDepotItemDao;// 调拨入库单表体
    @Autowired
    private RMQProducer rocketMQProducer;//MQ;
    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
    @Autowired
    private DepotInfoMongoDao depotInfoMongoDao;
    @Autowired
    private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
    @Autowired
    private MerchantInfoMongoDao merchantMongoDao;

    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_12203130001,model=DERP_LOG_POINT.POINT_12203130001_Label,keyword="orderId")
    public boolean updateDBOrderStatusInfo(String json, String keys, String topics, String tags) throws Exception {
        LOGGER.info("单据状态更新 —— 调拨" + json);

        // 将字符串转成json
        JSONObject jsonData = JSONObject.fromObject(json);
        OrderStatusUpdateJson rootJson = (OrderStatusUpdateJson) JSONObject.toBean(jsonData, OrderStatusUpdateJson.class);

        Long merchantId = rootJson.getMerchantId();
        String orderId = rootJson.getOrderId();
        String statusCode = rootJson.getStatusCode();
        String updateTime = rootJson.getUpdateTime();

        TransferOrderModel transferOrderModel =new TransferOrderModel();// 调拨单表
        transferOrderModel.setCode(orderId);//订单号
        transferOrderModel.setMerchantId(merchantId);
        transferOrderModel = transferOrderDao.searchByModel(transferOrderModel);
        if(transferOrderModel ==null) {
            LOGGER.error("订单在不存在"+orderId);
            throw new RuntimeException("订单在所有订单模块都不存在"+orderId);
        }

        TransferOutDepotModel outDepotModel = new TransferOutDepotModel();
        outDepotModel.setTransferOrderId(transferOrderModel.getId());
        TransferOutDepotModel existOutModel = transferOutDepotDao.getByModel(outDepotModel);
        if (existOutModel == null) {
            LOGGER.error("订单号："+orderId + "对应的调拨出库单不存在");
            throw new RuntimeException("订单号："+orderId + "对应的调拨出库单不存在");
        }

        if (DERP_ORDER.TRANSFEROUTDEPOT_STATUS_016.equals(existOutModel.getStatus()) ||
                DERP_ORDER.TRANSFEROUTDEPOT_STATUS_027.equals(existOutModel.getStatus())) {
            LOGGER.error("订单号："+orderId + "对应的调拨出库单已出库");
            throw new RuntimeException("订单号："+orderId + "对应的调拨出库单出库中/已出库");
        }

        // 根据仓库id到mongoDB中查询 仓库信息
        Map<String, Object> outDepotInfoMap = new HashMap<>();
        outDepotInfoMap.put("depotId", transferOrderModel.getOutDepotId());// 调出仓库id
        DepotInfoMongo outDepotInfoMongo = depotInfoMongoDao.findOne(outDepotInfoMap);// 调入仓库信息
        if(outDepotInfoMongo == null) {
            LOGGER.error("未查到调出仓库信息,订单编号" + orderId);
            throw new RuntimeException("未查到调出仓库信息,订单编号" + orderId);
        }
        // 根据仓库id到mongoDB中查询 仓库信息
        Map<String, Object> inDepotInfoMap = new HashMap<>();
        inDepotInfoMap.put("depotId", transferOrderModel.getInDepotId());// 调入仓库id
        DepotInfoMongo inDepotInfoMongo = depotInfoMongoDao.findOne(inDepotInfoMap);// 调入仓库信息
        if(inDepotInfoMongo == null) {
            LOGGER.error("未查到调入仓库信息,订单编号" + orderId);
            throw new RuntimeException("未查到调入仓库信息,订单编号" + orderId);
        }

        Map<String, Object> inRelDepotParam = new HashMap<>();
        inRelDepotParam.put("merchantId", merchantId);
        inRelDepotParam.put("depotId", inDepotInfoMongo.getDepotId());
        DepotMerchantRelMongo inRelDepot = depotMerchantRelMongoDao.findOne(inRelDepotParam);
        if (inRelDepot == null) {
            LOGGER.error("该商家下未查到调入仓库信息,订单编号" + orderId);
            throw new RuntimeException("该商家下未查到调入仓库信息,订单编号" + orderId);
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("merchantId", merchantId) ;
        MerchantInfoMongo merchant = merchantMongoDao.findOne(params);

        TransferOrderItemModel itemModel = new TransferOrderItemModel();
        itemModel.setTransferOrderId(transferOrderModel.getId());
        List<TransferOrderItemModel> orderItemModels = transferOrderItemDao.list(itemModel);

        TransferOutDepotItemModel outDepotItemModel = new TransferOutDepotItemModel();
        outDepotItemModel.setTransferDepotId(existOutModel.getId());
        List<TransferOutDepotItemModel> outDepotItemModels = transferOutDepotItemDao.list(outDepotItemModel);

        //更新调拨出库单状态
        TransferOutDepotModel updateOutModel = new TransferOutDepotModel();
        updateOutModel.setId(existOutModel.getId());
        updateOutModel.setStatus(DERP_ORDER.TRANSFEROUTDEPOT_STATUS_027);
        updateOutModel.setTransferDate(TimeUtils.parseFullTime(updateTime));
        transferOutDepotDao.modify(updateOutModel);

        List<InventoryFreezeGoodsListJson> freezeGoodList = new ArrayList<InventoryFreezeGoodsListJson>();//释放冻结商品列表
        List<InvetAddOrSubtractGoodsListJson> subGoodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();//减库存商品

        //释放冻结库存
        for(TransferOrderItemModel transferOrderItemModel : orderItemModels) {
            InventoryFreezeGoodsListJson fgood = new InventoryFreezeGoodsListJson();
            fgood.setGoodsId(String.valueOf(transferOrderItemModel.getOutGoodsId()));
            fgood.setGoodsName(transferOrderItemModel.getOutGoodsName());
            fgood.setGoodsNo(transferOrderItemModel.getOutGoodsNo());
            fgood.setDivergenceDate(updateTime);
            fgood.setNum(transferOrderItemModel.getTransferNum());
            freezeGoodList.add(fgood);
        }

        //释放冻结库存
        InventoryFreezeRootJson freezeAddLower = new InventoryFreezeRootJson();
        freezeAddLower.setMerchantId(String.valueOf(merchantId));
        freezeAddLower.setMerchantName(transferOrderModel.getMerchantName());
        freezeAddLower.setDepotId(String.valueOf(transferOrderModel.getOutDepotId()));
        freezeAddLower.setDepotName(transferOrderModel.getOutDepotName());
        freezeAddLower.setOrderNo(transferOrderModel.getCode());// 解冻取调拨出库单号
        freezeAddLower.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009); //DBDD("009","调拨订单"),
        freezeAddLower.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0011); //DBCK("0011","调拨出库"),
        freezeAddLower.setSourceDate(TimeUtils.formatFullTime());
        freezeAddLower.setOperateType(DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_1);//冻增\冻减	字符串 （0冻结，1解冻）
        freezeAddLower.setBusinessNo(transferOrderModel.getCode());
        freezeAddLower.setGoodsList(freezeGoodList);
        JSONObject jsonFree = JSONObject.fromObject(freezeAddLower);

        //拼装减库存接口参数
        for(TransferOutDepotItemModel transferOutDepotItemModel : outDepotItemModels) {
            //计算是否过期 字符串 （0 是 1 否）
            String isExpire = DERP.ISEXPIRE_1;
            if(transferOutDepotItemModel.getOverdueDate()!=null){
                isExpire = TimeUtils.isNotIsExpireByDate(transferOutDepotItemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
            }

            if (transferOutDepotItemModel.getTransferNum() != null && transferOutDepotItemModel.getTransferNum() > 0) {
                InvetAddOrSubtractGoodsListJson good = new InvetAddOrSubtractGoodsListJson();
                good.setGoodsId(String.valueOf(transferOutDepotItemModel.getOutGoodsId()));
                good.setGoodsNo(transferOutDepotItemModel.getOutGoodsNo());
                good.setGoodsName(transferOutDepotItemModel.getOutGoodsName());
                good.setBarcode(transferOutDepotItemModel.getBarcode());
                good.setBatchNo(transferOutDepotItemModel.getTransferBatchNo());
                good.setProductionDate(TimeUtils.formatDay(transferOutDepotItemModel.getProductionDate()));
                good.setOverdueDate(TimeUtils.formatDay(transferOutDepotItemModel.getOverdueDate()));
                good.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//商品分类 （0 好品 1坏品）	字符串
                good.setIsExpire(isExpire);//是否过期（0 是 1 否）
                good.setNum(transferOutDepotItemModel.getTransferNum());
                good.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//商品分类 （0 好品 1坏品）	字符串
                good.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//操作类型（调增\调减）	字符串 0 调减 1调增
                good.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
                good.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
                subGoodsList.add(good);
            }

            if (transferOutDepotItemModel.getWornNum() != null && transferOutDepotItemModel.getWornNum() > 0) {
                InvetAddOrSubtractGoodsListJson good = new InvetAddOrSubtractGoodsListJson();
                good.setGoodsId(String.valueOf(transferOutDepotItemModel.getOutGoodsId()));
                good.setGoodsNo(transferOutDepotItemModel.getOutGoodsNo());
                good.setGoodsName(transferOutDepotItemModel.getOutGoodsName());
                good.setBarcode(transferOutDepotItemModel.getBarcode());
                good.setBatchNo(transferOutDepotItemModel.getTransferBatchNo());
                good.setProductionDate(TimeUtils.formatDay(transferOutDepotItemModel.getProductionDate()));
                good.setOverdueDate(TimeUtils.formatDay(transferOutDepotItemModel.getOverdueDate()));
                good.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//商品分类 （0 好品 1坏品）	字符串
                good.setIsExpire(isExpire);//是否过期（0 是 1 否）
                good.setNum(transferOutDepotItemModel.getWornNum());
                good.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);//商品分类 （0 好品 1坏品）	字符串
                good.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//操作类型（调增\调减）	字符串 0 调减 1调增
                good.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
                good.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
                subGoodsList.add(good);
            }
        }
        InvetAddOrSubtractRootJson inventoryRoot = new InvetAddOrSubtractRootJson();
        inventoryRoot.setMerchantId(String.valueOf(transferOrderModel.getMerchantId()));
        inventoryRoot.setMerchantName(String.valueOf(transferOrderModel.getMerchantName()));
        inventoryRoot.setTopidealCode(transferOrderModel.getTopidealCode());
        inventoryRoot.setDepotType(outDepotInfoMongo.getType());
        inventoryRoot.setIsTopBooks(outDepotInfoMongo.getIsTopBooks());
        inventoryRoot.setDepotId(String.valueOf(outDepotInfoMongo.getDepotId()));
        inventoryRoot.setDepotCode(outDepotInfoMongo.getCode());
        inventoryRoot.setDepotName(outDepotInfoMongo.getName());
        inventoryRoot.setOrderNo(existOutModel.getCode());
        inventoryRoot.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009); //DBDD("009","调拨订单"),
        inventoryRoot.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0011); //DBCK("0011","调拨出库"),
        inventoryRoot.setSourceDate(TimeUtils.formatFullTime());
        inventoryRoot.setOwnMonth(updateTime.substring(0,7));
        inventoryRoot.setDivergenceDate(updateTime);
        inventoryRoot.setBusinessNo(transferOrderModel.getCode());
        inventoryRoot.setGoodsList(subGoodsList);
        inventoryRoot.setBuId(String.valueOf(transferOrderModel.getBuId()));
        inventoryRoot.setBuName(transferOrderModel.getBuName());
        //回调参数
        inventoryRoot.setBackTopic(MQPushBackOrderEnum.DB_OUTDEPOT_BACK.getTopic());
        inventoryRoot.setBackTags(MQPushBackOrderEnum.DB_OUTDEPOT_BACK.getTags());
        inventoryRoot.setCustomParam(new HashMap<String, Object>());
        //减库存
        JSONObject subjson = JSONObject.fromObject(inventoryRoot);
        // 把减库存放到最下方防止已出定入出现异常接口报错

        boolean flag = false;
        //调拨入仓仓库是 在对应商家下的“以出定入”标识为“是”的，需进行以出定入（查询调拨入库单是否存在，若存在则不保存调拨入库单）
        //如果调出库仓库为香港仓时，不走以出定入
        TransferInDepotModel existInDepotModel = null;
        if(DERP_SYS.DEPOTMERCHANTREL_ISOUTDEPENDIN_1.equals(inRelDepot.getIsOutDependIn())
            && !DERP_SYS.DEPOTINFO_TYPE_C.equals(inDepotInfoMongo.getType())) {
            TransferInDepotModel inDepotModel = new TransferInDepotModel();
            inDepotModel.setTransferOrderId(transferOrderModel.getId());
            existInDepotModel = transferInDepotDao.searchByModel(inDepotModel);
            if (existInDepotModel != null && DERP_ORDER.TRANSFERINDEPOT_STATUS_011.equals(existInDepotModel.getStatus())) {
                flag = true;
            }
        }

        if (flag) {

            //更新调拨入库单状态
            TransferInDepotModel updateInModel = new TransferInDepotModel();
            updateInModel.setId(existInDepotModel.getId());
            updateInModel.setStatus(DERP_ORDER.TRANSFERINDEPOT_STATUS_028);
            updateInModel.setTransferDate(TimeUtils.parseFullTime(updateTime));
            transferInDepotDao.modify(updateInModel);

            List<InvetAddOrSubtractGoodsListJson> insubGoodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();//加库存商品
            TransferInDepotItemModel transferInDepotItemModel = new TransferInDepotItemModel();
            transferInDepotItemModel.setTransferDepotId(existInDepotModel.getId());
            List<TransferInDepotItemModel> inDepotItemModels = transferInDepotItemDao.list(transferInDepotItemModel);

            for (TransferInDepotItemModel inDepotItemModel : inDepotItemModels) {
                //计算是否过期 字符串 （0 是 1 否）
				String isExpire = DERP.ISEXPIRE_1;
				if(inDepotItemModel.getOverdueDate()!=null){
					isExpire = TimeUtils.isNotIsExpireByDate(inDepotItemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
				}
                //好品
				if(inDepotItemModel.getTransferNum() != null && inDepotItemModel.getTransferNum().intValue() > 0){
					//拼装加库存商品
					InvetAddOrSubtractGoodsListJson aSGood = new InvetAddOrSubtractGoodsListJson();
                    aSGood.setGoodsId(String.valueOf(inDepotItemModel.getInGoodsId()));//原商品
                    aSGood.setGoodsNo(inDepotItemModel.getInGoodsNo());
                    aSGood.setGoodsName(inDepotItemModel.getInGoodsName());
                    aSGood.setBarcode(inDepotItemModel.getBarcode());
					aSGood.setBatchNo(inDepotItemModel.getTransferBatchNo());
					aSGood.setProductionDate(TimeUtils.formatDay(inDepotItemModel.getProductionDate()));
					aSGood.setOverdueDate(TimeUtils.formatDay(inDepotItemModel.getOverdueDate()));
					aSGood.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//商品分类(0 好品 1坏品)字符串
					aSGood.setIsExpire(isExpire);//是否过期(0 是 1 否)
					aSGood.setNum(inDepotItemModel.getTransferNum());
					aSGood.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//操作类型（调增\调减）	字符串 0 调减 1调增
                    aSGood.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
                    aSGood.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
					insubGoodsList.add(aSGood);
				}
				//坏品
				if(inDepotItemModel.getWornNum() != null && inDepotItemModel.getWornNum().intValue() > 0){
					//拼装加库存商品
					InvetAddOrSubtractGoodsListJson aSGood = new InvetAddOrSubtractGoodsListJson();
                    aSGood.setGoodsId(String.valueOf(inDepotItemModel.getInGoodsId()));//原商品
                    aSGood.setGoodsNo(inDepotItemModel.getInGoodsNo());
					aSGood.setBatchNo(inDepotItemModel.getTransferBatchNo());
					aSGood.setProductionDate(TimeUtils.formatDay(inDepotItemModel.getProductionDate()));
					aSGood.setOverdueDate(TimeUtils.formatDay(inDepotItemModel.getOverdueDate()));
					aSGood.setIsExpire(isExpire);//是否过期(0 是 1 否)
					aSGood.setNum(inDepotItemModel.getWornNum());
					aSGood.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);//商品分类 （0 好品 1坏品）	字符串
					aSGood.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//操作类型（调增\调减）	字符串 0 调减 1调增
                    aSGood.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
                    aSGood.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
					insubGoodsList.add(aSGood);
				}
            }
            InvetAddOrSubtractRootJson addInventoryRoot = new InvetAddOrSubtractRootJson();
            addInventoryRoot.setMerchantId(String.valueOf(transferOrderModel.getMerchantId()));
            addInventoryRoot.setMerchantName(transferOrderModel.getMerchantName());
            addInventoryRoot.setTopidealCode(transferOrderModel.getTopidealCode());
            addInventoryRoot.setDepotType(inDepotInfoMongo.getType());
            addInventoryRoot.setIsTopBooks(inDepotInfoMongo.getIsTopBooks());
            addInventoryRoot.setDepotId(String.valueOf(inDepotInfoMongo.getDepotId()));
            addInventoryRoot.setDepotCode(inDepotInfoMongo.getCode());
            addInventoryRoot.setDepotName(inDepotInfoMongo.getName());
            addInventoryRoot.setOrderNo(existInDepotModel.getCode());
            addInventoryRoot.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009);//DBDD("009","调拨订单"),
            addInventoryRoot.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0010); //DBRK("0010","调拨入库"),
            addInventoryRoot.setSourceDate(TimeUtils.formatFullTime());
            addInventoryRoot.setOwnMonth(updateTime.substring(0, 7));
            addInventoryRoot.setDivergenceDate(updateTime);
            addInventoryRoot.setBusinessNo(transferOrderModel.getCode());
            addInventoryRoot.setGoodsList(insubGoodsList);
            addInventoryRoot.setBuId(String.valueOf(transferOrderModel.getBuId()));
            addInventoryRoot.setBuName(transferOrderModel.getBuName());
            //回调参数
            addInventoryRoot.setBackTopic(MQPushBackOrderEnum.DB_INDEPOT_BACK.getTopic());
            addInventoryRoot.setBackTags(MQPushBackOrderEnum.DB_INDEPOT_BACK.getTags());
            addInventoryRoot.setCustomParam(new HashMap<String, Object>());
            //加库存
            JSONObject addSubjson = JSONObject.fromObject(addInventoryRoot);
            rocketMQProducer.send(addSubjson.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
        }
        // 把释放冻结存放到最下方防止已出定入出现异常接口报错
        rocketMQProducer.send(jsonFree.toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
        // 把减库存放到最下方防止已出定入出现异常接口报错
        rocketMQProducer.send(subjson.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
        return true;
    }
}
