package com.topideal.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.LogWarningMqDao;
import com.topideal.entity.dto.LogWarningMqDTO;
import com.topideal.entity.vo.LogWarningMqModel;
import com.topideal.service.WarningLogService;
import com.topideal.tools.ParseDaterangepicker;

/**
 * 日志预警
 * @author zhanghx
 */
@Service
public class WarningLogServiceImpl implements WarningLogService {

	@Autowired
	private LogWarningMqDao dao;
	
	
	/**
	 * 分页
	 * @param model
	 * @return
	 */
	@Override
	public LogWarningMqDTO listByPage(LogWarningMqDTO dto) throws SQLException {
		if (StringUtils.isNotBlank(dto.getEndDateStr())) {
			List<String> list = ParseDaterangepicker.parseToString(dto.getEndDateStr());
			if(list!=null && list.size() == 2){
				dto.setConsumeStartDate(list.get(0));//开始
				dto.setConsumeEndDate(list.get(1));//结束
			}
		}
		return dao.getListByPage(dto);
	}

	@Override
	public List<LogWarningMqModel> getList() {
		// TODO Auto-generated method stub
		return dao.getList();
	}

}
