package com.topideal.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;

import com.topideal.entity.vo.system.MerchantInfoModel;

/**
 * 标准成本单价service
 * @author gy
 *
 */
public interface SettlementPriceService {

	/**
	 * 获取标准成本单价
	 */
	BigDecimal getSettlementPrice(MerchantInfoModel merchant, Long goodId, Long depotId, String month,Long buId) throws ParseException, SQLException ;
}
