package com.topideal.service;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.LogStreamMqDTO;
import com.topideal.entity.vo.LogStreamMqModel;

/**
 * mq日志流水
 * @author zhanghx
 */
public interface StreamLogService {

	/**
	 * 分页
	 * @param model
	 * @return
	 */
	LogStreamMqDTO listByPage(LogStreamMqDTO dto) throws SQLException;
	
	/**
	 * 批量关闭
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	boolean closeBatch(List<Long> list, String modelCode) throws SQLException;
	
}
