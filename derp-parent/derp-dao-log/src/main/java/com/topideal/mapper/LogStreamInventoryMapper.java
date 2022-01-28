package com.topideal.mapper;

import java.sql.SQLException;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.LogStreamMqDTO;
import com.topideal.entity.vo.LogStreamInventoryModel;
import com.topideal.entity.vo.LogStreamMqModel;


@MyBatisRepository
public interface LogStreamInventoryMapper extends BaseMapper<LogStreamInventoryModel> {

	/**
	 * 根据月份，获取之前的数据
	 * @param month
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<LogStreamInventoryModel> getListByMonthByPage(LogStreamInventoryModel model) throws SQLException;

    /**整合web*/
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<LogStreamMqDTO> getListByPage(LogStreamMqDTO model) throws SQLException;
	
}
