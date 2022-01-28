package com.topideal.mapper;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.LogStreamMqDTO;
import com.topideal.entity.vo.LogStreamApiModel;
import com.topideal.entity.vo.LogStreamMqModel;


@MyBatisRepository
public interface LogStreamApiMapper extends BaseMapper<LogStreamApiModel> {

	/**
	 * 获取本月份API接收请求数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Long getRequestNum(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	/**
	 * 根据月份，获取之前的数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<LogStreamApiModel> getListByMonthByPage(LogStreamApiModel model) throws SQLException;

    /**整合web*/
	/**
	 * 分页
	 * @param model
	 * @return
	 */
	PageDataModel<LogStreamMqDTO> getListByPage(LogStreamMqDTO model)throws SQLException;

	/**
	 * 获取API 请求成功数
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	Long getRequestSuccNum(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
}
