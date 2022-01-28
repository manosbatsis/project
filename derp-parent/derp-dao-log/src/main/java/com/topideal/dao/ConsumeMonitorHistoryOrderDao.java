package com.topideal.dao;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.ConsumeMonitorMqDTO;
import com.topideal.entity.vo.ConsumeMonitorHistoryOrderModel;
import com.topideal.entity.vo.ConsumeMonitorMqModel;


public interface ConsumeMonitorHistoryOrderDao extends BaseDao<ConsumeMonitorHistoryOrderModel>{
		
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
	 * 错误重推
	 * @param model
	 * @return
	 */
	List<ConsumeMonitorMqModel> getErrorListByPush(ConsumeMonitorMqModel model) throws SQLException;








}
