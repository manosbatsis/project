package com.topideal.mongo.dao;

import com.topideal.mongo.entity.MerchandiseInfoMogo;

import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/7/4.
 */
public interface MerchandiseInfoMogoDao extends MongoDao<MerchandiseInfoMogo>{

    public void insertJson(String json);

    public List<MerchandiseInfoMogo> findAllByIn(String keyName, List valueList);

    public List<MerchandiseInfoMogo> findMerchandiseByDepotId(Map<String, Object> params, Long depotId);

}
