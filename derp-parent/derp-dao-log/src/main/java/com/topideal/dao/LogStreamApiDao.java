package com.topideal.dao;

import java.sql.SQLException;

import com.topideal.entity.dto.LogStreamMqDTO;
import com.topideal.entity.vo.LogStreamApiModel;
import com.topideal.entity.vo.LogStreamMqModel;


public interface LogStreamApiDao extends BaseDao<LogStreamApiModel>{
	/**
	 * 获取本月份API接收请求数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Long getRequestNum(String startDate, String endDate);
		
	/**
	 * 根据月份，获取之前的数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	LogStreamApiModel getListByMonthByPage(LogStreamApiModel model) throws SQLException;

    /**整合web*/
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	LogStreamMqDTO getListByPage(LogStreamMqDTO model) throws SQLException;

	/**
	 * 请求成功数
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	Long getRequestSuccNum(String sDate, String eDate);







}
