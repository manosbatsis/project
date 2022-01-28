package com.topideal.report.service.common.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.common.OperateReportLogDao;
import com.topideal.entity.vo.common.OperateReportLogModel;
import com.topideal.report.service.common.ReportCommonService;

@Service
public class ReportCommonServiceImpl implements ReportCommonService{

	@Autowired
	private OperateReportLogDao operateReportLogDao;

	/**
	 * 获取日志数据
	 */
	@Override
	public List<OperateReportLogModel> getOperateLogList(OperateReportLogModel model) throws SQLException {	
		return operateReportLogDao.list(model);
	}
	




	
	
}
