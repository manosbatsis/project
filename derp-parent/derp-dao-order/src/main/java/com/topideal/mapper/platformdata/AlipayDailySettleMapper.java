package com.topideal.mapper.platformdata;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.platformdata.AlipayDailySettleModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface AlipayDailySettleMapper extends BaseMapper<AlipayDailySettleModel> {

	/**
	 * 批量新增
	 * @param list
	 * @return
	 */
	Integer alipayBatchSave(List<AlipayDailySettleModel> list);


}
