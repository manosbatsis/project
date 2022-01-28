package com.topideal.mongo.dao;

import java.util.List;
import java.util.Map;

import com.topideal.mongo.tools.PageMongo;

import net.sf.json.JSONObject;

/**MQ 日志记录
 * Created by weixiaolei on 2018/6/8.
 */

public interface JSONMongoDao {
    /**
     * 以json的格式插入mongodb
     * @param json
     * @param collectionName
     */
    public void insertJson(String json, String collectionName);
    /**
     * 查询单个记录
     * @param params
     * @param collectionName
     */
    public JSONObject findOne(Map<String, Object> params, String collectionName);

    /**
     * 查询所有记录
     * @param collectionName
     */
    public List findAll(String collectionName);
    /**
     * 查询所有记录  条件查询
     * @param params
     * @param collectionName
     */
    public List findAll(Map<String, Object> params, String collectionName);
    /**
     * 查询所有记录  条件查询  排序
     * @param params
     * @param collectionName
     */
    public List findAll(Map<String, Object> params, Map<String, String> sort, String collectionName);
    /**
     * 查询所有记录  条件查询  排序  分而
     * @param params
     * @param collectionName
     */
    public PageMongo findAll(Map<String, Object> params, Map<String, String> sort, PageMongo pageMongo, String collectionName);

    /**
     * 修改
     * @param params
     * @param data
     */
    public void update(Map<String, Object> params, Map<String, Object> data, String collectionName);
    
    /**
     * 删除
     * @param params
     */
    public void remove(Map<String, Object> params, String collectionName);
    
    /**
     * 统计数量
     * @param params
     * @param collectionName
     * @return
     */
    public Long count(Map<String, Object> params, String collectionName);

}
