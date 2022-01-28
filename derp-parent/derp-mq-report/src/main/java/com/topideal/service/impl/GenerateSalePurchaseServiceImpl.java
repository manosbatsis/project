package com.topideal.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.order.SalePurchasedailyDayDao;
import com.topideal.entity.vo.reporting.SalePurchasedailyDayModel;
import com.topideal.service.GenerateSalePurchaseService;

import net.sf.json.JSONObject;

/**
 *
 * 生成采销日报天数据
 * 
 * @author longcheng.mao
 *
 */
@Service
public class GenerateSalePurchaseServiceImpl implements GenerateSalePurchaseService {
   
	/* 打印日志 */
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateSalePurchaseServiceImpl.class);
    
    @Autowired
    private SalePurchasedailyDayDao salePurchasedailyDayDao;
    
    /**
	 * 生成购销采销日报数据
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @return
	 * @throws Exception
	 */
    @Override
	public boolean generateSalePurchaseInterface(String json, String keys, String topics, String tags)
			throws Exception {
    	LOGGER.info("=============开始生成购销采销日报数据===========");
    	SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");  
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
    	String reportDate = dft.format(calendar.getTime());//报表时间，默认当前时间-1天
    	//解析json
    	JSONObject jsonData = JSONObject.fromObject(json);
    	//如果有传报表时间，则刷新该时间的报表数据
    	if(jsonData.containsKey("reportDate") && jsonData.get("reportDate") != null){
    		reportDate = (String) jsonData.get("reportDate");
    	}
    	/*
    	 * 生成报表数据
    	 * 1.判断报表时间的数据是否已存在，是：删除
    	 * 2.根据报表时间统计当天出库的数据，并保存到购销采销日报数据
    	 */
    	//根据报表时间获取报表数据
    	SalePurchasedailyDayModel model = new SalePurchasedailyDayModel();
    	model.setReportDate(reportDate);
    	List<SalePurchasedailyDayModel> list = salePurchasedailyDayDao.list(model);
    	//删除已存在的报表数据
    	List<Long> ids = new ArrayList<Long>();
    	for(SalePurchasedailyDayModel spdModel:list){
    		ids.add(spdModel.getId());
    	}
    	if(ids != null && ids.size() > 0){
    		salePurchasedailyDayDao.delete(ids);
    	}
    	//统计当天的购销采销日报数据
    	String startDate = reportDate;
    	Calendar calendar1 = Calendar.getInstance();
    	calendar1.setTime(dft.parse(reportDate));
    	calendar1.add(Calendar.DATE, 1);
    	String endDate = dft.format(calendar1.getTime());
    	Long merchantId = null;
    	if(jsonData.containsKey("merchantId") && jsonData.get("merchantId") != null){
    		merchantId = Long.valueOf(jsonData.getString("merchantId"));
    	}
    	List<SalePurchasedailyDayModel> salePurchasedailyDayList = salePurchasedailyDayDao.countSalePurchasedailyDayByParam(startDate,endDate,merchantId);
    	//遍历保存数据
    	for(SalePurchasedailyDayModel salePurchasedailyDay :salePurchasedailyDayList){
    		salePurchasedailyDay.setReportDate(reportDate);
    		String brandName = salePurchasedailyDay.getBrandName();
    		if(StringUtils.isBlank(brandName)){
    			salePurchasedailyDay.setBrandName("未分配");
    		}
    		String productTypeName = salePurchasedailyDay.getProductTypeName();
    		if(StringUtils.isBlank(productTypeName)){
    			salePurchasedailyDay.setProductTypeName("未分配");
    		}
    		salePurchasedailyDayDao.save(salePurchasedailyDay);
    	}
    	LOGGER.info("=============生成购销采销日报数据结束,报表日期:"+reportDate+"===============");
		return true;
	}
}
