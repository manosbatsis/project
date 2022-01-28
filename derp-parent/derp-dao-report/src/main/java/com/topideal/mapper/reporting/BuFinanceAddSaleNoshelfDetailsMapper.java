package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.BuFinanceAddSaleNoshelfDetailsModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuFinanceAddSaleNoshelfDetailsMapper extends BaseMapper<BuFinanceAddSaleNoshelfDetailsModel> {
	/**
	 * 清除商家 仓库 月份 (事业部财务经销存)累计销售在途明细表
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuFinanceAddSaleNoshelfDetails(Map<String, Object> map) throws SQLException;

	/**
	 * 导出本期销售在途
	 * @param model
	 * @return
	 */
	List <BuFinanceAddSaleNoshelfDetailsModel> getBuFinanceAddSaleNoshelfDetails(Map<String, Object> map);

	/**
	 * 获取累计销售在途导出
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List <BuFinanceAddSaleNoshelfDetailsModel>getAddSaleNoshelfExport(Map<String, Object> map)throws SQLException; 



}
