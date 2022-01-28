package com.topideal.service;

import com.topideal.entity.vo.system.MerchantInfoModel;

public interface YunjiDailySalesReportService {

	/**
	 * 新生成云集采销日报天数据
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public boolean getYunjiDailySalesReport(String json, String keys, String topics, String tags, MerchantInfoModel merchantInfo) throws Exception;
	

}
