package com.topideal.mapper.sale;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.sale.TmallscmPurchaseOrderModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface TmallscmPurchaseOrderMapper extends BaseMapper<TmallscmPurchaseOrderModel> {


	/**
	 * 分页获取天猫平台采购订单数据
	 * @return
	 * @throws Exception
	 */
	List <TmallscmPurchaseOrderModel>getTmallPlatformPurchaseList(Map<String, Object> paramMap) throws Exception;
}
