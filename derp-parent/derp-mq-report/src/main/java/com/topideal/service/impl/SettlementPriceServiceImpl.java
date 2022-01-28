package com.topideal.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.reporting.BuFinanceInventorySummaryDao;
import com.topideal.dao.reporting.SettlementPriceRecordDao;
import com.topideal.dao.system.MerchandiseInfoDao;
import com.topideal.entity.vo.reporting.SettlementPriceRecordModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.service.SettlementPriceService;

@Service
public class SettlementPriceServiceImpl implements SettlementPriceService{

	//新报表dao
	@Autowired
	private BuFinanceInventorySummaryDao buFinanceInventorySummaryDao;
	@Autowired
	private SettlementPriceRecordDao settlementPriceRecordDao;
	// 商品dao
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao;
	
	@Override
	public BigDecimal getSettlementPrice(MerchantInfoModel merchant, Long goodId, Long depotId, String month,Long buId)
			throws ParseException, SQLException {
		
		MerchandiseInfoModel model = merchandiseInfoDao.searchById(goodId);
		
		/**
         * 标准成本单价：
		 * 取上月报表中的【调整】标准成本单价，
		 * 上月若【调整】标准成本单价为空，则取标准成本单价管理中该商品生效的标准成本单价且价格生效月份小于或等于报表归属月份。
		 * */
		Map<String, Object> lastMonthMap=new HashMap<String, Object>();
		lastMonthMap.put("merchantId", merchant.getId());
		lastMonthMap.put("depotId", depotId);
		lastMonthMap.put("buId", buId);
		lastMonthMap.put("month", TimeUtils.getUpMonth(month));//上一个月
		lastMonthMap.put("goodsNo", model.getGoodsNo());

		Map<String,Object> goodsLastMonthMap = buFinanceInventorySummaryDao.getGoodsNoSummary(lastMonthMap);
		BigDecimal price = null;//标准成本单价 默认为空
		if(goodsLastMonthMap!=null){
			price = (BigDecimal)goodsLastMonthMap.get("tz_price");//取上月调整标准单价
		}

		Map<String, Object> lastMonthPriceMap=new HashMap<String, Object>();
		lastMonthPriceMap.put("merchantId", merchant.getId());
		lastMonthPriceMap.put("buId", buId);
		lastMonthPriceMap.put("month", TimeUtils.getUpMonth(month));//上月
		lastMonthPriceMap.put("barcode",  model.getBarcode());
		
		//上月若报表【调整】标准成本单价为空，则从标准价格管理表中取上月的标准价格
		if(price==null){
			SettlementPriceRecordModel lastMonthPrice = settlementPriceRecordDao.getBarcodePrice(lastMonthPriceMap);
		    if(lastMonthPrice!=null) {
		    	price = lastMonthPrice.getPrice();
		    }
		}
		
		Map<String, Object> thisMonthPriceMap=new HashMap<String, Object>();
		thisMonthPriceMap.put("merchantId", merchant.getId());
		thisMonthPriceMap.put("buId", buId);//事业部
		thisMonthPriceMap.put("month", month);//本月
		thisMonthPriceMap.put("barcode",  model.getBarcode());
		//上月若标准价格管理表中取上月的标准价格未空，则取本月
		if(price==null){
			SettlementPriceRecordModel thisMonthPrice = settlementPriceRecordDao.getBarcodePrice(thisMonthPriceMap);
		    if(thisMonthPrice!=null) {
		    	price = thisMonthPrice.getPrice();
		    }
		}
		return price ;
	}

}
