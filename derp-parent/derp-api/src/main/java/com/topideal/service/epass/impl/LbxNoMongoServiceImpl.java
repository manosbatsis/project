package com.topideal.service.epass.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.mongo.dao.LbxNoMongoDao;
import com.topideal.mongo.entity.LbxNoMongo;
import com.topideal.service.epass.LbxNoMongoService;

/**
 * LbxMongo
 * @author 杨创
 *2018/7/17
 */
@Service
public class LbxNoMongoServiceImpl implements LbxNoMongoService{
	@Autowired
	private LbxNoMongoDao lbxNoMongoDao;
	// 根据lbx号查询LbxMongo信息
	@Override
	public LbxNoMongo getLbxNoMongInfo(Map<String, Object> params) throws Exception {		
		LbxNoMongo lbxNoMongo = lbxNoMongoDao.findOne(params);	
		return lbxNoMongo;
	}

}
