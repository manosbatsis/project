package com.topideal.mapper.platformdata;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.platformdata.AlipayDailySettlebatchModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface AlipayDailySettlebatchMapper extends BaseMapper<AlipayDailySettlebatchModel> {
	/**
	 * 批量新增
	 * @param list
	 * @return
	 */
	Integer alipayBatchSave(List<AlipayDailySettlebatchModel> list);

	/**
	 * 平台结算单-统计
	 * @param queryMap
	 * @return
	 */
	List<AlipayDailySettlebatchModel> getPlatformStatementData(Map<String, Object> queryMap);

}
