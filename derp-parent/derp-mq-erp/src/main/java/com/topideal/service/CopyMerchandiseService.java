package com.topideal.service;

import com.topideal.entity.vo.main.MerchantInfoModel;

public interface CopyMerchandiseService {

	/**
	 * 生成/修改卓普信和嘉宝商品
	 * @param json
	 */
	void saveCopyMerchandise(String json,MerchantInfoModel merchantInfoModel) throws Exception;

}
