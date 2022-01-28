package com.topideal.service.epass;

import java.util.Map;

import com.topideal.mongo.entity.LbxNoMongo;

/**
 * LbxNoMong 实现类
 * @author 杨创
 *2018/7/17
 */
public interface LbxNoMongoService {
	/**
	 * 根据lbx号查询mongdb
	 * @param lbx
	 * @return
	 * @throws Exception
	 */
	public LbxNoMongo getLbxNoMongInfo(Map<String, Object> params)throws Exception;
}
