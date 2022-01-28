package com.topideal.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.ConsumeMonitorMqDTO;
import com.topideal.entity.vo.ConsumeMonitorMqModel;

/**
 * mq消费监控
 * @author zhanghx
 */
public interface ConsumeMonitorService {

	/**
	 * 分页
	 * @param model
	 * @return
	 */
	ConsumeMonitorMqDTO listByPage(ConsumeMonitorMqDTO dto) throws SQLException;

	/**
	 * 统计数量
	 * @param model
	 * @param endDateStr
	 * @return
	 */
	Long count(ConsumeMonitorMqModel model, String endDateStr, String createDateStr, String selectScope) throws SQLException;
	
	/**
	 * 获取导出集合
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<ConsumeMonitorMqDTO> getExportList(ConsumeMonitorMqDTO model, String endDateStr, Integer count, String createDateStr, String selectScope, String modelCode) throws SQLException;
	
	/**
	 * 批量关闭
	 * @param list
	 * @return
	 * @throws SQLException
	 * @throws ParameterException
	 */
	boolean closeBatch(List<Long> list, String modelCode) throws SQLException;
	
	/**
	 * 查询错误的电商订单日志信息 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	ConsumeMonitorMqDTO getErrorListByPage(ConsumeMonitorMqDTO model) throws SQLException;
	
	/**
	 * 重推所有错误的电商订单日志信息
	 * @return
	 * @throws SQLException
	 */
	boolean allPush(ConsumeMonitorMqModel model, String endDateStr, String createDateStr) throws Exception;
	
	/**
	 * 批量推送
	 * @param ids
	 * @param collectionName
	 * @return
	 * @throws Exception
	 */
	boolean resetSend(List<String> ids, String modelCode) throws Exception;
	
	
	/**
	 * 批量导入日志监控重推
	 * @param data
	 * @return
	 */
	Map importConsumeMonitorData(Map<Integer, List<List<Object>>> data)throws Exception;
}
