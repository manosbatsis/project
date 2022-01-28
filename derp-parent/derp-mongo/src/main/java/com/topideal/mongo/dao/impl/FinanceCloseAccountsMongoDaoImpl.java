package com.topideal.mongo.dao.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.mongo.dao.FinanceCloseAccountsMongoDao;
import com.topideal.mongo.entity.FinanceCloseAccountsMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;

import net.sf.json.JSONObject;

/**
 * Created by weixiaolei on 2018/7/4.
 */
@Repository
public class FinanceCloseAccountsMongoDaoImpl implements FinanceCloseAccountsMongoDao {
    //表名
    private  String  collectionName= CollectionEnum.FINANCE_CLOSE_ACCOUNTS.getCollectionName();
    
    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(FinanceCloseAccountsMongo object) {
        mongoTemplate.insert(object, collectionName);
    }

    public FinanceCloseAccountsMongo findOne(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query, FinanceCloseAccountsMongo.class,collectionName);
    }

    public List<FinanceCloseAccountsMongo> findAll(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<FinanceCloseAccountsMongo> result = mongoTemplate.find(query, FinanceCloseAccountsMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update, FinanceCloseAccountsMongo.class,collectionName);
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
        mongoTemplate.remove(query,FinanceCloseAccountsMongo.class,collectionName);
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
     * 1.只校验关账 商家id/事业部id 必填
     * 2.只校验月结
     * 3.校验关账和月结
     * 说明 当事业部为空时 只校验月结
     * @param mode
     * @param flag
     * @return
     */
     public String findFinanceCloseAccount(FinanceCloseAccountsMongo model,String flag) {
     	String maxMonth=null;// 最大时间
         String colseMonth=null;// 关账时间
         String month=null; // 月结时间
     	// 查询财务关账
     	if (("1".equals(flag)||"3".equals(flag))&& model.getBuId()!=null) {
     		Map<String, Object> colseParams=new HashMap<String, Object>();
         	colseParams.put("merchantId", model.getMerchantId());//商家
         	colseParams.put("buId", model.getBuId());//事业部
         	colseParams.put("source", "1");
         	Criteria colseCriteria = BaseUtils.parseCriteriaAndDate(colseParams);
             //封闭查询条件
             Query colseQuery=new Query(colseCriteria);
             colseQuery.with(new Sort(new Sort.Order(Sort.Direction.DESC,"month")));
             FinanceCloseAccountsMongo colseParamsFindOne = mongoTemplate.findOne(colseQuery, FinanceCloseAccountsMongo.class,collectionName);		
             // 关账
             if (colseParamsFindOne!=null) {
             	colseMonth = colseParamsFindOne.getMonth();
     		}
     	}
     	// 查询月结
     	if ("2".equals(flag)||"3".equals(flag)) {                       
         	Map<String, Object> monthParams=new HashMap<String, Object>();
         	monthParams.put("merchantId", model.getMerchantId());
         	monthParams.put("depotId", model.getDepotId());
         	monthParams.put("source", "2");
         	Criteria monthcriteria = BaseUtils.parseCriteriaAndDate(monthParams);
         	Query monthQuery=new Query(monthcriteria);
         	monthQuery.with(new Sort(new Sort.Order(Sort.Direction.DESC,"month")));
         	FinanceCloseAccountsMongo monthParamsFindOne = mongoTemplate.findOne(monthQuery, FinanceCloseAccountsMongo.class,collectionName);
         	 //月结
             if (monthParamsFindOne!=null) {
             	month = monthParamsFindOne.getMonth();
     		}
     	} 
     	/**
     	 * 月结为空取关账
     	 * 关账为空取月结
     	 * 月结和关账都不为空 取最大
     	 */
         if (StringUtils.isNotBlank(colseMonth)&&StringUtils.isNotBlank(month)) {// 关账月份和月结月份都不为空取最大的
         	if (Timestamp.valueOf(colseMonth+"-01 00:00:00").getTime()>Timestamp.valueOf(month+"-01 00:00:00").getTime()) {
         		maxMonth=colseMonth;
     		}else {
     			maxMonth=month;
 			}        	
 		}else if (StringUtils.isBlank(colseMonth)&&StringUtils.isNotBlank(month)) {//关账月份为空和月结月份都不为空 取月结
 			maxMonth=month;
 		}else if (StringUtils.isNotBlank(colseMonth)&&StringUtils.isBlank(month)) {//关账月份不为空和月结月份都为空  取关账
 			maxMonth=colseMonth;
 		}
         

                 
         return maxMonth;
     }
     
    
}
