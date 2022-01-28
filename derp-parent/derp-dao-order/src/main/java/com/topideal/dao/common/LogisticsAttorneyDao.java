package com.topideal.dao.common;

import java.sql.SQLException;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.common.LogisticsAttorneyModel;


public interface LogisticsAttorneyDao extends BaseDao<LogisticsAttorneyModel> {
		
	/**
	 * 更新数据，为空也更新
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	int modifyWithNull(LogisticsAttorneyModel  model) throws SQLException; 



}
