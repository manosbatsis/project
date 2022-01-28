package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.YunjiDailySalesReportModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface YunjiDailySalesReportMapper extends BaseMapper<YunjiDailySalesReportModel> {

	/**
	 * 删除云集采销日报
	 * @param model
	 * @throws Exception
	 */
	public void deleteYunjiDailySalesReport(YunjiDailySalesReportModel model)throws SQLException;
	
    /**
     * 分页查询所有数据
     * @return
     */
	public List<YunjiDailySalesReportModel>  getListPage(YunjiDailySalesReportModel model)throws SQLException;

	/**
	 * 云集采销日报导出
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
