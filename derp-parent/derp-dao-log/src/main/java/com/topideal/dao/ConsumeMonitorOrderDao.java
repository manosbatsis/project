package com.topideal.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.ConsumeMonitorMqDTO;
import com.topideal.entity.vo.ConsumeMonitorMqModel;
import com.topideal.entity.vo.ConsumeMonitorOrderModel;


public interface ConsumeMonitorOrderDao extends BaseDao<ConsumeMonitorOrderModel>{

	/**
	 * 获取本月已处理的失败数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Long getTreatedNum(String startDate, String endDate);

	/**
	 * 获取本月未处理的失败数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Long getUntreatedNum(String startDate, String endDate);
		
	/**
	 * 根据状态统计各模块的数量
	 * 
	 * @param status
	 * @return
	 */
	List<Map<String, Object>> getGroupCountByStatus(String status, String startDate, String endDate);


	/**
	 * 根据月份，获取之前的数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	ConsumeMonitorOrderModel getListByMonth(ConsumeMonitorOrderModel model) throws SQLException;

	/**
	 * 根据时间获取MQ消费失败的信息
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<ConsumeMonitorOrderModel> getConsumeFailList(String startDate, String endDate);

    /**整合web*/
	/**
	 * 分页
	 * @param model
	 * @return
	 */
	ConsumeMonitorMqDTO getListByPage(ConsumeMonitorMqDTO model) throws SQLException;

	/**
	 * 统计数量
	 * @param model
	 * @return
	 */
	Long getCount(ConsumeMonitorMqModel model) throws SQLException;
	/**
	 * 导出-分页
	 * @param model
	 * @return
	 */
	ConsumeMonitorMqModel getExportListByPage(ConsumeMonitorMqModel model) throws SQLException;
	/**
	 * 导出
	 * @param model
	 * @return
	 */
	List<ConsumeMonitorMqDTO> getExportList(ConsumeMonitorMqDTO model) throws SQLException;
	/**
	 * 错误页面-分页
	 * @param model
	 * @return
	 */
	ConsumeMonitorMqDTO getErrorListByPage(ConsumeMonitorMqDTO model) throws SQLException;
	/**
	 * 错误重推
	 * @param model
	 * @return
	 */
	List<ConsumeMonitorMqModel> getErrorListByPush(ConsumeMonitorMqModel model) throws SQLException;

	/**
	 * 获取错误类型数量
	 * @param querymap
	 * @return
	 */
	List<Map<String, Object>> getErrorTypeAccountByMap(Map<String, Object> querymap);





}
