package com.topideal.service.pushback.sale.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.sale.InventoryLocationAdjustmentOrderDao;
import com.topideal.entity.vo.sale.InventoryLocationAdjustmentOrderModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.pushback.sale.InventoryLocationAdjustPushBackService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 库位调整单商品收发明细生成成功回推
 **/
@Service
public class InventoryLocationAdjustPushBackServiceImpl implements InventoryLocationAdjustPushBackService {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryLocationAdjustPushBackServiceImpl.class);

    @Autowired
    private InventoryLocationAdjustmentOrderDao adjustmentOrderDao;

    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201159100,model=DERP_LOG_POINT.POINT_13201159100_Label,keyword="code")
    public boolean updateAdjsutOrderStatus(String json, String keys, String topics, String tags) throws Exception {
        Thread.sleep(5000);
        //获取json对象
        JSONObject jsonData = JSONObject.fromObject(json);
        Map classMap = new HashMap();
        BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
        Map<String, Object> customParam = rootJson.getCustomParam();
        String code =  (String) customParam.get("code");

        InventoryLocationAdjustmentOrderModel adjustmentOrderModel = new InventoryLocationAdjustmentOrderModel();
        adjustmentOrderModel.setCode(code);
        adjustmentOrderModel = adjustmentOrderDao.searchByModel(adjustmentOrderModel);

        if (adjustmentOrderModel==null) {
            LOGGER.error(DERP.MQ_FAILTYPE_04 + "根据库位调整单单号没有查到移库单:code"+code);
            throw new  RuntimeException(DERP.MQ_FAILTYPE_04 + "根据库位调整单单号没有查到移库单:code"+code);
        }
        if (!DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_STATUS_027.equals(adjustmentOrderModel.getState())) {
            LOGGER.error("库位调整单号:code"+code+"不是“调整中”状态");
            throw new  RuntimeException("库位调整单号:code"+code+"不是“调整中”状态");
        }
        // 修改库位调整单状态为已审核
        InventoryLocationAdjustmentOrderModel updateModel = new InventoryLocationAdjustmentOrderModel();
        updateModel.setId(adjustmentOrderModel.getId());
        updateModel.setState(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_STATUS_003);//003:已审核
        adjustmentOrderDao.modify(updateModel);
        return true;
    }
}
