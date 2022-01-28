package com.topideal.mapper.system;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.system.ExchangeRateModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 汇率管理表 mapper
 * @author lian_
 */
@MyBatisRepository
public interface ExchangeRateMapper extends BaseMapper<ExchangeRateModel> {
	/**
	 * 根据对象查询汇率表
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	ExchangeRateModel getByDateOrigCurrency(ExchangeRateModel model)throws SQLException;
	
	/**
	 * 获取最近 小于账单日期 汇率
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<ExchangeRateModel>getRecentExchangeRateList(ExchangeRateModel model)throws SQLException;

	List<ExchangeRateModel>getRecentRateList(ExchangeRateModel model)throws SQLException;

	/**
	 * 查询汇率当天的即期汇率、平均汇率是否为空
	 * @return
	 */
	List<ExchangeRateModel> getNullRateList();

	/**
	 * 获取查询月份的原位币兑本位币的最新一条记录
	 * @return
	 */
	ExchangeRateModel getLatestRate(@Param("month") String month, @Param("origCurrencyCode") String origCurrencyCode, @Param("tgtCurrencyCode") String tgtCurrencyCode);


}
