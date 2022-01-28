package com.topideal.service.timer;

import java.sql.SQLException;
import java.text.ParseException;

public interface SavePlatformTempCostOrderService {

    /**
     * 生成平台暂估费用订单
     * @param json
     * @param keys
     * @param topics
     * @param tags
     * @throws SQLException
     * @throws ParseException
     */
    void savePlatformTempCostOrder(String json, String keys, String topics, String tags) throws Exception;

}
