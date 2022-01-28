package com.topideal.dao;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.LogWarningMqDTO;
import com.topideal.entity.vo.LogWarningMqModel;


public interface LogWarningMqDao extends BaseDao<LogWarningMqModel>{
		
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	LogWarningMqDTO getListByPage(LogWarningMqDTO dto) throws SQLException;

	/**
	 * 根据月份，获取之前的数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	LogWarningMqModel getListByMonthByPage(LogWarningMqModel model) throws SQLException;
	
	/**
	 * 根据时间删除数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	void delByCreateTime(String createTime);
	
	/**整合web*/
	List<LogWarningMqModel> getList();
	
}

