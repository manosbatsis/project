package com.topideal.service;

import com.topideal.entity.vo.system.MerchantInfoModel;

public interface InventoryFallingPriceReservesService {

	void saveSummaryReport(String json, String keys, String topics, String tags,String month,MerchantInfoModel merchantInfoModel) throws Exception;

}
