package com.topideal.service.timer.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.ReceiveCloseAccountsDao;
import com.topideal.entity.vo.bill.ReceiveCloseAccountsModel;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.service.timer.ReceiveCloseAccountsService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/22 11:17
 * @Description:
 */
@Service
public class ReceiveCloseAccountsServiceImpl implements ReceiveCloseAccountsService {

    @Autowired
    private MerchantBuRelMongoDao merchantBuRelMongoDao;
    @Autowired
    private ReceiveCloseAccountsDao receiveCloseAccountsDao;


    @Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_20100000006, model = DERP_LOG_POINT.POINT_20100000006_Label)
    public void saveReceiveCloseAccounts(String json, String keys, String topics, String tags) throws Exception {
        JSONObject jsonData = JSONObject.fromObject(json);
        String month = jsonData.containsKey("month") ? jsonData.getString("month") : null;
        Integer merchantId = jsonData.containsKey("merchantId")? (Integer) jsonData.get("merchantId") : null;

        if(StringUtils.isBlank(month)) {
            month = TimeUtils.formatMonth(new Date());
        }

        Map<String, Object> params = new HashMap<>();
        params.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);
        ReceiveCloseAccountsModel searchModel = new ReceiveCloseAccountsModel();
        // 获取所有的公司
        if(merchantId != null) {
            // 获取所有的公司
            params.put("merchantId", merchantId.longValue());
        }

        searchModel.setMerchantId(merchantId == null ? null : merchantId.longValue());
        searchModel.setMonth(month);
        List<ReceiveCloseAccountsModel> extistList = receiveCloseAccountsDao.list(searchModel);
        Map<String, Object> closeAccountMap = new HashMap<>();
        extistList.stream().forEach(entity -> {
            String key = entity.getMerchantId() + "-" + entity.getBuId() + "-" + entity.getMonth();
            closeAccountMap.put(key, entity);
        });

        List<MerchantBuRelMongo> allMerchantBuRel = merchantBuRelMongoDao.findAll(params);
        String finalMonth = month;
        List<ReceiveCloseAccountsModel> modelList = new ArrayList<>();
        allMerchantBuRel.stream().filter(entity -> {
            String key = entity.getMerchantId() + "-" + entity.getBuId() + "-" + finalMonth;
           return !closeAccountMap.containsKey(key);
        }).forEach(entity -> {
            ReceiveCloseAccountsModel model = new ReceiveCloseAccountsModel();
            model.setMerchantId(entity.getMerchantId());
            model.setMerchantName(entity.getMerchantName());
            model.setBuId(entity.getBuId());
            model.setBuName(entity.getBuName());
            model.setStatus(DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_029);
            model.setFirstDate(TimeUtils.parse(finalMonth, "yyyy-MM"));
            String lastDayEndTime = TimeUtils.getLastDayEndTime(finalMonth, null);
            model.setEndDate(Timestamp.valueOf(lastDayEndTime));
            model.setMonth(finalMonth);
            modelList.add(model);
        });

        if(modelList != null && !modelList.isEmpty()) {
            receiveCloseAccountsDao.batchSave(modelList);
        }

        extistList.clear();
        modelList.clear();
        closeAccountMap.clear();
    }
}
