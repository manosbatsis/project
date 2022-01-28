package com.topideal.dao;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.ConsumeMonitorMqDTO;
import com.topideal.entity.vo.ConsumeMonitorHistoryInventoryModel;
import com.topideal.entity.vo.ConsumeMonitorMqModel;


public interface ConsumeMonitorHistoryInventoryDao extends BaseDao<ConsumeMonitorHistoryInventoryModel>{
		

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
	 * 导出
	 * @param model
	 * @return
	 */
	List<ConsumeMonitorMqDTO> getExportList(ConsumeMonitorMqDTO model) throws SQLException;






}
