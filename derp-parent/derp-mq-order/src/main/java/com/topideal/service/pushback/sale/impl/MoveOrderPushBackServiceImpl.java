package com.topideal.service.pushback.sale.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.sale.BuMoveInventoryDao;
import com.topideal.entity.vo.sale.BuMoveInventoryModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.pushback.sale.MoveOrderPushBackService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 移库单商品收发明细生成成功回推
 **/
@Service
public class MoveOrderPushBackServiceImpl implements MoveOrderPushBackService {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(MoveOrderPushBackServiceImpl.class);

    @Autowired
    private BuMoveInventoryDao buMoveInventoryDao;

    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201157500,model=DERP_LOG_POINT.POINT_13201157500_Label,keyword="code")
    public boolean updateMoveOrderStatus(String json, String keys, String topics, String tags) throws Exception {
        Thread.sleep(5000);
        //获取json对象
        JSONObject jsonData = JSONObject.fromObject(json);
        Map classMap = new HashMap();
        BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
        Map<String, Object> customParam = rootJson.getCustomParam();
        String code =  (String) customParam.get("code");
        BuMoveInventoryModel model= new BuMoveInventoryModel();
        model.setCode(code);
        model = buMoveInventoryDao.searchByModel(model);
        if (model==null) {
            LOGGER.error(DERP.MQ_FAILTYPE_04 + "根据移库单单号没有查到移库单:code"+code);
            throw new  RuntimeException(DERP.MQ_FAILTYPE_04 + "根据移库单单号没有查到移库单:code"+code);
        }
        if (!DERP_ORDER.BUMOVEINVENTORY_STATUS_027.equals(model.getStatus())) {
            LOGGER.error("移库单号:code"+code+"不是“移库中”状态");
            throw new  RuntimeException("移库单号:code"+code+"不是“移库中”状态");
        }
        // 修改移库单状态为已移库
        BuMoveInventoryModel buMoveInventoryModel = new BuMoveInventoryModel();
        buMoveInventoryModel.setId(model.getId());
        buMoveInventoryModel.setStatus(DERP_ORDER.BUMOVEINVENTORY_STATUS_018);
        buMoveInventoryDao.modify(buMoveInventoryModel);
        return true;
    }
}
