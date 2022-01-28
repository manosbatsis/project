package com.topideal.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.ConsumeMonitorMqDTO;
import com.topideal.entity.vo.ConsumeMonitorMqModel;
import com.topideal.entity.vo.ConsumeMonitorOrderModel;


@MyBatisRepository
public interface ConsumeMonitorOrderMapper extends BaseMapper<ConsumeMonitorOrderModel> {

	/**
	 * 获取本月已处理的失败数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Long getTreatedNum(@Param("startDate") String startDate, @Param("endDate") String endDate);

	/**
	 * 获取本月未处理的失败数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Long getUntreatedNum(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	/**
	 * 根据状态统计各模块的数量
	 * 
	 * @param status
	 * @return
	 */
	List<Map<String, Object>> getGroupCountByStatus(@Param("status") String status,
                                                    @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	/**
	 * 根据月份，获取之前的数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<ConsumeMonitorOrderModel> getListByMonthByPage(ConsumeMonitorOrderModel model) throws SQLException;
	/**
	 * 根据时间获取MQ消费失败的信息
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<ConsumeMonitorOrderModel> getConsumeFailList(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
	/**整合web*/
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<ConsumeMonitorMqDTO> getListByPage(ConsumeMonitorMqDTO dto) throws SQLException;
	
	/**
	 * 获取数量
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	Long getCount(ConsumeMonitorMqModel model) throws SQLException;
	
	/**
	 * 获取导出集合
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<ConsumeMonitorMqDTO> getExportList(ConsumeMonitorMqDTO model) throws SQLException;
	
	/**
	 * 获取导出集合
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<ConsumeMonitorMqModel> getExportListByPage(ConsumeMonitorMqModel model) throws SQLException;
	
	/**
	 * 查询错误的电商订单日志信息 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<ConsumeMonitorMqDTO> getErrorListByPage(ConsumeMonitorMqDTO model) throws SQLException;
	
	/**
	 * 获取重推的所有信息
	 * @return
	 * @throws SQLException
	 */
	List<ConsumeMonitorMqModel> getErrorListByPush(ConsumeMonitorMqModel model) throws SQLException;

	/**
	 * 获取错误类型日志
	 * @param querymap
	 * @return
	 */
	List<Map<String, Object>> getErrorTypeAccountByMap(Map<String, Object> querymap);
	
}
