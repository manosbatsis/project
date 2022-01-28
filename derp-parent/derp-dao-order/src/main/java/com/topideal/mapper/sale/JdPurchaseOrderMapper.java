package com.topideal.mapper.sale;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.sale.JdPurchaseOrderModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface JdPurchaseOrderMapper extends BaseMapper<JdPurchaseOrderModel> {

	/**
	 * 分页获取京东平台采购订单数据
	 * @return
	 * @throws Exception
	 */
	List <JdPurchaseOrderModel>getPlatformPurchaseList(Map<String, Object> paramMap) throws Exception;

}
