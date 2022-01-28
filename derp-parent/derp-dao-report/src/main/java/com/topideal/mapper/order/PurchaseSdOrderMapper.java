package com.topideal.mapper.order;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.PurchaseSdOrderModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface PurchaseSdOrderMapper extends BaseMapper<PurchaseSdOrderModel> {
	/**
	 * 获取采购调整sd单	
	 * @param paramMap
	 * @return
	 */
	List<Map<String,Object>> getTzPurchaseSdOrde(Map<String, Object> paramMap);
	/**
	 * 获取sd数据
	 * @param model
	 * @return
	 */
	List<Map<String,Object>> getSdOrderItemList(Map<String, Object> map);


}
