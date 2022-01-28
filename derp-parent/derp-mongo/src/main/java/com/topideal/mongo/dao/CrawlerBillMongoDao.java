package com.topideal.mongo.dao;

import java.util.List;
import java.util.Map;

import com.topideal.mongo.entity.CrawlerBillMongo;
import com.topideal.mongo.tools.PageMongo;

/**
 * 爬虫账单生成出库清单日志Dao
 *
 */
public interface CrawlerBillMongoDao extends MongoDao<CrawlerBillMongo>{

	public void insertJson(String json);
	/**
     * 查询所有记录  条件查询
     * @param params
     * @param collectionName
     */
	List findAll(Map<String, Object> params, Map<String, String> sort, String collectionName);
	/**
     * 查询所有记录  条件查询  排序  分页
     * @param params
     * @param collectionName
     */
    public PageMongo findAll(Map<String, Object> params, Map<String, String> sort, PageMongo pageMongo, String collectionName);
}
