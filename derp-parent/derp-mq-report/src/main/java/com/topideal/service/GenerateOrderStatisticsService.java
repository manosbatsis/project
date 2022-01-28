package com.topideal.service;

import java.sql.SQLException;

/**
 * @Description: 首页—统计电商订单销售总量
 * @Date: 2019-12-24 11:50
 **/
public interface GenerateOrderStatisticsService {

    public void generateOrderStatisticsByShopType(String json) throws SQLException;
}
