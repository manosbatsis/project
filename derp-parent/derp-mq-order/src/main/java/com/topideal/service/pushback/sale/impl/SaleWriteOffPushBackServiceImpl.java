package com.topideal.service.pushback.sale.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.*;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.pushback.sale.SaleWriteOffPushBackService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleWriteOffPushBackServiceImpl implements SaleWriteOffPushBackService {
    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(SaleWriteOffPushBackServiceImpl.class);
    @Autowired
    private SaleOrderDao saleOrderDao;	// 销售订单
    @Autowired
    private SaleOutDepotDao saleOutDepotDao;	// 销售出库
    @Autowired
    private SaleShelfIdepotDao saleShelfIdepotDao;// 销售上架入库
    @Autowired
    private ShelfDao shelfDao;   // 销售上架
    @Autowired
    private SaleShelfDao saleShelfDao;   // 销售上架表体
    @Autowired
    private SaleSdOrderDao saleSdOrderDao;   // 销售SD单
    @Autowired
    private SaleSdOrderItemDao saleSdOrderItemDao;   // 销售SD单表体
    @Autowired
    private RMQProducer rocketMQProducer;

    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_20100000011, model = DERP_LOG_POINT.POINT_20100000011_Label,keyword="code")
    public void modifyStatus(String json, String keys, String topics, String tags) throws Exception {
        // 实例化json对象
        JSONObject jsonData = JSONObject.fromObject(json);
        // JSON对象转实体
        BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class);
        //红冲单号
        String code = rootJson.getCode();
        Map<String, Object> customParam = rootJson.getCustomParam();
        String orderType =  (String) customParam.get("orderType");	//红冲类型
        Long saleOrderId = Long.valueOf((String) customParam.get("saleOrderId"));	//销售单号
        Timestamp writeOffDate = null;
        //1-销售出库单 2-上架入库单
        if("1".equals(orderType)){
            // 根据销售出库单号查询销售出库清单
            SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
            saleOutDepotModel.setCode(code);
            saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepotModel);
            if (saleOutDepotModel == null) {
                LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售出库清单不存在,订单号code" + code);
                throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售出库清单不存在,订单号code" + code);
            }
            if (null == saleOutDepotModel.getBuId()) {
                LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售出库清单单号code" + code+"事业部信息值为空");
                throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售出库清单单号code" + code+"事业部信息值为空");
            }
            SaleOutDepotModel originSaleOutModel = saleOutDepotDao.searchById(saleOutDepotModel.getOriginalSaleOutOrderId());
            if(originSaleOutModel == null){
                LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售出库单关联源单号不存在,订单号code" + saleOutDepotModel.getOriginalSaleOutOrderCode());
                throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售出库单关联源单号不存在,订单号code" + saleOutDepotModel.getOriginalSaleOutOrderCode());
            }

            SaleOutDepotModel updateOutDepotModel = new SaleOutDepotModel();
            updateOutDepotModel.setId(saleOutDepotModel.getId());
            updateOutDepotModel.setStatus(originSaleOutModel.getStatus());
            saleOutDepotDao.modify(updateOutDepotModel);

            writeOffDate = saleOutDepotModel.getDeliverDate();

        }else if("2".equals(orderType)){
            // 上架入库单
            SaleShelfIdepotModel saleShelfIdepotModel = new SaleShelfIdepotModel();
            saleShelfIdepotModel.setCode(code);
            saleShelfIdepotModel = saleShelfIdepotDao.searchByModel(saleShelfIdepotModel);
            if (saleShelfIdepotModel == null) {
                LOGGER.error(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售上架入库单,上架入库单号:code" + code);
                throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售上架入库单,上架入库单号:code" + code);
            }
            SaleShelfIdepotModel originSaleShelfIdepotModel = saleShelfIdepotDao.searchById(saleShelfIdepotModel.getOriginalShelfIdepotId());
            if(originSaleShelfIdepotModel == null){
                LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售上架入库单关联源单号不存在,订单号code" + saleShelfIdepotModel.getOriginalShelfIdepotCode());
                throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售上架入库单关联源单号不存在,订单号code" + saleShelfIdepotModel.getOriginalShelfIdepotCode());
            }

            SaleShelfIdepotModel updateSaleShelfIdepotModel = new SaleShelfIdepotModel();
            updateSaleShelfIdepotModel.setId(saleShelfIdepotModel.getId());
            updateSaleShelfIdepotModel.setState(originSaleShelfIdepotModel.getState());
            saleShelfIdepotDao.modify(updateSaleShelfIdepotModel);

            writeOffDate = saleShelfIdepotModel.getShelfDate();
        }
        SaleOrderModel saleOrderModel = saleOrderDao.searchById(saleOrderId);
        if (saleOrderModel == null) {
            LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售订单不存在,订单编号code:" + code);
            throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售订单不存在,订单编号code:" + code);
        }
        if(DERP_ORDER.SALEORDER_WRITEOFF_STATUS_003.equals(saleOrderModel.getWriteOffStatus())){
            LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售订单code:" + code+"已红冲");
            throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售订单code:" + code+"已红冲");
        }
        //判断是否的红冲单全部除入库完成
        //1、判断销售订单对应的红冲出库单，是否还存在出库中状态的单据
        boolean isAllOut = true;
        SaleOutDepotModel queryOutDepotModel = new SaleOutDepotModel();
        queryOutDepotModel.setSaleOrderId(saleOrderId);
        queryOutDepotModel.setStatus(DERP_ORDER.SALEOUTDEPOT_STATUS_027);
        queryOutDepotModel.setIsWriteOff(DERP_ORDER.SALEOUTDEPOT_ISWRITEOFF_1);
        List<SaleOutDepotModel> querySaleOutList = saleOutDepotDao.list(queryOutDepotModel);
        if(querySaleOutList != null && querySaleOutList.size() > 0){
            isAllOut = false;
        }
        //2、判断销售订单对应的红冲上架入库单，是否还存在入仓中状态的单据
        boolean isAllShelf = true;
        SaleShelfIdepotModel querySaleShelfIdepotModel = new SaleShelfIdepotModel();
        querySaleShelfIdepotModel.setSaleOrderId(saleOrderId);
        querySaleShelfIdepotModel.setState(DERP_ORDER.SALESHELFIDEPOT_STATUS_028);
        querySaleShelfIdepotModel.setIsWriteOff(DERP_ORDER.SALEOUTDEPOT_ISWRITEOFF_1);
        List<SaleShelfIdepotModel> querySaleShelfIdepotList = saleShelfIdepotDao.list(querySaleShelfIdepotModel);
        if(querySaleShelfIdepotList != null && querySaleShelfIdepotList.size() > 0){
            isAllShelf = false;
        }
        if(isAllOut && isAllShelf){
            List<String> shelfCodes = new ArrayList<>();
            //1、若有上架单，生成红冲上架单
            ShelfModel queryShelfModel = new ShelfModel();
            queryShelfModel.setSaleOrderId(saleOrderId);
            List<ShelfModel> queryShelfList = shelfDao.list(queryShelfModel);
            if(queryShelfList != null && queryShelfList.size() > 0){
                Map<Long, Long> newIdByOriginalId = new HashMap<>();
                for(ShelfModel shelfModel : queryShelfList){
                    // 根据原单号找红冲单
                    SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
                    saleOutDepotModel.setOriginalSaleOutOrderCode(shelfModel.getSaleOutDepotCode());
                    saleOutDepotModel.setIsWriteOff(DERP_ORDER.SALEOUTDEPOT_ISWRITEOFF_1);
                    saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepotModel);

                    Long originOrderId = shelfModel.getId();
                    String originalOrderCode = shelfModel.getCode();

                    shelfModel.setId(null);
                    shelfModel.setCode(SmurfsUtils.getID(DERP.UNIQUE_ID_SJD));
                    shelfModel.setShelfDate(writeOffDate);
                    shelfModel.setIsWriteOff(DERP_ORDER.SALEOUTDEPOT_ISWRITEOFF_1);
                    shelfModel.setCreateDate(TimeUtils.getNow());
                    shelfModel.setOriginalShelfId(originOrderId);
                    shelfModel.setOriginalShelfCode(originalOrderCode);
                    shelfModel.setSaleOutDepotId(saleOutDepotModel.getId());
                    shelfModel.setSaleOutDepotCode(saleOutDepotModel.getCode());

                    Long num = shelfDao.save(shelfModel);

                    newIdByOriginalId.put(originOrderId, num);
                    shelfCodes.add(shelfModel.getCode());
                }

                SaleShelfModel querySaleShelfModel = new SaleShelfModel();
                querySaleShelfModel.setOrderId(saleOrderId);
                List<SaleShelfModel> querySaleShelfList = saleShelfDao.list(querySaleShelfModel);
                for(SaleShelfModel item : querySaleShelfList){
                    // 根据原单号找红冲单
                    SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
                    saleOutDepotModel.setOriginalSaleOutOrderId(item.getSaleOutDepotId());
                    saleOutDepotModel.setIsWriteOff(DERP_ORDER.SALEOUTDEPOT_ISWRITEOFF_1);
                    saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepotModel);

                    item.setId(null);
                    item.setShelfId(newIdByOriginalId.get(item.getShelfId()));
                    item.setShelfNum((item.getShelfNum() == null ? 0 : item.getShelfNum()) * (-1));
                    item.setDamagedNum((item.getDamagedNum() == null ? 0 : item.getDamagedNum()) * (-1));
                    item.setLackNum((item.getLackNum() == null ? 0 : item.getLackNum())* (-1));
                    item.setCreateDate(TimeUtils.getNow());
                    item.setShelfDate(writeOffDate);
                    item.setSaleOutDepotId(saleOutDepotModel.getId());

                    saleShelfDao.save(item);
                }

                //4、更新上架入库单的上架单id
                for(Long oldShelfId : newIdByOriginalId.keySet()){
                    Long newShelfId = newIdByOriginalId.get(oldShelfId);
                    //查询上架入库单
                    SaleShelfIdepotModel queryShelfIdepotModel = new SaleShelfIdepotModel();
                    queryShelfIdepotModel.setSaleOrderId(saleOrderId);
                    queryShelfIdepotModel.setSaleShelfId(oldShelfId);
                    queryShelfIdepotModel.setIsWriteOff(DERP_ORDER.SALESHELFIDEPOT_ISWRITEOFF_1);
                    List<SaleShelfIdepotModel> shelfIdepotList = saleShelfIdepotDao.list(queryShelfIdepotModel);
                    if(shelfIdepotList !=null && shelfIdepotList.size() > 0){
                        for(SaleShelfIdepotModel saveShelfIdepot : shelfIdepotList){
                            saveShelfIdepot.setSaleShelfId(newShelfId);
                            saleShelfIdepotDao.modify(saveShelfIdepot);
                        }
                    }
                }
            }
            //2、若有SD单，生成红冲SD单
            SaleSdOrderModel querySaleSdModel = new SaleSdOrderModel();
            querySaleSdModel.setBusinessId(saleOrderId);
            querySaleSdModel.setState("001");
            List<SaleSdOrderModel> querySaleSdList = saleSdOrderDao.list(querySaleSdModel);
            if(querySaleSdList != null && querySaleSdList.size() > 0){
                for(SaleSdOrderModel saleSdModel : querySaleSdList ){
                    // 根据原单号找红冲单
                    ShelfModel shelfModel = new ShelfModel();
                    shelfModel.setOriginalShelfId(saleSdModel.getOrderId());
                    shelfModel.setIsWriteOff(DERP_ORDER.SALEOUTDEPOT_ISWRITEOFF_1);
                    shelfModel = shelfDao.searchByModel(shelfModel);

                    Long originOrderId = saleSdModel.getId();
                    String originalOrderCode = saleSdModel.getCode();

                    saleSdModel.setId(null);
                    saleSdModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSSD));
                    saleSdModel.setOwnDate(writeOffDate);
                    saleSdModel.setIsWriteOff(DERP_ORDER.SALEOUTDEPOT_ISWRITEOFF_1);
                    saleSdModel.setTotalSdNum((saleSdModel.getTotalSdNum()== null ? 0 : saleSdModel.getTotalSdNum())*(-1));
                    saleSdModel.setTotalSdAmount((saleSdModel.getTotalSdAmount()== null ? BigDecimal.ZERO : saleSdModel.getTotalSdAmount()).multiply(new BigDecimal(-1)));
                    saleSdModel.setCreateDate(TimeUtils.getNow());
                    saleSdModel.setOriginalSaleSdOrderId(originOrderId);
                    saleSdModel.setOriginalSaleSdOrderCode(originalOrderCode);
                    saleSdModel.setOrderId(shelfModel.getId());
                    saleSdModel.setOrderCode(shelfModel.getCode());

                    Long num = saleSdOrderDao.save(saleSdModel);

                    SaleSdOrderItemModel querySaleSdItemModel = new SaleSdOrderItemModel();
                    querySaleSdItemModel.setSaleSdOrderId(originOrderId);
                    List<SaleSdOrderItemModel> querySaleSdItemList = saleSdOrderItemDao.list(querySaleSdItemModel);
                    for(SaleSdOrderItemModel item : querySaleSdItemList){
                        item.setId(null);
                        item.setSaleSdOrderId(num);
                        item.setAmount((item.getAmount()== null ? BigDecimal.ZERO : item.getAmount()).multiply(new BigDecimal(-1)));
                        item.setNum((item.getNum() == null ? 0 : item.getNum()) * (-1));
                        item.setSdAmount((item.getSdAmount()== null ? BigDecimal.ZERO : item.getSdAmount()).multiply(new BigDecimal(-1)));
                        item.setCreateDate(TimeUtils.getNow());

                        saleSdOrderItemDao.save(item);
                    }
                }
            }
            if(shelfCodes.size() > 0){
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("orderCodes", StringUtils.join(shelfCodes, ","));
                map.put("dataSource", DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_1);
                rocketMQProducer.send(JSONObject.fromObject(map).toString(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTopic(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTags());
            }

            //3、修改销售订单状态
            SaleOrderModel updateSaleOrderModel = new SaleOrderModel();
            updateSaleOrderModel.setId(saleOrderId);
            updateSaleOrderModel.setWriteOffStatus(DERP_ORDER.SALEORDER_WRITEOFF_STATUS_003);
            updateSaleOrderModel.setWriteOffDate(writeOffDate);
            updateSaleOrderModel.setModifyDate(TimeUtils.getNow());
            saleOrderDao.modify(updateSaleOrderModel);


        }

    }
}
