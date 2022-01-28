package com.topideal.dao.reporting;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.reporting.YunjiDailySalesReportModel;


public interface YunjiDailySalesReportDao extends BaseDao<YunjiDailySalesReportModel> {
		

	/**
	 * 删除云集采销日报
	 * @param model
	 * @throws Exception
	 */
	public void deleteYunjiDailySalesReport(YunjiDailySalesReportModel model)throws SQLException;

	
	/**
	 * 云集报表分页
	 * @param model
	 * @return
	 */
	public YunjiDailySalesReportModel getListByPage(YunjiDailySalesReportModel model) throws SQLException;

	
	/**
	 * 导出明细
	 * @param model
	 * @return
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
