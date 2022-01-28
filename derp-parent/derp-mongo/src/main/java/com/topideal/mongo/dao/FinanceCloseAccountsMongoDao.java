package com.topideal.mongo.dao;

import com.topideal.mongo.entity.FinanceCloseAccountsMongo;

/**
 * Created by weixiaolei on 2018/7/4.
 */
public interface FinanceCloseAccountsMongoDao extends MongoDao<FinanceCloseAccountsMongo>{

    public void insertJson(String json);

    public String findFinanceCloseAccount(FinanceCloseAccountsMongo model, String flag);
     

}
