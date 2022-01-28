package com.topideal.mapper.order;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.TransferOutDepotModel;
import com.topideal.mapper.BaseMapper;


/**
 * 调拨出库表 mapper
 * @author zhanghx
 */
@MyBatisRepository
public interface TransferOutDepotMapper extends BaseMapper<TransferOutDepotModel> {
   /**
    * 根据创建修改时间查询出库单
    * */
	public List<Map<String, Object>> getOutDepotOrderByTime(Map<String, Object> paramMap);
	/**
    * 根据创建修改时间查询出库单统计数量
    * */
	public Integer getOutDepotOrderByTimeCount(Map<String, Object> paramMap);
	/**
	 * 根据出库单号查询出库单商品
	 * */
	public List<Map<String, Object>> getOutDepotItemByCodes(List<String> codes);
	/**
	 * 根据出库单查询商品批次
	 * */
	public List<Map<String, Object>> getItemBatchByCode(Map<String, Object> paramMap);

}

