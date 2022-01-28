package com.topideal.service.epass;

import java.util.List;
import java.util.Map;

import com.topideal.mongo.entity.ContractNoMongo;

/**
 * ContractNoMong 实现类
 * @author 杨创
 *2018/10/10
 */
public interface ContractNoMongoService {
	/**
	 * 根据合同号查询mongdb
	 * @param contractNo
	 * @return
	 * @throws Exception
	 */
	public List<ContractNoMongo>  getContractNoMongoInfoList(Map<String, Object> params)throws Exception;
}
