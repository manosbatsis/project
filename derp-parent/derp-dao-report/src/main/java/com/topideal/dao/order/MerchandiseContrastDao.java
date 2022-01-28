package com.topideal.dao.order;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.MerchandiseContrastModel;

/**
 * 爬虫商品对照表 dao
 * @author lian_
 *
 */
public interface MerchandiseContrastDao extends BaseDao<MerchandiseContrastModel> {

	/**
	 * 根据货号获取唯品爬虫货号
	 * @param queryMap
	 * @return
	 */
	List<MerchandiseContrastModel> getSkusByGoodsNo(Map<String, Object> queryMap);
		





}

