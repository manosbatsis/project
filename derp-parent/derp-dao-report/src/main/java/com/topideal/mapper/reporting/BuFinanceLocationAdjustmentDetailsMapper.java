package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.BuFinanceLocationAdjustmentDetailsModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuFinanceLocationAdjustmentDetailsMapper extends BaseMapper<BuFinanceLocationAdjustmentDetailsModel> {

	/**
	 * 删除(事业部财务经销存)库位类型调整明细表
	 * 
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delFinanceLocationAdjustmentDetails(Map<String, Object> map) throws SQLException;

}
