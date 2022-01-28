package com.topideal.service;

import java.sql.SQLException;

/**
 *
 * 主服务、业务库、仓储库、库存库>报表库 数据同步检查预警
 */
public interface SynCheckService {


	 String synCheck(String json, String keys, String topics, String tags) throws Exception;
}
