package com.topideal.mongo.dao.impl;

import com.topideal.mongo.dao.FileTaskMongoDao;
import com.topideal.mongo.entity.BatchValidationInfoMongo;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件列表
 */
@Repository
public class FileTaskMongoDaoImpl implements FileTaskMongoDao {


    //表名
    private String collectionName = CollectionEnum.FILE_TASK.getCollectionName();
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void insert(FileTaskMongo object) {
        mongoTemplate.insert(object, collectionName);
    }

    @Override
    public FileTaskMongo findOne(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if (criteria != null) {
            query = new Query(criteria);
        } else {
            query = new Query();
        }
        return mongoTemplate.findOne(query, FileTaskMongo.class, collectionName);
    }

    @Override
    public List<FileTaskMongo> findAll(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if (criteria != null) {
            query = new Query(criteria);
        } else {
            query = new Query();
        }
        List<FileTaskMongo> result = mongoTemplate.find(query, FileTaskMongo.class, collectionName);
        return result;
    }

    @Override
    public void update(Map<String, Object> queryParams, Map<String, Object> data) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(queryParams);
        Update update = BaseUtils.parseUpdate(data);
        //封闭查询条件
        if (criteria != null) {
            query = new Query(criteria);
        } else {
            query = new Query();
        }
        mongoTemplate.upsert(query, update, FileTaskMongo.class, collectionName);
    }

    @Override
    public void remove(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if (criteria != null) {
            query = new Query(criteria);
        } else {
            query = new Query();
        }
        mongoTemplate.remove(query, FileTaskMongo.class, collectionName);
    }

    /**
     * 以json的模式插入数据库
     *
     * @param json
     */
    @Override
    public void insertJson(String json) {
        mongoTemplate.insert(json, collectionName);
    }

    @Override
    public List<FileTaskMongo> findAllOrderByDate(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if (criteria != null) {
            query = new Query(criteria);
        } else {
            query = new Query();
        }

        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        query.with(sort);

        List<FileTaskMongo> result = mongoTemplate.find(query, FileTaskMongo.class, collectionName);
        return result;
    }

    @Override
    public PageMongo<FileTaskMongo> findAllByPage(Map<String, Object> params, Integer begin, Integer pageSize) {
        PageMongo<FileTaskMongo> pageMongo = new PageMongo<>();

        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if (criteria != null) {
            query = new Query(criteria);
        } else {
            query = new Query();
        }

        query.limit(pageSize).skip(begin);

        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        query.with(sort);

        List<FileTaskMongo> result = mongoTemplate.find(query, FileTaskMongo.class, collectionName);

        long totalCount = mongoTemplate.count(query, FileTaskMongo.class, collectionName);

        pageMongo.setTotal(totalCount);
        pageMongo.setList(result);
        pageMongo.setBegin(begin);
        pageMongo.setPageSize(pageSize);
        return pageMongo;
    }

    @Override
    public List<FileTaskMongo> findAllInParams(Map<BaseUtils.Operator, Map<String, Object>> params) {
        Query query = null;
        Criteria criteria = BaseUtils.customParseCriteria(params);
        //封闭查询条件
        if (criteria != null) {
            query = new Query(criteria);
        } else {
            query = new Query();
        }
        List<FileTaskMongo> result = mongoTemplate.find(query, FileTaskMongo.class, collectionName);
        return result;
    }


}
