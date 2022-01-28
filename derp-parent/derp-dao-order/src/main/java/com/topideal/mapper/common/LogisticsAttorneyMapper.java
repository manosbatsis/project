package com.topideal.mapper.common;

import java.sql.SQLException;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.common.LogisticsAttorneyModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface LogisticsAttorneyMapper extends BaseMapper<LogisticsAttorneyModel> {
	/**
	 * 更新数据，为空也更新
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	int modifyWithNull(LogisticsAttorneyModel  model) throws SQLException; 


}
