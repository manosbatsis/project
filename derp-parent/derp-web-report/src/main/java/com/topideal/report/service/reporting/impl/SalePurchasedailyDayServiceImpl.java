package com.topideal.report.service.reporting.impl;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.topideal.entity.dto.SalePurchasedailyDayDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.SalePurchasedailyDayDao;
import com.topideal.entity.vo.reporting.SalePurchasedailyDayModel;
import com.topideal.report.service.reporting.SalePurchasedailyDayService;

/**
 * 购销采销报表 serviceImpl
 */
@Service
public class SalePurchasedailyDayServiceImpl implements SalePurchasedailyDayService {
	@Autowired
    private SalePurchasedailyDayDao salePurchasedailyDayDao;
	
	@Override
	public SalePurchasedailyDayDTO listSalePurchaseDailyByPage(SalePurchasedailyDayModel model) throws SQLException, ParseException {
		SalePurchasedailyDayModel spdModel = salePurchasedailyDayDao.listSalePurchaseDailyByPage(model);
		//获取结束时间--查询的月份是当前月份，取报表最大时间；小于当前月份，取查询月份的最后一天
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat formatter1 = new SimpleDateFormat("yyyyMM");
		//查询时间
		Date reportDate = formatter.parse(model.getReportDate());
		String reportMoth = formatter1.format(reportDate);
		//当前时间
		Date now = new Date();
		String nowMonth = formatter1.format(now);
		//结束时间--默认查询时间
		Date date1 = reportDate;
		if(reportMoth.compareTo(nowMonth)<=-1){//小于当前月份，取查询月份的最后一天
			Calendar calast = Calendar.getInstance();
			calast.setTime(reportDate);
			calast.set(Calendar.DAY_OF_MONTH, calast.getActualMaximum(Calendar.DAY_OF_MONTH));
			date1 = calast.getTime();
		}else{//取报表最大时间
			SalePurchasedailyDayModel maxModel = salePurchasedailyDayDao.getMaxReportDate(model.getMerchantId());
			if(maxModel != null && StringUtils.isNotBlank(maxModel.getReportDate())){
				if(maxModel.getReportDate().compareTo(model.getReportDate())>=1){//最大日期大于查询，取最大日期
					date1 = formatter.parse(maxModel.getReportDate());
				}
			}
		}
		//获取起始时间--报表当月1号的日期
		Calendar cale = Calendar.getInstance();
		cale.setTime(date1);
	    cale.set(Calendar.DAY_OF_MONTH, 1);  
		Date date2 = cale.getTime();
		List<SalePurchasedailyDayModel> newList = new ArrayList<SalePurchasedailyDayModel>();
		List<SalePurchasedailyDayModel> list = spdModel.getList();
		for(SalePurchasedailyDayModel spdModel1:list){
			if(spdModel1.getMonSaleCount() != null){
				Double count = Double.valueOf(spdModel1.getMonSaleCount());
				int different = TimeUtils.differentDays(date1, date2);
				different += 1;//刷新某天的数据，日期距离1号的天数需要加一天
				Long avg = Math.round(count/different);
				if(avg == 0 && count > 0){
					avg+=1;
				}
				spdModel1.setMonAvgCount(avg.intValue());
			}else{
				spdModel1.setMonAvgCount(0);
			}
			newList.add(spdModel1);
		}
		spdModel.setList(newList);
		SalePurchasedailyDayDTO dto=new SalePurchasedailyDayDTO();
		BeanUtils.copyProperties(spdModel,dto);
		return dto;
	}

	@Override
	public List<SalePurchasedailyDayDTO> exportSalePurchaseDailyDetails(SalePurchasedailyDayModel model)
			throws SQLException, ParseException {
		//获取结束时间--查询的月份是当前月份，取报表最大时间；小于当前月份，取查询月份的最后一天
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat formatter1 = new SimpleDateFormat("yyyy-MM");
		//查询时间
		Date reportDate = formatter.parse(model.getReportDate());
		String reportMoth = formatter1.format(reportDate);
		//当前时间
		Date now = new Date();
		String nowMonth = formatter1.format(now);
		//结束时间--默认查询时间
		Date date1 = reportDate;
		if(reportMoth.compareTo(nowMonth)<=-1){//小于当前月份，取查询月份的最后一天
			Calendar calast = Calendar.getInstance();
			calast.setTime(reportDate);
			calast.set(Calendar.DAY_OF_MONTH, calast.getActualMaximum(Calendar.DAY_OF_MONTH));
			date1 = calast.getTime();
		}else{//取报表最大时间
			SalePurchasedailyDayModel maxModel = salePurchasedailyDayDao.getMaxReportDate(model.getMerchantId());
			if(maxModel != null && StringUtils.isNotBlank(maxModel.getReportDate())){
				if(maxModel.getReportDate().compareTo(model.getReportDate())>=1){//最大日期大于查询，取最大日期
					date1 = formatter.parse(maxModel.getReportDate());
				}
			}
		}
		//获取起始时间--报表当月1号的日期
		Calendar cale = Calendar.getInstance();
		cale.setTime(date1);
	    cale.set(Calendar.DAY_OF_MONTH, 1);  
		Date date2 = cale.getTime();
		List<SalePurchasedailyDayDTO> newList = new ArrayList<SalePurchasedailyDayDTO>();
		List<SalePurchasedailyDayModel> list = salePurchasedailyDayDao.exportSalePurchaseDailyDetails(model);
		for(SalePurchasedailyDayModel spdModel1:list){
			if(spdModel1.getMonSaleCount() != null){
				Double count = Double.valueOf(spdModel1.getMonSaleCount());
				int different = TimeUtils.differentDays(date1, date2);
				different += 1;//刷新某天的数据，日期距离1号的天数需要加一天
				Long avg = Math.round(count/different);
				if(avg == 0 && count > 0){
					avg+=1;
				}
				spdModel1.setMonAvgCount(avg.intValue());
			}else{
				spdModel1.setMonAvgCount(0);
			}
			SalePurchasedailyDayDTO dto=new SalePurchasedailyDayDTO();
			BeanUtils.copyProperties(spdModel1,dto);
			newList.add(dto);
		}
		return newList;
	}
	/**
	 * 获取导出的总条数
	 * @param model
	 * @return
	 */
	@Override
	public Long getExportTotalCount(SalePurchasedailyDayModel model) {
		return salePurchasedailyDayDao.getExportTotalCount(model);
	}

}
