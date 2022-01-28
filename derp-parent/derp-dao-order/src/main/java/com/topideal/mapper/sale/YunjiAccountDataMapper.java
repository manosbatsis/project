package com.topideal.mapper.sale;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.sale.YunjiAccountDataModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface YunjiAccountDataMapper extends BaseMapper<YunjiAccountDataModel> {

	/**
	 * 获取云集结算账单表头有哪些商家
	 * @return
	 */
	public List<Map<String, Object>> getYunjiAccountMerchant(Map<String, Object> map);

	/**
	 * 获取生成平台结算单数据
	 * @param queryMap
	 * @return
	 */
	public List<YunjiAccountDataModel> getPlatformStatementData(Map<String, Object> queryMap);

}
