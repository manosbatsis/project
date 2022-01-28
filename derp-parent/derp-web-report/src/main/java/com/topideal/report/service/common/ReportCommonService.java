package com.topideal.report.service.common;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.common.OperateReportLogModel;
import com.topideal.entity.vo.system.BrandModel;

/**
 *公共方法
 * @author 杨创
 */
public interface ReportCommonService {

	/**
	 * 查询日志 数据
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	public List<OperateReportLogModel> getOperateLogList(OperateReportLogModel model) throws SQLException;

}
