package com.topideal.mongo.dao;

import java.util.List;
import java.util.Map;

import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.tools.BaseUtils.Operator;

/**
 * Created by weixiaolei on 2018/7/4.
 */
public interface CustomerInfoMongoDao extends MongoDao<CustomerInfoMogo>{

    public void insertJson(String json);

    /**
     * 查询mongo数据
     * @param params 查询条件  -- Operator(操作类型),map(key:属性名 vaule:值)
     */
    public List<CustomerInfoMogo> findAllCustomers(Map<Operator, Map<String, Object>> params);
}
