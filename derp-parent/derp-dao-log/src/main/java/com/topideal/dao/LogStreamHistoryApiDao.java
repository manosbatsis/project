package com.topideal.dao;

import java.sql.SQLException;

import com.topideal.entity.dto.LogStreamMqDTO;
import com.topideal.entity.vo.LogStreamHistoryApiModel;
import com.topideal.entity.vo.LogStreamMqModel;


public interface LogStreamHistoryApiDao extends BaseDao<LogStreamHistoryApiModel>{
		
	/**
	 * 根据时间删除数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	void delByCreateTime(String createTime);

    /**整合web*/
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	LogStreamMqDTO getListByPage(LogStreamMqDTO model) throws SQLException;

}
