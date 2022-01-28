package com.topideal.service;

import java.sql.SQLException;

/**
 * 维护标准条码中的标准品牌信息——定时器
 * @author
 */
public interface UpdateCommbarcodeBrandService {

    /**
     * 维护标准条码中的标准品牌信息
     * @param json
     * @param keys
     * @param topics
     * @param tags
     * @throws SQLException
     */
    void UpdateCommbarcodeBrand(String json, String keys, String topics, String tags) throws SQLException;

}
