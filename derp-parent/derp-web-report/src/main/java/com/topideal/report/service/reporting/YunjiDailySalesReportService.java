package com.topideal.report.service.reporting;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.vo.reporting.YunjiDailySalesReportModel;

/**
 * 
 * 云集报表service
 * @author 杨创
 *
 */
public interface YunjiDailySalesReportService {
	
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	YunjiDailySalesReportModel listDailySalesReportByPage(YunjiDailySalesReportModel model) throws SQLException;

	/**
	 * 导出明细
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<YunjiDailySalesReportModel> listDailySalesReportExport(YunjiDailySalesReportModel model) throws SQLException;
	
	/**
	 * 获取导出明细的数量
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	int getDailySalesReportCount(YunjiDailySalesReportModel model)throws SQLException;


}
