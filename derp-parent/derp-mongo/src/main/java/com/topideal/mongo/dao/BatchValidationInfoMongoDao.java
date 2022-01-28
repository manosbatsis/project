package com.topideal.mongo.dao;

import java.util.List;
import java.util.Map;

import com.topideal.mongo.entity.BatchValidationInfoMongo;
import com.topideal.tools.BaseUtils.Operator;

/**
 * Created by weixiaolei on 2018/7/4.
 */
public interface BatchValidationInfoMongoDao extends MongoDao<BatchValidationInfoMongo>{

    public void insertJson(String json);
    /**
     * 查询mongo数据（返回所有数据）
     * @param params 查询条件  -- Operator(操作类型),map(key:属性名 vaule:值)
     * @param sortList 排序 -- map(derection：排序方式；  property：排序字段)
     */
	List<BatchValidationInfoMongo> findAll(Map<Operator, Map<String, Object>> params, List<Map<String, String>> sortList);
	/**
     * 查询mongo数据（返回一条记录）
     * @param params 查询条件  -- Operator(操作类型),map(key:属性名 vaule:值)
     * @param sortList 排序 -- map(derection：排序方式；  property：排序字段)
     */
	BatchValidationInfoMongo findOne(Map<Operator, Map<String, Object>> params, List<Map<String, String>> sortList);

}
