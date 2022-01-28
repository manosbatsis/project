package com.topideal.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.LogStreamMqDTO;
import com.topideal.entity.vo.LogStreamMqModel;
import com.topideal.entity.vo.LogStreamOrderModel;


@MyBatisRepository
public interface LogStreamOrderMapper extends BaseMapper<LogStreamOrderModel> {
	
	/**
	 * 统计前一天各接口每个时段的数量
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Map<String, Object>> getNumGroupCount(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	/**
	 * 获取某接口某时段的最大耗时、最小耗时、平均耗时
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Map<String, Object> getConsumingByDate(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("point") String point);
	
	/**
	 * 根据时间获取接口的总单量
	 * @param startDate
	 * @param endDate
	 * @param point
	 * @return
	 */
	Map<String, Object> getTotalNumByDate(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("point") String point);
	
	/**
	 * 根据月份，获取之前的数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<LogStreamOrderModel> getListByMonthByPage(LogStreamOrderModel model) throws SQLException;

    /**整合web*/
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<LogStreamMqDTO> getListByPage(LogStreamMqDTO model)throws SQLException;
}
