package com.topideal.dao.order;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.TransferOutDepotModel;

/**
 * 调拨出库表 dao
 * @author zhanghx
 *
 */
public interface TransferOutDepotDao extends BaseDao<TransferOutDepotModel> {
		
	public List<Map<String, Object>> getOutDepotOrderByTime(Map<String, Object> paramMap);
	/**
    * 根据创建修改时间查询出库单统计数量
    * */
	public Integer getOutDepotOrderByTimeCount(Map<String, Object> paramMap);
	
	public List<Map<String, Object>> getOutDepotItemByCodes(List<String> codes);
	
	public List<Map<String, Object>> getItemBatchByCode(Map<String, Object> paramMap);

}

