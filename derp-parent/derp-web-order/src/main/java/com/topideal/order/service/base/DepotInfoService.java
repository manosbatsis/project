package com.topideal.order.service.base;

import com.topideal.mongo.entity.DepotInfoMongo;

public interface DepotInfoService {

	/**
	 * 获取仓库信息
	 * @return
	 */
	DepotInfoMongo getDetails(Long id);
}
