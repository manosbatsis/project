package com.topideal.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.ConsumeMonitorMqDTO;
import com.topideal.entity.vo.ConsumeMonitorApiModel;
import com.topideal.entity.vo.ConsumeMonitorMqModel;


@MyBatisRepository
public interface ConsumeMonitorApiMapper extends BaseMapper<ConsumeMonitorApiModel> {

	/**
	 * 根据月份，获取之前的数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<ConsumeMonitorApiModel> getListByMonthByPage(ConsumeMonitorApiModel model) throws SQLException;
    /**整合web*/
	/**
	 * 分页
	 * @param model
	 * @return
	 */
	PageDataModel<ConsumeMonitorMqDTO> getListByPage(ConsumeMonitorMqDTO model) throws SQLException;
	/**
	 * 导出
	 * @param model
	 * @return
	 */
	List<ConsumeMonitorMqDTO> getExportList(ConsumeMonitorMqDTO model) throws SQLException;
	/**
	 * 统计数量
	 * @param model
	 * @return
	 */
	Long getCount(ConsumeMonitorMqModel model) throws SQLException;
	/**
	 * 获取处理失败数
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	Long getTreatedNum(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	/**
	 * 获取为处理失败数
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	Long getUntreatedNum(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	/**
	 * 获取错误类型失败数
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	List<Map<String, Object>> getErrorNumGroupByType(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	/**
	 * 获取错误类型未处理失败数
	 * @param queryMap
	 * @return
	 */
	Integer getUnTreatNumByMap(Map<String, Object> queryMap);
	
	/**
	 * 获取成功类型失败数
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	Long getSuccNum(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
