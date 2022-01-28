package com.topideal.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.ConsumeMonitorMqDTO;
import com.topideal.entity.vo.ConsumeMonitorHistoryOrderModel;
import com.topideal.entity.vo.ConsumeMonitorMqModel;


@MyBatisRepository
public interface ConsumeMonitorHistoryOrderMapper extends BaseMapper<ConsumeMonitorHistoryOrderModel> {

	/**
	 * 根据时间删除数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	void delByCreateTime(@Param("createTime") String createTime);
	
    /**整合web*/
	/**
	 * 分页
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<ConsumeMonitorMqDTO> getListByPage(ConsumeMonitorMqDTO model) throws SQLException;

	/**
	 * 获取数量
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	Long getCount(ConsumeMonitorMqModel model) throws SQLException;

	/**
	 * 获取导出集合
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<ConsumeMonitorMqDTO> getExportList(ConsumeMonitorMqDTO model) throws SQLException;

	/**
	 * 获取导出集合
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<ConsumeMonitorMqModel> getExportListByPage(ConsumeMonitorMqModel model) throws SQLException;

	/**
	 * 获取重推的所有信息
	 * @return
	 * @throws SQLException
	 */
	List<ConsumeMonitorMqModel> getErrorListByPush(ConsumeMonitorMqModel model) throws SQLException;

}
