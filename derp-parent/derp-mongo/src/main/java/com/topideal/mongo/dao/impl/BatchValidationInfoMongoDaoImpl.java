package com.topideal.mongo.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.mongo.dao.BatchValidationInfoMongoDao;
import com.topideal.mongo.entity.BatchValidationInfoMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.BaseUtils.Operator;
import com.topideal.tools.CollectionEnum;

/**
 * Created by weixiaolei on 2018/7/4.
 */
@Repository
public class BatchValidationInfoMongoDaoImpl implements BatchValidationInfoMongoDao {
    //表名
    private  String  collectionName= CollectionEnum.BATCH_VALIDATION_INFO.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(BatchValidationInfoMongo object) {
        mongoTemplate.insert(object, collectionName);
    }

    public BatchValidationInfoMongo findOne(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query, BatchValidationInfoMongo.class,collectionName);
    }

    public List<BatchValidationInfoMongo> findAll(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<BatchValidationInfoMongo> result = mongoTemplate.find(query, BatchValidationInfoMongo.class,collectionName);
        return result;
    }

    public void update(Map<String, Object> queryParams,Map<String,Object> data) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(queryParams);
        Update update = BaseUtils.parseUpdate(data);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        mongoTemplate.upsert(query, update, BatchValidationInfoMongo.class,collectionName);
    }

    public void remove(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        mongoTemplate.remove(query,BatchValidationInfoMongo.class,collectionName);
    }

    /**
     * 以json的模式插入数据库
     * @param json
     */
    @Override
    public void insertJson(String json) {
        mongoTemplate.insert(json, collectionName);
    }
    /**
     * 查询mongo数据（返回一条记录）
     * @param params 查询条件  -- Operator(操作类型),map(key:属性名 vaule:值)
     * @param sort 排序 -- derection：排序方式；  property：排序字段
     */
    @Override
    public BatchValidationInfoMongo findOne(Map<Operator,Map<String,Object>> params,  List<Map<String, String>> sortList) {
        Query query = null;
        Criteria criteria = BaseUtils.customParseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        //排序
        List<Sort.Order> directionList = new ArrayList<Sort.Order>();
        for(Map<String, String> sort:sortList){
        	if(sort!=null){
                if("DESC".equals(sort.get("derection").toUpperCase())){
                	directionList.add(new Sort.Order(Sort.Direction.DESC,sort.get("property")));
                }else{
                	directionList.add(new Sort.Order(Sort.Direction.ASC,sort.get("property")));
                }
            }
        }
        if(directionList.size() > 0){
        	 query.with(new Sort(directionList));
        }
        return mongoTemplate.findOne(query, BatchValidationInfoMongo.class,collectionName);
    }
    /**
     * 查询mongo数据（返回所有数据）
     * @param params 查询条件 -- Operator(操作类型),map(key:属性名 vaule:值)
     * @param sort 排序 -- derection：排序方式；  property：排序字段
     */
    @Override
    public List<BatchValidationInfoMongo> findAll(Map<Operator,Map<String,Object>> params, List<Map<String, String>> sortList) {
        Query query = null;
        Criteria criteria = BaseUtils.customParseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        //排序
        List<Sort.Order> directionList = new ArrayList<Sort.Order>();
        for(Map<String, String> sort:sortList){
        	if(sort!=null){
                if("DESC".equals(sort.get("derection").toUpperCase())){
                	directionList.add(new Sort.Order(Sort.Direction.DESC,sort.get("property")));
                }else{
                	directionList.add(new Sort.Order(Sort.Direction.ASC,sort.get("property")));
                }
            }
        }
        if(directionList.size() > 0){
        	 query.with(new Sort(directionList));
        }
        List<BatchValidationInfoMongo> result = mongoTemplate.find(query, BatchValidationInfoMongo.class,collectionName);
        return result;
    }
}
