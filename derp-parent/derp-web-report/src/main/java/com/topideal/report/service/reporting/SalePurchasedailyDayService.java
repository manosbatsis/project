package com.topideal.report.service.reporting;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.topideal.entity.dto.SalePurchasedailyDayDTO;
import com.topideal.entity.vo.reporting.SalePurchasedailyDayModel;

/**
 * 购销采销报表service
 */
public interface SalePurchasedailyDayService {
	
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 * @throws ParseException 
	 */
	SalePurchasedailyDayDTO listSalePurchaseDailyByPage(SalePurchasedailyDayModel model) throws SQLException, ParseException;

	/**
	 * 导出
	 * @param model
	 * @return
	 * @throws SQLException
	 * @throws ParseException 
	 */
	List<SalePurchasedailyDayDTO> exportSalePurchaseDailyDetails(SalePurchasedailyDayModel model) throws SQLException, ParseException;
	/**
	 * 获取导出的总条数
	 * @param model
	 * @return
	 */
	Long getExportTotalCount(SalePurchasedailyDayModel model);


}
