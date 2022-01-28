package com.topideal.service.epass.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.mongo.dao.ContractNoMongoDao;
import com.topideal.mongo.entity.ContractNoMongo;
import com.topideal.service.epass.ContractNoMongoService;

/**
 * LbxMongo
 * @author 杨创
 *2018/7/17
 */
@Service
public class ContractNoMongoServiceImpl implements ContractNoMongoService{
	@Autowired
	private ContractNoMongoDao contractNoMongoDao;// 合同号
	// 根据lbx号查询LbxMongo信息
	@Override
	public List<ContractNoMongo> getContractNoMongoInfoList(Map<String, Object> params) throws Exception {		
		 List<ContractNoMongo> contractNoMongoList = contractNoMongoDao.findAll(params);	
		return contractNoMongoList;
	}
	

}
