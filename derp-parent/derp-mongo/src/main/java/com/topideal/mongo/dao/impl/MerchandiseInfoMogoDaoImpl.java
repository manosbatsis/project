package com.topideal.mongo.dao.impl;

import com.topideal.mongo.dao.MerchandiseDepotRelMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.MerchandiseDepotRelMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by weixiaolei on 2018/7/4.
 */
@Repository
public class MerchandiseInfoMogoDaoImpl implements MerchandiseInfoMogoDao{
    //表名
    private  String  collectionName= CollectionEnum.MERCHANDISE_INFO.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MerchandiseDepotRelMongoDao merchandiseDepotRelMongoDao ;

    public void insert(MerchandiseInfoMogo object) {
        mongoTemplate.insert(object, collectionName);
    }

    public MerchandiseInfoMogo findOne(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query, MerchandiseInfoMogo.class,collectionName);
    }

    public List<MerchandiseInfoMogo> findAll(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<MerchandiseInfoMogo> result = mongoTemplate.find(query, MerchandiseInfoMogo.class,collectionName);
        return result;
    }
    public List<MerchandiseInfoMogo> findAllByIn(String keyName,List valueList) {
        Query query = new Query();
        if(valueList!=null){
            query.addCriteria(Criteria.where(keyName).in(valueList));
        }
        List<MerchandiseInfoMogo> result = mongoTemplate.find(query, MerchandiseInfoMogo.class,collectionName);
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
        mongoTemplate.upsert(query, update, MerchandiseInfoMogo.class,collectionName);
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
        mongoTemplate.remove(query,MerchandiseInfoMogo.class,collectionName);
    }

    /**
     * 以json的模式插入数据库
     * @param json
     */
    @Override
    public void insertJson(String json) {
        mongoTemplate.insert(json, collectionName);
    }

    public List<MerchandiseInfoMogo> findMerchandiseByDepotId(Map<String, Object> params, Long depotId){
//        Criteria criteria = BaseUtils.parseCriteria(params);
//        if(StringUtils.isNotBlank(depotId)){
//            if(criteria == null ){
//                criteria = Criteria.where("merchandiseInfo.skuCode").is(depotId);
//            }else{
//                criteria = criteria.and("merchandiseInfo.skuCode").is(depotId);
//            }
//        }
//        Criteria countCriteria = Criteria.where("merchandiseInfo").not().size(0);
//
//        LookupOperation lookupOperation = Aggregation.lookup(CollectionEnum.MERCHANDISE_EXTERNAL_WAREHOUSE.getCollectionName(),
//                "merchandiseId","goodsId", "merchandiseInfo");
//        AggregationOperation match = Aggregation.match(criteria);
//        AggregationOperation match1 = Aggregation.match(countCriteria);
//        Aggregation aggregation = Aggregation.newAggregation(match, lookupOperation, match1);
//        List<MerchandiseInfoMogo> list = mongoTemplate.aggregate(aggregation,collectionName,MerchandiseInfoMogo.class).getMappedResults();

        List<MerchandiseInfoMogo>  merchandiseList = findAll(params);
        if(merchandiseList == null || merchandiseList.size() < 1){
           return null;
        }
        if(depotId != null){
            List<Long> merchandiseIds = merchandiseList.stream().map(MerchandiseInfoMogo::getMerchandiseId).collect(Collectors.toList());

            Criteria criteria = Criteria.where("goodsId").in(merchandiseIds);
            criteria.and("depotId").is(depotId);

            Query query = new Query();
            query.addCriteria(criteria);
            List<MerchandiseDepotRelMongo> merchandiseDepotRelList = mongoTemplate.find(query, MerchandiseDepotRelMongo.class,CollectionEnum.MERCHANDISE_DEPOT_REL.getCollectionName());
            if(merchandiseDepotRelList == null || merchandiseDepotRelList.size() < 1){
                return null;
            }
            List<Long> finalMerchandiseIds = merchandiseDepotRelList.stream().map(MerchandiseDepotRelMongo::getGoodsId).collect(Collectors.toList());

            merchandiseList = merchandiseList.stream().filter(m-> finalMerchandiseIds.contains(m.getMerchandiseId())).collect(Collectors.toList());
        }
//        merchandiseList = merchandiseList.stream().sorted(Comparator.comparing(MerchandiseInfoMogo::getFactoryNo,Comparator.nullsLast(String::compareTo))).collect(Collectors.toList());
        Collections.sort(merchandiseList, new Comparator<MerchandiseInfoMogo>() {
            @Override
            public int compare(MerchandiseInfoMogo o1, MerchandiseInfoMogo o2) {
                if(StringUtils.isNotBlank(o1.getFactoryNo()) && StringUtils.isBlank(o2.getFactoryNo())) {
                    return -1;
                }else if(StringUtils.isNotBlank(o2.getFactoryNo()) && StringUtils.isBlank(o1.getFactoryNo())){
                    return 1;
                }else{
                    return 0;
                }

            }
        });
        return merchandiseList;
    }
}
