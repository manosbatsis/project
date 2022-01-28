package com.topideal.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.ExchangeRateConfigDao;
import com.topideal.dao.base.ExchangeRateDao;
import com.topideal.entity.vo.base.ExchangeRateConfigModel;
import com.topideal.entity.vo.base.ExchangeRateModel;
import com.topideal.entity.vo.timer.CurrencyAvgExchangeRateResDetailJson;
import com.topideal.entity.vo.timer.CurrencyAvgExchangeRateResponseJson;
import com.topideal.entity.vo.timer.CurrencyExchangeRateResDetailJson;
import com.topideal.entity.vo.timer.CurrencyExchangeRateResponseJson;
import com.topideal.http.HttpClientUtil;
import com.topideal.service.CurrencyRateGetService;
import com.topideal.tools.EpassSignUtils;

import net.sf.json.JSONObject;

@Service
public class CurrencyRateGetServiceImpl implements CurrencyRateGetService {

	private static final Logger LOGGER= LoggerFactory.getLogger(CurrencyRateGetServiceImpl.class);

	@Autowired
	private ExchangeRateDao exchangeRateDao;
	@Autowired
	private ExchangeRateConfigDao exchangeRateConfigDao;

	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201700000,model=DERP_LOG_POINT.POINT_13201700000_Label)
	public void currencyRateGet(String json) throws Exception {
		try {
			JSONObject jsonData = JSONObject.fromObject(json);
			Map<String, Object> jsonMap = jsonData;
			String currencyDate = (String) jsonMap.get("currencyDate");

			if (StringUtils.isBlank(currencyDate)) {
				//抓取汇率日期（取今天）
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
				Calendar cal = Calendar.getInstance();
				Date time = cal.getTime();
				currencyDate = sdf.format(time);
			}

			exchangeRateDao.deleteByEffectiveDate(currencyDate);
			ExchangeRateConfigModel rateConfigModel = new ExchangeRateConfigModel();
			List<ExchangeRateConfigModel> exchangeRateConfigModels = exchangeRateConfigDao.list(rateConfigModel);
			if (exchangeRateConfigModels == null || exchangeRateConfigModels.size() == 0) {
				throw new RuntimeException("获取汇率接口,没有汇率配置信息");
			}

			//根据汇率配置信息查询即期汇率和平均汇率
			for (ExchangeRateConfigModel rateConfig : exchangeRateConfigModels) {
				//每种兑换币种+汇率日期仅有一条记录；
				ExchangeRateModel exist = new ExchangeRateModel();
				exist.setOrigCurrencyCode(rateConfig.getOrigCurrencyCode());
				exist.setTgtCurrencyCode(rateConfig.getTgtCurrencyCode());
				exist.setEffectiveDate(TimeUtils.parseDay(currencyDate));
				if (isExist(exist)) {
					LOGGER.info("获取汇率接口,"+ rateConfig.getOrigCurrencyCode() + "兑" + rateConfig.getTgtCurrencyCode() + "已经存在");
					continue;
				}
				ExchangeRateModel rateModel = new ExchangeRateModel();
				rateModel.setDataSource(rateConfig.getDataSource());
				rateModel.setCreateName("接口回传");
				rateModel.setOrigCurrencyCode(rateConfig.getOrigCurrencyCode());
				rateModel.setOrigCurrencyName(rateConfig.getOrigCurrencyName());
				rateModel.setTgtCurrencyCode(rateConfig.getTgtCurrencyCode());
				rateModel.setTgtCurrencyName(rateConfig.getTgtCurrencyName());
				rateModel.setEffectiveDate(TimeUtils.parseDay(currencyDate));

				//汇率接口查询
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("requestTime", TimeUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				jsonObject.put("method", ApolloUtil.currency001Method);
				jsonObject.put("v", ApolloUtil.currencyV);
				jsonObject.put("appKey", ApolloUtil.currencyAppkey);// appkey
				jsonObject.put("appSecret", ApolloUtil.currencyAppsecret);//appSecret
				jsonObject.put("timesTamp", TimeUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				jsonObject.put("rateStartDate", currencyDate);
				jsonObject.put("rateEndDate", currencyDate);
				jsonObject.put("dataSource", rateConfig.getDataSource());
				jsonObject.put("localCurrency", rateConfig.getTgtCurrencyCode());
				jsonObject.put("foreignCurrency", rateConfig.getOrigCurrencyCode());
				CurrencyExchangeRateResDetailJson detailJson = getCurrency(jsonObject, rateConfig);
				if (detailJson != null) {
					rateModel.setRate(detailJson.getMiddlePrice());
				}

				// 获取平均汇率接口
				/**1、查询汇率配置表中已有配置的汇率关系且查询日期为“每月1号~月末前一天”时，则获取汇率接口对应“本币+外币+数据来源”上月的”cumAverageMiddlePrice 中间价累计均价“字段值
				   2、查询汇率配置表中已有配置的汇率关系且查询日期为“月末最后一天”时，则获取汇率接口对应“本币+外币+数据来源”本月的”cumAverageMiddlePrice 中间价累计均价“字段值
				   3、1月1号~1月30号的平均汇率，等于1月1号的当天汇率。*/
				JSONObject averageJson = new JSONObject();
				averageJson.put("method", ApolloUtil.currency002Method);
				averageJson.put("v", ApolloUtil.currencyV);
				averageJson.put("appKey", ApolloUtil.currencyAppkey);// appkey
				averageJson.put("appSecret", ApolloUtil.currencyAppsecret);//appSecret
				averageJson.put("dataSource", rateConfig.getDataSource());
				averageJson.put("localCurrency", rateConfig.getTgtCurrencyCode());
				averageJson.put("foreignCurrency", rateConfig.getOrigCurrencyCode());
				if (isBetweenJan(currencyDate)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Timestamp timestamp = TimeUtils.parseDay(currencyDate);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(timestamp);
					calendar.set(Calendar.MONTH, 0);
					calendar.set(Calendar.DATE, 1);
					jsonObject.put("rateStartDate", sdf.format(calendar.getTime()));
					jsonObject.put("rateEndDate", sdf.format(calendar.getTime()));
					CurrencyExchangeRateResDetailJson rateResDetailJson = getCurrency(jsonObject, rateConfig);
					if (rateResDetailJson != null) {
						rateModel.setAvgRate(rateResDetailJson.getMiddlePrice());
					}
				} else {
					Timestamp timestamp = TimeUtils.parseDay(currencyDate);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(timestamp);
					if (!isLastDay(currencyDate)) {
						calendar.set(Calendar.DATE, 1);
						calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
					}
					averageJson.put("rateYear", calendar.get(Calendar.YEAR));
					averageJson.put("rateMonth", calendar.get(Calendar.MONTH)+1 > 9 ? calendar.get(Calendar.MONTH)+1 : "0" + (calendar.get(Calendar.MONTH)+1));
					String json1 = averageJson.toString();
					json1 = URLDecoder.decode(json1, "utf-8");
					String sign = EpassSignUtils.sign(json1, ApolloUtil.currencyAppsecret);
					averageJson.put("sign", sign);
					LOGGER.info("获取平均汇率接口请求" + averageJson);
					String result = HttpClientUtil.doPost(ApolloUtil.currencyApi, averageJson.toString(), "utf-8");
					LOGGER.info("获取平均汇率接口响应" + result);

					JSONObject resultJson = JSONObject.fromObject(result);
					Map classMap = new HashMap();
					classMap.put("averageRateList", CurrencyAvgExchangeRateResDetailJson.class);

					CurrencyAvgExchangeRateResponseJson currencyAvgExchangeRateResponseJson = (CurrencyAvgExchangeRateResponseJson) JSONObject.toBean(resultJson,
							CurrencyAvgExchangeRateResponseJson.class, classMap);
					if (currencyAvgExchangeRateResponseJson.getStatus()!=1) {
						throw new RuntimeException("获取平均汇率接口响应返回错误消息" + resultJson.getString("notes") + "，数据来源：" + rateConfig.getDataSource()
								+ "，本币：" + rateConfig.getOrigCurrencyCode() + "，外币：" + rateConfig.getTgtCurrencyCode());
					} else if (!resultJson.containsKey("averageRateList") || (currencyAvgExchangeRateResponseJson.getAverageRateList() == null
							&& currencyAvgExchangeRateResponseJson.getAverageRateList().size() == 0)) {
						throw new RuntimeException("获取平均汇率接口，数据来源：" + rateConfig.getDataSource()
								+ "，本币：" + rateConfig.getOrigCurrencyCode()
								+ "，外币：" + rateConfig.getTgtCurrencyCode() + "抓取汇率明细为空");
					} else if (currencyAvgExchangeRateResponseJson.getAverageRateList().size() > 1) {
						throw new RuntimeException("获取平均汇率接口，数据来源：" + rateConfig.getDataSource()
								+ "，本币：" + rateConfig.getOrigCurrencyCode()
								+ "，外币：" + rateConfig.getTgtCurrencyCode() + "汇率明细存在多条");
					} else if (currencyAvgExchangeRateResponseJson.getAverageRateList().size() == 1) {
						rateModel.setAvgRate(currencyAvgExchangeRateResponseJson.getAverageRateList().get(0).getCumAverageMiddlePrice());
					}
				}
				rateModel.setStatus("1");
				exchangeRateDao.save(rateModel);				
			}
			//韩币兑港币转换
			createRateKRWToHKD(currencyDate);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	private boolean isExist(ExchangeRateModel model) throws SQLException {
		List<ExchangeRateModel> list = exchangeRateDao.list(model);
		if (list.size()>0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断传入日期是否是月末最后一天
	 */
	private boolean isLastDay(String date) {
		Timestamp timestamp = TimeUtils.parseDay(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(timestamp);
		return calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 判断传入日期是否在1月1号~1月30号
	 */
	private boolean isBetweenJan(String date) {
		Timestamp timestamp = TimeUtils.parseDay(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(timestamp);
		int month = calendar.get(Calendar.MONTH);
		boolean isLastDay = calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (month == 0 && !isLastDay) {
			return true;
		}
		return false;
	}

	/**
	 * 获取汇率信息
	 */
	private CurrencyExchangeRateResDetailJson getCurrency(JSONObject jsonObject, ExchangeRateConfigModel rateConfig) throws UnsupportedEncodingException {
		String json = jsonObject.toString();
		json = URLDecoder.decode(json, "utf-8");
		String sign = EpassSignUtils.sign(json, ApolloUtil.currencyAppsecret);
		jsonObject.put("sign", sign);
		LOGGER.info("获取汇率接口请求" + jsonObject);
		String result = HttpClientUtil.doPost(ApolloUtil.currencyApi, jsonObject.toString(), "utf-8");
		LOGGER.info("获取汇率接口响应" + result);

		JSONObject resultJson = JSONObject.fromObject(result);
		Map classMap = new HashMap();
		classMap.put("exchangeRateList", CurrencyExchangeRateResDetailJson.class);

		CurrencyExchangeRateResponseJson currencyExchangeRateResponseJson = (CurrencyExchangeRateResponseJson) JSONObject.toBean(resultJson,
				CurrencyExchangeRateResponseJson.class, classMap);
		if (currencyExchangeRateResponseJson.getStatus()!=1) {
			throw new RuntimeException("获取汇率接口响应返回错误消息" + resultJson.getString("notes") + "，数据来源：" + rateConfig.getDataSource()
					+ "，本币：" + rateConfig.getOrigCurrencyCode() + "，外币：" + rateConfig.getTgtCurrencyCode());
		}

		if (!resultJson.containsKey("exchangeRateList") || (currencyExchangeRateResponseJson.getExchangeRateList() == null
				&& currencyExchangeRateResponseJson.getExchangeRateList().size() == 0)) {
			throw new RuntimeException("获取汇率接口，数据来源：" + rateConfig.getDataSource()
					+ "，本币：" + rateConfig.getOrigCurrencyCode()
					+ "，外币：" + rateConfig.getTgtCurrencyCode() + "抓取汇率明细为空");
		} else if (currencyExchangeRateResponseJson.getExchangeRateList().size() > 1) {
			throw new RuntimeException("获取汇率接口，数据来源：" + rateConfig.getDataSource()
					+ "，本币：" + rateConfig.getOrigCurrencyCode()
					+ "，外币：" + rateConfig.getTgtCurrencyCode() + "汇率明细存在多条");
		}
		return currencyExchangeRateResponseJson.getExchangeRateList().get(0);
	}
	/**
	 * 新增韩币兑港币转换
	 * @throws SQLException 
	 */
	private void createRateKRWToHKD(String currencyDate) throws SQLException {
		Date effectiveDate = TimeUtils.parseDay(currencyDate);
		
		//记录韩币兑美元汇率信息
		ExchangeRateModel rateKRWModel = new ExchangeRateModel();
		rateKRWModel.setOrigCurrencyCode("KRW");
		rateKRWModel.setTgtCurrencyCode("USD");
		rateKRWModel.setStatus("1");
		rateKRWModel.setEffectiveDate(effectiveDate);//汇率日期为当天
		rateKRWModel = exchangeRateDao.searchByModel(rateKRWModel);
		
		//记录美元兑港币汇率信息
		ExchangeRateModel rateHKDModel = new ExchangeRateModel();
		rateHKDModel.setOrigCurrencyCode("USD");
		rateHKDModel.setTgtCurrencyCode("HKD");
		rateHKDModel.setStatus("1");
		rateHKDModel.setEffectiveDate(effectiveDate);//汇率日期为当天
		rateHKDModel = exchangeRateDao.searchByModel(rateHKDModel);
		
		BigDecimal rateBigDecimal = new BigDecimal(rateKRWModel.getRate()).multiply(new BigDecimal(rateHKDModel.getRate()));
		Double rate = rateBigDecimal.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
		BigDecimal avgRateBigDecimal = new BigDecimal(rateKRWModel.getAvgRate()).multiply(new BigDecimal(rateHKDModel.getAvgRate()));
		Double avgRate = avgRateBigDecimal.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		//注入信息 韩币兑港币
		ExchangeRateModel rateModel = new ExchangeRateModel();
		rateModel.setDataSource("SYS");
		rateModel.setCreateName("系统创建");
		rateModel.setOrigCurrencyCode(rateKRWModel.getOrigCurrencyCode());
		rateModel.setOrigCurrencyName(rateKRWModel.getOrigCurrencyName());
		rateModel.setTgtCurrencyCode(rateHKDModel.getTgtCurrencyCode());
		rateModel.setTgtCurrencyName(rateHKDModel.getTgtCurrencyName());
		rateModel.setEffectiveDate(effectiveDate);
		rateModel.setRate(rate);
		rateModel.setAvgRate(avgRate);
		rateModel.setStatus("1");
		
		exchangeRateDao.save(rateModel);
		
	}
}
