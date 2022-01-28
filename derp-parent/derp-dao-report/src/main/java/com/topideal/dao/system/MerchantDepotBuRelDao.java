package com.topideal.dao.system;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.system.MerchantDepotBuRelModel;

import java.util.List;
import java.util.Map;


public interface MerchantDepotBuRelDao extends BaseDao<MerchantDepotBuRelModel> {

	int deleteByMap(Map<String, Object> delMap);
	
	/**
	 * 获取事业部商家仓库中间表
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBuMerchAndDepotBuMap(Map<String, Object> map);


    List<Map<String, Object>> getListByMap(Map<String, Object> map);
	/**查询商家仓库、事业部统计存货跌价为是公司事业部状态为启用的仓库、事业部数据
	 * */
	List<Map<String, Object>> getDepotAndBuList(Map<String, Object> map);
}
