package com.topideal.mapper.order;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.PurchaseReturnOdepotModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface PurchaseReturnOdepotMapper extends BaseMapper<PurchaseReturnOdepotModel> {


	/**
	 * 新财务报表-查询采购退货出库数量
	 * @param paramMap
	 */
	List<Map<String,Object>> getBuPurchaseReturnOdepotNum(Map<String, Object> paramMap);

	/**
	 * 新财务报表-查询采购退货出库明细
	 * @param paramMap
	 */
	List<Map<String,Object>> getBuPurchaseReturnOdepot(Map<String, Object> paramMap);

}
