package com.topideal.mapper.platformdata;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.platformdata.AlipayDailyFeeModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface AlipayDailyFeeMapper extends BaseMapper<AlipayDailyFeeModel> {
	/**
	 * 批量新增
	 * @param list
	 * @return
	 */
	Integer alipayBatchSave(List<AlipayDailyFeeModel> list);



}
