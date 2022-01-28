package com.topideal.mongo.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.tools.BaseUtils;

import net.sf.json.JSONObject;

/**
 * Created by weixiaolei on 2018/6/8.
 */
@Repository
public class JSONMongoDaoImpl implements JSONMongoDao{


    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void insertJson(String json,String collectionName) {
        mongoTemplate.insert(json, collectionName);
    }

    @Override
    public JSONObject findOne(Map<String, Object> params, String collectionName) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query, JSONObject.class,collectionName);
    }

    @Override
    public List findAll(String collectionName) {
        return findAll(new HashMap(),collectionName);
    }

    @Override
    public List findAll(Map<String, Object> params, String collectionName) {
        return findAll(params,null,collectionName);
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

    /**
     * 修改
     * @param params
     * @param data
     */
	@Override
	public void update(Map<String, Object> params, Map<String, Object> data, String collectionName) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        Update update = BaseUtils.parseUpdate(data);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        mongoTemplate.upsert(query, update, JSONObject.class,collectionName);
	}

	/**
     * 删除
     * @param params
     */
	@Override
	public void remove(Map<String, Object> params, String collectionName) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        mongoTemplate.remove(query,JSONObject.class,collectionName);
	}

	/**
     * 统计数量
     * @param params
     * @param collectionName
     * @return
     */
	@Override
	public Long count(Map<String, Object> params, String collectionName) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteriaAndDate(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.count(query,collectionName);
	}
}
