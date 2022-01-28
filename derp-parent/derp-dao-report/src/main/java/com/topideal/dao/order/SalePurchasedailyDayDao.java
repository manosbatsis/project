package com.topideal.dao.order;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.reporting.SalePurchasedailyDayModel;


public interface SalePurchasedailyDayDao extends BaseDao<SalePurchasedailyDayModel> {
	/**
	 * 根据条件统计当天的购销采销日报数据
	 * @param startDate 开始时间(必传)
	 * @param endDate   结束时间(必传)
	 * @param merchantId 商家id
	 * @return
	 */
	List<SalePurchasedailyDayModel> countSalePurchasedailyDayByParam(String startDate, String endDate, Long merchantId);

	/**
	 * 分页查询
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	SalePurchasedailyDayModel listSalePurchaseDailyByPage(SalePurchasedailyDayModel model) throws SQLException;
	
	/**
	 * 导出
	 * @param model
	 * @return
	 */
	List<SalePurchasedailyDayModel> exportSalePurchaseDailyDetails(SalePurchasedailyDayModel model);
	/**
	 * 获取导出的总条数
	 * @param model
	 * @return
	 */
	Long getExportTotalCount(SalePurchasedailyDayModel model);
	
	/**
	 * 根据条件获取最大的报表日期的
	 * @param merchantId
	 * @return
	 */
	SalePurchasedailyDayModel getMaxReportDate(Long merchantId);
		










}
