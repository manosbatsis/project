package com.topideal.service.pushback.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.dao.InventoryDetailsDao;
import com.topideal.entity.vo.InventoryDetailsModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.json.inventory.j05.InventoryDetailBackJson;
import com.topideal.json.inventory.j05.InventoryDetailJson;
import com.topideal.service.pushback.CrawlerOutDepotInventoryLowerPushBackService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 爬虫出库单库存扣减回推
 **/
@Service
public class CrawlerOutDepotInventoryLowerPushBackServiceImpl implements CrawlerOutDepotInventoryLowerPushBackService {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerOutDepotInventoryLowerPushBackServiceImpl.class);

    @Autowired
    private InventoryDetailsDao inventoryDetailsDao;

    @Autowired
    private RMQProducer rocketMQProducer;//MQ;

    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201306400,model=DERP_LOG_POINT.POINT_13201306400_Label,keyword="code")
    public boolean FetchCrawlerOutDepotInfo(String json, String keys, String topics, String tags) throws Exception {
        Thread.sleep(1000L);
        // 实例化json对象
        JSONObject jsonData = JSONObject.fromObject(json);
        // JSON对象转实体
        BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class);

        InventoryDetailsModel inventoryDetailsModel = new InventoryDetailsModel();
        inventoryDetailsModel.setOrderNo(rootJson.getCode());
        List<InventoryDetailsModel> inventoryDetailsModels = inventoryDetailsDao.list(inventoryDetailsModel);
        if(inventoryDetailsModels == null || inventoryDetailsModels.size() == 0){
            LOGGER.error(DERP.MQ_FAILTYPE_04 + "爬虫出库单对应的商品收发明细不存在"+rootJson.getCode());
            throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "爬虫出库单对应的商品收发明细不存在"+rootJson.getCode());
        }
        List<InventoryDetailJson> inventoryDetailJsons = new ArrayList<>();
        for (InventoryDetailsModel detailsModel : inventoryDetailsModels) {
            InventoryDetailJson detailJson = new InventoryDetailJson();
            detailJson.setGoodsId(detailsModel.getGoodsId());
            detailJson.setGoodsNo(detailsModel.getGoodsNo());
            detailJson.setGoodsName(detailsModel.getGoodsName());
            detailJson.setBatchNo(detailsModel.getBatchNo());
            detailJson.setCommbarcode(detailsModel.getCommbarcode());
            if (detailsModel.getOverdueDate() != null) {
                detailJson.setOverdueDate(new Date(detailsModel.getOverdueDate().getTime()));
            }
            if (detailsModel.getProductionDate() != null){
                detailJson.setProductionDate(new Date(detailsModel.getProductionDate().getTime()));
            }
            detailJson.setNum(detailsModel.getNum());
            inventoryDetailJsons.add(detailJson);
        }
        InventoryDetailBackJson inventoryDetails = new InventoryDetailBackJson();
        inventoryDetails.setCode(rootJson.getCode());
        inventoryDetails.setDetailJsons(inventoryDetailJsons);
        inventoryDetails.setMerchantName(inventoryDetailsModels.get(0).getMerchantName());
        JSONObject jsonObject = JSONObject.fromObject(inventoryDetails);
        rocketMQProducer.send(jsonObject.toString(), MQPushBackOrderEnum.CRAWLER_INVENTORY_DETAIL_PUSH_BACK.getTopic(),
                MQPushBackOrderEnum.CRAWLER_INVENTORY_DETAIL_PUSH_BACK.getTags());


        return true;
    }
}
