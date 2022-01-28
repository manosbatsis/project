package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.BuFinanceDecreaseSaleNoshelfModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuFinanceDecreaseSaleNoshelfMapper extends BaseMapper<BuFinanceDecreaseSaleNoshelfModel> {

	/**
	 * 清除商家 仓库 月份 (事业部财务经销存)本期减少销售在途
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuFinanceDecreaseSaleNoshelf(Map<String, Object> map) throws SQLException;

	/**
	 * 获取本期减少销售在途导出
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<BuFinanceDecreaseSaleNoshelfModel>getdecreaseSaleNoshelfExport(Map<String, Object> map)throws SQLException;

}
