package com.topideal.service;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.LogWarningMqDTO;
import com.topideal.entity.vo.LogWarningMqModel;

/**
 * mq日志预警
 * @author zhanghx
 */
public interface WarningLogService {

	/**
	 * 分页
	 * @param model
	 * @return
	 */
	LogWarningMqDTO listByPage(LogWarningMqDTO dto) throws SQLException;

	List<LogWarningMqModel> getList()throws SQLException;
}
