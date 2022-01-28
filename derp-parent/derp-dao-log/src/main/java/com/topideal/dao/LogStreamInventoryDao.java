package com.topideal.dao;

import java.sql.SQLException;

import com.topideal.entity.dto.LogStreamMqDTO;
import com.topideal.entity.vo.LogStreamInventoryModel;
import com.topideal.entity.vo.LogStreamMqModel;


public interface LogStreamInventoryDao extends BaseDao<LogStreamInventoryModel>{
		
	/**
	 * 根据月份，获取之前的数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	LogStreamInventoryModel getListByMonthByPage(LogStreamInventoryModel model) throws SQLException;

    /**整合web*/
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	LogStreamMqDTO getListByPage(LogStreamMqDTO model) throws SQLException;




}
