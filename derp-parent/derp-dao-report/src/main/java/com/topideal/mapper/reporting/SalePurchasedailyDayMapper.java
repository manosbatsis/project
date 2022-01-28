package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;

import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.vo.reporting.SalePurchasedailyDayModel;


@MyBatisRepository
public interface SalePurchasedailyDayMapper extends BaseMapper<SalePurchasedailyDayModel> {
	/**
	 * 根据条件统计当天的购销采销日报数据
	 * @param startDate 开始时间(必传)
	 * @param endDate   结束时间(必传)
	 * @param merchantId 商家id
	 * @return
	 */
	List<SalePurchasedailyDayModel> countSalePurchasedailyDayByParam(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("merchantId") Long merchantId);
	/**
	 * 分页查询
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	PageDataModel<SalePurchasedailyDayModel> listSalePurchaseDailyByPage(SalePurchasedailyDayModel model);
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
	 * 根据条件获取最大的报表日期
	 * @param merchantId
	 * @return
	 */
	List<SalePurchasedailyDayModel> getMaxReportDate(@Param("merchantId") Long merchantId);



}
