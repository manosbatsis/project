package com.topideal.mapper.order;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.MerchandiseContrastModel;
import com.topideal.mapper.BaseMapper;

/**
 * 爬虫商品对照表 mapper
 * @author lian_
 */
@MyBatisRepository
public interface MerchandiseContrastMapper extends BaseMapper<MerchandiseContrastModel> {

	/**
	 * 根据货号获取唯品爬虫货号
	 * @param queryMap
	 * @return
	 */
	List<MerchandiseContrastModel> getSkusByGoodsNo(Map<String, Object> queryMap);



}

