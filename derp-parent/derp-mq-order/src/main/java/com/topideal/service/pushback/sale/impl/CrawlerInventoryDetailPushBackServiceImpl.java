package com.topideal.service.pushback.sale.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.sale.BillOutinDepotBatchDao;
import com.topideal.dao.sale.BillOutinDepotDao;
import com.topideal.entity.vo.sale.BillOutinDepotBatchModel;
import com.topideal.entity.vo.sale.BillOutinDepotModel;
import com.topideal.json.inventory.j05.InventoryDetailBackJson;
import com.topideal.json.inventory.j05.InventoryDetailJson;
import com.topideal.service.pushback.sale.CrawlerInventoryDetailPushBackService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 爬虫出库单库存扣减回推商品批次信息
 * @Author: Chen Yiluan
 * @Date: 2020/03/31 11:47
 **/
@Service
public class CrawlerInventoryDetailPushBackServiceImpl implements CrawlerInventoryDetailPushBackService {

    /* 打印日志 */
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerInventoryDetailPushBackServiceImpl.class);
    @Autowired
    private BillOutinDepotDao billOutinDepotDao;
    @Autowired
    private BillOutinDepotBatchDao billOutinDepotBatchDao;

    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201156500,model=DERP_LOG_POINT.POINT_13201156500_Label,keyword="code")
    public boolean saveBillBatch(String json, String keys, String topics, String tags) throws Exception {
        JSONObject jsonData = JSONObject.fromObject(json);
        Map classMap = new HashMap();
        classMap.put("detailJsons", InventoryDetailJson.class);
        InventoryDetailBackJson rootJson = (InventoryDetailBackJson) JSONObject.toBean(jsonData, InventoryDetailBackJson.class,classMap);
        String code =  rootJson.getCode();
        List<InventoryDetailJson> inventoryDetailJsons = rootJson.getDetailJsons();
        BillOutinDepotModel billOutinDepotModel = new BillOutinDepotModel();
        billOutinDepotModel.setCode(code);
        BillOutinDepotModel outinDepotModel = billOutinDepotDao.searchByModel(billOutinDepotModel);
        if (outinDepotModel == null) {
            LOGGER.error(DERP.MQ_FAILTYPE_04 + "没有查询到对应的账单出入库单,单号:" + code);
            throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "没有查询到对应的账单出入库单,单号:" + code);
        }

        if (!DERP_ORDER.BILLOUTINDEPOT_STATE_01.equals(outinDepotModel.getState())) {
            LOGGER.error("账单出入库单非“处理中”状态,单号:" + code);
            throw new RuntimeException("账单出入库单非“处理中”状态,单号:" + code);
        }

        BillOutinDepotBatchModel billOutinDepotBatchModel = new BillOutinDepotBatchModel();
        billOutinDepotBatchModel.setOutinDepotId(outinDepotModel.getId());
        List<BillOutinDepotBatchModel> billOutinDepotBatchModels = billOutinDepotBatchDao.list(billOutinDepotBatchModel);
        if (billOutinDepotBatchModels != null && billOutinDepotBatchModels.size() > 0) {
            LOGGER.error("账单出入库单商品批次信息已存在,单号:" + code);
            return true;
        }

        if (inventoryDetailJsons == null || inventoryDetailJsons.size() == 0) {
            LOGGER.error("账单出入库单没有找到对应商品批次信息,单号:" + code);
            throw new RuntimeException("账单出入库单没有找到对应商品批次信息,单号:" + code);
        }

        //保存账单出库单商品批次信息
        for (InventoryDetailJson detailJson : inventoryDetailJsons) {
            BillOutinDepotBatchModel batchModel = new BillOutinDepotBatchModel();
            batchModel.setOutinDepotId(outinDepotModel.getId());
            batchModel.setGoodsId(detailJson.getGoodsId());
            batchModel.setGoodsNo(detailJson.getGoodsNo());
            batchModel.setGoodsName(detailJson.getGoodsName());
            batchModel.setCommbarcode(detailJson.getCommbarcode());
            batchModel.setBatchNo(detailJson.getBatchNo());
            batchModel.setOverdueDate(detailJson.getOverdueDate());
            batchModel.setProductionDate(detailJson.getProductionDate());
            batchModel.setNum(detailJson.getNum());
            billOutinDepotBatchDao.save(batchModel);
        }

        BillOutinDepotModel model = new BillOutinDepotModel();
        model.setId(outinDepotModel.getId());
        //修改账单出入库单状态
        if (outinDepotModel.getOperateType().equals(DERP_ORDER.BILLOUTINDEPOT_OPERATE_TYPE_0)) {
            model.setState(DERP_ORDER.BILLOUTINDEPOT_STATE_02);
        } else if (outinDepotModel.getOperateType().equals(DERP_ORDER.BILLOUTINDEPOT_OPERATE_TYPE_1)) {
            model.setState(DERP_ORDER.BILLOUTINDEPOT_STATE_03);
        }
        billOutinDepotDao.modify(model);
        return true;
    }
}
