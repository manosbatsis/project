package com.topideal.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.ConsumeMonitorMqDTO;
import com.topideal.entity.vo.ConsumeMonitorApiModel;
import com.topideal.entity.vo.ConsumeMonitorMqModel;


public interface ConsumeMonitorApiDao extends BaseDao<ConsumeMonitorApiModel>{
		
	/**
	 * 根据月份，获取之前的数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	ConsumeMonitorApiModel getListByMonth(ConsumeMonitorApiModel model) throws SQLException;
   
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
	 * 导出
	 * @param model
	 * @return
	 */
	List<ConsumeMonitorMqDTO> getExportList(ConsumeMonitorMqDTO model) throws SQLException;

	/**
	 * 获取已处理失败数
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	Long getTreatedNum(String sDate, String eDate);

	/**
	 * 获取未处理失败数
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	Long getUntreatedNum(String sDate, String eDate);

	/**
	 * 获取错误类型失败数
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	List<Map<String, Object>> getErrorNumGroupByType(String sDate, String eDate);

	Integer getUnTreatNumByMap(Map<String, Object> queryMap);

	/**
	 * 获取成功数
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	Long getSuccNum(String sDate, String eDate);









}
