package com.topideal.mongo.dao;

import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.tools.BaseUtils;

import java.util.List;
import java.util.Map;

/**
 * 文件任务
 */
public interface FileTaskMongoDao extends MongoDao<FileTaskMongo> {

    public void insertJson(String json);

    public List<FileTaskMongo> findAllOrderByDate(Map<String, Object> params);

    public PageMongo<FileTaskMongo> findAllByPage(Map<String, Object> params, Integer begin, Integer pageSize);

    /**
     * 查询mongo数据（返回所有数据）
     *
     * @param params 查询条件 -- Operator(操作类型),map(key:属性名 vaule:值)
     */
    public List<FileTaskMongo> findAllInParams(Map<BaseUtils.Operator, Map<String, Object>> params);
}
