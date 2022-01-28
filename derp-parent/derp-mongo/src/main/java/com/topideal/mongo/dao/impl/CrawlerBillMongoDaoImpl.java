package com.topideal.mongo.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.mongo.dao.CrawlerBillMongoDao;
import com.topideal.mongo.entity.CrawlerBillMongo;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;

import net.sf.json.JSONObject;

/**
 * 爬虫账单生成出库清单日志Dao实现
 *
 */
@Repository
public class CrawlerBillMongoDaoImpl implements CrawlerBillMongoDao{
	
	//表名
    private String collectionName= CollectionEnum.CRAWLER_BILL_LOG.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;

	@Override
	public void insert(CrawlerBillMongo object) {
		mongoTemplate.insert(object,collectionName);
	}

	@Override
	public CrawlerBillMongo findOne(Map<String, Object> params) {
		 Query query = null;
	        Criteria criteria = BaseUtils.parseCriteria(params);
	        //封闭查询条件
	        if(criteria!=null){
	            query=new Query(criteria);
	        }else{
	            query = new Query();
	        }
	        return mongoTemplate.findOne(query, CrawlerBillMongo.class,collectionName);
	}

	@Override
	public List<CrawlerBillMongo> findAll(Map<String, Object> params) {
		 Query query = null;
	        Criteria criteria = BaseUtils.parseCriteria(params);
	        //封闭查询条件
	        if(criteria!=null){
	            query=new Query(criteria);
	        }else{
	            query = new Query();
	        }
	        List<CrawlerBillMongo> result = mongoTemplate.find(query, CrawlerBillMongo.class,collectionName);
	        return result;
	}
	@Override
    public List findAll(Map<String, Object> params, Map<String, String> sort, String collectionName) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteriaAndDate(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        //排序
        if(sort!=null){
            if("DESC".equals(sort.get("derection"))){
                query.with(new Sort(new Sort.Order(Sort.Direction.DESC,sort.get("property"))));
            }else{
                query.with(new Sort(new Sort.Order(Sort.Direction.ASC,sort.get("property"))));
            }
        }
        List<JSONObject> result = mongoTemplate.find(query, JSONObject.class,collectionName);
        return result;
    }
	 @Override
    public PageMongo findAll(Map<String, Object> params, Map<String, String> sort, PageMongo pageMongo, String collectionName) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteriaAndDate(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        //排序
        if(sort!=null){
            if("DESC".equals(sort.get("derection"))){
                query.with(new Sort(new Sort.Order(Sort.Direction.DESC,sort.get("property"))));
            }else{
                query.with(new Sort(new Sort.Order(Sort.Direction.ASC,sort.get("property"))));
            }
        }
        //分页
        query.skip(pageMongo.getBegin());
        query.limit(pageMongo.getPageSize());
        pageMongo.setTotal(mongoTemplate.count(query,collectionName));
        pageMongo.setList(mongoTemplate.find(query, JSONObject.class,collectionName));
        return pageMongo;
    }
	@Override
	public void update(Map<String, Object> queryParams, Map<String, Object> data) {
		  Query query = null;
	        Criteria criteria = BaseUtils.parseCriteria(queryParams);
	        Update update = BaseUtils.parseUpdate(data);
	        //封闭查询条件
	        if(criteria!=null){
	            query=new Query(criteria);
	        }else{
	            query = new Query();
	        }
	        mongoTemplate.upsert(query, update, CrawlerBillMongo.class,collectionName);
		
	}

	@Override
	public void remove(Map<String, Object> params) {
	    Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        mongoTemplate.remove(query,CrawlerBillMongo.class,collectionName);
	}

    /**
     * 以json的模式插入数据库
     * @param json
     */
    @Override
    public void insertJson(String json) {
        mongoTemplate.insert(json, collectionName);
    }
}
