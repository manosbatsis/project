package com.topideal.order.service.base.imp;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.order.service.base.DepotInfoService;

/**
 * 仓库
 * @author zhanghx
 *
 */
@Service
public class DepotInfoServiceImpl implements DepotInfoService {

	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	
	@Override
	public DepotInfoMongo getDetails(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depotId", id);
		return depotInfoMongoDao.findOne(params);
	}

}
