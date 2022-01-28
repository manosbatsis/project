package com.topideal.mapper.order;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.SaleReturnOdepotItemModel;
import com.topideal.mapper.BaseMapper;

/**
 * 销售退货出库表体
 * @author lian_
 */
@MyBatisRepository
public interface SaleReturnOdepotItemMapper extends BaseMapper<SaleReturnOdepotItemModel> {

	/**
	 * 商家、仓库、po号、标准条码的退货数量汇总
	 * @param queryMap
	 * @return
	 */
	Integer getSaleReturnOdepotAccount(Map<String, Object> map);

	List<Map<String, Object>> getVipSaleReturnOdepot(Map<String, Object> map);

	

}

