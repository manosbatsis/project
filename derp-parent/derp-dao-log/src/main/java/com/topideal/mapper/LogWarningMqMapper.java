package com.topideal.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.LogWarningMqDTO;
import com.topideal.entity.vo.LogWarningMqModel;


@MyBatisRepository
public interface LogWarningMqMapper extends BaseMapper<LogWarningMqModel> {

	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<LogWarningMqDTO> getListByPage(LogWarningMqDTO dto) throws SQLException;

	/**
	 * 根据月份，获取之前的数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<LogWarningMqModel> getListByMonthByPage(LogWarningMqModel model) throws SQLException;
	
	/**
	 * 根据时间删除数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	void delByCreateTime(@Param("createTime") String createTime);
	
	/**整合web*/
	List<LogWarningMqModel> getList();
}

