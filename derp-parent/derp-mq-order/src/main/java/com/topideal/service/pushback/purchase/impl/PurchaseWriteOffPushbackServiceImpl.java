package com.topideal.service.pushback.purchase.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.*;
import com.topideal.entity.vo.purchase.*;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.pushback.purchase.PurchaseWriteOffPushbackService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseWriteOffPushbackServiceImpl implements PurchaseWriteOffPushbackService {

    @Autowired
    private PurchaseWarehouseDao purchaseWarehouseDao;
    @Autowired
    private PurchaseOrderDao purchaseOrderDao;
    @Autowired
    private WarehouseOrderRelDao warehouseOrderRelDao;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseReturnOdepotPushbackServiceImpl.class);

    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_13201153500, model = DERP_LOG_POINT.POINT_13201153500_Label, keyword = "code")
    public void modifyStatus(String json, String keys, String topics, String tags) throws Exception {

        // 实例化json对象
        JSONObject jsonData = JSONObject.fromObject(json);
        // JSON对象转实体
        BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class);

        PurchaseWarehouseModel purchaseWarehouseModel = new PurchaseWarehouseModel();
        purchaseWarehouseModel.setCode(rootJson.getCode());

        PurchaseWarehouseModel exist = purchaseWarehouseDao.searchByModel(purchaseWarehouseModel);

        if (exist == null) {
            LOGGER.error(DERP.MQ_FAILTYPE_04 + "采购入库单不存在"+rootJson.getCode());
            throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "采购入库单不存在"+rootJson.getCode());
        }

        //采购入库单关联采购单
        WarehouseOrderRelModel warehouseOrderRelModel = new WarehouseOrderRelModel();
        warehouseOrderRelModel.setWarehouseId(exist.getId());
        List<WarehouseOrderRelModel> relModels = warehouseOrderRelDao.list(warehouseOrderRelModel);

        if (relModels.isEmpty()) {
            LOGGER.error(DERP.MQ_FAILTYPE_04 + "采购入库单没有关联的入库单"+rootJson.getCode());
            throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "采购入库单没有关联的入库单"+rootJson.getCode());
        }

        if (relModels.size() > 1) {
            LOGGER.error(DERP.MQ_FAILTYPE_04 + "采购入库单关联多个入库单"+rootJson.getCode());
            throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "采购入库单关联多个入库单"+rootJson.getCode());
        }

        PurchaseWarehouseModel updateWarehouseModel = new PurchaseWarehouseModel();
        updateWarehouseModel.setId(exist.getId());
        updateWarehouseModel.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);
        purchaseWarehouseDao.modify(updateWarehouseModel);

        //查询采购订单对应的入库单是否已全部红冲成功，若成功则修改采购订单红冲状态为“已红冲”
        Integer oriNum = warehouseOrderRelDao.countWriteOffNum(relModels.get(0).getPurchaseOrderId(), DERP_ORDER.PURCHASEWAREHOUSE_ISWRITEOFF_0);
        Integer writeOffNum = warehouseOrderRelDao.countWriteOffNum(relModels.get(0).getPurchaseOrderId(), DERP_ORDER.PURCHASEWAREHOUSE_ISWRITEOFF_1);

        if (oriNum.equals(writeOffNum)) {
            PurchaseOrderModel updateModel = new PurchaseOrderModel();
            updateModel.setId(relModels.get(0).getPurchaseOrderId());
            updateModel.setWriteOffStatus(DERP_ORDER.PURCHASEORDER_WRITEOFFSTATUS_003);
            updateModel.setModifyDate(TimeUtils.getNow());
            purchaseOrderDao.modify(updateModel);
        }

    }

}
